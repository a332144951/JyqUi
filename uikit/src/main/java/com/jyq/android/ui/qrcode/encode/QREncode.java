package com.jyq.android.ui.qrcode.encode;

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
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.result.ParsedResultType;


/**
 * Created by Administrator on 2017/3/9.
 */

public class QREncode {
    private QREncode() {
    }

    /**
     * @param codeEncoder {@linkplain Builder#build() QREncode.Builder().build()}
     * @return
     */
    public static Bitmap encodeQR(QRCodeEncoder codeEncoder) {
        // This assumes the view is full screen, which is a good assumption
        Context context = codeEncoder.getContext();
        int smallerDimension = getSmallerDimension(context.getApplicationContext());
        try {
            return codeEncoder.encodeAsBitmap(smallerDimension);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getSmallerDimension(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 7 / 8;
        return smallerDimension;
    }

    public static class Builder {

        private Context context;
        private BarcodeFormat barcodeFormat=BarcodeFormat.QR_CODE;
        private ParsedResultType parsedResultType = ParsedResultType.TEXT;
        private String contents;//原内容
        private int color;//颜色
        public Builder(Context context) {
            this.context = context;
        }

        BarcodeFormat getBarcodeFormat() {
            return barcodeFormat;
        }


        ParsedResultType getParsedResultType() {
            return parsedResultType;
        }


        String getContents() {
            return contents;
        }

        /**
         * 二维码内容
         *
         * @param contents tel、email等不需要前缀
         * @return
         */
        public Builder setContents(String contents) {
            this.contents = contents;
            return this;
        }

        int getColor() {
            return color;
        }

        /**
         * 设置二维码颜色
         *
         * @param color
         * @return
         */
        public Builder setColor(int color) {
            this.color = color;
            return this;
        }


        public QRCodeEncoder build() {
            if (context == null)
                throw new IllegalArgumentException("context no found...");
            if (contents==null){
                throw new IllegalArgumentException("contents not found...");
            }
            return new QRCodeEncoder(this, context.getApplicationContext());
        }
    }
}
