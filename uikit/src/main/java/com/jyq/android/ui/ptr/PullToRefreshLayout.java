package com.jyq.android.ui.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;

import com.jyq.android.ui.R;
import com.jyq.android.ui.ptr.refreshview.ProgressRefreshView;
import com.jyq.android.ui.ptr.refreshview.CarRefreshView;


/**
 * 下拉刷新控件，可以配合 RecyclerView，Scrollview，ListView
 * Created by fish on 16/5/17.
 */
public class PullToRefreshLayout extends SuperSwipeRefreshLayout {

    public interface OnRefreshListener {
        void onPullDownToRefresh();

        void onPullUpToRefresh();
    }

    private IRefreshView loadingLayoutDown;
    private IRefreshView loadingLayoutUp;
    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }


    public PullToRefreshLayout(Context context) {
        super(context);
        initLoadingView(true, true);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout);
        boolean refresh = a.getBoolean(R.styleable.PullToRefreshLayout_refreshEnable, true);
        boolean loadmore = a.getBoolean(R.styleable.PullToRefreshLayout_loadmoreEnable, true);
        int refreshView = a.getInt(R.styleable.PullToRefreshLayout_refreshView, 0);
        initHeaderView(refreshView);
        initFooterView();
        initLoadingView(refresh, loadmore);
    }

    private void initHeaderView(int type) {
        switch (type) {
            case 0:
                loadingLayoutDown = new ProgressRefreshView(getContext());
                break;
            case 1:
                loadingLayoutDown = new CarRefreshView(getContext());
                break;
        }
    }

    private void initFooterView() {
        loadingLayoutUp = new ProgressRefreshView(getContext());
    }

    //一般用于进页面第一次刷新
    public void autoRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                loadingLayoutDown.refreshing();
                if (listener != null) {
                    listener.onPullDownToRefresh();
                }
            }
        }, 100);
    }

    public void initLoadingView(boolean pullDown, boolean pullUp) {
        setHeaderView(loadingLayoutDown.getView());
        setPullDownEnable(pullDown);
        setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                loadingLayoutDown.refreshing();
                if (listener != null) {
                    listener.onPullDownToRefresh();
                }
            }

            @Override
            public void onPullDistance(int distance) {
                if (distance == 0) {
                    loadingLayoutDown.reset();
                }
                loadingLayoutDown.onPull(distance * 1.0f / loadingLayoutDown.getContentSize());
            }

            @Override
            public void onPullEnable(boolean enable) {
//                    textView.setText(enable ? "松开刷新" : "下拉刷新");
            }
        });

        setFooterView(loadingLayoutUp.getView());
        setPullUpEnable(pullUp);
        setOnPushLoadMoreListener(new OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadingLayoutUp.refreshing();
                if (listener != null) {
                    listener.onPullUpToRefresh();
                }
            }

            @Override
            public void onPushDistance(int distance) {
                if (distance == 0) {
                    loadingLayoutUp.reset();
                }
                loadingLayoutUp.onPull(distance * 1.0f / loadingLayoutUp.getContentSize());
            }

            @Override
            public void onPushEnable(boolean enable) {

            }
        });

    }


}
