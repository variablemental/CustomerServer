package com.emotibot.xychatlib.viewholders;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.helpers.XYlibTextlinkHelper;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibTextlinkVH extends XYlibBaseViewHolder {
    TextView timeView;
    SimpleDraweeView headView;
    TextView contentView;
    RelativeLayout contentContainer;

    XYlibChatMessage chatMessage;

    public XYlibTextlinkVH(View view) {
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

        final XYlibTextlinkHelper tl = activity.getGson().fromJson(chatMessage.getMsg(),
                                                                XYlibTextlinkHelper.class);

        if (TextUtils.isEmpty(tl.getLink())) {
            contentView.setText(tl.getMsg());
            contentView.setOnClickListener(null);
        } else {
            String text = tl.getMsg();
            String click = "点击查看详情。";

            SpannableString ss = new SpannableString(text + click);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length()-1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.BLUE), tl.getMsg().length(),
                    text.length()+click.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentView.setText(ss);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.getWebViewDialog().loadWebPage(tl.getLink());
                    mActivity.getWebViewDialog().show();
                }
            });
        }

        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
    }
}
