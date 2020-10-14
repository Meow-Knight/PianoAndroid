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
import com.lecaoviethuy.piano.entities.Key;
import com.lecaoviethuy.piano.util.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class PianoView extends View {
    private int keyNumber = 14;
    private List<Key> whites, blacks;
    private Paint black, white, yellow, red;
    private int keyWidth, keyHeight, screenWidth, screenHeight;
    private SoundManager soundManager;
    private List<Integer> soundKeys;

    private List<Key> lastTouchedKey;

    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        soundManager = SoundManager.getInstance();
        soundManager.init(context);
        soundKeys = soundManager.getSoundKeys();

        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);
        red = new Paint();
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);

        whites = new ArrayList<>();
        blacks = new ArrayList<>();
        lastTouchedKey = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        keyWidth = w / keyNumber;
        keyHeight = h / 2;
        screenWidth = w;
        screenHeight = h;

        int bottomBlackKeyCount = 14;
        int topBlackKeyCount = 23;
        for (int i = 0; i < keyNumber; i++){
            int left = i * keyWidth;
            int right = left + keyWidth;

            // bottom white
            RectF rectF = new RectF(left, keyHeight, right, screenHeight);
            whites.add(new Key(soundKeys.get(i), rectF, false));
            // top white
            rectF = new RectF(left, 0, right, keyHeight);
            whites.add(new Key(soundKeys.get(keyNumber - 1 - i), rectF, false));

            // bottom black
            if(i != 0 && i != 3 && i!=7 && i!=10){
                rectF = new RectF((float) (i-1) * keyWidth + 0.75f * keyWidth
                                , keyHeight
                                , (float) i*keyWidth + 0.25f * keyWidth
                                , 1.67f*keyHeight);
                blacks.add(new Key(soundKeys.get(bottomBlackKeyCount++), rectF, false));
            }

            // top black
            int j = keyNumber - 1 - i;
            if(j != 0 && j != 3 && j!=7 && j!=10){
                rectF = new RectF((float) (i) * keyWidth + 0.75f * keyWidth
                        , (1 - 0.67f)*keyHeight
                        , (float) (i+1)*keyWidth + 0.25f * keyWidth
                        , keyHeight);
                blacks.add(new Key(soundKeys.get(topBlackKeyCount--), rectF, false));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Key key : whites){
            canvas.drawRect(key.getRect(), key.isDown() ? yellow : white);
        }
        for(Key key : blacks){
            canvas.drawRect(key.getRect(), key.isDown() ? yellow : black);
        }

        for(int i = 0; i < keyNumber; i++){
            // bottom line
            if(i != 0 && i != 3 && i!=7 && i!=10){
                canvas.drawLine(i * keyWidth, 1.67f * keyHeight, i * keyWidth, screenHeight, black);
            } else {
                canvas.drawLine(i * keyWidth, keyHeight, i * keyWidth, screenHeight, black);
            }
            //top line
            int j = keyNumber - 1 - i;
            if(j != 0 && j != 3 && j!=7 && j!=10){
                canvas.drawLine((i + 1) * keyWidth, 0, (i + 1) * keyWidth, (1-0.67f) * keyHeight, black);
            } else {
                canvas.drawLine((i + 1) * keyWidth, 0, (i + 1) * keyWidth, keyHeight, black);
            }
        }

        // line separate
        canvas.drawLine(0, keyHeight, screenWidth, keyHeight, red);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        boolean isDownAction = (action == MotionEvent.ACTION_DOWN
                            || action == MotionEvent.ACTION_MOVE
                            || action == MotionEvent.ACTION_POINTER_1_DOWN);

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++){
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            boolean isBlackKeyTyped = false;
            for (Key key : blacks){
                if(key.getRect().contains(x, y)){
                    if(!key.isDown() && !lastTouchedKey.contains(key)){
                        soundManager.playSound(key.getSound());
                    }
                    lastTouchedKey.add(key);
                    key.setDown(isDownAction);
                    isBlackKeyTyped = true;
                }
            }

            for (Key key : whites){
                if(key.getRect().contains(x, y)){
                    if(!isBlackKeyTyped){
                        if(!key.isDown() && !lastTouchedKey.contains(key)){
                            soundManager.playSound(key.getSound());
                        }
                        lastTouchedKey.add(key);
                        key.setDown(isDownAction);
                    }
                }
            }
        }

        // handle last touched key
        if(!isDownAction){
            for(Key key : lastTouchedKey){
                key.setDown(false);
            }
            lastTouchedKey.clear();
        }

        if(action == MotionEvent.ACTION_MOVE){
            List<Key> tmpKey = new ArrayList<>();
            for(Key key : lastTouchedKey){
                key.setDown(false);
            }

            for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++){
                float x = event.getX(touchIndex);
                float y = event.getY(touchIndex);

                for(Key key : lastTouchedKey){
                    if(key.getRect().contains(x, y)){
                        key.setDown(true);
                        tmpKey.add(key);
                    }
                }
            }
            lastTouchedKey = tmpKey;
        }

        invalidate();
        return true;
    }
}
