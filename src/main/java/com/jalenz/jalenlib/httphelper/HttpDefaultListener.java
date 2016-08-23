/**
 * 
 */
package com.jalenz.jalenlib.httphelper;

import com.jalenz.jalenlib.model.ResponseBean;

/**
 * @author Jalen Zheng
 *
 */
public abstract class HttpDefaultListener implements HttpListener {

    /* (non-Javadoc)
     * @see com.sotao.stlib.utils.httptools.HttpListener#onSuccess(int, com.sotao.stlib.entity.base.BaseBean)
     */
    @Override
    public abstract void onResult(int errCode, ResponseBean responseBean) ;

    /* (non-Javadoc)
     * @see com.sotao.stlib.utils.httptools.HttpListener#onCancelled()
     */
    @Override
    public void onCancelled() {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sotao.stlib.utils.httptools.HttpListener#onStart()
     */
    @Override
    public void onStart() {
        // Do nothing
        
    }

}
