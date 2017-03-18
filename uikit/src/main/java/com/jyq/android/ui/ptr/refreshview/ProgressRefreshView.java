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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jyq.android.ui.R;
import com.jyq.android.ui.ptr.IRefreshView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ProgressRefreshView extends RelativeLayout implements IRefreshView {
    public ProgressRefreshView(Context context) {
        this(context,null);
    }

    public ProgressRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.progress_footer,this,true);
    }
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void reset() {

    }

    @Override
    public void pull() {

    }

    @Override
    public void refreshing() {

    }

    @Override
    public void onPull(float currentPos) {

    }

    @Override
    public void complete() {

    }

    @Override
    public int getContentSize() {
        return getHeight();
    }

}
