package com.jyq.android.ui.starView;

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
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.jyq.android.ui.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * Created by Administrator on 2017/3/10.
 */

public class CountDownTextView extends AppCompatTextView {
    private countThread mCountDownTimer;
    private boolean isCountDown = false;
    private SimpleDateFormat simpleDateFormat;
    private OnFinishListener listener;

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        if (!isInEditMode())
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void start(long millisInFuture) {
        if (mCountDownTimer==null){
            mCountDownTimer=new countThread(millisInFuture);
        }
        setTimeText(millisInFuture);
        mCountDownTimer.start();
    }

    public void stop() {
        isCountDown = false;
        mCountDownTimer.interrupt();
    }

    public boolean isCountDown() {
        return isCountDown;
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
////        isCountDown=false;
////        if (mCountDownTimer!=null){
////            mCountDownTimer.cancel();
////        }
//        super.setText(text, type);
//    }
    public void setCustomText(CharSequence text){
        isCountDown=false;
        if (mCountDownTimer!=null){
            mCountDownTimer.interrupt();
        }
        super.setText(text);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isCountDown = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.interrupt();
        }
    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (mCountDownTimer!=null){
//            mCountDownTimer.start();
//        }
//    }

    private void setTimeText(long millisUntilFinished) {
        isCountDown = true;
        CharSequence text = simpleDateFormat.format(millisUntilFinished);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(0.5f), 8, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        super.setText(spannableString);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public interface OnFinishListener {
        void onCountFinish(View view);
    }

    private class countThread extends Thread {
        private long millisInFuture;
        private CountDownTimer countDownTimer;
        public countThread(long millisInFuture) {
            this.millisInFuture = millisInFuture;
            countDownTimer = new CountDownTimer(millisInFuture, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setTimeText(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    setTimeText(0);
                    isCountDown=false;
                    if (listener != null) {
                        listener.onCountFinish(CountDownTextView.this);
                    }
                }
            };
        }

        @Override
        public void run() {
            countDownTimer.start();
        }
    }
}
