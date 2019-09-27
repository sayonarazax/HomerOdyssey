package com.sayonarazax.homerodyssey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerShip {
    private int speed = 0;
    private int x, y;
    private Bitmap bitmap;
    private boolean boosting;

    private final int GRAVITY = -12;
    // Stop ship leaving the screen
    private int maxY;
    private int minY;
    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private int shieldStrength;

    private Rect hitBox;

    public int getShieldStrength() {
        return shieldStrength;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public PlayerShip(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_ship);
        speed = 1;
        minY = 0;
        maxY = screenY - bitmap.getHeight();
        x = 50;
        y = 50;
        shieldStrength = 2;
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void startBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void update() {
        if(boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }

        if(speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if(speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY;

        if(y > maxY) {
            y = maxY;
        }

        if(y < minY) {
            y = minY;
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public void reduceShieldStrength(){
        shieldStrength --;
    }
}
