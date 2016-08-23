/**
 * 
 */
package com.jalenz.jalenlib.model;

import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.Header;

import java.util.HashMap;

/**
 * @author Jalen Zheng
 *
 * Response Data Model
 */
public class ResponseBean extends BaseBean {
    private int code = -1;
    private String description = null;
    private String message = null;
    private String stackTrace = null;
    private HashMap<String, String> errorMessage = null;
    private BaseBean data = null;

    /**
     * Http info
     */
    transient ResponseInfo<String> responseInfo;
    transient String HttpCacheType;

    public String getHttpCacheType() {
        return HttpCacheType;
    }

    public void setHttpCacheType(String httpCacheType) {
        HttpCacheType = httpCacheType;
    }

    public ResponseInfo<String> getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo<String> responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getResponseHeader(String name) {
        String value = "";
        if (responseInfo != null) {
            Header[] headers = responseInfo.getHeaders(name);
            if (headers != null  && headers.length >  0) {
                value = headers[0].getValue();
            }
        }
        return value;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @return the stackTrace
     */
    public String getStackTrace() {
        return stackTrace;
    }
    /**
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    /**
     * @return the data
     */
    public BaseBean getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(BaseBean data) {
        this.data = data;
    }
    /**
     * @return the errMsg
     */
    public HashMap<String, String> getErrorMessage() {
        return errorMessage;
    }
    /**
     * @param errorMessage the errMsg to set
     */
    public void setErrorMessage(HashMap<String, String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", errorMessage=" + errorMessage +
                ", data=" + data +
                "} " + super.toString();
    }
}
