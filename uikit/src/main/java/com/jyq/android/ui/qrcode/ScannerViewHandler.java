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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.jyq.android.ui.qrcode.camera.CameraManager;
import com.jyq.android.ui.qrcode.decode.DecodeThread;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/9.
 */

 class ScannerViewHandler extends Handler {
    private final ScannerView scannerView;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    ScannerViewHandler(ScannerView scannerView,
                       Collection<BarcodeFormat> decodeFormats,
                       Map<DecodeHintType, ?> baseHints, String characterSet,
                       CameraManager cameraManager) {
        this.scannerView = scannerView;
        this.cameraManager = cameraManager;
        //启动扫描线程
        decodeThread = new DecodeThread(cameraManager, this, decodeFormats, baseHints,
                characterSet);
        decodeThread.start();
        state = State.SUCCESS;
        //开启相机预览界面
        cameraManager.startPreview();
        //将preview回调函数与decodeHandler绑定、调用viewfinderView
        restartPreviewAndDecode();
    }

    private static final String TAG = "ScannerViewHandler";
    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case Intents.Scan.RESTART_PREVIEW:
                restartPreviewAndDecode();
                break;
            case Intents.Scan.DECODE_SUCCEEDED:
                state = State.SUCCESS;
                Bundle bundle = message.getData();
                Bitmap barcode = null;
                float scaleFactor = 1.0f;
                if (bundle != null) {
                    byte[] compressedBitmap = bundle
                            .getByteArray(DecodeThread.BARCODE_BITMAP);
                    if (compressedBitmap != null) {
                        barcode = BitmapFactory.decodeByteArray(compressedBitmap,
                                0, compressedBitmap.length, null);
                        barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
                    }
                    scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
                }
                scannerView.handleDecode((Result) message.obj, barcode, scaleFactor);
                break;
            case Intents.Scan.DECODE_FAILED:
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), Intents.Scan.DECODE);
                break;
            case Intents.Scan.RETURN_SCAN_RESULT:
                break;
            case Intents.Scan.LAUNCH_PRODUCT_QUERY:
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), Intents.Scan.QUIT);
        quit.sendToTarget();
        try {
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        removeMessages(Intents.Scan.DECODE_SUCCEEDED);
        removeMessages(Intents.Scan.DECODE_FAILED);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Intents.Scan.DECODE);
            scannerView.drawViewfinder();
        }
    }
}
