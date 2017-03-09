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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.jyq.android.ui.R;
import com.jyq.android.ui.qrcode.camera.CameraManager;

/**
 * Created by Administrator on 2017/3/8.
 */

public class ViewfinderView extends View {

    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private int maskColor = Color.parseColor("#66000000");
    private int laserCornerColor = Color.parseColor("#FF66DF02");
    private float laserCornerLength;
    private float laserCornerWidth;
    private int laserFrameColor = Color.parseColor("#FF777978");
    private float LaserFrameWidth;
    private String drawText = "将二维码放入框内，即可扫描";
    private int drawTextColor = Color.parseColor("#FFB3B3B1");
    private float drawTextSize;
    private float drawTextTop;

    private Paint paint;
    private Bitmap resultBitmap;
    private CameraManager cameraManager;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        laserCornerLength = getResources().getDimension(R.dimen.laserCornerLength);
        laserCornerWidth = getResources().getDimension(R.dimen.laserCornerWidth);
        drawTextSize = getResources().getDimension(R.dimen.scanTipTextSize);
        drawTextTop = getResources().getDimension(R.dimen.scanTipTextMargin);
        LaserFrameWidth = getResources().getDimension(R.dimen.laserBoundWidth);
    }


    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setLaserCornerColor(int laserCornerColor) {
        this.laserCornerColor = laserCornerColor;
    }

    public void setLaserCornerLength(float laserCornerLength) {
        this.laserCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, laserCornerLength, getResources().getDisplayMetrics());
    }

    public void setLaserCornerWidth(float laserCornerWidth) {
        this.laserCornerWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, laserCornerWidth, getResources().getDisplayMetrics());
        ;
    }

    public void setDrawText(String text, int textSize, int textColor
            , int textMargin) {
        if (!TextUtils.isEmpty(text)) {
            this.drawText = text;
        }
        if (textSize > 0)
            this.drawTextSize = textSize;
        if (textColor > 0)
            this.drawTextColor = textColor;
        if (textMargin > 0)
            this.drawTextTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textMargin, getResources().getDisplayMetrics());;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();//扫描框

        Rect previewFrame = cameraManager.getFramingRectInPreview();//预览框
        if (frame == null || previewFrame == null) {
            return;
        }
        drawMask(canvas, frame);
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            drawFrame(canvas, frame);//绘制扫描框
            drawFrameCorner(canvas, frame);//绘制扫描框4角
            drawText(canvas, frame);// 画扫描框下面的字
        }
    }

    //    遮罩
    private void drawMask(Canvas canvas, Rect rect) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        paint.setColor(maskColor);
//        paint.setAlpha(102);
//        top
        canvas.drawRect(0, 0, width, rect.top, paint);
//        left
        canvas.drawRect(0, rect.top, rect.left, rect.bottom + 1, paint);
//        right
        canvas.drawRect(rect.right + 1, rect.top, width, rect.bottom + 1, paint);
//        bottom
        canvas.drawRect(0, rect.bottom + 1, width, height, paint);
    }

    //    扫描框
    private void drawFrame(Canvas canvas, Rect rect) {
        paint.setColor(laserFrameColor);
        paint.setStrokeWidth(LaserFrameWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
    }

    //    扫描框边角
    private void drawFrameCorner(Canvas canvas, Rect rect) {
        paint.setColor(laserCornerColor);
        paint.setStyle(Paint.Style.FILL);

//左上
        canvas.drawRect(rect.left, rect.top, rect.left + laserCornerWidth, rect.top + laserCornerLength, paint);//竖条
        canvas.drawRect(rect.left, rect.top, rect.left + laserCornerLength, rect.top + laserCornerWidth, paint);//横条
//        右上
        canvas.drawRect(rect.right - laserCornerWidth, rect.top, rect.right, rect.top + laserCornerLength, paint);//竖条
        canvas.drawRect(rect.right - laserCornerLength, rect.top, rect.right, rect.top + laserCornerWidth, paint);//横条
//        左下
        canvas.drawRect(rect.left, rect.bottom - laserCornerLength, rect.left + laserCornerWidth, rect.bottom, paint);
        //竖条
        canvas.drawRect(rect.left, rect.bottom - laserCornerWidth, rect.left + laserCornerLength, rect.bottom, paint);
        //横条
//        右下
        canvas.drawRect(rect.right - laserCornerWidth, rect.bottom - laserCornerLength, rect.right, rect.bottom, paint);
        //竖条
        canvas.drawRect(rect.right - laserCornerLength, rect.bottom - laserCornerWidth, rect.right, rect.bottom, paint);
        //横条
    }

    //    提示文字
    private void drawText(Canvas canvas, Rect rect) {
        int width = canvas.getWidth();
        paint.setColor(drawTextColor);
        paint.setTextSize(drawTextSize);
        final float textWidth = paint.measureText(drawText);
        float left = (width - textWidth) / 2;
        float top = rect.bottom + drawTextTop;
        canvas.drawText(drawText, left, top, paint);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

}
