package com.emotibot.xychatlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.emotibot.xychatlib.XYlibConfig;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibFileUtils {
    public static File fileDir;
    private static final String VOICE_CHAT = "voice_chat";
    private static final String IMAGE_CHAT = "image_chat";
    public static String buildRootPath() {
        String path;
        if (XYlibSDCardUtils.isSdCardExist()) {
            if (!TextUtils.isEmpty(XYlibSDCardUtils.getSdCardPath())) {
                path = XYlibSDCardUtils.getSdCardPath() + XYlibConfig.ROOT_PATH + File.separator;
            } else {
                path = fileDir.getPath() + File.separator;
            }
        } else {
            path = fileDir.getPath() + File.separator;
        }

        return path;
    }

    public static String buildVoiceChatPath() {
        String path = buildRootPath();
        path = path + VOICE_CHAT + File.separator;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }

    public static String buildImageChatPath() {
        String path = buildRootPath();
        path = path + IMAGE_CHAT + File.separator;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }

    public static File saveToSDCard(byte[] data, String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            file = null;
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }

        return file;
    }

    public static Uri resIdToUri(Context context, int resourceId) {
        Resources resources = context.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        return uri;
    }
}
