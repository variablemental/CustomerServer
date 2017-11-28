package com.emotibot.xychatlib.factorys;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.vhproducers.XYlibBaseProducer;
import com.emotibot.xychatlib.vhproducers.XYlibNBAVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibNewsVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibResponseVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibRobotImageVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibRobotKuaidiVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibRobotResWaitProducer;
import com.emotibot.xychatlib.vhproducers.XYlibRobotTextVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibRobotVoiceVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibTextlinkVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibUserTextVHProducer;
import com.emotibot.xychatlib.vhproducers.XYlibUserVoiceVHProducer;
import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibViewHolderFactory {
    private static Map<Integer, XYlibBaseProducer> producerMap = new HashMap<>();

    public static XYlibBaseViewHolder createViewHolder (LayoutInflater inflater, ViewGroup parent, int viewType) {
        XYlibBaseViewHolder viewHolder;

        //设置Viewtype和各个构造方法的映射关系
        initMap();
        if (producerMap.containsKey(viewType)) {
            viewHolder = producerMap.get(viewType).createViewHolder(inflater, parent, viewType);
        } else {
            viewHolder = null;
        }

        return viewHolder;
    }

    private static void initMap() {
        if (producerMap.size() > 0) {
            return;
        }
        producerMap.put(XYlibChatMessageUtils.TO_TEXT, new XYlibUserTextVHProducer());
        producerMap.put(XYlibChatMessageUtils.TO_VOICE, new XYlibUserVoiceVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_TEXT, new XYlibRobotTextVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_TEXT_LINK, new XYlibTextlinkVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_TYPING, new XYlibRobotResWaitProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_VOICE, new XYlibRobotVoiceVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_KUAIDI, new XYlibRobotKuaidiVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_NEWS_PAGES, new XYlibNewsVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_NBA, new XYlibNBAVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_IMAGE, new XYlibRobotImageVHProducer());
        producerMap.put(XYlibChatMessageUtils.FROM_LIST,new XYlibResponseVHProducer());
    }
}
