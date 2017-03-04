package com.jyq.android.ui.base;

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

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/3/3.
 */

public abstract class JMvpActivity<P extends JPresenter> extends JActivity implements JMvpView {

    private P mvpPresenter;

    protected abstract P createPresenter();

    public P getPresenter() {
        return mvpPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter=createPresenter();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mvpPresenter!=null){
            mvpPresenter.deatchView();
        }
    }
    @Override
    public void showLoadingPage() {
        super.showLoadingPage();
    }

    @Override
    public void showLoadingModal() {
        super.showModalProgress();
    }

    @Override
    public void dismissLoadingModal() {
        super.dismissModalProgress();
    }

    @Override
    public void showErrorToast(String message) {
        super.showErrorToast(message);
    }
}
