package com.emotibot.xychatlib.utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

import com.emotibot.xychatlib.XYlibConfig;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibDBUtils {
    private static LiteOrm liteOrm;

    public static void initDB(Context context) {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(context, XYlibConfig.DB_NAME);
            liteOrm.setDebugged(XYlibConfig.DEBUG);
        }
    }

    public static LiteOrm getDB() {
        return liteOrm;
    }
}
