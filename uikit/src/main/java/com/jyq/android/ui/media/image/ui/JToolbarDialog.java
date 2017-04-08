package com.jyq.android.ui.media.image.ui;

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
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.jyq.android.ui.R;
import com.jyq.android.ui.widget.LoadingLayout;
import com.jyq.android.ui.widget.dialog.JDialogFragment;

/**
 * Created by Administrator on 2017/4/2.
 */

 abstract class JToolbarDialog extends JDialogFragment {
    @Override
    protected boolean hasTitle() {
        return false;
    }

    abstract
    @LayoutRes
    int getLayoutId();

    abstract void onBackClick();

    abstract void onCreateToolbarMenu(Menu menu, MenuInflater inflater);

    abstract boolean onToolbarMenuItemClick(MenuItem item);
    abstract void initViews();
    ViewStub toolbarStub;
    Toolbar toolBar;
    LoadingLayout loadingLayout;
    ViewStub contentStub;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private static final String TAG = "JToolbarDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.uikit_base_layout, container, false);
        toolbarStub = (ViewStub) root.findViewById(R.id.uikit_base_toolbar_stub);
        loadingLayout = (LoadingLayout) root.findViewById(R.id.uikit_loading_layout);
        contentStub = (ViewStub) root.findViewById(R.id.uikit_base_content);
        toolbarStub.inflate();
        toolBar = (Toolbar) root.findViewById(R.id.uikit_toolbar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + v);
                onBackClick();
            }
        });
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onToolbarMenuItemClick(item);
            }
        });
        contentStub.setLayoutResource(getLayoutId());
        contentStub.inflate();
        loadingLayout.showContent();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menuInflater=inflater;
        onCreateToolbarMenu(toolBar.getMenu(), inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onToolbarMenuItemClick(item)||super.onOptionsItemSelected(item);
    }

    private MenuInflater menuInflater;
    public void invalidateOptionsMenu(){
        toolBar.getMenu().clear();
        onCreateToolbarMenu(toolBar.getMenu(),menuInflater);
    }
}
