package com.jyq.android.ui.ptr.refreshview;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           佛祖保佑       永无BUG
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import com.jyq.android.ui.R;
import com.jyq.android.ui.ptr.IRefreshView;


/**
 * Created by Administrator on 2017/3/14.
 */

public class CarRefreshView extends RelativeLayout implements IRefreshView {


    private AppCompatImageView backgroundImage;
    private AppCompatImageView carImage;
    private AppCompatImageView frontWheelImage;
    private AppCompatImageView backWheelImage;
    private RotateAnimation wheelAnimation;
    private RotateAnimation roadAnimation;
    private ObjectAnimator carAnimation;
    private int mHeight;
    public CarRefreshView(Context context) {
        this(context, null);
    }

    public CarRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.car_header_layout, this, true);
        backgroundImage = (AppCompatImageView) findViewById(R.id.car_header_bg);
        carImage = (AppCompatImageView) findViewById(R.id.car_header_car);
        frontWheelImage = (AppCompatImageView) findViewById(R.id.car_header_frontwheel);
        backWheelImage = (AppCompatImageView) findViewById(R.id.car_header_backwheel);
        if (isInEditMode())
            return;
        prepareAnimator();
    }

    private void prepareAnimator() {
        wheelAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        wheelAnimation.setInterpolator(new LinearInterpolator());
        wheelAnimation.setRepeatCount(-1);
        wheelAnimation.setDuration(1000);
        roadAnimation = new RotateAnimation(0, -359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        roadAnimation.setInterpolator(new LinearInterpolator());
        roadAnimation.setRepeatCount(-1);
        roadAnimation.setDuration(4000);
        carAnimation = ObjectAnimator.ofFloat(carImage, "translationY", 0, 2.0f, 2.0f, 0);
        carAnimation.setRepeatCount(-1);
        carAnimation.setDuration(1000);
    }

    @Override
    public int getContentSize() {
        return getHeight();
    }

    @Override
    public void reset() {
        resetAnim();
    }

    @Override
    public void pull() {

    }

    @Override
    public void refreshing() {
        starAnim();
    }

    private void starAnim() {
        frontWheelImage.startAnimation(wheelAnimation);
        backWheelImage.startAnimation(wheelAnimation);
        backgroundImage.startAnimation(roadAnimation);
        carAnimation.start();
    }

    private void resetAnim() {
        frontWheelImage.clearAnimation();
        backWheelImage.clearAnimation();
        backgroundImage.clearAnimation();
        carAnimation.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: " +getMeasuredHeight());
//        mHeight=getMeasuredHeight();
    }

    private static final String TAG = "CarHeaderView";

    @Override
    public void onPull(float currentPos) {

    }

    @Override
    public void complete() {
        resetAnim();
    }


    @Override
    public View getView() {
        return this;
    }
}
