/**
 *
 */
package com.jalenz.jalenlib.model;

import com.lidroid.xutils.http.ResponseInfo;

/**
 * @author Jalen Zheng
 *
 * Base Model
 */
public class BaseBean {
    public static final int DATA_TYPE_OBJECT = 0;
    public static final int DATA_TYPE_ARRAY = 1;
    protected int _dataType = DATA_TYPE_OBJECT;

    @Override
    public String toString() {
        return "BaseBean{" +
                "_dataType=" + _dataType +
                '}';
    }
}
