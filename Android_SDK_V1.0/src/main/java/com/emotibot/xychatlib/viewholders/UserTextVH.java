package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/2/23.
 */

public class UserTextVH extends XYlibBaseViewHolder {
    TextView timeView;
    ImageView headView;
    ImageView statusIndicator;
    TextView contentView;
    RelativeLayout contentContainer;

    public UserTextVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        timeView = (TextView) v.findViewById(R.id.timeView);
        headView = (ImageView) v.findViewById(R.id.headView);
        contentView = (TextView) v.findViewById(R.id.contentView);
        contentContainer = (RelativeLayout) v.findViewById(R.id.content_container);
        statusIndicator = (ImageView) v.findViewById(R.id.status_indicator);
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        contentView.setText(chatMessage.getMsg());

        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
        //todo copy
        //setChatContentLongClickListener(contentContainer, contentView.getText().toString());
        if(chatMessage.getState() == XYlibChatMessageUtils.MSG_STATUS_ERROR){
            statusIndicator.setVisibility(View.VISIBLE);
        }else {
            statusIndicator.setVisibility(View.GONE);
        }
    }
}
