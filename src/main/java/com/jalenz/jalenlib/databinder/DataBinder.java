package com.jalenz.jalenlib.databinder;

/**
 * Created by Jalen Zheng on 21/08/2016.
 */

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jalen Zheng on 21/08/2016.
 *
 * DataBinder bind Data to a View, then could refresh the Data onto the View, or update input into the Data
 */
public class DataBinder {
    static final int INVALID_RES_ID = -1;
    final static String TAG = DataBinder.class.getName();
    Object mDataObj;
    Class<?> mCls;
    View mLayoutView;
    Class<?> mResIdCls;
    HashMap<Integer, DataViewHolder> mDataViewMap = new HashMap<Integer, DataViewHolder>();
    HashSet<Integer> mInvalidResIds = new HashSet<Integer>();

    public DataBinder(Class<?> cls, View layoutView, Class<?> resIdCls) {
        mCls = cls;
        mLayoutView = layoutView;

        Field[] fields = cls.getDeclaredFields();
        for (Field f: fields) {
            int resId = getResId(f.getName());
            if(resId != INVALID_RES_ID) {
                DataViewHolder dataViewHolder = new DataViewHolder(f);
                mDataViewMap.put(resId, dataViewHolder);
            }

        }
    }

    public DataBinder(Object dataObj, View layoutView, Class<?> resIdCls) {
        this(dataObj.getClass(), layoutView, resIdCls);
        bindData(dataObj);
    }

    /**
     * Bind to Object Data
     * @param dataObj
     * @return
     */
    public boolean bindData(Object dataObj){
        try {
            if (mCls.isInstance(dataObj)) {
                mDataObj = dataObj;
                return true;
            } else {
                throw new Exception(this.getClass().getName() + ": Cannot bind "+dataObj.getClass()+ " object to binder(" + mCls.getName() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * update data onto the bound view
     */
    public void refreshView() {
        Set<Integer> keys = mDataViewMap.keySet();
        for (Integer key: keys) {
            DataViewHolder dataViewHolder = mDataViewMap.get(key);
            if (!dataViewHolder.refreshView(key)) {
                mInvalidResIds.add(key);
            }
        }
        //remove invalid ResIds
        for (Integer key: mInvalidResIds) {
            mDataViewMap.remove(key);
        }
        mInvalidResIds.clear();
    }

    /**
     * get the input from the bound view to update the bound data instance
     */
    public void updateData() {
        Set<Integer> keys = mDataViewMap.keySet();
        for (Integer key: keys) {
            DataViewHolder dataViewHolder = mDataViewMap.get(key);
            dataViewHolder.updateData(key);
        }
    }

    class DataViewHolder {
        DataViewResolver resolver;
        Field dataField;
        View view;

        public DataViewHolder(DataViewResolver resolver) {
            this.resolver = resolver;
        }

        public DataViewHolder(Field dataField) {
            this.dataField = dataField;
        }

        public Field getDataField() {
            return dataField;
        }

        public View getView() {
            return view;
        }

        public boolean refreshView(int resId) {
            try {
                if (view == null) {
                    view = mLayoutView.findViewById(resId);
                    if (view == null) {
                        if (resolver != null) {
                            throw new Exception(TAG + ": cannot find view by ResId:" + resId);
                        }
                        return false;
                    }
                }
                if (resolver != null) {
                    resolver.OnRefreshView(view, mDataObj);
                } else {
                    if (TextView.class.isInstance(view)) {
                        ((TextView) view).setText("" + dataField.get(mDataObj));
                    } else if (ImageView.class.isInstance(view)) {
                        ((ImageView) view).setImageDrawable((Drawable) dataField.get(mDataObj));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean updateData(int resId) {
            try {
                if (view == null) {
                    view = mLayoutView.findViewById(resId);
                    if (view == null) {
                        if (resolver != null) {
                            throw new Exception(TAG + ": cannot find view by ResId:" + resId);
                        }
                        return false;
                    }
                }
                if (resolver != null) {
                    resolver.OnUpdateData(view, mDataObj);
                } else {
                    if (EditText.class.isInstance(view)) {
                        dataField.set(mDataObj, ((EditText) view).getText());
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }


    public String getResIdName(int resId) {
        Field[] fields = mResIdCls.getFields();
        for (Field f: fields) {
            try {
                if(f.get(null).equals(resId)) {
                    return f.getName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public int getResId(String resIdName) {
        try {
            Log.d(TAG, "ResId(" + resIdName + "):" + (Integer) (mResIdCls.getField(resIdName).get(null)));
            return (Integer) (mResIdCls.getField(resIdName).get(null));
        } catch (Exception e) {
            return INVALID_RES_ID;
        }
    }

    public void printResIds() {
        Field[] fields = mResIdCls.getFields();
        Log.d( this.getClass().getName(),"R.id:" + fields);
        for (Field f: fields) {
            try {
                Log.d( this.getClass().getName(),"R.id." + f.getName() + ":" + f.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DataViewResolver
     * To support customized data-view binder
     */
    interface DataViewResolver {
        boolean OnRefreshView(View view, Object dataObj);
        boolean OnUpdateData(View view, Object dataObj);
    }

    /**
     * Add a data-view resolver to a given res id
     * @param viewResId
     * @param resolver
     */
    public void addDataViewResolver (int viewResId, DataViewResolver resolver) {
        DataViewHolder dataViewHolder = new DataViewHolder(resolver);
        mDataViewMap.put(viewResId, dataViewHolder);
    }
}
