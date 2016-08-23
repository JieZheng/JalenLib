/**
 * 
 */
package com.jalenz.jalenlib.httphelper;

import com.jalenz.jalenlib.model.ResponseBean;

/**
 * @author Jalen Zheng
 *
 */
public interface HttpListener {
    public void onResult(int errCode, ResponseBean responseBean);

    public void onCancelled();

    public void onStart();
}