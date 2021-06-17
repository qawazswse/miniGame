package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Interceptor {
    private static int count = 0;
    private final MainActivity mainActivity;
    private ImageView imageview;
    private final float startX, startY;
    private float endX;
    private float endY;
    private ObjectAnimator moveX, moveY;

    public Interceptor(MainActivity mainActivity, float startX, float startY, float endX, float endY) {
        this.mainActivity = mainActivity;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        int id = count++;
        initialize();
    }

    private void initialize() {
        MainActivity.icCount++;
        imageview = new ImageView(mainActivity);
        SoundPlayer.getInstance().start("launch_interceptor");
        imageview.setImageResource(R.drawable.interceptor);

        final int www = (int) (imageview.getDrawable().getIntrinsicWidth() * 0.5);

        imageview.setX(startX);
        imageview.setY(startY);
        imageview.setZ(-10);

        endX -= www;
        endY -= www;

        float a = calculateAngle(imageview.getX(), imageview.getY(), endX, endY);  //missile image angle

        imageview.setRotation(a);

        mainActivity.getLayout().addView(imageview);

        double distance =  Math.sqrt((endY - imageview.getY()) * (endY - imageview.getY()) + (endX - imageview.getX()) * (endX - imageview.getX()));
        long duration = (long) (distance * 2);

        moveX = ObjectAnimator.ofFloat(imageview, "x", endX);
        moveX.setInterpolator(new AccelerateInterpolator());
        moveX.setDuration(duration);


        moveY = ObjectAnimator.ofFloat(imageview, "y", endY);
        moveY.setInterpolator(new AccelerateInterpolator());
        moveY.setDuration(duration);

        moveX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.missileCount --;
                mainActivity.getLayout().removeView(imageview);
                makeBlast();
            }
        });

    }

    private void makeBlast() {
        MainActivity.icCount--;
        final ImageView explodeView = new ImageView(mainActivity);
        explodeView.setImageResource(R.drawable.i_explode);

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

        mainActivity.applyInterceptorBlast(this, imageview.getId());
    }

    private float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (190.0f - angle);
    }

    public void launch() {
        moveX.start();
        moveY.start();
    }
    float getX() {
        int xVar = imageview.getWidth() / 2;
        return imageview.getX() + xVar;
    }

    float getY() {
        int yVar = imageview.getHeight() / 2;
        return imageview.getY() + yVar;
    }
}
