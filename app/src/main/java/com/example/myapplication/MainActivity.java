package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ConstraintLayout layout;
    public static int screenHeight;
    public static int screenWidth;
    private MissileMaker missileMaker;
    private ImageView launcher1;
    private ImageView launcher2;
    private ImageView launcher3;
    public static int levelValue = 1;
    private BaseLauncher baseLauncher;
    public static int scoreValue = 0;
    private static final int CODE_FOR_CHECK_SCORE = 111;
    private TextView score, level;
    public int missileCount = 0;
    public static int icCount;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        score = findViewById(R.id.score);
        score.setText("score: 0");
        level = findViewById(R.id.levelText);
        level.setText("Level 1");
        incrementScore(0);
        if(launcher1 == null || launcher2 == null || launcher3 == null) {
            launcher1 = findViewById(R.id.launcher1);
            launcher2 = findViewById(R.id.launcher2);
            launcher3 = findViewById(R.id.launcher3);
        }

        baseLauncher = new BaseLauncher(this,launcher1,launcher2,launcher3);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handleTouch(motionEvent.getX(), motionEvent.getY());
                }
                return false;
            }
        });
        setupFullScreen();
        getScreenDimensions();

        new ScrollingCloud(this,layout,R.drawable.clouds,9000);
        missileMaker = new MissileMaker(this,screenWidth,screenHeight);
        new Thread(missileMaker).start();

    }

    public ConstraintLayout getLayout() {
        return layout;
    }
    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
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

    public void handleTouch(float x1, float y1) {
        if(icCount >= 3) return;
        if(baseLauncher.activeBase.size() == 0)
        {
            doStop();
        }
        else {
            baseLauncher.setBaseImageOnScreen(x1,y1);
        }
    }

    public void doStop()
    {
        Intent selected_intent = new Intent(MainActivity.this, EndActivity.class);
        startActivityForResult(selected_intent, CODE_FOR_CHECK_SCORE);
        missileMaker.setRunning(false);
        finish();
    }
    public void callInterceptor(double startX, double startY, float x1, float y1)
    {
        Interceptor i = new Interceptor(this,  (float) (startX - 70), (float) (startY+20), x1, y1);
        i.launch();
        missileCount ++;
    }

    public void removeMissile(Missile missile)
    {
        missileMaker.removeMissile(missile);
    }

    public void setLevel(final int levelNum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                level.setText(String.format(Locale.getDefault(), "Level: %d", levelNum));
                levelValue = levelNum;
            }
        });
    }


    public void applyMissileBlast(Missile missile, int id) {
        baseLauncher.applyMissileBlast(missile);
    }



    public void applyInterceptorBlast(Interceptor interceptor, int id) {
        missileMaker.applyInterceptorBlast(interceptor,id);
        baseLauncher.applyInterceptorBlast(interceptor);
    }

    public int getScore()
    {
        return scoreValue;
    }
    public void incrementScore(int scoreValue) {
        this.scoreValue = scoreValue;
        score.setText("score: " + scoreValue);
    }
}
