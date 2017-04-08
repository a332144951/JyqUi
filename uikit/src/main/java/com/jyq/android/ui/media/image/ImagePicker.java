package com.jyq.android.ui.media.image;

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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.jyq.android.common.storage.StorageType;
import com.jyq.android.common.storage.StorageUtil;
import com.jyq.android.common.string.StringUtil;
import com.jyq.android.ui.media.image.bean.ImageItem;
import com.jyq.android.ui.media.image.ui.ImagePickerFragment;
import com.jyq.android.ui.media.image.utils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class ImagePicker {
    public static final int REQUEST_CODE_TAKE = 1001;
    private static final ImagePicker ourInstance = new ImagePicker();
    private String outputPath ;

    public static ImagePicker getInstance() {
        return ourInstance;
    }

    private ImagePicker() {
    }

    public static void showImagePicker(Context context,ArrayList<ImageItem> mSelectedImages, ImagePickerFragment.OnImagePickerListener listener){
        showImagePicker(context,9,mSelectedImages,listener);
    }
    public static void showImagePicker(Context context,int limit, ArrayList<ImageItem> mSelectedImages, ImagePickerFragment.OnImagePickerListener listener){
        showImagePicker(context,limit,true,mSelectedImages,listener);
    }
    public static void showImagePicker(Context context,int limit, boolean multiMode, ArrayList<ImageItem> selectedImages, ImagePickerFragment.OnImagePickerListener listener){
//        new ImagePickerFragment().show(context);

    }

    public Intent takePicture(Context context,String filePath) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            outputPath=filePath;
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));
        }
        return takePictureIntent;
    }

    public void galleryAddPic(Context context) {
        insertPic(context);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(outputPath));
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    private void insertPic(Context context){
        final ContentValues values=new ContentValues(1);
        values.put(MediaStore.Images.Media.DATA,outputPath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
    }

    public void resolveImageDegree(){
        BitmapUtils.resolveBitmapDegress(outputPath);
    }



}
