package com.jalenz.jalenlib.httphelper;

import com.jalenz.jalenlib.model.ResponseBean;

/**
 * Created by  Jalen Zheng on 13/08/2016.
 *
 * DataRefreshAdapter to update Object Data
 */
public interface DataRefreshAdapter {
    void freshData(ResponseBean responseBean);
}
