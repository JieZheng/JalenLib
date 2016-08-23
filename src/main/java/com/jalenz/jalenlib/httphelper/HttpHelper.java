/**
 *
 */
package com.jalenz.jalenlib.httphelper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jalenz.jalenlib.model.BaseBean;
import com.jalenz.jalenlib.model.RequestBean;
import com.jalenz.jalenlib.model.ResponseBean;
import com.jalenz.jalenlib.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Jalen Zheng
 *
 */
public class HttpHelper {
    private String requestUrl = "";
    protected Context context = null;
    private HttpListener listener = null;
    private HttpListener pageManagerListener = null;
    private boolean allowToast = false;
    private boolean isLoadingData = false;
    private static HttpListener globalListener = null;
    
    //Timeout of connection
    private final static int TIMEOUT_OF_CONNECTION = 30000; //in milliseconds
    //Timeout for the cache
    private final static int EXPIRY_OF_CACHE = 5; //in milliseconds

    //Errors definition
    public final static int HTTP_SUCCESS = 0;
    public final static int HTTP_ERROR_AUTHORITY = 1;
    public final static int HTTP_ERROR_CHECK = 2;
    public final static int HTTP_ERROR_BUSINESS = 3;
    public final static int HTTP_ERROR_API_EXCEPTION = 4;
    public final static int HTTP_ERROR_AUTHORITY_TOKEN_INVALID = 101; //Invalide token (when auth token string was invalid)
    public final static int HTTP_ERROR_AUTHORITY_TOKEN_EXPIRED = 102; //Token verification failed (Get GUID from auth token, compare it with the GUID on redis. if different, means it is invalid. This was designed to avoid one account login on multiple devices at same time)
    //Defined by App if the error code is minus
    public final static int HTTP_ERROR_UNKOWN = -1;
    public final static int HTTP_ERROR_NO_NETWORK = -2;
    public final static int HTTP_ERROR_TIMEOUT = -3;
    public final static int HTTP_ERROR_SERVER_ERROR = -4;
    public final static int HTTP_ERROR_CANCELLED = -5;

    public final static String HTTP_HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
    public final static String HTTP_HEADER_AUTHORIZATION = "Authorization";
    
    public static Map<String, String> httpGlobalHeaders = new HashMap<String, String>();
    
    // keys will be removed from request parameters
    static HashSet<String> filterKeys = new HashSet<String>();

	public HttpHelper(Context context, String url, HttpListener httpListener) {
	    requestUrl = url;
	    this.context = context;
	    this.listener = httpListener;

	    // Init filter keys to remove from request parameters
	    filterKeys.add("IsHttpCached");
        filterKeys.add("_dataType");
	}
	
	public static void configHttpGlobalHeaders(Map<String, String> headers) {
        if(headers != null) {
            httpGlobalHeaders.clear();
            httpGlobalHeaders.putAll(headers);
        }
    }

    /**
     * Set page manager listener
     * @param httpListener
     */
    protected void setPageManagerListener(HttpListener httpListener){
        pageManagerListener = httpListener;
    }

    /**
     * RESTful Get request
     * @param requestParamsBean
     * @param beanType
     * @return
     */
    public boolean httpGet(RequestBean requestParamsBean, Type beanType) {
        HttpSender httpSender = new HttpSender(requestUrl, beanType);
        return httpSender.sendUrl(HttpMethod.GET, requestParamsBean, null);
    }

    /**
     * RESTful Post request
     * @param requestParamsBean
     * @param data
     * @param beanType
     * @return
     */
    public boolean httpPost(RequestBean requestParamsBean, BaseBean data, Type beanType) {
        HttpSender httpSender = new HttpSender(requestUrl, beanType);
        return httpSender.sendUrl(HttpMethod.POST, requestParamsBean, data);
    }

    /**
     * RESTful Put request
     * @param requestParamsBean
     * @param data
     * @return
     */
    public boolean httpPut(RequestBean requestParamsBean, BaseBean data) {
        HttpSender httpSender = new HttpSender(requestUrl);
        return httpSender.sendUrl(HttpMethod.PUT, requestParamsBean, data);
    }

    /**
     * RESTful Delete request
     * @param requestParamsBean
     * @return
     */
    public boolean httpDelete(RequestBean requestParamsBean) {
        HttpSender httpSender = new HttpSender(requestUrl);
        return httpSender.sendUrl(HttpMethod.DELETE, requestParamsBean, null);
    }

    public HttpListener getHttpListener() {
        return this.listener;
    }

	public static Map<String, String> getHttpGlobalHeaders() {
        return httpGlobalHeaders;
    }
	
	public boolean isLoadingData() {
	    return isLoadingData;
	}

	public boolean isAllowToast() {
        return allowToast;
    }

    public static HttpListener getGlobalListener() {
        return globalListener;
    }

    /**
     * Set Global listener, any request will trigger Global listener
     * @param globalListener
     */
    public static void setGlobalListener(HttpListener globalListener) {
        HttpHelper.globalListener = globalListener;
    }

    /**
     * Allow HttpHelper show/hide toasts
     * @param allowToast
     */
    public void setAllowToast(boolean allowToast) {
        this.allowToast = allowToast;
    }

    private void callListenersOnResult(int errCode, ResponseBean responseBean) {
        if (pageManagerListener != null) {
            pageManagerListener.onResult(errCode, responseBean);
        }
	    if (listener != null) {
            listener.onResult(errCode, responseBean);
        }
        if (globalListener != null) {
            listener.onResult(errCode, responseBean);
        }
	}

	private class HttpSender {
	    HttpUtils httpUtils = null;
	    Type responseType = null;
	    String url = "";
	    String realUrl= "";

        public HttpSender(String url) {
            this.url = url;
        }

        public HttpSender(String url, Type responseType) {
            this.url = url;
            this.responseType = responseType;
        }

        private boolean isValidRequestParams(String key, String value) {
            String filterKey;
            // Check StartIndex and RequestCount
            if (key.equals("StartIndex") && value.equals("-1")) {
                LogUtils.d("Filter out " + key + ":" + value);
                return false;
            }
            if (key.equals("RequestCount") && value.equals("-1")) {
                LogUtils.d("Filter out " + key + ":" + value);
                return false;
            }
            if (key.equals("pageNo") && value.equals("-1")) {
                LogUtils.d("Filter out " + key + ":" + value);
                return false;
            }

            Iterator<String> it = filterKeys.iterator();
            while (it.hasNext()) {
                filterKey = (String) it.next();
                if (filterKey.equals(key)) {
                    LogUtils.d("Filter out " + key + ":" + value);
                    return false;
                }
            }
            return true;
        }

        // build the real URL with URL template and request parameters
        private String buildRealUrl(String urlTemplet, RequestBean requestParamsBean) {
            String realUrl = null;
            Gson gson = new Gson();
            HashMap<String, String> realParams = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            String tmpUrl = new String(urlTemplet);
            try {
                if (requestParamsBean != null) {
                    String jsonStr = gson.toJson(requestParamsBean);
                    Type type = new TypeToken<HashMap<String, String>>() {}.getType();
                    map = gson.fromJson(jsonStr, type);
                    Set<String> keys = map.keySet();
                    Iterator<String> it = keys.iterator();

                    // Replace patterns
                    while (it.hasNext()) {
                        String key = it.next();
                        LogUtils.d("key=" + key + "   value:" + map.get(key));

                        String pattern = "{" + key + "}";
                        String pattern2 = ":" + key;
                        if (tmpUrl.contains(pattern)) {     //check pattern {key}
                            tmpUrl = tmpUrl.replace(pattern, URLEncoder.encode(map.get(key)));
                        } else if (tmpUrl.contains(pattern2)){ //check pattern :key
                            tmpUrl = tmpUrl.replace(pattern2,  URLEncoder.encode(map.get(key)));
                        } else {
                            realParams.put(key, URLEncoder.encode(map.get(key)));
                        }
                    }
                }

                if (tmpUrl.contains("{")) {
                    LogUtils.d("Real URL:" + tmpUrl + " Still has pattern");
                    return null;
                }

                // Add parameters
                if (realParams.size() > 0) {
                    tmpUrl += "?";
                    Set<String> keys = realParams.keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (isValidRequestParams(key, realParams.get(key))) {
                            if (!tmpUrl.endsWith("?")) {
                                tmpUrl += "&";
                            }

                            LogUtils.d("Real param: key=" + key + "  value=" + realParams.get(key));
                            tmpUrl += key + "=" + realParams.get(key);
                        }
                    }
                }

                realUrl = tmpUrl;
                LogUtils.d("Real URL:" + realUrl);
            }catch (Exception e){
                LogUtils.e("Failed to build real URL from URL templet:" + url + " with requestParams:" + requestParamsBean.toString());
                e.printStackTrace();
            }
            return realUrl;
        }

        private boolean sendUrl(HttpMethod method, RequestBean requestParamsBean, BaseBean data) {
            if (isLoadingData == true) {
                return false;
            }
            
            isLoadingData = true;
            
            // Check device network status
            if (NetworkHelper.isNetworkConnected(context) == false) {
                isLoadingData = false;
                LogUtils.e("There is no network for HTTP connection.");
                if (allowToast) {
                    ToastUtil.TextToastOnce(context, "Please check the connection state of your network.");
                }
                callListenersOnResult(HTTP_ERROR_NO_NETWORK, null);
                return false;
            }

            //Build real URL from URL template
            if (httpUtils == null) {
                httpUtils = new HttpUtils(TIMEOUT_OF_CONNECTION);
                httpUtils.configCurrentHttpCacheExpiry(EXPIRY_OF_CACHE);
            }
            realUrl = buildRealUrl(url, requestParamsBean);
            if (realUrl == null) {
                isLoadingData = false;
                LogUtils.e("Can not build real URL from URL templet: " + url +
                        "\n with request bean:" + ((requestParamsBean == null) ? "null" :requestParamsBean.toString()));
                return false;
            }

            // Set post json data
            RequestParams requestParams = new RequestParams();
            requestParams.setContentType("application/json");
            if (httpGlobalHeaders.size() > 0) {
                Set<Map.Entry<String, String>> headers = httpGlobalHeaders.entrySet();
                for (Iterator<Map.Entry<String, String>> it = headers.iterator(); it.hasNext(); ) {
                    Map.Entry<String, String> header = (Map.Entry<String, String>) it.next();
                    requestParams.addHeader(header.getKey(), header.getValue());
                }
            }
           
            if (data != null) {
                Gson gson = new Gson();
                String dataJsonStr = gson.toJson(data);
                StringEntity dataEntity;
                try {
                    dataEntity = new StringEntity(dataJsonStr, "UTF-8");
                } catch (Exception e1) {
                    LogUtils.e("Failed to generate StringEntity for :" + dataJsonStr);
                    e1.printStackTrace();
                    isLoadingData = false;
                    return false;
                }
                requestParams.setBodyEntity(dataEntity);
                LogUtils.d("HttpUtils Charset:" + requestParams.getCharset());
            }
            realUrl=realUrl.replaceAll(" ", "%20");
            // Send http request
            httpUtils.send(method, realUrl, requestParams, new RequestCallBack<String>() {

                @Override
                public void onStart() {
                    isLoadingData = false;
                    super.onStart();
                    if (listener != null) {
                        listener.onStart();
                    }
                    if (pageManagerListener != null) {
                        pageManagerListener.onStart();
                    }
                    if (globalListener != null) {
                        globalListener.onStart();
                    }
                }

                @Override
                public void onCancelled() {
                    isLoadingData = false;
                    super.onCancelled();
                    if (listener != null) {
                        listener.onCancelled();
                    }
                    if (pageManagerListener != null) {
                        pageManagerListener.onCancelled();
                    }
                    if (globalListener != null) {
                        globalListener.onCancelled();
                    }
                    callListenersOnResult(HTTP_ERROR_CANCELLED, null);
                }

                @Override
                public void onFailure(HttpException exception, String paramString) {
                    isLoadingData = false;
                    LogUtils.e("Network access failed. \n-- code:" + exception.getExceptionCode() + "\n--URL:" + realUrl + "\n--Info:" + paramString);
                    if (allowToast) {
                        ToastUtil.TextToastOnce(context, "Network access failed.");
                    }
                    callListenersOnResult(HTTP_ERROR_SERVER_ERROR, null);
                }

                @Override
                public void onSuccess(ResponseInfo<String> info) {
                    isLoadingData = false;
                    LogUtils.d("Response of API:" + realUrl + "\n" + info.result);
                    try {
                        String log;
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResponseBean>(){}.getType();
                        ResponseBean responseBean = gson.fromJson(info.result, type);
                        BaseBean data = responseBean.getData();

                        try {
                            if (responseType != null) {
                                JSONObject object = new JSONObject(info.result);
                                if (object.has("data")) {
                                    String dataString = object.getString("data");
                                    if (dataString != null) {
                                        //LogUtils.d("dataString:" + dataString);
                                        data = gson.fromJson(dataString, responseType);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e("URL=" + realUrl + "\n" + "Exception while try to generate Data bean!");
                            e.printStackTrace();
                            if (allowToast) {
                                ToastUtil.TextToastOnce(context, "Failed to process the data");
                            }
                            callListenersOnResult(HTTP_ERROR_UNKOWN, null);
                            return;
                        }

                        if (data == null) {
                            data = new BaseBean();
                        }
                        // set response data
                        responseBean.setData(data);
                        // set response info
                        responseBean.setResponseInfo(info);

                        if (allowToast && responseBean.getCode() != HTTP_SUCCESS && responseBean.getMessage() != null && responseBean.getMessage().length() > 0) {
                            ToastUtil.TextToastOnce(context, responseBean.getMessage());
                        }
                        switch (responseBean.getCode()) {
                        case HTTP_SUCCESS:
                            LogUtils.d("Successfully to access URL=" + realUrl);
                            callListenersOnResult(responseBean.getCode(), responseBean);
                            break;
                        case HTTP_ERROR_AUTHORITY:
                            callListenersOnResult(responseBean.getCode(), responseBean);
                            LogUtils.e("HTTP_ERROR_AUTHORITY: URL=" + realUrl);
                            break;
                        case HTTP_ERROR_API_EXCEPTION:
                            String stackTrace = responseBean.getStackTrace() != null ? responseBean.getStackTrace() : "No stacktrace!";
                            log = "URL=" + realUrl + "\n" + "Exception from server for API:" + realUrl + "\n" + stackTrace;
                            LogUtils.e(log);
                            //StErrUpload.save(log);
                            callListenersOnResult(responseBean.getCode(), responseBean);
                            break;
                        case HTTP_ERROR_BUSINESS:
                            callListenersOnResult(responseBean.getCode(), responseBean);
                            break;
                        case HTTP_ERROR_CHECK:
                            if (responseBean.getErrorMessage() != null) {
                                callListenersOnResult(responseBean.getCode(), responseBean);
                                LogUtils.e("URL=" + realUrl +"\nErrorMessages for HTTP_ERROR_CHECK(" + HTTP_ERROR_CHECK + "):" + responseBean.getErrorMessage());
                            } else {
                                LogUtils.e("URL=" + realUrl +"\nCan not find ErrMsg for HTTP_ERROR_CHECK(" + HTTP_ERROR_CHECK + ")");
                            }
                            break;
                        default:
                            callListenersOnResult(responseBean.getCode(), responseBean);
                            LogUtils.e("URL=" + realUrl + "\n  ErrCode:" + responseBean.getCode());
                            break;
                        }
                    } catch (Exception e) {
                        LogUtils.e("Exception while access URL:" +  realUrl);
                        e.printStackTrace();
                        if (allowToast) {
                            ToastUtil.TextToastOnce(context, "Failed to fetch data from Internet");
                        }
                        callListenersOnResult(HTTP_ERROR_UNKOWN, null);
                        return;
                    }
                }
            });
            return true;
        }
	}
}
