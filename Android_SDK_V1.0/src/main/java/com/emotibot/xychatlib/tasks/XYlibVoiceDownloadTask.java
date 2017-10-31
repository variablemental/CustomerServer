package com.emotibot.xychatlib.tasks;

import android.os.AsyncTask;

import java.io.File;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.utils.XYlibVoiceUtils;

/**
 * Created by ldy on 2017/3/2.
 */

public class XYlibVoiceDownloadTask extends AsyncTask<String, Void, File> {
    XYlibChatController mController;
    String mDestPath;

    public XYlibVoiceDownloadTask(XYlibChatController mController, String mDestPath) {
        this.mController = mController;
        this.mDestPath = mDestPath;
    }

    protected File doInBackground(String... urls) {
        String url = urls[0];
        File file = mController.getNetworkHelper().downloadFile(url, mDestPath);
        return file;
    }

    protected void onPostExecute(File result) {
        if (result == null) {
            mController.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        int seconds = XYlibVoiceUtils.getVoiceLength(mController.getActivity(), mDestPath);
        mController.showRobotSayVoice(seconds, mDestPath);
    }
}
