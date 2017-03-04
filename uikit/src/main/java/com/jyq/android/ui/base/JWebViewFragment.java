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

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import com.jyq.android.common.preferences.AppPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/4.
 */

public class JWebViewFragment extends WebViewFragment {
    WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebView= (WebView) super.onCreateView(inflater, container, savedInstanceState);
        init();
        return mWebView;
    }
    private void init(){
        mWebView.setWebChromeClient(chromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        parseArguments();
    }
    private void parseArguments(){
        Bundle bundle= getArguments();
        String url=bundle.getString("targetUrl");
        mWebView.loadUrl(url,getHeader());
    }
    private Map<String,String> getHeader(){
        Map<String,String> header=new HashMap<>();
        header.put("Authorization","Bearer "+ AppPreference.getApiToken());
        return header;
    }
    private WebChromeClient chromeClient=new WebChromeClient(){

    };
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")){
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
}
