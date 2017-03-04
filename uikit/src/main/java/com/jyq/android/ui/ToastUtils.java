package com.jyq.android.ui;

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
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ToastUtils {
    private ToastUtils() {
    }
    private static Toast toast;
    public static void showLong(Context context,CharSequence message){
        show(context,message,Toast.LENGTH_LONG);
    }
    public static void showLong(Context context,@StringRes int resId){
        show(context,resId,Toast.LENGTH_LONG);
    }
    public static void showShort(Context context,CharSequence message){
        show(context,message,Toast.LENGTH_SHORT);
    }
    public static void showShort(Context context,@StringRes int resId){
        show(context,resId,Toast.LENGTH_SHORT);
    }
    public static void show(Context context, @StringRes int resId,int duration){
        show(context,context.getApplicationContext().getResources().getString(resId),duration);
    }
    public static void show(Context context,CharSequence message, int duration){
        if (toast==null){
            toast=new Toast(context.getApplicationContext());
        }
            toast.setText(message);
        toast.setDuration(duration);
        toast.show();
    }

}
