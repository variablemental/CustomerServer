package com.emotibot.xychatlib.models;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.util.Date;

/**
 * Created by ldy on 2017/2/22.
 */
@Table("chat_message")
public class XYlibChatMessage{
    //--------------column index--------------
    public static final String COL_ID = "_id";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column(COL_ID)
    private int id;

    @Column("_uuid")
    private String uuid;
    @Column("_uid")
    private String uid;
    @Column("_msgType")
    private int msgType;
    @Column("_msg")
    private String msg;
    @Column("_time")
    private Date time;
    @Column("_state")
    private int state;
    @Column("_emotion")
    private String emotion;
    @Column("direction")
    private int direction;
    @Column("voice_path")
    private String voice_path;
    @Column("unread")
    private boolean unread;
    @Column("voice_length")
    private int voice_length;
    @Column("imageUrl")
    private String imageUrl;

    //    private String
    private boolean playingVoice;
    private boolean showTime;

    public XYlibChatMessage() {}

    public XYlibChatMessage(String uid, String uuid, int msgType, int direction, String msg,
                            int state, String voice_path, int voice_length, String emotion,
                            String imageUrl) {
        this.uid = uid;
        this.uuid = uuid;
        this.msgType = msgType;
        this.direction = direction;
        this.msg = msg;
        this.time = new Date();
        this.state = state;
        this.voice_path = voice_path;
        this.unread = true;
        this.voice_length = voice_length;
        this.emotion = emotion;
        this.imageUrl = imageUrl;
        this.playingVoice = false;
        this.showTime = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public int getDirection() {
        return direction;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPlayingVoice() {
        return playingVoice;
    }

    public void setPlayingVoice(boolean playingVoice) {
        this.playingVoice = playingVoice;
    }

    public String getVoicePath() {
        return voice_path;
    }

    public int getVoice_length() {
        return voice_length;
    }
}
