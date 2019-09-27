package com.sayonarazax.homerodyssey;

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
    private SoundPool soundPool;
    private int music = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        AssetManager assetManager = this.getAssets();
        AssetFileDescriptor descriptor;
        try {
            descriptor = assetManager.openFd("mainmusic.ogg");
            music = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        soundPool.play(music, 1, 1, 0, 1, 1);
        gameView = new TDView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        gameView.resume();
    }
}
