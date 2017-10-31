package com.emotibot.xychatlib.tasks;

import android.os.AsyncTask;

import java.io.File;

import com.emotibot.xychatlib.controllers.XYlibChatController;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibImageDownloadTask extends AsyncTask<String, Void, File> {
    XYlibChatController mController;
    String mDestPath;

    public XYlibImageDownloadTask(XYlibChatController mController, String mDestPath) {
        this.mController = mController;
        this.mDestPath = mDestPath;
    }

    protected File doInBackground(String... urls) {
        String url = urls[0];
        File file = mController.getNetworkHelper().downloadFile(url, mDestPath);
        return file;
    }
}
