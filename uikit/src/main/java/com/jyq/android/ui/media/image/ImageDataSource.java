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
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.jyq.android.ui.R;
import com.jyq.android.ui.media.image.bean.ImageFolder;
import com.jyq.android.ui.media.image.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/2.
 */

public class ImageDataSource implements LoaderCallbacks<Cursor> {
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageFolder> imageFolders);
    }

    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
//            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
//            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED

    };

    private OnImagesLoadedListener loadedListener;
    private Fragment context;
    private ArrayList<ImageFolder> imageFolders=new ArrayList<>();
    private int id;
    private Bundle bundle;
    public ImageDataSource(Fragment context, String path, OnImagesLoadedListener loadedListener) {
        this.context=context;
        this.loadedListener = loadedListener;
        LoaderManager loaderManager = ((Fragment) context).getLoaderManager();
        if (TextUtils.isEmpty(path)) {
            id=LOADER_ALL;
            bundle=null;
        } else {
            id=LOADER_CATEGORY;
             bundle = new Bundle();
            bundle.putString("path", path);
        }
        loaderManager.initLoader(id, bundle, this);
    }
    public void resetLoader(){
        context.getLoaderManager().restartLoader(id,bundle,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader=null;
        if (id==LOADER_ALL){
            cursorLoader=new CursorLoader(context.getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,null,null,IMAGE_PROJECTION[4]+" DESC");
        }
        if (id==LOADER_CATEGORY){
            cursorLoader=new CursorLoader(context.getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,IMAGE_PROJECTION[1]+" like '%"+args.getString("path")+"%'",null,IMAGE_PROJECTION[4]+" DESC");
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageFolders.clear();
        if (data!=null){
            ArrayList<ImageItem> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                //封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                allImages.add(imageItem);
                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0) {
                //构造所有图片的集合
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.name = context.getResources().getString(R.string.all_images);
                allImagesFolder.path = "/";
                allImagesFolder.cover = allImages.get(0);
                allImagesFolder.images = allImages;
                imageFolders.add(0, allImagesFolder);  //确保第一条是所有图片
            }
        }
        //回调接口，通知图片数据准备完成
//        ImagePicker.getInstance().setImageFolders(imageFolders);
        loadedListener.onImagesLoaded(imageFolders);
    }

    private static final String TAG = "ImageDataSource";
    @Override
    public void onLoaderReset(Loader loader) {
        Log.e(TAG, "onLoaderReset: " );
    }
}
