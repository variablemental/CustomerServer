package com.emotibot.xychatlib.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;

/**
 * Created by ldy on 2017/2/23.
 */

public abstract class XYlibBaseViewHolder extends RecyclerView.ViewHolder {
    public static final int ANIM_VIEW_LEFT = 0x201;
    public static final int ANIM_VIEW_RIGHT = 0x202;

    protected XYlibChatActivity mActivity;

    protected int mMinItemWidth;
    protected int mMaxItemWidth;

    public XYlibBaseViewHolder(View view) {
        super(view);
        initViews(view);
    }

    protected abstract void initViews(View view);
    public abstract void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity,
                                  int pos);
}
