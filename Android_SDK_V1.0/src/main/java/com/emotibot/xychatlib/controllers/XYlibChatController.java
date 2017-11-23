package com.emotibot.xychatlib.controllers;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.XYlibChatMessageAdapter;
import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.helpers.XYlibNetworkHelper;
import com.emotibot.xychatlib.helpers.XYlibResultProcessHelper;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibAudioPlayerUtils;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.utils.XYlibDBUtils;
import com.emotibot.xychatlib.utils.XYlibEmojiFilterUtils;
import com.emotibot.xychatlib.utils.XYlibLogUtils;
import com.emotibot.xychatlib.utils.XYlibReturnResultUtils;
import com.emotibot.xychatlib.widgets.XYlibRecordBtn;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibChatController implements View.OnClickListener {
    public static final String tag = XYlibChatController.class.getSimpleName();
    private XYlibChatActivity mActivity;
    private XYlibNetworkHelper mNetworkHelper;

    private EditText etInput;
    private Button btnSend;
    private RecyclerView rvChatList;
    private SwipeRefreshLayout srl;
    private XYlibRecordBtn recordBtn;
    private XYlibChatMessageAdapter mAdapter;

    private List<XYlibChatMessage> mChatMsgList;
    private boolean isRefresh;

    public XYlibChatController(XYlibChatActivity chatActivity) {
        mActivity = chatActivity;
        mNetworkHelper = chatActivity.getNetworkHelper();

        Fresco.initialize(mActivity);
        XYlibDBUtils.initDB(mActivity);

        initView();
        initData();
    }

    public XYlibChatMessageAdapter getAdapter() {
        return mAdapter;
    }

    public XYlibChatActivity getActivity() {
        return mActivity;
    }

    public XYlibNetworkHelper getNetworkHelper() {
        return mNetworkHelper;
    }

    private void initView() {
        //控件注册
        etInput = (EditText) mActivity.findViewById(R.id.et_input);
        btnSend = (Button) mActivity.findViewById(R.id.send_btn);
        rvChatList = (RecyclerView) mActivity.findViewById(R.id.rv_chat_list);
        srl = (SwipeRefreshLayout) mActivity.findViewById(R.id.srl);
        recordBtn = (XYlibRecordBtn) mActivity.findViewById(R.id.press_talk_btn);

        try {
            srl.setColorSchemeResources(R.color.colorAccent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //使用SwipeRefreshLayout上拉刷新界面
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOlderChatMessages();
                srl.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvChatList.setLayoutManager(linearLayoutManager);

        btnSend.setOnClickListener(this);

        recordBtn.setRecordBtnListener(recordBtnListener);
    }

    private void initData() {
        mChatMsgList = new ArrayList<>();
        mAdapter = new XYlibChatMessageAdapter(mActivity, mChatMsgList);
        rvChatList.setAdapter(mAdapter);

        loadChatMessages();
    }

    //user send message start---------------
    private void userSay(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        showUserSay(msg);
        sendTextMsg(msg);
    }

    private void sendTextMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        showRobotSayWait();
        mNetworkHelper.sendTextMsg(mActivity.getUid(), msg, "", new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result = response.body();

                XYlibLogUtils.d(tag, "request url:"+ call.request().url());
                XYlibLogUtils.d(tag, "RESULT:" + result);

                result = XYlibEmojiFilterUtils.convertToEmoji(result);
                processReply(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                showRobotSay(XYlibConfig.ERR_SEND_FAIL);
            }
        });
    }

    private void showUserSay(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        XYlibChatMessage chatmessage = XYlibChatMessageUtils.createUserMsg(mActivity.getUid(),
                XYlibChatMessageUtils.TEXT, msg);
        saveAndUpdateMsg(chatmessage);
    }

    private void showUserSayVoice(XYlibChatMessage chatMessage) {
        if (chatMessage == null) {
            return;
        }

        saveAndUpdateMsg(chatMessage);
    }

    private void userSayVoice(XYlibChatMessage chatMessage) {
        if (chatMessage == null) {
            return;
        }

        showUserSayVoice(chatMessage);

        sendMsg(mActivity.getUid(), "", "", chatMessage.getVoicePath(),
                URLConstants.FORMAT_VOICE, URLConstants.FORMAT_VOICE);
    }

    private void sendMsg(String uid, String msg, String location, String voicePath, String oformat,
                         String iformat) {
        showRobotSayWait();
        File voiceFile = new File(voicePath);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("audio/amr"),
                        voiceFile
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(URLConstants.FILE, voiceFile.getName(), requestFile);


        mNetworkHelper.sendMsg(uid, msg, location, body, oformat, iformat, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result = response.body();

                XYlibLogUtils.d(tag, "request url:"+ call.request().url());
                XYlibLogUtils.d(tag, "RESULT:" + result);

                result = XYlibEmojiFilterUtils.convertToEmoji(result);
                processReply(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                showRobotSay(XYlibConfig.ERR_SEND_FAIL);
            }
        });
    }

    //todo 持久化
    private void saveAndUpdateMsg(XYlibChatMessage chatMsg) {
        mChatMsgList.add(chatMsg);
        mAdapter.notifyDataSetChanged();
        rvChatList.scrollToPosition(mChatMsgList.size() - 1);
        XYlibDBUtils.getDB().save(chatMsg);
    }

    private void onSendClick() {
        String input = etInput.getText().toString();
        etInput.setText("");

        userSay(input);
    }
    //user send message end---------------

    //robot reply start---------------
    private void processReply(String result) {
        XYlibReturnResultUtils returnResultUtils = new XYlibReturnResultUtils(result);
        if (returnResultUtils.getReturnValue() < 0) {
            showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        new XYlibResultProcessHelper(this).process(returnResultUtils.getAllElement());
    }

    public void showRobotSay(String msg) {
        deleteRobotSayWait();
        //设置机器人发言内容
        XYlibChatMessage chatmessage = XYlibChatMessageUtils.createRobotMsg(mActivity.getUid(),
                XYlibChatMessageUtils.TEXT, msg);
        saveAndUpdateMsg(chatmessage);
    }

    public void showRobotSayWait() {
        deleteRobotSayWait();
        XYlibChatMessage chatmessage = XYlibChatMessageUtils.createRobotMsg(mActivity.getUid(),
                XYlibChatMessageUtils.TYPING, "wait");
        mChatMsgList.add(chatmessage);
        mAdapter.notifyDataSetChanged();
        rvChatList.scrollToPosition(mChatMsgList.size() - 1);
    }

    public void showRobotSayVoice(int seconds, String filePath) {
        deleteRobotSayWait();
        XYlibChatMessage cm = XYlibChatMessageUtils.createVoiceMsg(mActivity.getUid(),
                                    XYlibChatMessageUtils.FROM_ROBOT, seconds, filePath);
        saveAndUpdateMsg(cm);
    }

    public void showRobotSay(String str, int msgType) {
        deleteRobotSayWait();
        XYlibChatMessage cm = XYlibChatMessageUtils.createRobotMsg(mActivity.getUid(), msgType,
                                                                    str);
        saveAndUpdateMsg(cm);
    }


    public void deleteRobotSayWait() {
        int size = mChatMsgList.size();
        if (size >0
                && mChatMsgList.get(size - 1).getMsgType()
                    == XYlibChatMessageUtils.TYPING) {
            mChatMsgList.remove(size - 1);
        }
    }
    //robot reply end---------------

    //load data from db start ------
    private void loadChatMessages() {
        List<XYlibChatMessage> messages = XYlibDBUtils.getDB()
                .query(new QueryBuilder<>(XYlibChatMessage.class)
                .limit(XYlibConfig.DB_QUERY_LIMIT)
                .appendOrderDescBy(XYlibChatMessage.COL_ID));

        for (XYlibChatMessage msg:messages) {
            mChatMsgList.add(0, msg);
        }

        mAdapter.notifyDataSetChanged();
        rvChatList.scrollToPosition(mChatMsgList.size()-1);
    }

    private void loadOlderChatMessages() {
        if (!isRefresh) {
            isRefresh = true;
            loadDataFromDb();
            mAdapter.notifyDataSetChanged();
            isRefresh = false;
        }
    }

    private void loadDataFromDb(){
        int idStart;

        if (mChatMsgList.size() <= 0) {
            idStart = 1;
        } else {
            idStart = mChatMsgList.get(0).getId();
        }

        List<XYlibChatMessage> messages = XYlibDBUtils.getDB()
                .query(new QueryBuilder<>(XYlibChatMessage.class)
                .where(XYlibChatMessage.COL_ID+" < ?", ""+idStart)
                .limit(XYlibConfig.DB_QUERY_LIMIT)
                .appendOrderDescBy(XYlibChatMessage.COL_ID));
        for (XYlibChatMessage msg:messages) {
            Log.d(tag, ""+msg.getId());
            mChatMsgList.add(0, msg);
        }
    }
    //load data from db end --------

    private XYlibRecordBtn.RecordBtnListener recordBtnListener = new XYlibRecordBtn.RecordBtnListener() {
        @Override
        public void onAudioRecordStart() {
            XYlibAudioPlayerUtils.getInstance(mActivity.getApplicationContext()).pause();
        }

        @Override
        public void onAudioRecordFinish(float seconds, String filePath) {
            XYlibChatMessage cm = XYlibChatMessageUtils.createVoiceMsg(mActivity.getUid(),
                                    XYlibChatMessageUtils.TO_ROBOT, (int)(seconds+0.5), filePath);
            userSayVoice(cm);
        }

        @Override
        public void onReachMaxRecordDuration() {

        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_btn) {
            onSendClick();
        }else if(v.getId()==R.id.contentView){
            Toast.makeText(mActivity,"yes",Toast.LENGTH_LONG).show();
        }
    }

    public void onItemClick(View view){
        //int childAdapterPosition = rvChatList.getChildAdapterPosition(view);
        //Toast.makeText(mActivity, "item click index = "+childAdapterPosition, Toast.LENGTH_SHORT).show();
                int childAdapterPosition = rvChatList.getChildAdapterPosition(view);
                TextView tx = (TextView) rvChatList.getChildViewHolder(view).itemView.findViewById(R.id.contentView);
                String net_pattern="http[s]?://(\\w+.)+";
                Pattern r = Pattern.compile(net_pattern);
                Matcher m = r.matcher(tx.getText());
                String URL="";
                while(m.find()){
                    URL=m.group();
                }
                if(URL.isEmpty()) {                                                                 //如果不包括网址，处理是否包含序号
                    net_pattern = "\\[[1-9]*\\]";
                    r = Pattern.compile(net_pattern);
                    m = r.matcher(tx.getText());
                    String num="";
                    while (m.find()){
                        num=m.group();
                        num=num.substring(1,num.length()-1);                                        //截取框内数字部分
                        if(!num.isEmpty()){
                            userSay(num);
                            break;
                        }
                    }
                    return;
                }
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri url=Uri.parse(URL);
                intent.setData(url);
                mActivity.startActivity(intent);
    }

}
