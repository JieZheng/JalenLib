package com.jalenz.jalenlib.views;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by  Jalen Zheng on 12/08/2016.
 */
public class BottomBarBehavior extends CoordinatorLayout.Behavior<View> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    ViewPropertyAnimator animator;

    private int sinceDirectionChange;


    public BottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //check the move on vertical orientation
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    //show/hide bottom view according to the distance of the movement
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && sinceDirectionChange < 0 || dy < 0 && sinceDirectionChange > 0) {
            sinceDirectionChange = 0;
        }
        sinceDirectionChange += dy;
        if (sinceDirectionChange > child.getHeight()/4 && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (sinceDirectionChange < -(child.getHeight()/5) && child.getVisibility() == View.GONE) {
            show(child);
        }
    }


    private void hide(final View view) {
        if (animator != null) {
            return;
        }
        animator = view.animate().translationY(view.getHeight()).setInterpolator(INTERPOLATOR).setDuration(500);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                LogUtils.e("hide onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
//                LogUtils.e("hide onAnimationEnd");
                BottomBarBehavior.this.animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
//                LogUtils.e("hide onAnimationCancel");
                BottomBarBehavior.this.animator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }


    private void show(final View view) {
        if (animator != null) {
            return;
        }
        animator = view.animate().translationY(0).setInterpolator(INTERPOLATOR).setDuration(500);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                LogUtils.e("show onAnimationStart");
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                LogUtils.e("show onAnimationEnd");
                BottomBarBehavior.this.animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
//                LogUtils.e("show onAnimationCancel");
                BottomBarBehavior.this.animator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

}
