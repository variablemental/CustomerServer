package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.openapiresult.XYlibResult;
import com.emotibot.xychatlib.openapiresult.items.XYlibNBAData;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.utils.XYlibImageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNBAVH extends XYlibBaseViewHolder {
    private static final int ITEM_COUNT=3;
    public static final String FINISH = "已完成";
    public static final String[] ONGOING = {"第1节","第2节","第3节","第4节","加时1","加时2","加时3","加时4","加时5"};

    XYlibChatMessage chatMessage;
    XYlibChatActivity mActivity;
    private static int[] llIds = {R.id.ll_nba1, R.id.ll_nba2, R.id.ll_nba3};

    TextView timeView;
    List<LinearLayout> llNBAItems;
    public XYlibNBAVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        llNBAItems = new ArrayList<>();
        timeView = (TextView) v.findViewById(R.id.timeView);

        for (int id:llIds) {
            llNBAItems.add((LinearLayout) v.findViewById(id));
        }
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        this.chatMessage = chatMessage;
        parseJson(chatMessage.getMsg());
        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
    }

    private void parseJson(String json) {
        Type type = new TypeToken<XYlibResult<XYlibNBAData>>() {}.getType();
        XYlibResult<XYlibNBAData> result  = mActivity.getGson().fromJson(json, type);
        List<XYlibNBAData.MatchItem> items = result.getData().getMatchlist();
        if (items.size() < 1) {
            return;
        }

        for (int i = ITEM_COUNT - 1; i >= items.size(); i--) {
            llNBAItems.get(i).setVisibility(View.GONE);
        }

        List<ViewMatchItem> viewMatchItems = new ArrayList<ViewMatchItem>();
        for (int i = 0; i < items.size(); i++) {
            llNBAItems.get(i).setVisibility(View.VISIBLE);
            ViewMatchItem viewMatchItem = new ViewMatchItem(items.get(i), llNBAItems.get(i));
            viewMatchItems.add(viewMatchItem);

            if (i == items.size() - 1) {
                viewMatchItem.rlLine.setVisibility(View.GONE);
            } else {
                viewMatchItem.rlLine.setVisibility(View.VISIBLE);
            }
        }
    }

    class MatchItemOnClickListener implements View.OnClickListener {
        String url;

        MatchItemOnClickListener(String url) {
            this.url = url;
        }
        @Override
        public void onClick(View v) {
            mActivity.getWebViewDialog().loadWebPage(url);
            mActivity.getWebViewDialog().show();
        }
    }

    class ViewMatchItem {
        TextView tvMatchTime;
        ImageView ivTeamLogo1;
        TextView tvTeamName1;
        TextView tvScore;
        TextView tvStatus;
        ImageView ivTeamLogo2;
        TextView tvTeamName2;
        RelativeLayout rlLine;

        ViewMatchItem(XYlibNBAData.MatchItem item, LinearLayout view) {
            tvMatchTime = (TextView) view.findViewById(R.id.tv_match_time);
            ivTeamLogo1 = (ImageView) view.findViewById(R.id.iv_team_logo_host);
            tvTeamName1 = (TextView) view.findViewById(R.id.tv_team_name_host);
            tvScore = (TextView) view.findViewById(R.id.tv_score);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            ivTeamLogo2 = (ImageView) view.findViewById(R.id.iv_team_logo_client);
            tvTeamName2 = (TextView) view.findViewById(R.id.tv_team_name_client);
            rlLine = (RelativeLayout) view.findViewById(R.id.rl_line);

            tvMatchTime.setText(item.getDescribe());

            String logoUrl = item.getTeamimgurl1();
            String filename = URLUtil.guessFileName(logoUrl, null, null);
            XYlibImageUtils.showImage(mActivity, logoUrl, filename, ivTeamLogo1);

            //x.image().bind(ivTeamLogo1, item.getTeamimgurl1());
            tvTeamName1.setText(item.getTeamname1());

            String score1 = item.getTeamscore1();
            String score2 = item.getTeamscore2();

            tvScore.setText(score1+":"+score2);
            tvScore.setTextColor(getScoreColorRid(item.getState()));

            tvStatus.setText(item.getState());
            tvStatus.setTextColor(getStatusColorRid(item.getState()));
            tvStatus.setBackgroundResource(getStatusBackgroundRid(item.getState()));


            logoUrl = item.getTeamimgurl2();
            filename = URLUtil.guessFileName(logoUrl, null, null);
            XYlibImageUtils.showImage(mActivity, item.getTeamimgurl2(), filename, ivTeamLogo2);
            //x.image().bind(ivTeamLogo2, item.getTeamimgurl2());
            tvTeamName2.setText(item.getTeamname2());

            view.setOnClickListener(new MatchItemOnClickListener(item.getMatchurl()));
        }

        private int getStatusBackgroundRid(String state) {
            int ret;
            if (state.equals(FINISH)) {
                ret = R.drawable.bg_score_end;
            } else if (inOngoing(state)) {
                ret = R.drawable.bg_score_ongoing;
            } else {
                ret = R.drawable.bg_score_not_begin;
            }
            return ret;
        }

        private int getScoreColorRid(String state) {
            int ret;
            if (state.equals(FINISH)) {
                ret = R.color.score_finish;
            } else if (inOngoing(state)) {
                ret = R.color.score_other;
            } else {
                ret = R.color.score_not_begin;
            }
            return mActivity.getResources().getColor(ret);
        }
        private int getStatusColorRid(String state) {
            int ret;
            if (state.equals(FINISH) || inOngoing(state)) {
                ret = R.color.white;
            } else {
                ret = R.color.score_not_begin;
            }
            return mActivity.getResources().getColor(ret);
        }
    }

    private boolean inOngoing(String state) {
        for (int i = 0; i < ONGOING.length; i++) {
            if (state.equals(ONGOING[i])) {
                return true;
            }
        }
        return false;
    }
}
