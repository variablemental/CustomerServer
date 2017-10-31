package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/2/23.
 */

public class RobotTextVH extends XYlibBaseViewHolder {
    TextView timeView;
    SimpleDraweeView headView;
    TextView contentView;
    RelativeLayout contentContainer;

    public RobotTextVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        timeView = (TextView) v.findViewById(R.id.timeView);
        headView = (SimpleDraweeView) v.findViewById(R.id.headView);
        contentView = (TextView) v.findViewById(R.id.contentView);
        contentContainer = (RelativeLayout) v.findViewById(R.id.content_container);
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
        contentView.setText(chatMessage.getMsg());
        //todo copy
        //setChatContentLongClickListener(contentContainer, contentView.getText().toString());
    }


}
