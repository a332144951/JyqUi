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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.ui.R;

/**
 * Created by Administrator on 2017/4/10.
 */

public class EditAbleImageView extends FrameLayout implements View.OnClickListener {
    public interface EditableImageViewListener{
        void OnDeleteClick(EditAbleImageView view);
        void OnImageClick(EditAbleImageView view,boolean isPlus);
    }
    private boolean editable = false;
    private ImageButton deleteIcon;
    private ImageView imageView;
    private EditableImageViewListener listener;
    private boolean isPlus=false;

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
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(this);
        addView(imageView);
        deleteIcon=new ImageButton(getContext());
        deleteIcon.setBackgroundDrawable(null);
        deleteIcon.setVisibility(GONE);
        deleteIcon.setImageResource(R.drawable.edit_image_ic_delete);
        deleteIcon.setPadding(0,0,0,0);
        deleteIcon.setOnClickListener(this);
        addView(deleteIcon,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.TOP|Gravity.RIGHT));
    }

    public void setListener(EditableImageViewListener listener) {
        this.listener = listener;
    }

    private static final String TAG = "EditAbleImageView";

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        imageView.layout(0,0,getWidth(),getHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        imageView.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImageResource(String url){
        isPlus=false;
        if (editable) {
            deleteIcon.setVisibility(VISIBLE);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderKit.getInstance().displayImage(getContext(),url,R.drawable.default_image,R.drawable.default_image,imageView);
    }
    public void setImageResource(@DrawableRes int resource){
        isPlus=true;
        imageView.setImageResource(resource);
        deleteIcon.setVisibility(GONE);
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
        deleteIcon.setVisibility(editable?VISIBLE:GONE);
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            if (v==imageView){
                listener.OnImageClick(this,isPlus);
            }else{
                listener.OnDeleteClick(this);
            }
        }
    }
}
