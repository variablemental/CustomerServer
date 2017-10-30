package com.emotibot.xychatlib.vhproducers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.viewholders.UserTextVH;
import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibUserTextVHProducer implements XYlibBaseProducer {
    public XYlibBaseViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent,
                                                int viewType) {
        View view = inflater.inflate(R.layout.item_chat_user_text, parent, false);
        return new UserTextVH(view);
    }
}
