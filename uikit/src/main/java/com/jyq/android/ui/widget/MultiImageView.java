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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.ui.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */

public class MultiImageView extends ViewGroup {
    private int mMaxItem = 9;
    private int mItemSize;
    private int mItemSpace;
    private boolean mEditable;
    private ArrayList<String> mImages;

    public MultiImageView(Context context) {
        this(context, null);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mItemSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, displayMetrics);
        mItemSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics);
        mEditable = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childrenCount=getItemCount();
        for (int i=0;i<childrenCount;i++){
            EditAbleImageView view= (EditAbleImageView) getChildAt(i);
            if (view==null){
                view=generalItemView();
                addView(view,generalItemLayoutParams());
            }
            int col=getCol(i);
            int row=getRow(i);
            int left=col*(mItemSize+mItemSpace);
            int top=row*(mItemSize+mItemSpace);
            view.layout(left,top,left+mItemSize,top+mItemSize);
            view.postInvalidate();
            String url=getImageUrl(i);
            if (TextUtils.isEmpty(url)){
                view.getImageView().setImageResource(R.drawable.edit_image_plus_selector);
            }else{
                ImageLoaderKit.getInstance().displayImage(getContext(),url,R.drawable.default_image,R.drawable.default_image,view.getImageView());
            }
        }
    }
    public String getImageUrl(int position){
        int offset=(mImages.size()<mMaxItem&&mEditable)?1:0;
        int realPos=position-offset;
        return realPos<0?null:mImages.get(realPos);
    }
    private int getItemCount(){
        return mEditable?Math.min(mImages.size()+1,mMaxItem):mImages.size();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
//    private void layoutChild(){
//        removeAllViews();
//        if (mImages==null&&!mEditable){
//            return;
//        }
//        int count=mEditable?Math.min(mImages.size()+1,mMaxItem):mImages.size();
//        for (int i=0;i<count;i++){
//            View view=generalItemView();
//            addView(view);
//        }
//    }
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int width = MeasureSpec.getSize(measureSpec);
        width = width - getPaddingLeft() - getPaddingRight();
        int maxCol = getMaxCol();
        mItemSize = (width - (maxCol * mItemSpace)) / 3;
        return width;
    }

    private int getMaxCol() {
        return getItemCount() >= 3 ? 3 :getItemCount();
    }

    private static final String TAG = "MultiImageView";

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int width = MeasureSpec.getSize(measureSpec);
        int maxRow = getMaxRow();
        result = maxRow * mItemSize +  (maxRow-1)*mItemSpace;
        return result;
    }

    private static int getCol(int position) {
        return position % 3;
    }


    /**
     * 行数
     *
     * @return
     */
    private int getMaxRow() {
        return getRow(mImages.size() - 1)+1;
    }

    private static int getRow(int position) {
        return position/3;
    }

    public void setImages(ArrayList<String> mImages) {
        this.mImages = mImages;
        removeAllViews();
        requestLayout();
        postInvalidate();
    }

    private EditAbleImageView generalItemView(){
        EditAbleImageView imageView=new EditAbleImageView(getContext());
        imageView.setEditable(mEditable);
//        imageView.setLayoutParams(generalItemLayoutParams());
        return imageView;
    }
    private LayoutParams generalItemLayoutParams(){
        Log.e(TAG, "generalItemLayoutParams: "+mItemSize );
        return new LayoutParams(mItemSize,mItemSize);
    }
}
