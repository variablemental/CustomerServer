package com.emotibot.xychatlib.factorys;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.resultprocessors.XYlibBaseProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibCommonProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibImageProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibKuaidiProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibNBAProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibNewsProcessor;
import com.emotibot.xychatlib.resultprocessors.XYlibVoiceProcessor;

/**
 * Created by ldy on 2017/2/23.
 */

public class ResultProcessorFactory {
    public static Map<String, XYlibBaseProcessor> processorMap = new HashMap<>();
    public static XYlibCommonProcessor commonProcessor;
    public static XYlibImageProcessor imageProcessor;

    public static XYlibBaseProcessor createProcessor(String cmd) {
        initMap();

        XYlibBaseProcessor processor;

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
            commonProcessor = new XYlibCommonProcessor();
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
        processorMap.put(URLConstants.CMD_VOICE, new XYlibVoiceProcessor());
        processorMap.put(URLConstants.CMD_KUAIDI, new XYlibKuaidiProcessor());
        processorMap.put(URLConstants.CMD_NEWS, new XYlibNewsProcessor());
        processorMap.put(URLConstants.CMD_NBA, new XYlibNBAProcessor());
        processorMap.put(URLConstants.CMD_IMAGE, imageProcessor);
        processorMap.put(URLConstants.CMD_PICTURE, imageProcessor);
    }
}
