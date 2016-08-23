package com.jalenz.jalenlib.httphelper;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.jalenz.jalenlib.model.BaseBean;
import com.jalenz.jalenlib.model.DataListBean;
import com.jalenz.jalenlib.model.RequestBean;
import com.jalenz.jalenlib.model.ResponseBean;
import com.jalenz.jalenlib.views.JieLoadingLayout;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jalen Zheng on 13/08/2016.
 *
 * Build a bridge between HttpHelper, PageManager and Views, especially for RecyclerView, SwipeRefreshLayout, JieLoadingLayout
 */
public class HttpUiBridge<BB extends BaseBean> {
    private static final int MAX_PAGE_SIZE = 1000;

    Context context;
    String httpApi;
    Type type;
    RequestBean requestBean;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    JieLoadingLayout jieLoadingLayout;
    PageManager pageManager;
    PageLoadingCoordinator pageLoadingCoordinator;
    DataRefreshAdapter dataRefreshAdapter;

    public HttpUiBridge(Context context, String httpApi, Type type) {
        this.context = context;
        this.httpApi = httpApi;
        this.type = type;
    }

    /**
     * Http API request for List Data
     * @param recyclerView
     * @param swipeRefreshLayout
     * @param jieLoadingLayout
     */
    public void httpDataListGet(RequestBean requestBean, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, JieLoadingLayout jieLoadingLayout) {
        this.requestBean = requestBean;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.jieLoadingLayout = jieLoadingLayout;

        HttpHelper httpHelper = new HttpHelper(context, httpApi, new HttpListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onResult(int errCode, ResponseBean responseBean) {
                switch (errCode) {
                    case HttpHelper.HTTP_SUCCESS:
                        if (responseBean.getData() != null) {
                            DataListBean<BB> dataListBean = (DataListBean<BB>) responseBean.getData();
                            RecyclerView.Adapter adapter = HttpUiBridge.this.recyclerView.getAdapter();
                            List<BB> adapterDataList = (List<BB>) ((DataListRefreshAdapter<BB>) adapter).getDataList();
                            if (dataListBean.getArrayInfo().getPageIndex() == 0) {
                                adapterDataList.clear();
                            }
                            adapterDataList.addAll(dataListBean.getArray());
                            adapter.notifyDataSetChanged();
                            break;
                        }

                    case HttpHelper.HTTP_ERROR_AUTHORITY:
                    default:
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    case HttpHelper.HTTP_ERROR_NO_NETWORK:
                    case HttpHelper.HTTP_ERROR_BUSINESS:
                        //// TODO: 4/08/2016 More to do for these errors
                        if (HttpUiBridge.this.jieLoadingLayout != null) {
                            HttpUiBridge.this.jieLoadingLayout.hide();
                        }
                        break;
                }
            }

            @Override
            public void onCancelled() {
            }
        });

        if (pageManager == null) {
            if (jieLoadingLayout == null) {
                pageManager = new PageManager(httpHelper, requestBean, type, MAX_PAGE_SIZE);
            } else {
                pageManager = new PageManager(httpHelper, requestBean, type);
            }
        }
        if (jieLoadingLayout == null) {
            pageLoadingCoordinator = PageLoadingCoordinator.createCoordinator(pageManager, swipeRefreshLayout);
        } else {
            pageLoadingCoordinator = PageLoadingCoordinator.createCoordinator(pageManager, jieLoadingLayout, swipeRefreshLayout);
        }
        pageManager.getNextPage();
    }

    /**
     * Http API request for List Data
     * no page manage http call
     * @param recyclerView
     * @param swipeRefreshLayout
     */
    public void httpDataListGet(RequestBean requestBean, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        httpDataListGet(requestBean, recyclerView, swipeRefreshLayout, null);
    }

    /**
     * Http API request for Object Data
     * @param requestBean
     * @param dataRefreshAdapter
     * @param swipeRefreshLayout
     */
    public void httpDataGet(RequestBean requestBean, DataRefreshAdapter dataRefreshAdapter, final SwipeRefreshLayout swipeRefreshLayout) {
        this.requestBean = requestBean;
        this.dataRefreshAdapter = dataRefreshAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;

        HttpHelper httpHelper = new HttpHelper(context, httpApi, new HttpListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onResult(int errCode, ResponseBean responseBean) {
                switch (errCode) {
                    case HttpHelper.HTTP_SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        if (responseBean.getData() != null) {
                            HttpUiBridge.this.dataRefreshAdapter.freshData(responseBean);
                            break;
                        }
                    case HttpHelper.HTTP_ERROR_AUTHORITY:
                    default:
                        Toast.makeText(context, "Account authorization failed", Toast.LENGTH_SHORT).show();
                    case HttpHelper.HTTP_ERROR_NO_NETWORK:
                    case HttpHelper.HTTP_ERROR_BUSINESS:
                        //// TODO: 4/08/2016 More to do for this error
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

            @Override
            public void onCancelled() {
            }
        });

        httpHelper.httpGet(requestBean, type);
    }
}