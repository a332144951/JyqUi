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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.ui.R;
import com.jyq.android.ui.media.image.ImagePicker;
import com.jyq.android.ui.media.image.bean.ImageItem;
import com.jyq.android.ui.media.image.utils.ImagePickerUtils;
import com.jyq.android.ui.widget.SuperCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

class ImagePickerGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ImageItem> imageItems;
    private int mImageSize;
    private ArrayList<ImageItem> mSelectedImages;
    private OnImageItemClickListener listener;   //图片被点击的监听
    private int limit;
    private boolean isMultiMode;
    public ImagePickerGridAdapter(Context context, ArrayList<ImageItem> imageItems,int limit,ArrayList<ImageItem> mSelectedImages,boolean multiMode) {
        this.context = context;
        if (imageItems == null) {
            this.imageItems = new ArrayList<>();
        } else {
            this.imageItems = imageItems;
        }
        mImageSize = ImagePickerUtils.getImageItemWidth(context);
        this.mSelectedImages = mSelectedImages;
        this.limit=limit;
        isMultiMode=multiMode;
    }

    public void refreshData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) this.imageItems = new ArrayList<>();
        else this.imageItems = images;
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return imageItems.size();
    }

    @Override
    public ImageItem getItem(int position) {
        return imageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_image_list_item, parent, false);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ImageItem imageItem = getItem(position);

            holder.ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onImageItemClick(holder.rootView, imageItem, position);
                }
            });
            holder.cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.cbCheck.isChecked() && mSelectedImages.size() >= limit) {
                        Toast.makeText(context, context.getString(R.string.select_limit, limit), Toast.LENGTH_SHORT).show();
                        holder.cbCheck.setChecked(false);
                    } else {
                        if (listener!=null){
                            listener.onImageSelected(position, imageItem, holder.cbCheck.isChecked());
                        }

                    }
                    holder.mask.setVisibility(holder.cbCheck.isChecked() ? View.VISIBLE : View.GONE);
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (isMultiMode) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                boolean checked = mSelectedImages.contains(imageItem);
                if (checked) {
                    holder.mask.setVisibility(View.VISIBLE);
                    holder.cbCheck.setChecked(true);
                } else {
                    holder.mask.setVisibility(View.GONE);
                    holder.cbCheck.setChecked(false);
                }
            } else {
                holder.cbCheck.setVisibility(View.GONE);
            }

            ImageLoaderKit.getInstance().displayImage(context, imageItem.path, R.drawable.default_image, R.drawable.default_image, holder.ivThumb);
        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView ivThumb;
        public View mask;
        public SuperCheckBox cbCheck;

        public ViewHolder(View view) {
            rootView = view;
            ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
            mask = view.findViewById(R.id.mask);
            cbCheck = (SuperCheckBox) view.findViewById(R.id.cb_check);
        }
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }
}
