/**
 *
 */
package com.jalenz.jalenlib.model;

/**
 * @author Jalen Zheng
 *
 * Array Info for Data List Model
 */
public class ArrayInfoBean extends BaseBean {
    private int total = -1;
    private int pageIndex = -1;
    private int startIndex = -1;
    private int arraySize = -1;
    private boolean isEnd = false;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "ArrayInfoBean{" +
                "total=" + total +
                ", pageIndex=" + pageIndex +
                ", startIndex=" + startIndex +
                ", arraySize=" + arraySize +
                ", isEnd=" + isEnd +
                "} " + super.toString();
    }
}
