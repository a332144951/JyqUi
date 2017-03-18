package com.jyq.android.ui.ptr;

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

import android.view.View;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface IRefreshView {

    View getView();

    /**
     * 松手，头部隐藏后会回调这个方法
     */
    void reset();

    /**
     * 下拉出头部的一瞬间调用
     */
    void pull();

    /**
     * 正在刷新的时候调用
     */
    void refreshing();

    /**
     * 头部滚动的时候持续调用
     *
     * @param currentPos target当前偏移高度
     *                   //     * @param lastPos    target上一次的偏移高度
     *                   //     * @param refreshPos 可以松手刷新的高度
     *                   //     * @param isTouch    手指是否按下状态（通过scroll自动滚动时需要判断）
     *                   //     * @param state      当前状态
     */
    void onPull(float currentPos);

    /**
     * 刷新成功的时候调用
     */
    void complete();

    int getContentSize();


}
