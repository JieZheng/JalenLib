/**
 * 
 */
package com.jalenz.jalenlib.model;

/**
 * @author Jalen Zheng
 *
 * Base Request Data Model
 */
public class RequestBean extends BaseBean {
    private int pageSize = -1;
    private int pageIndex = -1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                "} " + super.toString();
    }
}
