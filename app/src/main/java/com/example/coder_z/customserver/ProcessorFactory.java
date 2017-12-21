package com.example.coder_z.customserver;

import android.text.TextUtils;

import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.resultprocessors.XYlibBaseProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibCommonProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibImageProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibKuaidiProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibNBAProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibNewsProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibResponseProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibVoiceProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldy on 2017/2/23.
 */

public class ProcessorFactory {
    public static Map<String, BaseProcessor> processorMap = new HashMap<>();
    public static CommonProcessor commonProcessor;
    public static XYlibImageProcessor imageProcessor;

    public static BaseProcessor createProcessor(String cmd) {
        initMap();

        BaseProcessor processor;

        if (TextUtils.isEmpty(cmd)) {
            processor = commonProcessor;
        } else {
            if (processorMap.containsKey(cmd)) {
                processor = processorMap.get(cmd);
            } else {
                processor = commonProcessor;
            }
        }

        return processor;
    }

    private static void initProcessor() {
        if (commonProcessor == null) {
            commonProcessor = new CommonProcessor();
        }

        if (imageProcessor == null) {
            imageProcessor = new XYlibImageProcessor();
        }
    }

    private static void initMap() {
        if (processorMap.size() > 0) {
            return;
        }

        initProcessor();
        processorMap.put(URLConstants.CMD_SCENARIO, commonProcessor);
        processorMap.put(URLConstants.CMD_SONG, commonProcessor);
        processorMap.put(URLConstants.CMD_KNOWLEDGE, commonProcessor);
        processorMap.put(URLConstants.CMD_EXCHANGERAGE, commonProcessor);
        processorMap.put(URLConstants.CMD_TRANSLATE, commonProcessor);
    }
}
