package com.emotibot.xychatlib.vhproducers;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;

/**
 * Created by ldy on 2017/2/23.
 */

public interface XYlibBaseProducer {
    XYlibBaseViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent,
                                                int viewType);
}
