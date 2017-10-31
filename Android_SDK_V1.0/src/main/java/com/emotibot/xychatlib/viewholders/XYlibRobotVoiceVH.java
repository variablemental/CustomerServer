package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;

/**
 * Created by ldy on 2017/3/1.
 */

public class XYlibRobotVoiceVH extends XYlibVoiceVH {
    public XYlibRobotVoiceVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        super.initViews(v);
        statusIndicator = (ImageView) v.findViewById(R.id.unReadIndicator);
        default_background = R.drawable.chat_rec_shadow_voice_3;
        anim_background = R.drawable.chat_rec_shadow_voice_anim;
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        super.bindView(chatMessage, activity, pos);
        animView.setBackgroundResource(default_background);
        statusIndicator.setVisibility(View.GONE);
    }
}
