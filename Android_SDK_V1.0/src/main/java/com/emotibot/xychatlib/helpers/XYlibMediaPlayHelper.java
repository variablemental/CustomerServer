package com.emotibot.xychatlib.helpers;

import android.media.MediaPlayer;

import com.emotibot.xychatlib.utils.XYlibLogUtils;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibMediaPlayHelper extends MediaPlayer {
    private static final String tag = XYlibMediaPlayHelper.class.getSimpleName();
    private OnResetListener onResetListener;
    private OnReleaseListener onReleaseListener;

    public interface OnResetListener{
        void onPlayerReset();
    }

    public interface OnReleaseListener {
        void onPlayerRelease();
    }

    public void setOnResetListener(OnResetListener onResetListener){
        this.onResetListener = onResetListener;
    }

    public void setOnReleaseListener(OnReleaseListener onReleaseListener){
        this.onReleaseListener = onReleaseListener;
    }

    @Override
    public void reset() {
        super.reset();
        if(onResetListener != null){
            XYlibLogUtils.i(tag,"RESET");
            onResetListener.onPlayerReset();
        }
    }

    @Override
    public void release() {
        super.release();
        if(onReleaseListener != null){
            onReleaseListener.onPlayerRelease();
        }
    }
}
