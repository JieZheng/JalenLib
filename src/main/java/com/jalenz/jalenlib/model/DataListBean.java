/**
 *
 */
package com.jalenz.jalenlib.model;

import java.util.ArrayList;

/**
 * @author Jalen Zheng
 *
 * Base List Data Model
 */
public class DataListBean<T> extends BaseBean {
    ArrayInfoBean arrayInfo = null;
    ArrayList<T> array = null;

    public DataListBean(ArrayList<T> listItems) {
        _dataType = DATA_TYPE_ARRAY;
        array = listItems;
    }

    public ArrayInfoBean getArrayInfo() {
        return arrayInfo;
    }

    public void setArrayInfo(ArrayInfoBean arrayInfo) {
        this.arrayInfo = arrayInfo;
    }

    public ArrayList<T> getArray() {
        return array;
    }

    public void setArray(ArrayList<T> array) {
        this.array = array;
    }

    @Override
    public String toString() {
        return "DataListBean{" +
                "arrayInfo=" + arrayInfo +
                ", array=" + array +
                "} " + super.toString();
    }
}
