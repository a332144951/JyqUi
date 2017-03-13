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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.ui.R;
import com.jyq.android.ui.widget.dialog.DialogMaker;

/**
 * Created by Administrator on 2017/3/9.
 */

public class StartView extends RelativeLayout {
    private View container;
    private AppCompatImageView image;
    private TextView titleTextView;
    private TextView indexTextView;
    private CountDownTextView countDownTextView;
    private Drawable backendDrawable;
    private CharSequence countDownText;
    private boolean needAnim=true;
    private TextView tipView;
    private ImageView starView;

    public StartView(Context context) {
        this(context, null);
    }

    public StartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater.from(context).inflate(R.layout.uikit_star_card, this, true);
        titleTextView = (TextView) findViewById(R.id.uikit_star_title);
        indexTextView = (TextView) findViewById(R.id.uikit_star_index);
        countDownTextView = (CountDownTextView) findViewById(R.id.uikit_star_countDown);
        countDownTextView.setOnFinishListener(finishListener);
        container = findViewById(R.id.uikit_star_card);
        tipView= (TextView) findViewById(R.id.uikit_star_tip);
        image = (AppCompatImageView) findViewById(R.id.uikit_star_image);
        starView= (ImageView) findViewById(R.id.uikit_star_star);
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet.start();
            }
        });
        prepareAnimator();
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countDownTextView.isCountDown()&&l!=null){
                    l.onClick(v);
                }
            }
        });
    }

    public void turnBack(CharSequence countDownText,String imageUrl,boolean anim){
        this.needAnim=anim;
        this.countDownText=countDownText;
        setImageUrl(imageUrl);
    }
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }
    private CountDownTextView.OnFinishListener finishListener=new CountDownTextView.OnFinishListener() {
        @Override
        public void onCountFinish(View view) {
            container.setClickable(true);
            image.setImageResource(R.drawable.bg_star_ready);
        }
    };
    private void setImageUrl(String url) {
        DialogMaker.showProgressDialog(getContext(),"");
        ImageLoaderKit.getInstance().displayImage(getContext(), url, R.drawable.bg_star_ready, R.drawable.bg_star_fall, image, new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                e.printStackTrace();
                DialogMaker.dismissProgressDialog();
                backendDrawable = getResources().getDrawable(R.drawable.bg_star_fall);
                startTurn();
                return true;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                DialogMaker.dismissProgressDialog();
                backendDrawable = resource;
                startTurn();
                return true;
            }
        });
    }
    private void startTurn(){
        if (needAnim){
            animatorSet.start();
            return;
        }
        changeBackend();
    }

    public void setCountDownText(CharSequence text) {
        this.countDownText = text;
    }

    public void setCountDown(long millions) {
        image.setImageResource(R.drawable.bg_star_wait);
        countDownTextView.start(millions);
    }

    public void setIndex(int index) {
        indexTextView.setText(String.valueOf(index));
    }

    private AnimatorSet animatorSet;
    private static final String TAG = "StartView";

    private void changeBackend() {
        image.setImageDrawable(backendDrawable);
        countDownTextView.setCustomText(countDownText);
        tipView.setVisibility(VISIBLE);
        starView.setVisibility(VISIBLE);
    }

    private void prepareAnimator() {
        animatorSet = new AnimatorSet();
        final ObjectAnimator rotationY90 = ObjectAnimator.ofFloat(container, "rotationY", 0, 90).setDuration(1000);
        rotationY90.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                container.setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "onAnimationEnd: ");
                changeBackend();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(container, "rotationY", 90, -90).setDuration(0);
        ObjectAnimator rotationY0 = ObjectAnimator.ofFloat(container, "rotationY", -90, 0).setDuration(1000);
        animatorSet.playSequentially(rotationY90, rotationY, rotationY0);
        animatorSet.setTarget(container);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tipView.bringToFront();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isrunning = animatorSet.isRunning();
        getParent().requestDisallowInterceptTouchEvent(isrunning);
        return isrunning;
    }
}
