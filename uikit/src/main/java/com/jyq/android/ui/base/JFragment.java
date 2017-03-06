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

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.jyq.android.ui.R;
import com.jyq.android.ui.ToastUtils;
import com.jyq.android.ui.widget.LoadingLayout;
import com.jyq.android.ui.widget.dialog.DialogMaker;

/**
 * Created by Administrator on 2017/3/3.
 */

public abstract class JFragment extends Fragment {
    LoadingLayout loadingLayout;
    ViewStub contentStub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.uikit_base_fragment_layout, container, false);
        loadingLayout = (LoadingLayout) root.findViewById(R.id.uikit_loading_layout);
        contentStub = (ViewStub) root.findViewById(R.id.uikit_base_content);
        setHasOptionsMenu(HasOptionsMenu());
        if (getLayoutId() != 0) {
            contentStub.setLayoutResource(getLayoutId());
            contentStub.setVisibility(View.VISIBLE);
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
    }

    protected abstract boolean HasOptionsMenu();

    protected abstract int getLayoutId();

    protected abstract void findViews();

    protected <T extends View> T findView(int resId) {
        return (T) (getView().findViewById(resId));
    }


    void showError(String message) {
        ToastUtils.showShort(getContext(), message);
    }


    public Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return super.getContext();
        }
        return getActivity();
    }

    public Toolbar getToolbar() {
        return ((JActivity) getActivity()).getToolbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void showErrorPage() {
        loadingLayout.showError();
    }

    protected void showLoadingPage() {
        loadingLayout.showLoading();
    }

    protected void showContentPage() {
        loadingLayout.showContent();
    }

    protected void showEmptyPage() {
        loadingLayout.showEmpty();
    }

    protected void showErrorToast(String message) {
        ToastUtils.showShort(getContext(), message);
    }

    protected void showModalProgress() {
        DialogMaker.showProgressDialog(getContext(), null, false);
    }

    protected void dismissModalProgress() {
        DialogMaker.dismissProgressDialog();
    }
}
