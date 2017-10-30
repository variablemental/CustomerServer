package com.emotibot.xychatlib.helpers;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;
import com.emotibot.xychatlib.utils.XYlibCommonResultUtils;

/**
 * Created by ldy on 2017/2/24.
 */

public class XYlibResultProcessHelper {
    private XYlibChatController mController;

    public XYlibResultProcessHelper(XYlibChatController controller) {
        mController = controller;
    }

    public void process(List<String> answers) {
        if (answers.size() <= 0) {
            mController.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        boolean hasVoice = false;
        List<String> answersForKuaidi = new ArrayList<String>();
        for (String json:answers) {
            XYlibCommonResultUtils cru = new XYlibCommonResultUtils(json);
            String msgType = cru.getType();
            String command = cru.getCmd();
            String value = cru.getValue();
            if (command.equals(URLConstants.CMD_KUAIDI)) {
                answersForKuaidi.add(value);
            }
            if (msgType.equals(URLConstants.TYPE_URL)
                    && command.equals(URLConstants.CMD_VOICE)
                    && !TextUtils.isEmpty(value)) {
                hasVoice = true;
                break;
            }
        }

        for (String json:answers) {
            XYlibCommonResultUtils cru = new XYlibCommonResultUtils(json);
            String msgType = cru.getType();
            String command = cru.getCmd();
            String value = cru.getValue();

            if (hasVoice) {
                if (msgType.equals(URLConstants.TYPE_TEXT) && TextUtils.isEmpty(command)) {
                    continue;
                }
            }

            if (command.equals(URLConstants.CMD_KUAIDI)) {
                ResultProcessorFactory.createProcessor(command).process(mController,
                        answersForKuaidi);
                break;
            } else {
                //todo add more processor
                ResultProcessorFactory.createProcessor(command).process(mController, msgType,
                        command, value, json);
            }
        }

    }
}
