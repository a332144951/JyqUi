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

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jyq.android.ui.R;

/**
 * Created by Administrator on 2017/4/10.
 */

public class EditAbleImageView extends FrameLayout {
    private boolean editable = false;
    private ImageButton deleteIcon;
    private ImageView imageView;

    public EditAbleImageView(Context context) {
        this(context, null);
    }

    public EditAbleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditAbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        imageView=new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        addView(imageView);
        deleteIcon=new ImageButton(getContext());
        deleteIcon.setBackgroundDrawable(null);
        deleteIcon.setVisibility(GONE);
        deleteIcon.setImageResource(R.drawable.edit_image_ic_delete);
        deleteIcon.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP|Gravity.RIGHT));
        addView(deleteIcon);
    }

    private static final String TAG = "EditAbleImageView";





    public void setImageResource(@DrawableRes int resource){
        imageView.setImageResource(resource);
    }
    public ImageView getImageView(){
        return imageView;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
        deleteIcon.setVisibility(editable?VISIBLE:GONE);
    }

}
