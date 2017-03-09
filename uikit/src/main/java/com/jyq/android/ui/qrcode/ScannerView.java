package com.jyq.android.ui.qrcode;

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
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.jyq.android.ui.R;
import com.jyq.android.ui.qrcode.camera.CameraManager;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ScannerView extends FrameLayout implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewfinderView;

    private boolean hasSurface;
    private CameraManager mCameraManager;
    private ScannerViewHandler mScannerViewHandler;
    private BeepManager mBeepManager;
    private OnScannerCompletionListener mScannerCompletionListener;

    private float laserFrameWidth, laserFrameHeight;//扫描框大小
    private float laserFrameTopMargin;//扫描框离屏幕上方距离

    public ScannerView(@NonNull Context context) {
        this(context,null);
    }

    public ScannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context,AttributeSet attrs,int defStyle){
        if (!isInEditMode()) {
            mBeepManager = new BeepManager(context);
        }
        hasSurface=false;
        mSurfaceView = new SurfaceView(context, attrs, defStyle);
        addView(mSurfaceView, new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT));

        mViewfinderView = new ViewfinderView(context, attrs);
        addView(mViewfinderView, new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT));
        laserFrameHeight=getResources().getDimension(R.dimen.laserFrameHeight);
        laserFrameWidth=getResources().getDimension(R.dimen.laserFrameWidth);
        laserFrameTopMargin=getResources().getDimension(R.dimen.laserFrameMarginTop);
    }

    public void onResume() {
        mCameraManager = new CameraManager(getContext());
        mCameraManager.setLaserFrameTopMargin(laserFrameTopMargin);//扫描框与屏幕距离
        mViewfinderView.setCameraManager(mCameraManager);
        mBeepManager.updatePrefs();

        mScannerViewHandler = null;

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }
    }

    public void onPause() {
        if (mScannerViewHandler != null) {
            mScannerViewHandler.quitSynchronously();
            mScannerViewHandler = null;
        }
        mBeepManager.close();
        mCameraManager.closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
//            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            // Creating the mScannerViewHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mScannerViewHandler == null) {
                mScannerViewHandler = new ScannerViewHandler(this, null,
                        null, null, mCameraManager);
            }
            //设置扫描框大小
            if (laserFrameWidth > 0 && laserFrameHeight > 0)
                mCameraManager.setManualFramingRect(laserFrameWidth, laserFrameHeight);
        } catch (IOException ioe) {
//            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
//            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(final Result rawResult, final Bitmap barcode, float scaleFactor) {
        //扫描成功
        if (mScannerCompletionListener != null) {
            //转换结果
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mScannerCompletionListener.OnScannerCompletion(rawResult,
                            ResultParser.parseResult(rawResult)
                            , barcode);
                }
            });

        }
        //设置扫描结果图片
        if (barcode != null) {
            mViewfinderView.drawResultBitmap(barcode);
        }
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            mBeepManager.playBeepSoundAndVibrate();
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder == null) {
//            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
//        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        hasSurface = false;
        if (!hasSurface && surfaceHolder != null) {
            surfaceHolder.removeCallback(this);
        }
    }

    public void setOnScannerCompletionListener(OnScannerCompletionListener listener) {
        this.mScannerCompletionListener = listener;
    }





    /**
     * 设置扫描框4角颜色
     *
     * @param laserFrameBoundColor
     */
    public void setLaserFrameBoundColor(int laserFrameBoundColor) {
        mViewfinderView.setLaserCornerColor(laserFrameBoundColor);
    }

    /**
     * 设置扫描框4角长度
     *
     * @param laserFrameCornerLength dp
     */
    public void setLaserFrameCornerLength(int laserFrameCornerLength) {
        mViewfinderView.setLaserCornerLength(laserFrameCornerLength);
    }

    /**
     * 设置扫描框4角宽度
     *
     * @param laserFrameCornerWidth dp
     */
    public void setLaserFrameCornerWidth(int laserFrameCornerWidth) {
        mViewfinderView.setLaserCornerWidth(laserFrameCornerWidth);
    }

    /**
     * 设置文字
     *
     * @param text
     * @param textSize   文字大小 sp
     * @param textColor  文字颜色
     * @param textMargin 离扫描框间距 dp
     */
    public void setDrawText(String text, int textSize, int textColor
           , int textMargin) {
        mViewfinderView.setDrawText(text, textSize, textColor, textMargin);
    }

    /**
     * 设置扫描完成播放声音
     *
     * @param mediaResId
     */
    public void setMediaResId(int mediaResId) {
        mBeepManager.setMediaResId(mediaResId);
    }

    /**
     * 切换闪光灯
     *
     * @param mode true开；false关
     */
    public void toggleLight(boolean mode) {
        if (mCameraManager != null)
            mCameraManager.setTorch(mode);
    }

    /**
     * 设置扫描框大小
     *
     * @param width  dp
     * @param height dp
     */
    public void setLaserFrameSize(int width, int height) {
        this.laserFrameWidth= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,width,getResources().getDisplayMetrics());

        this.laserFrameHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,height,getResources().getDisplayMetrics());
    }

    /**
     * 设置扫描框与屏幕距离
     *
     * @param laserFrameTopMargin
     */
    public void setLaserFrameTopMargin(int laserFrameTopMargin) {
        this.laserFrameTopMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,laserFrameTopMargin,getResources().getDisplayMetrics());
    }

    /**
     * 重新扫描，支持延时
     *
     * @param delayMS 毫秒
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mScannerViewHandler != null) {
            mScannerViewHandler.sendEmptyMessageDelayed(Intents.Scan.RESTART_PREVIEW, delayMS);
        }
    }

    ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }
}
