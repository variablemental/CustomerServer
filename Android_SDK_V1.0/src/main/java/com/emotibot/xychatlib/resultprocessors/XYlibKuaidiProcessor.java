package com.emotibot.xychatlib.resultprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibKuaidiProcessor extends XYlibBaseProcessor {
    public static final int ANSWERSIZE=4;

    @Override
    public void process(XYlibChatController controller, List<String> answers){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type","kuaidi");

            JSONArray jsonArray = new JSONArray();
            for (String answer:answers) {
                jsonArray.put(answer);
            }

            jsonObject.put("answers", jsonArray);
            processKuaidi(jsonObject.toString(), controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processKuaidi(String json, XYlibChatController controller) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray answers = jsonObject.getJSONArray("answers");
            if (answers.length() > ANSWERSIZE || answers.length() <= 0) {
                controller.showRobotSay(XYlibConfig.ERR_FORMAT_FAIL);
                return;
            }

            String answer = answers.getString(0);
            controller.showRobotSay(answer);
            if (answers.length() <= 1) {
                return;
            }

            controller.showRobotSay(json, XYlibChatMessageUtils.KUAIDI);
        } catch (Exception e) {
            controller.showRobotSay(XYlibConfig.ERR_FORMAT_FAIL);
        }
    }
}
