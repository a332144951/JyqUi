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

import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/9/28.
 */

public abstract class JPresenter<V extends JMvpView> {
    protected WeakReference<V> mvpViewRef;
    protected CompositeSubscription subscriptions=new CompositeSubscription();
    protected JPresenter(V mvpView){
        attachView(mvpView);
    }
    public void attachView(V mvpView){
        this.mvpViewRef=new WeakReference<V>(mvpView);
    }
    public V  getMvpView(){
        return (V) this.mvpViewRef.get();
    }

    public void deatchView(){
        if(!subscriptions.isUnsubscribed()){
            subscriptions.unsubscribe();
        }
    }



}
