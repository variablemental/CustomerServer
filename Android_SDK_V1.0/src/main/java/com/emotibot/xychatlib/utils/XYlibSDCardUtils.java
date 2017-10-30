package com.emotibot.xychatlib.utils;

import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibSDCardUtils {
    /**
     * 判断当前设备上是否存在SD卡
     * @return	如果当前设备上存在SD卡，则返回true，否则返回false
     */
    public static boolean isSdCardExist(){
        boolean isExist = false;
        isExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return isExist;
    }

    /**
     * 获取SD卡的根路径
     * @return	返回SD卡的根路径
     */
    public static String getSdCardPath(){
        if(isSdCardExist()){
            try {
                File path;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    path = Environment.getExternalStorageDirectory();
                } else {
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                }

                if (path.exists()) {
                    return path.getAbsolutePath() + File.separator;
                }

                if (path.mkdir()) {
                    return path.getAbsolutePath() + File.separator;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
