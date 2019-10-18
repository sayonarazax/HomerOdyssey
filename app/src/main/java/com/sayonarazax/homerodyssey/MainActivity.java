package com.sayonarazax.homerodyssey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().
                getDecorView().
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Button playButtun = findViewById(R.id.buttonPlay);
        playButtun.setOnClickListener(this);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        final TextView highScores = findViewById(R.id.textHighScore);

        long fastestTime = prefs.getLong("fastestTime", 1000000);

        highScores.setText("Fastest Time: " + fastestTime);

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }
}
