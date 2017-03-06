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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jyq.android.ui.R;

/**
 * Created by Administrator on 2017/3/4.
 */

public class OptionItemView extends RelativeLayout {


    private Context mContext;
    private int defaultBgColor = 0xFFFFFFFF;//默认背景颜色
    private int defaultLineColor = 0xFFE8E8E8;//线的默认颜色
    private int defaultLinePadding = 0;//线的左右边距

    private ImageView leftIconIV;//左边图标
    private ImageView rightIconIV;//右边图标

    private TextView leftTV;//左边textView
    private TextView rightTV;//右边textView



    private Drawable leftIconRes;//左边图标资源
    private Drawable rightIconRes;//右边图标资源
    private String leftTextString;//左边显示的文字
    private String rightTextString;//右边显示的文字


    private int defaultPadding = 0;//默认边距

    private int centerSpaceHeight;//中间空间的高度

    private int bothLineWidth;
    private int topLineWidth;
    private int bottomLineWidth;
    private int lineColor = 0xFFE8E8E8;//线的默认颜色

    private int topLineMargin;//上边线的左右边距
    private int bottomLineMargin;//下边线的左右边距
    private int bothLineMargin;//两条线的左右边距

    private int leftIconMarginLeft;//左边图标的左边距

    private int leftTVMarginLeft;//左边文字的左边距

    private int leftIconWidth;//左边图标的宽
    private int leftIconHeight;//左边图标的高

    private int rightIconWidth;//右边图标的宽
    private int rightIconHeight;//右边图标的高


    private int rightTVMarginRight;//右边文字的右边距
    private int rightIconMarginRight;//右边图标的右边距

    private int defaultSize = 0;//默认字体大小

    private int leftTVSize;//左边文字字体大小
    private int rightTVSize;//右边文字字体大小


    private int defaultColor = 0xFF373737;//文字默认颜色

    private int leftTVColor;//左边文字颜色
    private int rightTVColor;//右边文字颜色

    private boolean isSingLines = true;//是否单行显示   默认单行
    private int maxLines = 1;//最多几行    默认显示一行
    private int maxEms = 10;//最多几个字    默认显示10个汉子

    private static final int NONE = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int BOTH = 3;
    private static final int DEFAULT = BOTTOM;

    public static final int leftTextViewId = 0;
    public static final int rightTextViewId = 4;
    public static final int leftImageViewId = 6;
    public static final int rightImageViewId = 7;

    private BadgeView leftBadge;
    private BadgeView rightBadge;

    private boolean showLeftBadge;
    private boolean showRightBadge;
    private boolean rightIconWithBadge;

    private int lineType;
    private LayoutParams centerBaseLineParams, topLineParams, bottomLineParams, leftImgParams, leftTextParams,
             rightTextParams, rightImgParams;

    private OnSuperTextViewClickListener onSuperTextViewClickListener;
    private Drawable rightTextStringRightIconRes;
    private int rightTextStringRightIconPadding;


    public OptionItemView(Context context) {
        this(context,null);
    }

    public OptionItemView(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.OptionItemViewStyle);
    }

    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
//        defaultLinePadding = dip2px(context, 16);
        defaultPadding = dip2px(context, 16);
        defaultSize = sp2px(context, 14);
        centerSpaceHeight = dip2px(context, 10);
        getAttr(context,attrs,defStyleAttr,R.style.optionItemStyle);
        initLayout();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.getAttr(context, attrs, defStyleAttr,defStyleRes);
    }

    /**
     * 获取自定义属性值
     *
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray=mContext.obtainStyledAttributes(attrs,R.styleable.OptionItemView,defStyleAttr,defStyleRes);

        //////设置字体大小////////
        leftTVSize = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sLeftTextSize, defaultSize);

        rightTVSize = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightTextSize, defaultSize);

        ///////设置textView的属性///////////SuperTextViewxEms
        isSingLines = typedArray.getBoolean(R.styleable.OptionItemView_sIsSingLines, isSingLines);
        maxLines = typedArray.getInt(R.styleable.OptionItemView_sMaxLines, maxLines);
        maxEms = typedArray.getInt(R.styleable.OptionItemView_sMaxEms, maxEms);
        rightTVMarginRight = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightTextMarginRight, defaultPadding);
        rightIconMarginRight = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightIconMarginRight, defaultPadding);
        //////设置字体颜色////////
        leftTVColor = typedArray.getColor(R.styleable.OptionItemView_sLeftTextColor, defaultColor);
        rightTVColor = typedArray.getColor(R.styleable.OptionItemView_sRightTextColor, defaultColor);



        leftIconWidth = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sLeftIconWidth, 0);
        leftIconHeight = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sLeftIconHeight, 0);

        rightIconWidth = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightIconWidth, 0);
        rightIconHeight = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightIconHeight, 0);

        leftIconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sLeftIconMarginLeft, defaultPadding);
        leftTVMarginLeft = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sLeftTextMarginLeft, defaultPadding);

        leftIconRes = typedArray.getDrawable(R.styleable.OptionItemView_sLeftIconRes);
        rightIconRes = typedArray.getDrawable(R.styleable.OptionItemView_sRightIconRes);
        leftTextString = typedArray.getString(R.styleable.OptionItemView_sLeftTextString);
        rightTextString = typedArray.getString(R.styleable.OptionItemView_sRightTextString);
        rightTextStringRightIconRes = typedArray.getDrawable(R.styleable.OptionItemView_sRightTextStringRightIconRes);
        ////////设置文字或者图片资源////////

        rightTextStringRightIconPadding = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sRightTextStringRightIconResPadding, dip2px(mContext, 5));

        lineType = typedArray.getInt(R.styleable.OptionItemView_sLineShow, DEFAULT);

        /////////设置view的边距////////
        centerSpaceHeight = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sCenterSpaceHeight, centerSpaceHeight);

        bothLineWidth = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sBothLineWidth, dip2px(mContext, 0.5f));
        topLineWidth = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sTopLineWidth, dip2px(mContext, 0.5f));
        bottomLineWidth = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sBottomLineWidth, dip2px(mContext, 0.5f));

        lineColor = typedArray.getColor(R.styleable.OptionItemView_sLineColor, lineColor);

        topLineMargin = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sTopLineMargin, defaultLinePadding);
        bottomLineMargin = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sBottomLineMargin, defaultLinePadding);
        bothLineMargin = typedArray.getDimensionPixelSize(R.styleable.OptionItemView_sBothLineMargin, defaultLinePadding);
        showLeftBadge=typedArray.getBoolean(R.styleable.OptionItemView_sLeftBadgeShow,false);
        showRightBadge=typedArray.getBoolean(R.styleable.OptionItemView_sRightBadgeShow,false);
//        setShowLeftBadge(showRightBadge);
        rightIconWithBadge=typedArray.getBoolean(R.styleable.OptionItemView_sRightIconWithBadge,false);


        typedArray.recycle();
    }
    public void setLeftTextString(CharSequence text){
        initLeftText();

    }
    /**
     * 初始化布局
     */
    private void initLayout() {

        initSuperTextView();
        initCenterBaseLine();

        if (leftIconRes != null) {
            initLeftIcon();
        }
        if (leftTextString != null) {
            initLeftText();
        }
        if (rightIconRes != null) {
            initRightIcon();
        }
        if (rightTextString != null || rightTextStringRightIconRes != null) {
            initRightText();
        }

        switch (lineType) {
            case NONE:
                break;
            case TOP:
                initTopLine(topLineMargin, topLineWidth);
                break;
            case BOTTOM:
                initBottomLine(bottomLineMargin, bottomLineWidth);
                break;
            case BOTH:
                initTopLine(bothLineMargin, bothLineWidth);
                initBottomLine(bothLineMargin, bothLineWidth);
                break;
        }
        if (showLeftBadge){
                initLeftBadge();
        }
    }

    public void setShowLeftBadge(boolean showLeftBadge) {
        initRightBadge();
//        rightBadge.bindTarget(this);
//        rightBadge.setBadgeGravity(Gravity.CENTER|Gravity.END);
//        rightBadge.setBadgeNumber(0);
    }

    private void initRightBadge(){
        if (rightBadge==null){
            rightBadge=new BadgeView(getContext());
        }
    }
    private void initLeftBadge(){
//            if (leftBadge==null){
//                leftBadge=new BadgeView(getContext());
//                leftBadge.bindTarget(leftTV);
//                leftBadge.setBadgeGravity(Gravity.CENTER|Gravity.END);
//                leftBadge.setBadgePadding(1,true);
//                leftBadge.setGravityOffset(18,0,true);
//                leftBadge.setBadgeTextColor(Color.WHITE);
//                leftBadge.setBadgeTextSize(10,true);
//            }
//            leftBadge.setBadgeNumber(0);

    }

    /**
     * 初始化上边的线
     */
    private void initTopLine(int lineMargin, int lineWidth) {
        View topLine = new View(mContext);
        topLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, lineWidth);
        topLineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        topLineParams.setMargins(lineMargin, 0, lineMargin, 0);
        topLine.setLayoutParams(topLineParams);
        topLine.setBackgroundColor(lineColor);
        addView(topLine);
    }

    /**
     * 初始化下边的线
     */
    private void initBottomLine(int lineMargin, int lineWidth) {
        View bottomLine = new View(mContext);
        bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, lineWidth);
        bottomLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        bottomLineParams.setMargins(lineMargin, 0, lineMargin, 0);
        bottomLine.setLayoutParams(bottomLineParams);
        bottomLine.setBackgroundColor(lineColor);

        addView(bottomLine);
    }

    /**
     * 初始化SuperTextView
     */
    private void initSuperTextView() {

//        this.setBackgroundColor(backgroundColor);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSuperTextViewClickListener != null) {
                    onSuperTextViewClickListener.onSuperTextViewClick();
                }
            }
        });

//        if (useRipple) {
//            this.setBackgroundResource(R.drawable.selector_white);
//        }
//        if (mBackground_drawable != null) {
//            this.setBackgroundDrawable(mBackground_drawable);
//        }
    }


    /**
     * 为了设置上下两排文字居中对齐显示而需要设置的基准线
     */
    private void initCenterBaseLine() {
        View view = new View(mContext);
        centerBaseLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, centerSpaceHeight);
        centerBaseLineParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
//        view.setId(R.id.sCenterBaseLineId);
        view.setLayoutParams(centerBaseLineParams);
        addView(view);
    }


    /**
     * 初始化左边图标
     */
    private void initLeftIcon() {
        leftIconIV = new ImageView(mContext);
        leftImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftImgParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        leftImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        if (leftIconHeight != 0 && leftIconWidth != 0) {
            leftImgParams.width = leftIconWidth;
            leftImgParams.height = leftIconHeight;
        }
        setMargin(leftImgParams, leftIconMarginLeft, 0, 0, 0);
        leftIconIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        leftIconIV.setId(R.id.sLeftIconId);
        leftIconIV.setLayoutParams(leftImgParams);
        if (leftIconRes != null) {
            leftIconIV.setImageDrawable(leftIconRes);
        }
        addView(leftIconIV);
    }

    /**
     * 初始化左边文字
     */
    private void initLeftText() {
        if (leftTV==null) {
            leftTV = new TextView(mContext);
            leftTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            leftTextParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
//            leftTextParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftIconId);
            setMargin(leftTextParams, leftTVMarginLeft, 0, dip2px(mContext, 10), 0);
//            leftTV.setId(R.id.sLeftTextId);
            leftTV.setLayoutParams(leftTextParams);
            leftTV.setText(leftTextString);
            setTextViewParams(leftTV, isSingLines, maxLines, maxEms);
            setTextColor(leftTV, leftTVColor);
            setTextSize(leftTV, leftTVSize);
            addView(leftTV);
        }
    }

    /**
     * 设置通用的textView显示效果属性
     *
     * @param textView    view
     * @param isSingLines 是否单行显示
     * @param maxLines    显示最大行
     * @param maxEms      最多显示多少个字
     */
    private void setTextViewParams(TextView textView, boolean isSingLines, int maxLines, int maxEms) {
        textView.setSingleLine(isSingLines);
        textView.setMaxLines(maxLines);
        textView.setMaxEms(maxEms);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }


    /**
     * 初始化右边文字
     */
    private void initRightText() {
        rightTV = new TextView(mContext);
        rightTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightTextParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
//        rightTextParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftTextId);
//        rightTextParams.addRule(RelativeLayout.LEFT_OF, R.id.sRightIconId);
        setMargin(rightTextParams, 0, 0, rightTVMarginRight, 0);
//        rightTV.setId(R.id.sRightTextId);
        rightTV.setLayoutParams(rightTextParams);
        rightTV.setText(rightTextString);
        setTextColor(rightTV, rightTVColor);
        setTextSize(rightTV, rightTVSize);
        setTextViewRightDrawble(rightTV, rightTextStringRightIconRes, rightTextStringRightIconPadding);
        rightTV.setGravity(Gravity.RIGHT);
        setTextViewParams(rightTV, isSingLines, maxLines, maxEms);
        addView(rightTV);
    }

    /**
     * 初始化右边图标
     */
    private void initRightIcon() {
        rightIconIV = new ImageView(mContext);
        rightImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightImgParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        rightImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        if (rightIconHeight != 0 && rightIconWidth != 0) {
            rightImgParams.width = rightIconWidth;
            rightImgParams.height = rightIconHeight;
        }
        setMargin(rightImgParams, 0, 0, rightIconMarginRight, 0);
        rightIconIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        rightIconIV.setId(R.id.sRightIconId);
        rightIconIV.setLayoutParams(rightImgParams);
        if (rightIconRes != null) {
            rightIconIV.setImageDrawable(rightIconRes);
        }
        addView(rightIconIV);
    }


    private void setMargin(LayoutParams params, int left, int top, int right, int bottom) {
        params.setMargins(left, top, right, bottom);
    }

    /**
     * 设置view的边距
     *
     * @param view   view对象
     * @param left   左边边距
     * @param top    上边边距
     * @param right  右边边距
     * @param bottom 下边边距
     */
    private void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    /**
     * 设置文字的字体大小
     *
     * @param textView textView对象
     * @param size     文字大小
     */
    private void setTextSize(TextView textView, int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置文字的颜色
     *
     * @param textView textView对象
     * @param color    文字颜色
     */
    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    //////////对外公布的方法///////////////

    /**
     * 设置左边图标
     *
     * @param leftIcon 左边图标
     * @return 返回对象
     */
    public OptionItemView setLeftIcon(Drawable leftIcon) {
        leftIconRes = leftIcon;
        if (leftIconIV == null) {
            initLeftIcon();
        } else {
            leftIconIV.setImageDrawable(leftIcon);
        }
        return this;
    }

    /**
     * 设置右边图标
     *
     * @param rightIcon 右边图标
     * @return 返回对象
     */
    public OptionItemView setRightIcon(Drawable rightIcon) {
        rightIconRes = rightIcon;
        if (rightIconIV == null) {
            initRightIcon();
        } else {
            rightIconIV.setImageDrawable(rightIcon);
        }
        return this;
    }

    /**
     * 设置左边显示文字
     *
     * @param leftString 左边文字
     * @return 返回对象
     */
    public OptionItemView setLeftString(String leftString) {
        leftTextString = leftString;
        if (leftTV == null) {
            initLeftText();
        } else {
            leftTV.setText(leftString);
        }
        return this;
    }


    /**
     * 设置右边显示的文字
     *
     * @param rightString 右边文字
     * @return 返回对象
     */
    public OptionItemView setRightString(String rightString) {
        rightTextString = rightString;
        if (rightTV == null) {
            initRightText();
        } else {
            rightTV.setText(rightString);
        }
        return this;
    }

    /**
     * 设置右边显示的文字和图片
     *
     * @param rightString     右边文字
     * @param drawable        drawable
     * @param drawablePadding drawablePadding
     * @return
     */
    public OptionItemView setRightString(String rightString, Drawable drawable, int drawablePadding) {
        rightTextString = rightString;
        rightTextStringRightIconRes = drawable;
        rightTextStringRightIconPadding = drawablePadding;
        if (rightTV == null) {
            initRightText();
        } else {
            rightTV.setText(rightString);
        }
        return this;
    }



    /**
     * 设置左边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public OptionItemView setLeftTVColor(int textColor) {
        leftTVColor = textColor;
        if (leftTV == null) {
            initLeftText();
        } else {
            leftTV.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置右边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public OptionItemView setRightTVColor(int textColor) {
        rightTVColor = textColor;
        if (rightTV == null) {
            initRightText();
        } else {
            rightTV.setTextColor(textColor);
        }
        return this;
    }


    //////////设置View的点击事件/////////////////

    /**
     * 点击事件
     *
     * @param listener listener对象
     * @return 返回对象
     */
    public OptionItemView setOnSuperTextViewClickListener(OnSuperTextViewClickListener listener) {
        onSuperTextViewClickListener = listener;
        return this;
    }

    /**
     * 点击事件接口
     */
    public static class OnSuperTextViewClickListener {
        public void onSuperTextViewClick() {
        }

        public void onLeftTopClick() {
        }

        public void onLeftBottomClick() {
        }

        public void onLeftBottomClick2() {
        }

    }

    /**
     * 获取控件ID便于根据ID设置值
     *
     * @param viewName 需要的textViewName
     * @return 返回ID
     */
    public int getViewId(int viewName) {
        int viewId = 0;
        switch (viewName) {
            case leftTextViewId:
                if (leftTV == null) {
                    initLeftText();
                }
//                viewId = R.id.sLeftTextId;
                break;
            case rightTextViewId:
                if (rightTV == null) {
                    initRightText();
                }
//                viewId = R.id.sRightTextId;
                break;
            case leftImageViewId:
                if (leftIconIV == null) {
                    initLeftIcon();
                }
//                viewId = R.id.sLeftIconId;
                break;
            case rightImageViewId:
                if (rightIconIV == null) {
                    initRightIcon();
                }
//                viewId = R.id.sRightIconId;
                break;
        }
        return viewId;
    }

    /**
     * 获取view对象
     *
     * @param viewName 传入viewName
     * @return 返回view
     */
    public View getView(int viewName) {
        View view = null;
        switch (viewName) {

            case leftImageViewId:
                if (leftIconIV == null) {
                    initLeftIcon();
                }
                view = leftIconIV;
                break;
            case rightImageViewId:
                if (rightIconIV == null) {
                    initRightIcon();
                }
                view = rightIconIV;
                break;
        }
        return view;
    }


    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static void setTextViewRightDrawble(TextView textView, Drawable drawable, int drawablePadding) {
        if (drawable != null && textView != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
            textView.setCompoundDrawablePadding(drawablePadding);
        }
    }
}
