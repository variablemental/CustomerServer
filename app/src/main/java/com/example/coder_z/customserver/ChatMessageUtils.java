package com.example.coder_z.customserver;

import android.view.View;
import android.widget.TextView;

import com.emotibot.xychatlib.XYlibChatMessageAdapter;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ldy on 2017/2/23.
 */

public class ChatMessageUtils {
    //--------------direction---------------
    public static final int TO_ROBOT = 0x301;
    public static final int FROM_ROBOT = 0x302;

    //--------------msg type--------------
    public static final int TEXT = 0x100;
    public static final int VOICE = 0x110;
    public static final int GIF_IMAGE = 0x120;
    public static final int TYPING = 0x130;
    public static final int LIAO = 0x140;
    public static final int STAR = 0X170;
    public static final int CHOOSE_STAR = 0x160;
    public static final int RECENT_STAR = 0X171;
    public static final int IMAGE = 0x150;
    public static final int MOVIE_RECOMMAND = 0x180;
    public static final int MOVIE_SHARE = 0x181;
    public static final int TEXT_LINK = 0x182;
    public static final int NEWS_PAGES = 0x183;
    public static final int XIAMI_MUSIC = 0x184;
    public static final int REMINDER = 0x185;
    public static final int ITEM_SELECT = 0x186;
    public static final int KUAIDI = 0x187;
    public static final int OLYMPIC = 0x188;
    public static final int HYPERLINK_TEXT = 0x189;
    public static final int CONCERT = 0x190;
    public static final int AUDIO_BOOK = 0X191;
    public static final int COOKING = 0X192;
    public static final int NBA = 0X193;
    public static final int RESPONSE=0X194;

    //--------------layout id-------------
    public static final int INVALID_TYPE = -1;
    public static final int TO_TEXT = 0;
    public static final int FROM_TEXT = 1;
    public static final int TO_VOICE = 2;
    public static final int FROM_VOICE = 3;
    public static final int TO_GIF_IMAGE = 4;
    public static final int FROM_GIF_IMAGE = 5;
    public static final int FROM_TYPING = 6;
    public static final int FROM_LIAO = 7;
    public static final int FROM_IMAGE = 8;
    public static final int TO_IMAGE = 9;
    public static final int FROM_START = 10;
    public static final int FROM_CHOOSE_STAR = 11;
    public static final int FROM_RECENT_STAR = 12;
    public static final int FROM_MOVIE_RECOMMAND = 13;
    public static final int FROM_MOVIE_SHARE = 14;
    public static final int FROM_TEXT_LINK = 15;
    public static final int FROM_NEWS_PAGES = 16;
    public static final int FROM_XIAMI_MUSIC = 17;
    public static final int FROM_REMINDER = 18;
    public static final int FROM_ITEM_SELECT = 19;
    public static final int FROM_KUAIDI = 20;
    public static final int FROM_OLYMPIC = 21;
    public static final int FROM_HYPER_LINK_TEXT = 22;
    public static final int FROM_CONCERT = 23;
    public static final int FROM_AUDIO_BOOK = 24;
    public static final int FROM_COOKING = 25;
    public static final int FROM_NBA = 26;
    public static final int FROM_LIST=27;

    //--------------msg status---------------
    public static final int MSG_STATUS_SENDING = 0x200;
    public static final int MSG_STATUS_ERROR = 0x210;
    public static final int MSG_STATUS_SENT = 0x220;
    public static final int MSG_STATUS_DEFAULT = 0x230;

    //--------------msg format----------------
    private static boolean MSG_LIST=false;

    private static Map<Integer, Integer> userMsgTypeMap = new HashMap<>();
    private static Map<Integer, Integer> robotMsgTypeMap = new HashMap<>();

    public static XYlibChatMessage createMsg(String uid, int msgType, int direction, String msg) {
        return new XYlibChatMessage(uid,"0",msgType, direction, msg, MSG_STATUS_DEFAULT, "", 0,
                URLConstants.EMOTION_NEUTRAL, "");
    }

    public static XYlibChatMessage createVoiceMsg(String uid, int direction,
                                                  int voiceLength, String filePath) {
        return new XYlibChatMessage(uid, "0", VOICE, direction, "voice", MSG_STATUS_DEFAULT,
                                    filePath, voiceLength, URLConstants.EMOTION_NEUTRAL, "");
    }

    public static XYlibChatMessage createUserMsg(String uid, int msgType, String msg) {
        return createMsg(uid, msgType, TO_ROBOT, msg);
    }

    /**
     *
     * @param uid       Activity名称
     * @param msgType  信息类型，文字/图片/声音
     * @param msg       信息主体
     * @return          生成一个XYLibChatMessage对象，定义多种状态属性
     */
    //机器人对话气泡创建
    public static XYlibChatMessage createRobotMsg(String uid, int msgType, String msg) {
        return createMsg(uid, msgType, FROM_ROBOT, msg);
    }

    private static void initMap() {
        if (userMsgTypeMap.size() == 0) {
            userMsgTypeMap.put(TEXT, TO_TEXT);
            userMsgTypeMap.put(VOICE, TO_VOICE);
            userMsgTypeMap.put(GIF_IMAGE, TO_GIF_IMAGE);
            userMsgTypeMap.put(IMAGE, TO_IMAGE);
        }

        if (robotMsgTypeMap.size() == 0) {
            robotMsgTypeMap.put(TEXT, FROM_TEXT);
            robotMsgTypeMap.put(TEXT_LINK, FROM_TEXT_LINK);
            robotMsgTypeMap.put(TYPING, FROM_TYPING);
            robotMsgTypeMap.put(VOICE, FROM_VOICE);
            robotMsgTypeMap.put(KUAIDI, FROM_KUAIDI);
            robotMsgTypeMap.put(GIF_IMAGE, FROM_GIF_IMAGE);
            robotMsgTypeMap.put(IMAGE, FROM_IMAGE);
            robotMsgTypeMap.put(NEWS_PAGES, FROM_NEWS_PAGES);
            robotMsgTypeMap.put(NBA, FROM_NBA);
            robotMsgTypeMap.put(RESPONSE,FROM_LIST);
        }
    }
    public static int getViewType(int direction, int msgType) {
        initMap();
        int type;
        if (direction == FROM_ROBOT) {
            type = robotMsgTypeMap.get(msgType);
        } else {
            type = userMsgTypeMap.get(msgType);
        }

        return type;
    }

    public static void shouldShowTimeTag(ChatMessageAdapter adapter, XYlibChatMessage chatMessage,
                                         TextView tv, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (position > 0) {
            if (adapter.getItemCount() <= position - 1) {
                return;
            }
            long delta = chatMessage.getTime().getTime() - adapter.getItem(position - 1).getTime().getTime();
            if (delta < 1000 * 60 * 3 && !chatMessage.isShowTime()) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
                Date contDate = chatMessage.getTime();
                String timeString = calcuTime(contDate);
                timeString += sdf.format(contDate);
                tv.setText(timeString);

            }
        } else {
            tv.setVisibility(View.VISIBLE);
            Date contDate = chatMessage.getTime();
            String timeString = calcuTime(contDate);
            timeString += sdf.format(contDate);
            tv.setText(timeString);
        }
    }

    public static String calcuTime(Date date) {
        Date todayStart = XYlibTimeUtils.getStartTime(Calendar.DAY_OF_YEAR, 0);
        Date yesterdayStart = XYlibTimeUtils.getStartTime(Calendar.DAY_OF_YEAR, 1);
        Date dbYesterdayStart = XYlibTimeUtils.getStartTime(Calendar.DAY_OF_YEAR, 2);
        Date lastWeekStart = XYlibTimeUtils.getStartTime(Calendar.WEEK_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        if (date.after(todayStart)) {
            return "";
        } else if (date.after(yesterdayStart)) {
            return "昨天 ";
        } else if (date.after(dbYesterdayStart)) {
            return "前天 ";
        } else if (date.after(lastWeekStart)) {
            String dayOfWeek = "周";
            switch (date.getDay()) {
                case 0:
                    dayOfWeek += "日";
                    break;
                case 1:
                    dayOfWeek += "一";
                    break;
                case 2:
                    dayOfWeek += "二";
                    break;
                case 3:
                    dayOfWeek += "三";
                    break;
                case 4:
                    dayOfWeek += "四";
                    break;
                case 5:
                    dayOfWeek += "五";
                    break;
                case 6:
                    dayOfWeek += "六";
                    break;
            }
            return dayOfWeek + " ";
        } else {
            return sdf.format(date) + " ";
        }
    }

    //格式化答案
    private static String msg_format(String msg){
        String pattern = "\\[[1-9]*\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        String[] splits=msg.split(pattern);
        LinkedList<String> seq=new LinkedList<String>();
        while(m.find()){
            seq.add(m.group());
        }
        if(seq.size()>0) {
            StringBuffer buf = new StringBuffer();
            //去掉头部段
            //buf.append(splits[0] + "\n");                                                                 //头部段
            for (int i = 1; i < splits.length - 1; i++) {
                buf.append(seq.get(i - 1) + splits[i] + "\n");                                              //内容段，给每一个标号段换行
            }
            buf.append(seq.get(seq.size() - 1) + splits[splits.length - 1]);                                  //尾部段
            MSG_LIST=true;
            return buf.toString();
        }
        return msg;
    }


/*    public static void main(String args[]) {
        String str = "您好，请选择需要办理的业务：\n" +
                "[1]危险废物经营许可\n" +
                "[2]固体废物跨省转移\n" +
                "[3]建设项目环境影响评价文件审批\n" +
                "[4]其他业务";
        String regex = "\\[[1-9]*\\]";
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(str);
        while(m.find()){
            System.out.println(m.group());
        }
        //System.out.println(m.group());
        for(String s:str.split(regex))
            System.out.println(s);
    }*/

    public static void main(String args[]){

        String t= "您好，请访问 http://www.jshb.gov.cn 点击查看";
        String t1="我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com";

        String net_pattern="http://(\\w+.)+";
        //String ss="http";
        Pattern r = Pattern.compile(net_pattern);
        Matcher m = r.matcher(t);
        while(m.find()) {
            String result=m.group();
            System.out.println(result.substring(1,result.length()-2));
        }

        String test="[2]";
        System.out.println(test.substring(1,test.length()-1));

    }

}
