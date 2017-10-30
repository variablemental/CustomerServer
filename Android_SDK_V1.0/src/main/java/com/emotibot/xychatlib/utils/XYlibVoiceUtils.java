package com.emotibot.xychatlib.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by ldy on 2017/3/2.
 */

public class XYlibVoiceUtils {
    public static int getVoiceLength(Context context, String voice_path) {
        Uri uri = Uri.parse(voice_path);
        int voiceLengthMills = MediaPlayer.create(context, uri).getDuration() + 500;
        if (voiceLengthMills < 1000) {
            voiceLengthMills = 1000;
        }
        return (voiceLengthMills) / 1000;
    }
}
