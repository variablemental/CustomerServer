package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/1.
 */

public class XYlibUserVoiceVH extends XYlibVoiceVH {
    public XYlibUserVoiceVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        super.initViews(v);
        statusIndicator = (ImageView) v.findViewById(R.id.status_indicator);
        default_background = R.drawable.chat_rec_user_voice_3;
        anim_background = R.drawable.chat_rec_user_voice_anim;
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        super.bindView(chatMessage, activity, pos);
        if(chatMessage.getState() == XYlibChatMessageUtils.MSG_STATUS_ERROR){
            statusIndicator.setVisibility(View.VISIBLE);
        }else {
            statusIndicator.setVisibility(View.GONE);
        }
        animView.setBackgroundResource(default_background);
    }
}
