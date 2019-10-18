package com.sayonarazax.homerodyssey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
    private SoundPool soundPool;
    int start = -1;
    int bump = -1;
    int destroyed = -1;
    int win = -1;
    int music = -1;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    volatile boolean playing;
    Thread gameThread = null;
    public ArrayList<SpaceDust> dustList = new ArrayList<>();
    private PlayerShip mPlayerShip;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    private Canvas mCanvas;
    private SurfaceHolder holder;
    private Paint mPaint;
    private int screenX, screenY;
    private Context context;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    private boolean gameEnded;

    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        initSounds();

        screenX = x;
        screenY = y;
        holder = getHolder();
        mPaint = new Paint();

        prefs = context.getSharedPreferences("HiScores",
                context.MODE_PRIVATE);
        editor = prefs.edit();
        fastestTime = prefs.getLong("fastestTime", 1000000);
        startGame();
    }

    private void initSounds() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            }
        });
        try{
//Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
//create our three fx in memory ready for use
            descriptor = assetManager.openFd("start.ogg");
            start = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("win.ogg");
            win = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("bump.ogg");
            bump = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("destroyed.ogg");
            destroyed = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("mainmusic.ogg");
            music = soundPool.load(descriptor, 0);
        }catch(IOException e){
//Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    private void startGame(){
//Initialize game objects
        mPlayerShip = new PlayerShip(context, screenX, screenY);
        enemy1 = new EnemyShip(context, screenX, screenY);
        enemy2 = new EnemyShip(context, screenX, screenY);
        enemy3 = new EnemyShip(context, screenX, screenY);
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
// Where will the dust spawn?
            SpaceDust spec = new SpaceDust(screenX, screenY);
            dustList.add(spec);
        }
// Reset time and distance
        distanceRemaining = 10000;// 10 km
        timeTaken = 0;
// Get start time
        timeStarted = System.currentTimeMillis();
        gameEnded = false;
        soundPool.play(start, 1, 1, 0,0, 1);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            mCanvas = holder.lockCanvas();
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));
            // For debugging
// Switch to white pixels
          //  mPaint.setColor(Color.argb(255, 255, 255, 255));
// Draw Hit boxes
         /*   mCanvas.drawRect(mPlayerShip.getHitBox().left,
                    mPlayerShip.getHitBox().top,
                    mPlayerShip.getHitBox().right,
                    mPlayerShip.getHitBox().bottom,
                    mPaint);
            mCanvas.drawRect(enemy1.getHitBox().left,
                    enemy1.getHitBox().top,
                    enemy1.getHitBox().right,
                    enemy1.getHitBox().bottom,
                    mPaint);
            mCanvas.drawRect(enemy2.getHitBox().left,
                    enemy2.getHitBox().top,
                    enemy2.getHitBox().right,
                    enemy2.getHitBox().bottom,
                    mPaint);
            mCanvas.drawRect(enemy3.getHitBox().left,
                    enemy3.getHitBox().top,
                    enemy3.getHitBox().right,
                    enemy3.getHitBox().bottom,
                    mPaint);
                    */
            // White specs of dust
            mPaint.setColor(Color.argb(255, 255, 255, 255));
//Draw the dust from our arrayList
            for (SpaceDust sd : dustList)
                mCanvas.drawPoint(sd.getX(), sd.getY(), mPaint);
            mCanvas.drawBitmap(mPlayerShip.getBitmap(), mPlayerShip.getX(), mPlayerShip.getY(), mPaint);
            mCanvas.drawBitmap
                    (enemy1.getBitmap(),
                            enemy1.getX(),
                            enemy1.getY(), mPaint);
            mCanvas.drawBitmap
                    (enemy2.getBitmap(),
                            enemy2.getX(),
                            enemy2.getY(), mPaint);
            mCanvas.drawBitmap
                    (enemy3.getBitmap(),
                            enemy3.getX(),
                            enemy3.getY(), mPaint);
            // Draw the hud
            if(!gameEnded) {
                mPaint.setTextAlign(Paint.Align.LEFT);
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(25);
                mCanvas.drawText("Fastest:" + fastestTime + "s", 10, 20, mPaint);
                mCanvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, mPaint);
                mCanvas.drawText("Distance:" + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 20, mPaint);
                mCanvas.drawText("Shield:" + mPlayerShip.getShieldStrength(), 10, screenY - 20, mPaint);
                mCanvas.drawText("Speed:" + mPlayerShip.getSpeed() * 60 + " MPS", (screenX / 3) * 2, screenY - 20, mPaint);
            } else {
                // Show pause screen
                mPaint.setTextSize(80);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mCanvas.drawText("Game Over", screenX/2, 100, mPaint);
                mPaint.setTextSize(25);
                mCanvas.drawText("Fastest:"+
                        fastestTime + "s", screenX/2, 160, mPaint);
                mCanvas.drawText("Time:" + timeTaken +
                        "s", screenX / 2, 200, mPaint);
                mCanvas.drawText("Distance remaining:" +
                        distanceRemaining/1000 + " KM",screenX/2, 240, mPaint);
                mPaint.setTextSize(80);
                mCanvas.drawText("Tap to replay!", screenX/2, 350, mPaint);
            }
            holder.unlockCanvasAndPost(mCanvas);
            }
    }

    private void update() {
        mPlayerShip.update();
        // Update the enemies
        for (SpaceDust sd : dustList) {
            sd.update(mPlayerShip.getSpeed());
        }
        enemy1.update(mPlayerShip.getSpeed());
        enemy2.update(mPlayerShip.getSpeed());
        enemy3.update(mPlayerShip.getSpeed());
        boolean hitDetected = false;
        if(Rect.intersects
                (mPlayerShip.getHitBox(), enemy1.getHitBox())){
            hitDetected = true;
            enemy1.setX(-1000);
        }
        if(Rect.intersects
                (mPlayerShip.getHitBox(), enemy2.getHitBox())){
            hitDetected = true;
            enemy2.setX(-1000);
        }
        if(Rect.intersects
                (mPlayerShip.getHitBox(), enemy3.getHitBox())){
            hitDetected = true;
            enemy3.setX(-1000);
        }
        if(hitDetected) {
            mPlayerShip.reduceShieldStrength();
            soundPool.play(bump, 1, 1, 0, 0, 1);
            if (mPlayerShip.getShieldStrength() < 0) {
                gameEnded = true;
                soundPool.play(destroyed, 1, 1, 0, 0, 1);
            }
        }

        if(!gameEnded) {
//subtract distance to home planet based on current speed
            distanceRemaining -= mPlayerShip.getSpeed();
//How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        //Completed the game!
        if(distanceRemaining < 0){
//check for new fastest time
            soundPool.play(win, 1, 1, 0, 0, 1);
            if(timeTaken < fastestTime) {
                editor.putLong("fastestTime", timeTaken);
                editor.commit();
                fastestTime = timeTaken;
            }
// avoid ugly negative numbers
// in the HUD
            distanceRemaining = 0;
// Now end the game
            gameEnded = true;
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
// There are many different events in MotionEvent
// We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                mPlayerShip.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                mPlayerShip.startBoosting();
                if(gameEnded) {
                    startGame();
                }
                break;
        }
        return true;
    }
}
