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

public abstract class JMvpFragment<P extends JPresenter> extends JFragment implements JMvpView {
    protected P mvpPresenter;

    protected abstract P createPresenter();

    public P getPresenter() {
        return mvpPresenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
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
