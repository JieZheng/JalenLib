package com.jalenz.jalenlib.views;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jalenz.jalenlib.R;


/**
 * Created by Jalen Zheng on 5/08/2016.
 * Loading more bottom bar
 */
public class JieLoadingLayout extends FrameLayout {
    private static final String SCHEME = "http://schemas.android.com/apk/res-auto";
    private static final String ATTRIBUTE_RECYCLER_VIEW_ID = "recycler_id";
    int recyclerId;
    RecyclerView recyclerView;
    OnLoadingMoreListener onLoadingMoreListener;

    String loadingText = getResources().getString(R.string.loading_more);
    String loadingCompletedText = getResources().getString(R.string.loading_completed);

    public static final int JIE_LOADING_STATE_IDLE = 0;
    public static final int JIE_LOADING_STATE_LOADING = 1;
    public static final int JIE_LOADING_STATE_PRE_LOADING = 2;

    boolean isLoadCompleted = false;
    int loadingState = JIE_LOADING_STATE_IDLE;
    boolean isLoading = false;
    int preLoadThreshold = 5;
    boolean isPreLoadingTriggered = false;
    int loadThreshold = 0;
    boolean isLoadingTriggered = false;

    ContentLoadingProgressBar loadingProgressBar;
    TextView loadingTextView;

    public static int StringToInt(String str) {
        int i = 0;
        if (str == null || str.trim().length() == 0) {
            str = "0";
        }
        try {
            i = Integer.valueOf(str);
        } catch (Exception e) {
            i = 0;
            e.printStackTrace();
        }
        return i;
    }

    public JieLoadingLayout(Context context) {
        super(context);
    }

    public JieLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.list_view_loading_progress_bar, this);
        loadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.loading_progress_bar);
        loadingTextView = (TextView) findViewById(R.id.loading_text);
        hide();

        recyclerId = StringToInt(attrs.getAttributeValue(SCHEME, ATTRIBUTE_RECYCLER_VIEW_ID));
    }

    public JieLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Show Loading bar
     */
    public void show(){
        isLoading = true;
        loadingState = JIE_LOADING_STATE_LOADING;
        this.setVisibility(VISIBLE);
    }

    /**
     * Hide Loading bar
     */
    public void hide() {
        isLoading = false;
        loadingState = JIE_LOADING_STATE_IDLE;
        this.setVisibility(GONE);
    }

    /**
     * Set text for loading bar
     */
    public void setLoadingText(String text) {
        loadingText = text;
    }

    /**
     * Get text on loading bar
     * @return
     */
    public String getLoadingText() {
        return loadingText;
    }

    /**
     * Show Text
     */
    public void showText() {
        loadingTextView.setVisibility(VISIBLE);
    }

    /**
     * Hide Text
     */
    public void hideText() {
        loadingTextView.setVisibility(GONE);
    }

    /**
     * Get threshold of invisible items for preloading
     * @return
     */
    public int getPreLoadThreshold() {
        return preLoadThreshold;
    }

    /**
     * Set threshold of invisible items for preloading
     * @param preLoadThreshold
     */
    public void setPreLoadThreshold(int preLoadThreshold) {
        this.preLoadThreshold = preLoadThreshold;
    }

    /**
     * Get threshold of invisible items for loading
     */
    public int getLoadThreshold() {
        return loadThreshold;
    }

    /**
     * Set threshold of invisible items for loading
     * @param loadThreshold
     */
    public void setLoadThreshold(int loadThreshold) {
        this.loadThreshold = loadThreshold;
    }

    /**
     * Check if in loading state
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Check if loading is completed
     * @return
     */
    public boolean isLoadCompleted() {
        return isLoadCompleted;
    }

    /**
     * Set Loading completed
     * @param loadCompleted
     */
    public void setLoadCompleted(boolean loadCompleted) {
        isLoadCompleted = loadCompleted;
        hide();
    }

    /**
     * Get OnLoadingMoreListener
     * @return
     */
    public OnLoadingMoreListener getOnLoadingMoreListener() {
        return onLoadingMoreListener;
    }

    /**
     * Set OnLoadingMoreListener
     * @param onLoadingMoreListener
     */
    public void setOnLoadingMoreListener(OnLoadingMoreListener onLoadingMoreListener) {
        this.onLoadingMoreListener = onLoadingMoreListener;
    }

    int getRecyclerViewInvisibleItemCount() {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int invisibleItemCount = itemCount - lastVisibleItem - 1;
            if (invisibleItemCount < 0) {
                invisibleItemCount = 0;
            }
            return invisibleItemCount;
        } else {
            return -1;
        }
    }

    /**
     * Attach RecyclerView onto Loading bar
     * @param recyclerView
     */
    public void attachRecycleView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                LogUtils.e("dy:"+dy + " isLoading:"+isLoading+" isPreloading:"+isLoadCompleted);

                if ((dy > 0 || getRecyclerViewInvisibleItemCount() == 0) && !isLoading && !isLoadCompleted) {
                    int invisibleItemCount = getRecyclerViewInvisibleItemCount();
//                    LogUtils.e("dy:" + dy + " invisibleItemCount:" + invisibleItemCount);
                    if (invisibleItemCount <= loadThreshold) {
//                        LogUtils.e("loading triggerred");
                        loadingState = JIE_LOADING_STATE_LOADING;
                        isLoadingTriggered = true;
                        isPreLoadingTriggered = true;
                        if (onLoadingMoreListener != null) {
                            show();
                            onLoadingMoreListener.OnLoadingMore();
                        }
                    } else if (invisibleItemCount <= preLoadThreshold) {
//                        LogUtils.e("pre-loading triggerred");
                        loadingState = JIE_LOADING_STATE_PRE_LOADING;
                        isLoadingTriggered = false;
                        isPreLoadingTriggered = true;
                        if (onLoadingMoreListener != null) {
                            show();
                            onLoadingMoreListener.OnLoadingMore();
                        }
                    } else {
//                        LogUtils.e("unloading state");
                        isLoadingTriggered = false;
                        isPreLoadingTriggered = false;
                    }
                } else {
//                    LogUtils.e("Not match the condition.");
                }
            }
        });
    }

    /**
     * Listener for loading more
     */
    public interface OnLoadingMoreListener {
        public void OnLoadingMore();
    }
}
