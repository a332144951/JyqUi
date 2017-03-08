package com.jyq.android.ui.widget;

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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.ui.R;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AvatarItemView extends RelativeLayout {
    private AppCompatImageView leftIcon;
    private AppCompatImageView rightIcon;
    private AppCompatTextView titleText;
    private AppCompatTextView subTitleText;
    private View bottomLine;
    public AvatarItemView(Context context) {
        this(context, null);
    }

    public AvatarItemView(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.AvatarItemViewStyle);
    }

    public AvatarItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        getAttr(context,attrs,defStyleAttr,R.style.avatarItemStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AvatarItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout();
        getAttr(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initLayout(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_avatar_item,this,true);
        leftIcon= (AppCompatImageView) findViewById(R.id.widget_avatar_item_left_icon);
        rightIcon= (AppCompatImageView) findViewById(R.id.widget_avatar_item_right_icon);
        titleText= (AppCompatTextView) findViewById(R.id.widget_avatar_item_title);
        subTitleText= (AppCompatTextView) findViewById(R.id.widget_avatar_item_subtitle);
        bottomLine=findViewById(R.id.widget_avatar_item_bottomLine);
    }
    private void getAttr(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.AvatarItemView,defStyleAttr,defStyleRes);
        setLeftIcon(typedArray.getDrawable(R.styleable.AvatarItemView_aLeftIconRes));
        setRightIcon(typedArray.getDrawable(R.styleable.AvatarItemView_aRightIconRes));
        setTitleText(typedArray.getText(R.styleable.AvatarItemView_aTitleTextString));
        setSubTitleText(typedArray.getText(R.styleable.AvatarItemView_aSubTitleTextString));
        showBottomLine(typedArray.getBoolean(R.styleable.AvatarItemView_aShowBottomLine,false));
        typedArray.recycle();
    }

    public void setRightIcon(String url,@DrawableRes int loadingRes,@DrawableRes int fallbackRes){
        ImageLoaderKit.getInstance().displayImage(getContext(),url,loadingRes,fallbackRes,rightIcon);
    }
    public void setLeftIcon(String url, @DrawableRes int loadingRes,@DrawableRes int fallbackRes){
        ImageLoaderKit.getInstance().displayImage(getContext(),url,loadingRes,fallbackRes,leftIcon);
        leftIcon.setVisibility(VISIBLE);
    }
    public void setLeftIcon(Drawable drawable){
        leftIcon.setImageDrawable(drawable);
        leftIcon.setVisibility(drawable!=null?VISIBLE:GONE);
    }
    public void setRightIcon(Drawable drawable){
        rightIcon.setImageDrawable(drawable);
        rightIcon.setVisibility(drawable!=null?VISIBLE:GONE);
    }
    public void setTitleText(CharSequence title){
        titleText.setText(title);
        titleText.setVisibility(title!=null?VISIBLE:GONE);
    }
    public void setSubTitleText(CharSequence subTitle){
        subTitleText.setText(subTitle);
        subTitleText.setVisibility(subTitle!=null?VISIBLE:GONE);
    }
    public void showBottomLine(boolean show){
        bottomLine.setVisibility(show?VISIBLE:GONE);
    }
    public void setLeftIconOnClickListener(OnClickListener onClickListener){
        leftIcon.setOnClickListener(onClickListener);
    }
    public void setRightIconOnClickistener(OnClickListener onClickListener){
        rightIcon.setOnClickListener(onClickListener);
    }
}
