package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ldy on 2017/2/24.
 * 普通类型信息处理
 */

public class XYlibCommonProcessor extends XYlibBaseProcessor {

    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value,
                        String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (type.equals(URLConstants.TYPE_TEXT)) {
            if (TextUtils.isEmpty(value.trim())) {
                value = XYlibConfig.ERR_REPLY_FAIL;
            }

            List<String> utterance=msg_format(value);
            if (utterance.size()!=0)
                for(String v:utterance){
                    controller.showRobotSay(v);                                                         //最终控制Rotbot说话
                }
            else
                controller.showRobotSay(value);
        } else {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }
    }

    //格式化答案
    private List<String> msg_format(String msg){
        String pattern = "\\[[1-9]*\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        String[] splits=msg.split(pattern);
        LinkedList<String> seq=new LinkedList<String>();
        int i=1;
        while(m.find()){
            seq.add(m.group()+splits[i++]);
        }
        return seq;
    }

}
