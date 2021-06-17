package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 6000;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSound();
        setContentView(R.layout.activity_splash);
        setupFullScreen();
        getScreenDimensions();

        new Handler().postDelayed(new Runnable() {  //postDelayed will run after specified delay
            @Override
            public void run() {
                SoundPlayer.getInstance().start("background");
                fadeTitleImage();
            }
        }, SPLASH_TIME_OUT);
    }
    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }
    private void loadSound() {
        SoundPlayer.getInstance().setupSound(this, "background", R.raw.background, true);
        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast,false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss,false);
        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);
    }



    private void setupFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void fadeTitleImage() {
        //SoundPlayer.getInstance().start("interceptor_blast"); //playing sound

        ImageView titleImage = findViewById(R.id.titleimage);
        //after explode, it should go away blast
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(titleImage, "alpha", 1.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(SPLASH_TIME_OUT); //within 3 second
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        alpha.start();


    }
}
