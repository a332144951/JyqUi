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

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.google.common.collect.Lists;
import com.jyq.android.ui.R;
import com.jyq.android.ui.media.image.ImageDataSource;
import com.jyq.android.ui.media.image.ImagePicker;
import com.jyq.android.ui.media.image.bean.ImageFolder;
import com.jyq.android.ui.media.image.bean.ImageItem;
import com.jyq.android.ui.media.image.utils.ImagePickerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/2.
 */

public class ImagePickerFragment extends JToolbarDialog implements ImageDataSource.OnImagesLoadedListener,ImagePickerGridAdapter.OnImageItemClickListener {
    public interface OnImagePickerListener{
        void onImagePicker(ArrayList<ImageItem> imageItems);
    }
    private OnImagePickerListener listener;
    private boolean showCamera=false;
    private int limit;
    private boolean multiMode;
    private ArrayList<ImageItem> mSelectedImages;

    public ImagePickerFragment(OnImagePickerListener listener, boolean showCamera, int limit, boolean multiMode, ArrayList<ImageItem> mSelectedImages) {
        this.listener = listener;
        this.showCamera = showCamera;
        this.limit = limit;
        this.multiMode = multiMode;
        this.mSelectedImages = mSelectedImages;
    }

    private static final String TAG = "ImagePickerFragment";
    private GridView gridView;
    private ImagePickerGridAdapter pickerGridAdapter;
    private ImageDataSource imageDataSource;
    private String filePath;
    @Override
    int getLayoutId() {
        return R.layout.image_picker_layout;
    }

    @Override
    void onBackClick() {
        dismiss();
    }

    @Override
    void onCreateToolbarMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_uikit_text,menu);
        ((AppCompatButton)menu.findItem(R.id.uikit_menu_text).getActionView()).setText(getString(R.string.picker_image_btn,pickerGridAdapter.getSelectedCount(),limit));
    }

    @Override
    boolean onToolbarMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    void initViews() {
        if (showCamera){
            takePicture();
            return;
        }
        gridView = (GridView) getView().findViewById(R.id.image_picker_grid);
       imageDataSource= new ImageDataSource(this, null, this);
        pickerGridAdapter = new ImagePickerGridAdapter(getContext(), null,limit,mSelectedImages,multiMode);
        pickerGridAdapter.setOnImageItemClickListener(this);
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        if (imageFolders.size() == 0) pickerGridAdapter.refreshData(null);
        else pickerGridAdapter.refreshData(imageFolders.get(0).images);
        gridView.setAdapter(pickerGridAdapter);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {

    }

     void takePicture() {
         filePath= ImagePickerUtils.radomNewPicPath();
        startActivityForResult(ImagePicker.getInstance().takePicture(getContext(),filePath),ImagePicker.REQUEST_CODE_TAKE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK&&requestCode==ImagePicker.REQUEST_CODE_TAKE){
            ImagePicker.getInstance().galleryAddPic(getContext());
            ImagePicker.getInstance().resolveImageDegree();
//            imageDataSource.resetLoader();
            ImageItem item=new ImageItem();
            item.path=filePath;
            if (listener!=null){
                listener.onImagePicker(Lists.<ImageItem>newArrayList(item));
            }
            dismiss();
        }
    }

    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
            invalidateOptionsMenu();
    }
}
