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
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.jyq.android.common.R;
import com.jyq.android.ui.ToastUtils;
import com.jyq.android.ui.widget.LoadingLayout;
import com.jyq.android.ui.widget.dialog.DialogMaker;
import com.jyq.android.ui.widget.dialog.JProgressDialog;

/**
 * Created by Administrator on 2017/2/28.
 */

public class JActivity extends AppCompatActivity {
    ViewStub toolbarStub;
    Toolbar toolBar;
    LoadingLayout loadingLayout;
    ViewStub contentStub;
    JProgressDialog modalLoading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.uikit_base_layout);
        toolbarStub = (ViewStub) findViewById(R.id.uikit_base_toolbar_stub);
        loadingLayout = (LoadingLayout) findViewById(R.id.uikit_loading_layout);
        contentStub = findView(R.id.uikit_base_content);
        initToolbar();
        initLoadingLayout();
        showLoadingPage();
    }

    private void initToolbar() {
        if (hasActionBar() || hasBackButton()) {
            toolbarStub.setVisibility(View.VISIBLE);
            toolBar = findView(R.id.uikit_toolbar);
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton());
        }
    }

    protected Context getContext() {
        return this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        contentStub.setLayoutResource(layoutResID);
        contentStub.setVisibility(View.VISIBLE);
    }

    private void initLoadingLayout() {

    }

    protected boolean hasBackButton() {
        return true;
    }

    protected boolean hasActionBar() {
        return true;
    }

    public Toolbar getToolbar() {
        return toolBar;
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
    protected void showErrorToast(String message){
        ToastUtils.showShort(getContext(),message);
    }
    protected void showModalProgress(){
        DialogMaker.showProgressDialog(getContext(),null,false);
    }
    protected void dismissModalProgress(){
        DialogMaker.dismissProgressDialog();
    }
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
