package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

import static com.example.myapplication.MainActivity.levelValue;
import static com.example.myapplication.MainActivity.scoreValue;

public class EndActivity extends AppCompatActivity {
    private static final int CODE_FOR_PRINT_ACTIVITY = 123 ;
    private static final String TAG = "EndActivity";
    private ConstraintLayout layout;
    private int current_score;
    private int current_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        layout = findViewById(R.id.endlayout);
        new ScrollingCloud(this,layout,R.drawable.clouds,9000);
        setupFullScreen();
        getScreenDimensions();

        checkTop10Score();
    }

    private void checkTop10Score()
    {
        current_score = scoreValue;
        GameDatabaseHandler gameDatabaseHandler = new GameDatabaseHandler(this);
        gameDatabaseHandler.execute(String.format(Locale.getDefault(), "%d", current_score));
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

    public void setResult(String s) {
        Intent selected_intent = new Intent(EndActivity.this, DisplayScoreActivity.class);
        selected_intent.putExtra("print",s);
        startActivityForResult(selected_intent,CODE_FOR_PRINT_ACTIVITY);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setupFullScreen();
        fadeTitleImage();
    }

    public void updateScoreInTable() {
        current_level = levelValue;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter your name initials(up to 3 characters)");
        builder.setTitle("You are Top-Player!");
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setFilters(new InputFilter[]
                {
                        new InputFilter.AllCaps(),
                        new InputFilter.LengthFilter(3)
                });

        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Log.d(TAG, "onClick: "+ et.getText().toString());
                insertScore(et.getText().toString(),current_level);
            }
        });
        builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(EndActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void insertScore(String initalize, int current_level) {
        current_score = scoreValue;
        GameDatabaseHandler gameDatabaseHandler = new GameDatabaseHandler(this);
        gameDatabaseHandler.execute(String.valueOf(current_score),initalize,String.valueOf(current_level));
    }

    private void fadeTitleImage() {

        ImageView titleImage = findViewById(R.id.imageView);
        titleImage.setZ(10);
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(titleImage, "alpha", 1.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
        alpha.start();


    }
}
