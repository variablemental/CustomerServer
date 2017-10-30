package com.emotibot.xychatlib.utils;

import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.tasks.XYlibImageDownloadTask;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibImageUtils {
    public static void showImage(XYlibChatActivity activity, final String url,
                                 final String filename, final ImageView imageView) {
        String path = XYlibFileUtils.buildImageChatPath() + filename;
        File file = new File(path);

        if (file.exists()) {
            imageView.setImageURI(Uri.parse("file:///" + file));
        } else {
            new XYlibImageDownloadTask(activity.getChatController(), path){
                @Override
                protected void onPostExecute(File result) {
                    if (result == null) {
                        imageView.setImageResource(R.drawable.loading_pic);
                        return;
                    }

                    imageView.setImageURI(Uri.parse("file:///" + result));
                }
            }.execute(url);
        }
    }
}
