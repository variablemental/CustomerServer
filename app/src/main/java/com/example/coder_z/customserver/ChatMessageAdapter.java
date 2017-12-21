package com.example.coder_z.customserver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.factorys.XYlibViewHolderFactory;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.viewholders.XYlibBaseViewHolder;

import java.util.List;

/**
 * Created by ldy on 2017/2/23.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<XYlibChatMessage> mChatMsgsList;
    private chatActivity mActivity;

    public ChatMessageAdapter(Context context, List<XYlibChatMessage> chatMessagesList) {
        try {
            mInflater = LayoutInflater.from(context);
            mChatMsgsList = chatMessagesList;
            mActivity = (chatActivity) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = XYlibViewHolderFactory.createViewHolder(mInflater, parent, viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder)holder;
        viewHolder.bindView(mChatMsgsList.get(position), mActivity, position);

        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mChatMsgsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        XYlibChatMessage chatMessage = mChatMsgsList.get(position);
        int type = XYlibChatMessageUtils.getViewType(chatMessage.getDirection(),
                chatMessage.getMsgType());
        return type;
    }

    public XYlibChatMessage getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }

        return mChatMsgsList.get(position);
    }

}
