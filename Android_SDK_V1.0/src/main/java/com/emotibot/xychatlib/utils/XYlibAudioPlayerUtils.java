package com.emotibot.xychatlib.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.emotibot.xychatlib.models.XYlibChatMessage;

/**
 * Created by ldy on 2017/2/28.
 */

public class XYlibAudioPlayerUtils {

    private static final String TAG = XYlibAudioPlayerUtils.class.getSimpleName();
    private static final String THREAD_SYSTEM_PLAYER = "system_player";

    public static final int MEDIA_XIMALAYA = 1;
    public static final int MEDIA_SYSTEM = 2;

    private static XYlibAudioPlayerUtils ourInstance;

    private int mSelectedMedia = MEDIA_XIMALAYA;

//    private XmPlayerManager mXmPlayerManager;
    private MediaPlayer mSystemMediaPlayer;

    private XYlibChatMessage mPlayingChatMessage;
    private AudioPlayerCallback mCallback;

    private static Context mContext;

    public static XYlibAudioPlayerUtils getInstance(Context context) {
        mContext = context;

        if (ourInstance == null) {
            ourInstance = new XYlibAudioPlayerUtils();
        }

        return ourInstance;
    }

    private XYlibAudioPlayerUtils() {
        xmPlayerSetup();
        systemPlayerSetup();
    }

    public void setCallback(AudioPlayerCallback callback) {
        this.mCallback = callback;
    }

    public void pause() {
        checkPlayerIsNull();
        if (mSelectedMedia == MEDIA_XIMALAYA) {

        }
        else if (mSelectedMedia == MEDIA_SYSTEM) {
            mSystemMediaPlayer.pause();
        }
        if (mPlayingChatMessage != null) {
            mPlayingChatMessage.setPlayingVoice(true);
            updateViewStatus(mPlayingChatMessage);
        }
    }

    public void play(XYlibChatMessage chatMessage) {
        checkPlayerIsNull();
        XYlibMediaUtils.reset();

        updateViewStatus(chatMessage);
    }

    private void updateViewStatus(XYlibChatMessage chatMessage) {
        if (mPlayingChatMessage == null || (chatMessage.getUuid() != mPlayingChatMessage.getUuid())) {
            if (mPlayingChatMessage != null) {
                mPlayingChatMessage.setPlayingVoice(false);
            }
            chatMessage.setPlayingVoice(true);
            mPlayingChatMessage = chatMessage;
        }
        else {
            mPlayingChatMessage.setPlayingVoice(!mPlayingChatMessage.isPlayingVoice());
        }
        if (mCallback != null) {
            mCallback.updateViewStatus();
        }
    }

    private void checkPlayerIsNull() {
        if (mSystemMediaPlayer == null) {
            systemPlayerSetup();
        }
    }

    private void xmPlayerSetup() {

    }

    private void systemPlayerSetup() {
        mSystemMediaPlayer = new MediaPlayer();
        mSystemMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayingChatMessage.setPlayingVoice(true);
                updateViewStatus(mPlayingChatMessage);
            }
        });
    }

    public interface AudioPlayerCallback {
        void updateViewStatus();
        void displayMessageProducedByAudioPlayer(String msg);
    }

}
