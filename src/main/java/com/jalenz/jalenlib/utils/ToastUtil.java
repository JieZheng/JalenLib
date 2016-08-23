package com.jalenz.jalenlib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import java.util.Date;


public class ToastUtil {
        private static Toast toast = null;
        public static int LENGTH_LONG = Toast.LENGTH_LONG;  
        private static int LENGTH_SHORT = Toast.LENGTH_SHORT;  
        private static Context context = null;
        private static long MIN_INTERVAL_BETWEEN_TWO_TOAST = 2000;
        private static long timeOfLastToast = 0;
	      
		public static void  TextToast(Context context,CharSequence text){
		    TextToast(context, text, LENGTH_SHORT);
	    }  
	      
		
	    /** 
	     * 普通文本消息提示 
	     * @param context 
	     * @param text 
	     * @param duration 
	     */  
	    public static void TextToast(Context context,CharSequence text,int duration){ 
	    	if (toast == null) {
	    	    ToastUtil.context = context;
	            //创建一个Toast提示消息  
	            toast = Toast.makeText(context, text, duration);  
	            //设置Toast提示消息在屏幕上的位置  
	            toast.setGravity(Gravity.CENTER, 0, 0);  
	        } else {  
	        	toast.setText(text);  
	        	toast.setDuration(duration);  
	        }  
	    	//显示消息  
	    	toast.show();  
	    }
	    
        public static void  TextToastOnce(Context context,CharSequence text){
            TextToastOnce(context, text, LENGTH_SHORT);
        }  
	    
        /** 
         * 普通文本消息提示  (MIN_INTERVAL_BETWEEN_TWO_TOAST 毫秒内只提示一次)
         * @param context 
         * @param text 
         * @param duration 
         */  
        public static void TextToastOnce(Context context,CharSequence text,int duration) {
            long timeOfToast = DateUtil.getTimestampMillis(new Date());
//            LogUtils.w("\ncurrent time:" + timeOfToast + " lasttime:" + timeOfToast + " interval:" + (timeOfToast - timeOfLastToast) + " text:" + text);
            if (timeOfToast - timeOfLastToast < MIN_INTERVAL_BETWEEN_TWO_TOAST) {
                LogUtils.w("Ignored Toast:" + text);
                return;
            } else {
                timeOfLastToast = timeOfToast;
            }

            if (toast == null) {
                ToastUtil.context = context;
                //创建一个Toast提示消息  
                toast = Toast.makeText(context, text, duration);  
                //设置Toast提示消息在屏幕上的位置  
                toast.setGravity(Gravity.CENTER, 0, 0);  
            } else {  
                toast.setText(text);  
                toast.setDuration(duration);  
            }  
            //显示消息  
            toast.show();  
        }
	    

	    public static void ImageToast(Context context,int ImageResourceId,CharSequence text,int duration){  
	        //鍒涘缓涓�涓猅oast鎻愮ず娑堟伅  
	        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);  
	        //璁剧疆Toast鎻愮ず娑堟伅鍦ㄥ睆骞曚笂鐨勪綅缃�  
	        toast.setGravity(Gravity.CENTER, 0, 0);  
	        //鑾峰彇Toast鎻愮ず娑堟伅閲屽師鏈夌殑View  
	        View toastView = toast.getView();  
	        //鍒涘缓涓�涓狪mageView  
	        ImageView img = new ImageView(context);  
	        img.setImageResource(ImageResourceId);  
	        //鍒涘缓涓�涓狶ineLayout瀹瑰櫒  
	        LinearLayout ll = new LinearLayout(context);  
	        //鍚慙inearLayout涓坊鍔營mageView鍜孴oast鍘熸湁鐨刅iew  
	        ll.addView(img);  
	        ll.addView(toastView);  
	        //灏哃ineLayout瀹瑰櫒璁剧疆涓簍oast鐨刅iew  
	        toast.setView(ll);  
	        //鏄剧ず娑堟伅  
	        toast.show();  
	    }

}
