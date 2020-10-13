package com.lecaoviethuy.piano.entities;

import android.graphics.RectF;

public class Key {
    private int sound;
    private RectF rect;
    private boolean down;

    public Key(int sound, RectF rect, boolean down) {
        this.sound = sound;
        this.rect = rect;
        this.down = down;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
