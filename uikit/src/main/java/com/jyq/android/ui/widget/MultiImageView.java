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
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jyq.android.ui.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */

public class MultiImageView extends GridLayout {
    public interface MultiImageItemClickListener{
        void OnItemClick(int position, ArrayList<String> images, Uri imageUri, boolean isPlus);
    }
    public interface MultiImageItemDeleteListener{
        void OnItemDelete(int position);
    }
    private int mMaxItem = 9;
    private int mItemSize;
    private int mItemSpace;
    private boolean mEditable;
    private ArrayList<String> mImages;
    private MultiImageItemClickListener mItemClickListener;
    private MultiImageItemDeleteListener mItemDeleteListener;
    public MultiImageView(Context context) {
        this(context,null);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.MultiImageView);
        mItemSpace=a.getDimensionPixelSize(R.styleable.MultiImageView_itemSpace,0);
        mEditable=a.getBoolean(R.styleable.MultiImageView_editable,false);
        mMaxItem=a.getInteger(R.styleable.MultiImageView_maxItemCount,9);
        a.recycle();
       setImages(new ArrayList<String>());
        setOrientation(HORIZONTAL);
    }

    public void setItemClickListener(MultiImageItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setItemDeleteListener(MultiImageItemDeleteListener mItemDeleteListener) {
        this.mItemDeleteListener = mItemDeleteListener;
    }

    public void setMaxItem(int mMaxItem) {
        this.mMaxItem = mMaxItem;
    }

    public String getImageUrl(int position) {
        int offset = (mImages.size() < mMaxItem && mEditable) ? 1 : 0;
        int realPos = position - offset;
        return realPos < 0 ? null : mImages.get(realPos);
    }

    private int getItemCount() {
        return mEditable ? Math.min(mImages.size() + 1, mMaxItem) : mImages.size();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view==null){
                continue;
            }
            int row = getRow(i);
            int col = getCol(i);
            int childLeft = col * (mItemSpace + mItemSize);
            int childTop = row * (mItemSize + mItemSpace);
            view.layout(childLeft, childTop, childLeft + mItemSize, childTop + mItemSize);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        measureChildren(MeasureSpec.makeMeasureSpec(mItemSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mItemSize, MeasureSpec.EXACTLY));
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

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
        return getItemCount() >= 3 ? 3 : getItemCount();
    }

    private static final String TAG = "MultiImageView";

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int width = MeasureSpec.getSize(measureSpec);
        int maxRow = getMaxRow();
        result = maxRow * mItemSize + (maxRow - 1) * mItemSpace;
        Log.e(TAG, String.format("measureHeight: row:%d,height:%d",maxRow,result) );
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
        return getRow(getItemCount() - 1) + 1;
    }

    private static int getRow(int position) {
        return position / 3;
    }


    public void setImages(ArrayList<String> mImages) {
        this.mImages = mImages;
        reloadImages();
    }
    private void reloadImages(){
        removeAllViews();
        setRowCount(getMaxRow());
        setColumnCount(getMaxCol());
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            EditAbleImageView view = generalItemView();
            view.setListener(itemClick);
            addView(view);
            String url = getImageUrl(i);
            if (TextUtils.isEmpty(url)) {
                view.setImageResource(R.drawable.edit_image_plus_selector);
            } else {
                view.setTag(mImages.indexOf(url));
                view.setImageResource(url);

            }
        }
        requestLayout();
        postInvalidate();
    }

    private EditAbleImageView generalItemView() {
        EditAbleImageView imageView = new EditAbleImageView(getContext());
        imageView.setEditable(mEditable);
        return imageView;
    }
    private EditAbleImageView.EditableImageViewListener itemClick=new EditAbleImageView.EditableImageViewListener() {
        @Override
        public void OnDeleteClick(EditAbleImageView view) {
            int position= (int) view.getTag();
            mImages.remove(position);
            reloadImages();
            if (mItemDeleteListener!=null){
                mItemDeleteListener.OnItemDelete(position);
            }
        }

        @Override
        public void OnImageClick(EditAbleImageView view, boolean isPlus) {
            if (!isPlus) {
                int position = (int) view.getTag();
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(position, mImages, Uri.parse(mImages.get(position)), false);
                }
            }else{
                if (mItemClickListener!=null){
                    mItemClickListener.OnItemClick(0,null,null,true);
                }
            }
        }

    };

}
