package com.sayonarazax.homerodyssey;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackGroundMusic extends Service {

    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.galaxy);
        player.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int resId) {
        player.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        player.stop();
        if(player != null) player.release();
    }
}
