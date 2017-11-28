package com.emotibot.xychatlib.vhproducers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;
import com.emotibot.xychatlib.viewholders.XYlibNewsVH;
import com.emotibot.xychatlib.viewholders.XYlibResponseVH;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class XYlibResponseVHProducer implements XYlibBaseProducer {
    public XYlibBaseViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent,
                                                int viewType) {
        View view = inflater.inflate(R.layout.item_chat_robot_response, parent, false);
        return new XYlibResponseVH(view);
    }
}