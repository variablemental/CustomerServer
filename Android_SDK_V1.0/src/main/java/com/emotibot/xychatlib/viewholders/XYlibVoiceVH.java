package com.emotibot.xychatlib.viewholders;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.helpers.XYlibMediaPlayHelper;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.utils.XYlibMediaUtils;

/**
 * Created by ldy on 2017/3/1.
 */

public class XYlibVoiceVH extends XYlibBaseViewHolder implements View.OnClickListener {
    TextView timeView;
    ImageView headView;
    RelativeLayout cotentContainer;
    TextView voiceLength;
    ImageView animView;
    ImageView statusIndicator;
    XYlibChatMessage mChatMessage;

    int default_background;
    int anim_background;

    public XYlibVoiceVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        timeView = (TextView) v.findViewById(R.id.timeView);
        headView = (ImageView) v.findViewById(R.id.headView);
        animView = (ImageView) v.findViewById(R.id.animView);
        animView.setTag(ANIM_VIEW_RIGHT);
        cotentContainer = (RelativeLayout) v.findViewById(R.id.content_container);
        voiceLength = (TextView) v.findViewById(R.id.voiceLength);

        cotentContainer.setOnClickListener(this);
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        mChatMessage = chatMessage;
        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);

        getMinMaxItemWidth();
        setVoiceItemLength(cotentContainer, chatMessage.getVoice_length());
        voiceLength.setText(String.valueOf(chatMessage.getVoice_length()) + "''");
    }

    private void playVoice(final XYlibChatMessage cm) {
        cm.setPlayingVoice(true);
        XYlibMediaUtils.playSound(cm.getVoicePath(), new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAnim(cm, animView);
            }
        }, new XYlibMediaPlayHelper.OnResetListener() {
            @Override
            public void onPlayerReset() {
                stopAnim(cm, animView);
            }
        }, new XYlibMediaPlayHelper.OnReleaseListener() {
            @Override
            public void onPlayerRelease() {
                stopAnim(cm, animView);
            }
        });
    }

    protected void setVoiceItemLength(View view, int length) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f * length));
    }

    protected void getMinMaxItemWidth() {
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.43f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
    }

    protected void stopAnim(XYlibChatMessage cm, ImageView animView) {
        cm.setPlayingVoice(false);
        animView.setBackgroundResource(default_background);
    }

    protected void playVoiceAnim(ImageView animView) {
        animView.setBackgroundResource(anim_background);
        AnimationDrawable anim = (AnimationDrawable) animView.getBackground();
        anim.start();
    }

    @Override
    public void onClick(View view) {
        if (mChatMessage.isPlayingVoice()) {
            XYlibMediaUtils.release();
        } else {
            XYlibMediaUtils.release();
            playVoice(mChatMessage);
            playVoiceAnim(animView);
        }
    }
}
