package com.jalenz.jalenlib.httphelper;

import android.support.v4.widget.SwipeRefreshLayout;

import com.jalenz.jalenlib.views.JieLoadingLayout;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by  Jalen Zheng on 5/08/2016.
 * Coordinate PageManager, JieLoading and SwipeRefreshLayout to make them work as a whole
 */
public class PageLoadingCoordinator {
    PageManager pageManager;
    JieLoadingLayout jieLoadingLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    public static PageLoadingCoordinator createCoordinator(final PageManager pageManager, final JieLoadingLayout jieLoadingLayout) {
        return createCoordinator(pageManager, jieLoadingLayout, null);
    }

    public static PageLoadingCoordinator createCoordinator(final PageManager pageManager, SwipeRefreshLayout swipeRefreshLayout) {
        return createCoordinator(pageManager, null, swipeRefreshLayout);
    }

    public static PageLoadingCoordinator createCoordinator(final PageManager pageManager, final JieLoadingLayout jieLoadingLayout, final SwipeRefreshLayout swipeRefreshLayout) {
        final PageLoadingCoordinator pageLoadingCoordinator = new PageLoadingCoordinator();
        pageLoadingCoordinator.pageManager = pageManager;
        pageLoadingCoordinator.jieLoadingLayout = jieLoadingLayout;
        pageLoadingCoordinator.swipeRefreshLayout = swipeRefreshLayout;

        if (pageLoadingCoordinator.swipeRefreshLayout != null) {
            pageLoadingCoordinator.swipeRefreshLayout = swipeRefreshLayout;
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageManager.refresh();
                }
            });
        }

        pageManager.setPageLoadingFinished(new PageManager.OnPageLoadingListener() {
            @Override
            public void OnPageLoadingStarted(int pageIndex, int pageSize) {
                if (pageIndex == 0) {
                    if (swipeRefreshLayout != null) {
                        LogUtils.e("on Padge Loading Started at 0");
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void OnPageLoadingFinished(int errCode, int pageIndex, int pageSize, int arraySize, boolean isEnd) {
                if (errCode == HttpHelper.HTTP_SUCCESS) {
                    if (jieLoadingLayout != null) {
                        jieLoadingLayout.setLoadCompleted(isEnd);
                    }
                }
                pageLoadingCoordinator.swipeRefreshLayout.setRefreshing(false);
                if (jieLoadingLayout != null) {
                    jieLoadingLayout.hide();
                }
            }
        });

        if (jieLoadingLayout != null) {
            jieLoadingLayout.setOnLoadingMoreListener(new JieLoadingLayout.OnLoadingMoreListener() {
                @Override
                public void OnLoadingMore() {
                    if (!pageManager.getNextPage()) {
                        jieLoadingLayout.hide();
                        jieLoadingLayout.setLoadCompleted(true);
                    }
                }
            });
        }

        return  pageLoadingCoordinator;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public JieLoadingLayout getJieLoadingLayout() {
        return jieLoadingLayout;
    }
}
