package com.jalenz.jalenlib.httphelper;

import com.jalenz.jalenlib.model.BaseBean;

import java.util.List;

/**
 * Created by Jalen Zheng on 13/08/2016.
 *
 * DataListRefreshAdapter designed to update list data
 */
public interface DataListRefreshAdapter <B extends BaseBean>{
    /**
     * Get data list from Adapter
     * @return
     */
    List<B> getDataList();

    /**
     * Add data list into adapter
     * @param dataList
     */
    void addDataList(List<B> dataList);

    /**
     * Update the whole data list in the adapter
     * @param dataList
     */
    void refreshDataList(List<B> dataList);
}
