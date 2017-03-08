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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
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
 * Created by Administrator on 2017/3/4.
 */

public class OptionItemView extends RelativeLayout {
    private AppCompatImageView leftIconImage;
    private AppCompatImageView centerIconImage;
    private AppCompatImageView rightIconImage;
    private BadgeView numberBadge;
    private BadgeView imageBadge;
    private BadgeView circleBadge;
    private AppCompatTextView leftTitleText;
    private AppCompatTextView rightDescriptionText;
    private AppCompatTextView rightSmallDescriptionText;
    private View bottomLine;
    public OptionItemView(Context context) {
        this(context,null);
    }

    public OptionItemView(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.OptionItemViewStyle);
    }

    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        getAttr(context,attrs,defStyleAttr,R.style.optionItemStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout();
        this.getAttr(context, attrs, defStyleAttr,defStyleRes);
    }

    /**
     * 获取自定义属性值
     *
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.OptionItemView,defStyleAttr,defStyleRes);
        setLeftIconImage(typedArray.getDrawable(R.styleable.OptionItemView_oLeftIconRes));
        setCenterIconImage(typedArray.getDrawable(R.styleable.OptionItemView_oCenterIconRes));
        showBottomLine(typedArray.getBoolean(R.styleable.OptionItemView_oShowBottomLine,false));
        final boolean showImageBadge=typedArray.getBoolean(R.styleable.OptionItemView_oShowImageBadge,false);
        setRightIconImage(typedArray.getDrawable(R.styleable.OptionItemView_oRightIconRes),showImageBadge);
        setNumberBadge(typedArray.getInt(R.styleable.OptionItemView_oNumberBadge,0));
        showCircleBadge(typedArray.getBoolean(R.styleable.OptionItemView_oShowCircleBadge,false));
        setLeftTitleText(typedArray.getText(R.styleable.OptionItemView_oLeftTextString));
        setRightDescriptionText(typedArray.getText(R.styleable.OptionItemView_oRightTextString));
        setRightSmallDescriptionText(typedArray.getText(R.styleable.OptionItemView_oRightSmallTextString));
        setRightDescriptionTextColor(typedArray.getColor(R.styleable.OptionItemView_oRightTextColor, getResources().getColor(R.color.main_default_bg)));
        typedArray.recycle();
    }
    /**
     * 初始化布局
     */
    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_option_item,this,true);
        leftIconImage= (AppCompatImageView) findViewById(R.id.widget_option_item_icon);
        centerIconImage= (AppCompatImageView) findViewById(R.id.widget_option_item_center_icon);
        rightIconImage= (AppCompatImageView) findViewById(R.id.widget_option_item_image);
        numberBadge= (BadgeView) findViewById(R.id.widget_option_item_numberbadge);
        imageBadge= (BadgeView) findViewById(R.id.widget_option_item_imagebadge);
        circleBadge= (BadgeView) findViewById(R.id.widget_option_item_badge);
        leftTitleText= (AppCompatTextView) findViewById(R.id.widget_option_item_title);
        rightDescriptionText= (AppCompatTextView) findViewById(R.id.widget_option_item_description);
        rightSmallDescriptionText= (AppCompatTextView) findViewById(R.id.widget_option_item_small_description);
        bottomLine=findViewById(R.id.widget_option_item_bottomLine);
    }
    public void showBottomLine(boolean show){
        bottomLine.setVisibility(show?VISIBLE:GONE);
    }
    public void setLeftIconImage(@DrawableRes int iconImage){
        setLeftIconImage(getResources().getDrawable(iconImage));
    }
    public void setLeftIconImage(Drawable drawable){
        leftIconImage.setImageDrawable(drawable);
        leftIconImage.setVisibility(drawable!=null?VISIBLE:GONE);
    }
    public void setCenterIconImage(@DrawableRes int iconId){
        setCenterIconImage(getResources().getDrawable(iconId));
        }
    public void setCenterIconImage(Drawable drawable){
        centerIconImage.setImageDrawable(drawable);
        centerIconImage.setVisibility(drawable!=null?VISIBLE:GONE);

    }
    public void setRightIconImage(String url,@DrawableRes int loadingRes,@DrawableRes int fallbackRes,boolean showBadge){
        rightIconImage.setVisibility(VISIBLE);
        ImageLoaderKit.getInstance().displayImage(getContext(),url,loadingRes,fallbackRes,rightIconImage);
        if (showBadge) {
            imageBadge.show();
        }else{
            imageBadge.hide();
        }
    }
    public void setRightIconImage(Drawable drawable,boolean showBadge){
        rightIconImage.setImageDrawable(drawable);
        rightIconImage.setVisibility(drawable!=null?VISIBLE:GONE);
        if (showBadge){
            imageBadge.show();
        }else{
            imageBadge.hide();
        }
    }
    public void setRightIconImage(@DrawableRes int iconId,boolean showBadge){
        setRightIconImage(getResources().getDrawable(iconId),showBadge);
    }
    public void setNumberBadge(int number){
        if (number<=0){
            numberBadge.hide();
            return;
        }
        numberBadge.text(number>99?"99+":String.valueOf(number)).show();
    }
    public void showCircleBadge(boolean show){
        if (show){
            circleBadge.show();
        }else{
            circleBadge.hide();
        }
    }
    public void setLeftTitleText(CharSequence title){

        leftTitleText.setText(title);
        leftTitleText.setVisibility(title!=null?VISIBLE:GONE);
    }
    public void setRightDescriptionText(CharSequence descriptionText){
        rightDescriptionText.setText(descriptionText);
        rightDescriptionText.setVisibility(descriptionText!=null?VISIBLE:GONE);
    }
    public void setRightSmallDescriptionText(CharSequence smallDescriptionText){
        rightSmallDescriptionText.setVisibility(smallDescriptionText!=null?VISIBLE:GONE);
        rightSmallDescriptionText.setText(smallDescriptionText);
    }
    public void setRightDescriptionTextColor(@ColorInt int color){
        rightDescriptionText.setTextColor(color);
    }
    public void setCenterIconImageClickable(OnClickListener onClickListener){
        centerIconImage.setClickable(onClickListener!=null);
        centerIconImage.setOnClickListener(onClickListener);
    }
    public void setRightIconImageClickable(OnClickListener onClickListener){
        rightIconImage.setClickable(onClickListener!=null);
        rightIconImage.setOnClickListener(onClickListener);
    }

}
