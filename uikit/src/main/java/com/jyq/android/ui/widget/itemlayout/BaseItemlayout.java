package com.jyq.android.ui.widget.itemlayout;

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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/6.
 */

public abstract class BaseItemlayout extends RelativeLayout{


    public BaseItemlayout(Context context) {
        super(context);
    }

    public BaseItemlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseItemlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseItemlayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    protected  abstract void initView();
    protected abstract void setTitle(CharSequence title);
    protected abstract void setIcon(@DrawableRes int iconRes);
    protected abstract void showNumberBadge(int number);
    protected abstract void showBadgeWithImage(String imageUrl);
    protected abstract void shwoBadgeWithImage(@DrawableRes int imageRes);
    protected abstract void hideNumberBadge();
}
