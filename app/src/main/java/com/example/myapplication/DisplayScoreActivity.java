package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DisplayScoreActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private static final String TAG = "DisplayScoreActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_score);
        layout = findViewById(R.id.displayscorelayout);
        setupFullScreen();
        getScreenDimensions();

        TextView displaytxt = findViewById(R.id.displayResult);
        Intent selected_intent = getIntent();
        if(selected_intent.hasExtra("print"))
        {
            String s = selected_intent.getStringExtra("print");
            displaytxt.setText(s);
        }
    }

    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
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

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void doExit(View v)
    {
        finish();
    }
}
