package com.sayonarazax.homerodyssey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
