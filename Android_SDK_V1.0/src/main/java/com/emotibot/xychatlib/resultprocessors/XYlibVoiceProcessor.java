package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.io.File;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;
import com.emotibot.xychatlib.tasks.XYlibVoiceDownloadTask;
import com.emotibot.xychatlib.utils.XYlibFileUtils;
import com.emotibot.xychatlib.utils.XYlibVoiceUtils;

/**
 * Created by ldy on 2017/3/1.
 */

public class XYlibVoiceProcessor extends XYlibBaseProcessor {

    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value, String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (type.equals(URLConstants.TYPE_URL)) {
            if (TextUtils.isEmpty(value.trim())) {
                value = XYlibConfig.ERR_REPLY_FAIL;
            }

            showVoiceMsg(controller, value);
        } else {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }
    }

    private void showVoiceMsg(XYlibChatController controller, String url) {
        final String filename = URLUtil.guessFileName(url, null, null);

        if (TextUtils.isEmpty(filename)) {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }

        final String path = XYlibFileUtils.buildVoiceChatPath() + filename;
        File voiceFile = new File(path);
        if (voiceFile.exists()) {
            int seconds = XYlibVoiceUtils.getVoiceLength(controller.getActivity(), path);
            controller.showRobotSayVoice(seconds, path);
        } else {
            new XYlibVoiceDownloadTask(controller, path).execute(url);
        }
    }
}
