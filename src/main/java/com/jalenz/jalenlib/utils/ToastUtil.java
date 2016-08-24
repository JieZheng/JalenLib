package com.jalenz.jalenlib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.lidroid.xutils.util.LogUtils;


public class ToastUtil {
    private static Toast toast = null;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    private static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    private static long MIN_INTERVAL_BETWEEN_TWO_TOAST = 2000;
    private static long timeOfLastToast = 0;

    public static void TextToast(Context context, CharSequence text) {
        TextToast(context, text, LENGTH_SHORT);
    }

    /**
     * 普通文本消息提示
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void TextToast(Context context, CharSequence text, int duration) {
        if (toast == null) {
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

    public static void TextToastOnce(Context context, CharSequence text) {
        TextToastOnce(context, text, LENGTH_SHORT);
    }

    /**
     * 普通文本消息提示  (MIN_INTERVAL_BETWEEN_TWO_TOAST 毫秒内只提示一次)
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void TextToastOnce(Context context, CharSequence text, int duration) {
        long timeOfToast = System.currentTimeMillis();
//            LogUtils.w("\ncurrent time:" + timeOfToast + " lasttime:" + timeOfToast + " interval:" + (timeOfToast - timeOfLastToast) + " text:" + text);
        if (timeOfToast - timeOfLastToast < MIN_INTERVAL_BETWEEN_TWO_TOAST) {
            LogUtils.w("Ignored Toast:" + text);
            return;
        } else {
            timeOfLastToast = timeOfToast;
        }

        if (toast == null) {
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

    /**
     * Show image toast
     *
     * @param context
     * @param ImageResourceId
     * @param text
     * @param duration
     */
    public static void ImageToast(Context context, int ImageResourceId, CharSequence text, int duration) {
        Toast imgToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        imgToast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = imgToast.getView();
        ImageView img = new ImageView(context);
        img.setImageResource(ImageResourceId);
        LinearLayout ll = new LinearLayout(context);
        ll.addView(img);
        ll.addView(toastView);
        imgToast.setView(ll);
        imgToast.show();
    }

}
