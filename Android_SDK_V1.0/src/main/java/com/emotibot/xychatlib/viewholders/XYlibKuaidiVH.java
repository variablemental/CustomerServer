package com.emotibot.xychatlib.viewholders;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibKuaidiVH extends XYlibBaseViewHolder {
    TextView timeView;
    TextView[] tvTexts;

    public XYlibKuaidiVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        timeView = (TextView) v.findViewById(R.id.timeView);
        tvTexts = new TextView[3];
        tvTexts[0] = (TextView) v.findViewById(R.id.text1);
        tvTexts[1] = (TextView) v.findViewById(R.id.text2);
        tvTexts[2] = (TextView) v.findViewById(R.id.text3);
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;

        parseJson(chatMessage.getMsg());
        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray answers = jsonObject.getJSONArray("answers");

            if (answers.length() < 2) {
                tvTexts[0].setText("数据格式错误");
                tvTexts[0].setVisibility(View.VISIBLE);
                return;
            }

            ArrayList<String> texts = new ArrayList<String>();
            for (int i = 1; i < answers.length(); i++) {
                texts.add(answers.getString(i));
            }

            for (int i = 0; i < tvTexts.length; i++) {
                tvTexts[i].setVisibility(View.GONE);
            }

            Pattern pattern = Pattern.compile("\\d{11}");
            Matcher matcher;
            int start, end;
            int size;
            if(texts.size() > 3) {
                size = 3;
            } else {
                size = texts.size();
            }
            for (int i = 0; i < size; i++) {
                matcher = pattern.matcher(texts.get(i));
                if (matcher.find()) {
                    start = matcher.start();
                    end = matcher.end();

                    String phone = texts.get(i).substring(start, end);
                    SpannableString ss = new SpannableString(texts.get(i));
                    ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, texts.get(i).length()-start,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new URLSpan("tel:"+phone), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    matcher.reset();
                    tvTexts[i].setText(ss);
                    tvTexts[i].setVisibility(View.VISIBLE);
                    tvTexts[i].setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    tvTexts[i].setText(texts.get(i));
                    tvTexts[i].setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
