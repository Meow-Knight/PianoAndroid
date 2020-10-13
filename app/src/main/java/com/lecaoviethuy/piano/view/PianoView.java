package com.lecaoviethuy.piano.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.lecaoviethuy.piano.MainActivity;
import com.lecaoviethuy.piano.R;
import com.lecaoviethuy.piano.entities.Key;
import com.lecaoviethuy.piano.util.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class PianoView extends View {
    private int keyNumber = 14;
    private List<Key> whites, blacks;
    private Paint black, white, yellow;
    private int keyWidth, keyHeight;
    private SoundManager soundManager;

    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        soundManager = SoundManager.getInstance();
        soundManager.init(context);

        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);

        whites = new ArrayList<>();
        blacks = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        keyWidth = MainActivity.widthOfScreen / keyNumber;
        keyHeight = MainActivity.heightOfScreen;

        for (int i = 0; i < keyNumber; i++){
            int left = i * keyWidth;
            int right = left + keyWidth;

            RectF rectF = new RectF(left, 0, right, keyHeight);
            whites.add(new Key(0, rectF, false));

            if(i != 0 && i != 3 && i!=7 && i!=10){
                rectF = new RectF((float) (i-1) * keyWidth + 0.75f * keyWidth
                                , 0
                                , (float) i*keyWidth + 0.25f * keyWidth
                                , 0.67f*keyHeight);
                blacks.add(new Key(0, rectF, false));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("onDraw");
        super.onDraw(canvas);

        for(Key key : whites){
            canvas.drawRect(key.getRect(), key.isDown() ? yellow : white);
        }
        for(Key key : blacks){
            canvas.drawRect(key.getRect(), key.isDown() ? yellow : black);
            if(key.isDown()){
                System.out.println("Downnnnnnnnnnnnnnnnnnnnnnnnnnnn");
            }
        }

        for(int i = 0; i < keyNumber; i++){
            if(i != 0 && i != 3 && i!=7 && i!=10){
                canvas.drawLine(i * keyWidth, 0.67f * keyHeight, i * keyWidth, keyHeight, black);
            } else {
                canvas.drawLine(i * keyWidth, 0, i * keyWidth, keyHeight, black);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("onTouch");
        for(Key key : whites){
            key.setDown(false);
        }
        for(Key key : blacks){
            key.setDown(false);
        }

        int action = event.getAction();
        System.out.println("action: " + action);
        boolean isDownAction = (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE);
        System.out.println("isDownAction: " + isDownAction);

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++){
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            for (Key key : blacks){
                if(key.getRect().contains(x, y)){
                    System.out.println("Black " + isDownAction);
                    key.setDown(isDownAction);
                    soundManager.playSound(key.getSound()); // not yet
                    invalidate();
                    return true;
                }
            }

            for (Key key : whites){
                if(key.getRect().contains(x, y)){
                    System.out.println("White" + isDownAction);
                    key.setDown(isDownAction);
                    soundManager.playSound(key.getSound()); // not yet
                    invalidate();
                    return true;
                }
            }

        }

        return true;
    }
}
