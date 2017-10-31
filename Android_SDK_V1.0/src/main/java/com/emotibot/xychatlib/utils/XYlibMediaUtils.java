package com.emotibot.xychatlib.utils;

import android.media.MediaPlayer;

import java.io.IOException;

import com.emotibot.xychatlib.helpers.XYlibMediaPlayHelper;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibMediaUtils {
    private static final String tag = XYlibMediaUtils.class.getSimpleName();
    private static XYlibMediaPlayHelper mediaPlayer;
    private static boolean isPause;

    public static void playSound(String filePath,
                                 MediaPlayer.OnCompletionListener onCompletionListener,
                                 XYlibMediaPlayHelper.OnResetListener onResetListener,
                                 XYlibMediaPlayHelper.OnReleaseListener onReleaseListener) {
        if (mediaPlayer == null) {
            mediaPlayer = new XYlibMediaPlayHelper();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.reset();
                    XYlibLogUtils.i(tag, "mp error");
                    return false;
                }
            });
        } else {
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnResetListener(onResetListener);
            mediaPlayer.setOnReleaseListener(onReleaseListener);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void reset(){
        if(mediaPlayer != null){
            mediaPlayer.reset();
        }
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mediaPlayer != null && isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
