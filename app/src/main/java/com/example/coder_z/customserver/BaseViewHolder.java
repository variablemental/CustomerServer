package com.example.coder_z.customserver;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;

/**
 * Created by ldy on 2017/2/23.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public static final int ANIM_VIEW_LEFT = 0x201;
    public static final int ANIM_VIEW_RIGHT = 0x202;

    protected chatActivity mActivity;

    protected int mMinItemWidth;
    protected int mMaxItemWidth;

    public BaseViewHolder(View view) {
        super(view);
        initViews(view);
    }

    protected abstract void initViews(View view);
    public abstract void bindView(XYlibChatMessage chatMessage, chatActivity activity,
                                  int pos);
}
