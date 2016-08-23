package com.jalenz.jalenlib.views;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jalen on 12/08/2016.
 */
public class BottomBarDependAppBarBehavior extends CoordinatorLayout.Behavior<View> {

        public BottomBarDependAppBarBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View bottomBar, View dependency) {
//            LogUtils.e("AppBarLayout:" + (dependency instanceof AppBarLayout));
            return dependency instanceof AppBarLayout;
        }


        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View bottomBar, View dependency) {
            float translationY = Math.abs(dependency.getTranslationY());
//            LogUtils.e("translationY:" + translationY);
            bottomBar.setTranslationY(translationY);
            return true;
        }
}
