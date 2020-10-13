package com.lecaoviethuy.piano.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.SparseIntArray;

import com.lecaoviethuy.piano.R;


public class SoundManager {

    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;
    private boolean mMuted = false;

    private static final int MAX_STREAMS = 10;
    private static final int STOP_DELAY_MILLIS = 10000;

    private static SoundManager instance = null;
    private Handler mHandler;
    private Context mContext;

    public SoundManager(){
        mSoundPool = new SoundPool(MAX_STREAMS,
                AudioManager.STREAM_MUSIC,
                0);
        mSoundPoolMap = new SparseIntArray();
        mHandler = new Handler();
    }

    public static SoundManager getInstance(){
        if(instance == null){
            instance = new SoundManager();
        }
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
        instance.initStreamtypeMedia((Activity) context);
        instance.addSound(R.raw.c3);
        instance.addSound(R.raw.c4);
        instance.addSound(R.raw.d3);
        instance.addSound(R.raw.d2);
        instance.addSound(R.raw.e3);
        instance.addSound(R.raw.e4);
        instance.addSound(R.raw.f3);
        instance.addSound(R.raw.f4);
        instance.addSound(R.raw.g3);
        instance.addSound(R.raw.g4);
        instance.addSound(R.raw.a3);
        instance.addSound(R.raw.a4);
        instance.addSound(R.raw.b3);
        instance.addSound(R.raw.b4);
        instance.addSound(R.raw.ab3);
        instance.addSound(R.raw.ab4);
        instance.addSound(R.raw.gb3);
        instance.addSound(R.raw.gb4);
//        instance.addSound(R.raw.bb4);
//        instance.addSound(R.raw.db4);
//        instance.addSound(R.raw.c4);
//        instance.addSound(R.raw.ab2);
//        instance.addSound(R.raw.ab2);
//        instance.addSound(R.raw.ab2);
    }

    public void initStreamtypeMedia(Activity activity){
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void addSound(int soundId){
        mSoundPoolMap.put(soundId, mSoundPool.load(mContext, soundId, 1));
    }

    public void playSound(int soundId){
        if(mMuted){
            return;
        }
        boolean hasSound = mSoundPoolMap.indexOfKey(soundId) >= 0;

        if(!hasSound){
            return;
        }

        final int sound = mSoundPool.play(mSoundPoolMap.get(soundId),
                1, 1, 1, 0, 1f);
    }

    private void scheduleSoundStop(final int soundId){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSoundPool.stop(soundId);
            }
        }, STOP_DELAY_MILLIS);
    }

}
