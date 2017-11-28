package com.emotibot.xychatlib.helpers;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;
import com.emotibot.xychatlib.utils.XYlibCommonResultUtils;

/**
 * Created by ldy on 2017/2/24.
 */

//解析服务器返回的JSON格式答案，根据信息类型将信息交付给不同Processor处理，控制Robot说话

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
            String msgType = cru.getType();                                                         //
            String command = cru.getCmd();
            String value = cru.getValue();                                                          //答案内容
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
                //检测在返回的过程中是否为LIST类型的数据，如果是，则设置相应的标志位
                boolean MSG_LIST=false;
                Pattern r = Pattern.compile("<list>");
                Matcher m = r.matcher(value);
                while(m.find()){
                    m.group();
                    MSG_LIST=true;
                    break;
                }
                if (MSG_LIST)
                    ResultProcessorFactory.createProcessor(URLConstants.CMD_RESPONSE).process(mController, msgType,
                            URLConstants.CMD_RESPONSE, value, json);
                else
                    ResultProcessorFactory.createProcessor(command).process(mController, msgType,
                            command, value, json);
            }
        }

    }
}
