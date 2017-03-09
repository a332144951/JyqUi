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

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;

/**
 * Created by Administrator on 2017/3/9.
 */

public interface OnScannerCompletionListener {
    /**
     * 扫描成功后将调用
     * <pre>
     *     ParsedResultType type = parsedResult.getType();
     *     switch (type) {
     *         case ADDRESSBOOK:
     *             AddressBookParsedResult addressResult = (AddressBookParsedResult) parsedResult;
     *         break;
     *         case URI:
     *              URIParsedResult uriParsedResult = (URIParsedResult) parsedResult;
     *         break;
     *     }
     * </pre>
     *
     * @param rawResult    扫描结果
     * @param parsedResult 抽象类，结果转换成目标类型
     * @param barcode      位图
     */
    void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode);
}
