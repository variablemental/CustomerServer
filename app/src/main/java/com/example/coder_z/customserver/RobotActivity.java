package com.example.coder_z.customserver;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by coder-z on 17-10-25.
 */

public class RobotActivity extends Activity {

    static final String PARAM_WHO="WHO";
    static final String PARAM_CONTENT="CONTENT";
    static final String WHO_SENDER="SENDER";
    static final String WHO_RECEIVER="RECEIVER";

    private ArrayList<Map<String,String>> chatList;


    @Override
    public void onCreate(Bundle onsavedInstanceState){
        super.onCreate(onsavedInstanceState);
        setContentView(R.layout.chatting_activity);

    }

    private void addtoChatList(String who,String content){
        Map<String,String> map=new HashMap<>();
        map.put(PARAM_WHO,who);
        map.put(PARAM_CONTENT,content);
        chatList.add(map);
    }

    private void initChat(){

    }

}
