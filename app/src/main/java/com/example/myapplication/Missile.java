package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Missile {
    private static final String TAG = "Missile";
    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final long screenTime;
    private final boolean hit = false;
    private final float endX;
    private final float startX;
    static final int INTERCEPTOR_BLAST = 120;

    public Missile(int screenWidth, int screenHeight, long missileTime, final MainActivity mainActivity) {
        this.screenHeight = screenHeight;
        this.mainActivity = mainActivity;
        this.screenTime = missileTime;


        imageView = new ImageView(mainActivity);
        imageView.setX(-500);
        startX = (float) (Math.random() * screenWidth);
        float startY = (float) -100;
        endX = (float) (Math.random() * screenWidth);

        float a = calang(startX, startY, endX, (float) screenHeight);
        imageView.setRotation(a);
        imageView.setZ(-10);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getLayout().addView(imageView);
            }
        });
    }

    private float calang(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (190.0f - angle);
    }

    public AnimatorSet setData(final int drawId) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(drawId);
            }
        });

        final ObjectAnimator yAnim = ObjectAnimator.ofFloat(imageView, "y", -100, screenHeight);
        yAnim.setInterpolator(new LinearInterpolator());
        yAnim.setDuration(screenTime);
        yAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!hit) {
                            mainActivity.getLayout().removeView(imageView);
                            mainActivity.removeMissile(Missile.this);
                        }
                    }
                });



            }
        });

        yAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if((float)(yAnim.getAnimatedValue()) > screenHeight * 0.85)
                    makeGroundBlast();
            }
        });

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "x", (int) (startX), (int) (endX));
        xAnim.setInterpolator(new LinearInterpolator());
        xAnim.setDuration(screenTime);

        aSet.playTogether(xAnim, yAnim);
        return aSet;
    }
    float getX() {
        int xVar = imageView.getWidth() / 2;
        return imageView.getX() + xVar;
    }

    float getY() {
        int yVar = imageView.getHeight() / 2;
        return imageView.getY() + yVar;
    }

    float getWidth() {
        return imageView.getWidth();
    }

    float getHeight() {
        return imageView.getHeight();
    }

    private void makeGroundBlast() {
        aSet.cancel();
        final ImageView explodeView = new ImageView(mainActivity);
        explodeView.setImageResource(R.drawable.explode);
        SoundPlayer.getInstance().start("missile_miss");
        explodeView.setTransitionName("Interceptor blast");

        float w = explodeView.getDrawable().getIntrinsicWidth();

        explodeView.setX(this.getX() - (w/2));

        explodeView.setY(this.getY() - (w/2));

        explodeView.setZ(-15);
        mainActivity.getLayout().addView(explodeView);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(explodeView, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(explodeView);
            }
        });
        alpha.start();
        mainActivity.applyMissileBlast(this,imageView.getId());
    }


    public void stop() {
        aSet.cancel();
    }

    public void interceptorBlast(float x2, float y2) {

        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.explode);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);
        SoundPlayer.getInstance().start("interceptor_hit_missile");
        iv.setX(x2 - offset);
        iv.setY(y2 - offset);
        iv.setZ(-15);
        iv.setRotation((float) (360.0 * Math.random()));

        aSet.cancel();

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();

    }

}