package com.emotibot.xychatlib.vhproducers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;
import com.emotibot.xychatlib.viewholders.XYlibRobotVoiceVH;

/**
 * Created by ldy on 2017/3/1.
 */

public class XYlibRobotVoiceVHProducer implements XYlibBaseProducer {
    public XYlibBaseViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent,
                                                int viewType) {
        View view = inflater.inflate(R.layout.item_chat_robot_voice, parent, false);
        return new XYlibRobotVoiceVH(view);
    }
}
