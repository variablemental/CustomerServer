package com.emotibot.xychatlib.utils;

import android.util.Log;

import com.emotibot.xychatlib.XYlibConfig;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibLogUtils {
    private static final boolean DEBUG = XYlibConfig.DEBUG;

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }
}
