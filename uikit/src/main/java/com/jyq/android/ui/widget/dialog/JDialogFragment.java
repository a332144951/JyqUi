package com.jyq.android.ui.widget.dialog;

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

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Administrator on 2017/3/18.
 */

public abstract class JDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasTitle()){
            setStyle(STYLE_NO_TITLE,android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        }
    }

    protected abstract boolean hasTitle();
    public void show(Context context){
        if (context instanceof FragmentActivity){
            show(((FragmentActivity) context).getFragmentManager(),context.getClass().getSimpleName());
        }else if(context instanceof Activity){
            show(((Activity) context).getFragmentManager(),context.getClass().getSimpleName());

        }
    }

    @Override
    public Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           return super.getContext();
        }
        return getActivity();
    }
}
