package com.sayonarazax.homerodyssey;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import java.io.IOException;

public class GameActivity extends AppCompatActivity {
    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, BackGroundMusic.class));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gameView = new TDView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
        stopService(new Intent(this, BackGroundMusic.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        gameView.resume();
        startService(new Intent(this, BackGroundMusic.class));
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, BackGroundMusic.class));
        finish();
    }

}
