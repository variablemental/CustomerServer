package com.example.coder_z.customserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by coder-z on 17-10-27.
 */

public class ChatAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<HashMap<String,String>> chatList;

    ChatAdapter(Context context,ArrayList chatList){
        this.mContext=context;
        this.chatList=chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewPair{
        ImageView imageView;
        TextView textView;
        ViewPair(ImageView imageView,TextView textView){
            this.imageView=imageView;
            this.textView=textView;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewPair viewPair=null;
        String who=chatList.get(i).get(RobotActivity.PARAM_WHO);
        if(who.equals(RobotActivity.WHO_SENDER)){
            view= LayoutInflater.from(mContext).inflate(MsgDic.RECEIVER_LAYOUT,null);
            viewPair=new ViewPair((ImageView)view.findViewById(MsgDic.RECEIVER_ICON),(TextView)view.findViewById(MsgDic.RECEIVER_TEXT));
            viewPair.imageView.setImageResource(MsgDic.RECEIVER_DRAWER);
        }else{
            view=LayoutInflater.from(mContext).inflate(MsgDic.SENDER_LAYOUT,null);
            viewPair=new ViewPair((ImageView)view.findViewById(MsgDic.SENDER_ICON),(TextView)view.findViewById(MsgDic.SENDER_TEXT));
            viewPair.imageView.setImageResource(MsgDic.SEND_DRAWER);
        }
        String content=chatList.get(i).get(RobotActivity.PARAM_CONTENT);
        viewPair.textView.setText(content);

        return null;

    }
}
