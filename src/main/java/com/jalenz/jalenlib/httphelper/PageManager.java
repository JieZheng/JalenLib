/**
 *
 */
package com.jalenz.jalenlib.httphelper;

import com.jalenz.jalenlib.model.ArrayInfoBean;
import com.jalenz.jalenlib.model.BaseBean;
import com.jalenz.jalenlib.model.DataListBean;
import com.jalenz.jalenlib.model.RequestBean;
import com.jalenz.jalenlib.model.ResponseBean;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Type;


/**
 * @author Jalen Zheng
 * Designed to manage pages when loading more list data by HttpHelper
 */
public class PageManager {
    private HttpHelper httpHelper = null;
    private HttpListener httpListener = null;
    private OnPageLoadingListener pageLoadingFinished = null;
    private int pageSize = 0;
    private int total = 0;
//    private int startIndex = 0;         //index start from 0
    private int pageIndex = -1;         //index start from 0
    private int arraySize = 0;
    private boolean isEnd = false;
    private Type arrayBeanType;
    private RequestBean requestBean;
    public final static int DEFAULT_PAGE_SIZE = 2;


    public PageManager(HttpHelper httpHelper, RequestBean requestBean, Type listBeanType) {
        init(httpHelper, requestBean, listBeanType, DEFAULT_PAGE_SIZE);
    }

    public PageManager(HttpHelper httpHelper, RequestBean requestBean, Type listBeanType, int pageSize) {
        init(httpHelper, requestBean, listBeanType, pageSize);
    }

    private void init(HttpHelper httpHelper, RequestBean requestBean, Type listBeanType, int pageSize) {
        this.httpHelper = httpHelper;
        this.requestBean = requestBean;
        this.arrayBeanType = listBeanType;
        this.pageSize = pageSize;
        this.requestBean.setPageIndex(0);
        httpListener = new PageManagerHttpListener();
        httpHelper.setPageManagerListener(httpListener);
    }

    //Page Manager's http listener for HttpHelper
    class PageManagerHttpListener implements HttpListener {
        @Override
        public void onResult(int errCode, ResponseBean responseBean) {
            String log;
            if (errCode == HttpHelper.HTTP_SUCCESS) {
                if(responseBean.getData() instanceof DataListBean) {
                    DataListBean<BaseBean> listBean = (DataListBean<BaseBean>) responseBean.getData();
                    ArrayInfoBean listInfo = listBean.getArrayInfo();
                    if (listInfo != null) {
                        total = listInfo.getTotal();
                        arraySize = listInfo.getArraySize();
                        if (arraySize < pageSize || listInfo.isEnd()) {
                            isEnd = true;
                        }
                        if (listInfo.getPageIndex() > 0) {
                            pageIndex = listInfo.getPageIndex();
                        }
//                        startIndex += arraySize; // Update startIndex only if the previous request is successful

                        //update arrayinfo
//                        listBean.getArrayInfo().setStartIndex(startIndex);
                        listBean.getArrayInfo().setPageIndex(pageIndex);
                        listBean.getArrayInfo().setEnd(isEnd);
                        listBean.getArrayInfo().setArraySize(arraySize);
                    } else {
                        log = "ListInfo is null while successfully call:";
                        LogUtils.e(log);
                    }
                }
            }
            //call OnPageLoadingListener interface
            if (pageLoadingFinished != null) {
                pageLoadingFinished.OnPageLoadingFinished(errCode, pageIndex, pageSize, arraySize, isEnd);
            }
        }

        @Override
        public void onCancelled() {

        }

        @Override
        public void onStart() {

        }
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public boolean getNextItems(int number) {
        if (isEnd) {
//            ToastUtil.TextToastOnce(httpHelper.context, "All data loaded");
            return false;
        }
        pageIndex++;
        requestBean.setPageIndex(pageIndex);
        requestBean.setPageSize(number);
//        requestBean.setStartIndex(startIndex);
        if (pageLoadingFinished != null) {
            pageLoadingFinished.OnPageLoadingStarted(pageIndex, pageSize);
        }
        return httpHelper.httpGet(requestBean, arrayBeanType);
    }

    public boolean isEnd(){
        return isEnd;
    }

    public int getTotal() {
        return total;
    }

    public boolean getNextPage() {
        return getNextItems(pageSize);
    }

    public void refresh() {
        this.requestBean.setPageIndex(-1);
//        this.requestBean.setStartIndex(0);
//        this.startIndex = 0;
        this.pageIndex = -1;
        this.isEnd = false;
        getNextPage();
    }


    /**
     * Get OnPageLoadingListener
     * @return OnPageLoadingListener
     */
    public OnPageLoadingListener getPageLoadingFinished() {
        return pageLoadingFinished;
    }

    /**
     * Set OnPageLoadingListener
     * @param pageLoadingFinished
     */
    public void setPageLoadingFinished(OnPageLoadingListener pageLoadingFinished) {
        this.pageLoadingFinished = pageLoadingFinished;
    }

    /**
     * Page loading listener for PageManager
     */
    public interface OnPageLoadingListener {
        void OnPageLoadingStarted(int pageIndex, int pageSize);
        void OnPageLoadingFinished(int errCode, int pageIndex, int pageSize, int arraySize, boolean isEnd);
    }
}
