package com.emotibot.xychatlib.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibEmojiFilterUtils {
    private static HashMap<String,String> emojiToStringMap = new HashMap<>();
    private static HashMap<String,String> stringToEmojiMap = new HashMap<>();
    private static Set<String> emojiIconSet = new HashSet<>();

    public static boolean containsEmoji(String source) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;

            }

        }

        return false;

    }


    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回

        }

        //到这里铁定包含
        StringBuilder buf = null;
        StringBuilder emojiBuf = null;


        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());

                }

                buf.append(codePoint);

            } else {
                if (emojiBuf == null) {
                    emojiBuf = new StringBuilder(source.length());

                }


                emojiBuf.append(codePoint);
                if(emojiBuf.length() ==2)
                {
                    try {
                        String result = URLEncoder.encode(emojiBuf.toString(),"UTF-8");
                        System.out.println(result);
                        if(buf == null)
                            buf = new StringBuilder(source.length());
                        buf.append(emojiReplace(result));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    emojiBuf.delete(0,emojiBuf.length());
                }




            }
        }

        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串

        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;

            } else {
                return buf.toString();

            }

        }


    }

    public static String convertToEmoji(String input)
    {
        for(String emojiIcon : emojiIconSet)
        {
            if(input.contains(emojiIcon)){
                String utf8Code = stringToEmojiMap.get(emojiIcon);
                try {
                    String byteCode = URLDecoder.decode(utf8Code,"UTF-8");
                    input = input.replaceFirst(emojiIcon, byteCode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return input;
    }

    private static String emojiReplace(String utf8){
        System.out.println(emojiToStringMap.size()+"@@@@@@@@@@");
        if(emojiToStringMap.containsKey(utf8))
        {
            return emojiToStringMap.get(utf8);
        }else {
            return "[斗图]";
        }
    }

    static{

        emojiToStringMap.put("%F0%9F%98%80","/::D");
        emojiToStringMap.put("%F0%9F%98%81","/::D");
        emojiToStringMap.put("%F0%9F%98%83","/::>");
        emojiToStringMap.put("%F0%9F%98%84","/::>");
        emojiToStringMap.put("%F0%9F%98%8F","/:,@P");
        emojiToStringMap.put("%F0%9F%98%8E","/:B-)");
        emojiToStringMap.put("%F0%9F%98%9D","/:B-)");
        emojiToStringMap.put("%F0%9F%98%A1","/::@");
        emojiToStringMap.put("%F0%9F%98%A4","/::-S");
        emojiToStringMap.put("%F0%9F%98%96","/::~");
        emojiToStringMap.put("%F0%9F%98%92","/::(");
        emojiToStringMap.put("%F0%9F%98%AD","/::’(");
        emojiToStringMap.put("%F0%9F%98%A5","/::’|");
        emojiToStringMap.put("%F0%9F%98%AD","/::<");
        emojiToStringMap.put("%F0%9F%98%B1","/::O");
        emojiToStringMap.put("%F0%9F%98%AF","/::|");
        emojiToStringMap.put("%E2%9D%93","/:?");
        emojiToStringMap.put("%E2%9D%94","/:?");
        emojiToStringMap.put("%F0%9F%98%B3","/:–b");
        emojiToStringMap.put("%F0%9F%98%A8","/::!");
        emojiToStringMap.put("%F0%9F%98%AE","/:8*");
        emojiToStringMap.put("%F0%9F%98%A3","/:8*");
        emojiToStringMap.put("%F0%9F%98%AB","/::-O");
        emojiToStringMap.put("%F0%9F%8C%9D","/:@x");
        emojiToStringMap.put("%F0%9F%98%B4","/::Z");
        emojiToStringMap.put("%F0%9F%98%92","/:dig");
        emojiToStringMap.put("%F0%9F%98%86","/::>");
        emojiToStringMap.put("%F0%9F%98%8A","/::D");
        emojiToStringMap.put("%F0%9F%98%94","/::(");
        emojiToStringMap.put("%F0%9F%98%A2","/::|");
        emojiToStringMap.put("%F0%9F%98%AA","/::Z");
        emojiToStringMap.put("%F0%9F%98%AB","[无聊]");
        emojiToStringMap.put("%F0%9F%98%AB","[无聊]");
        emojiToStringMap.put("%F0%9F%98%92","[无聊]");
        emojiToStringMap.put("%F0%9F%98%B4","[无聊]");
        emojiToStringMap.put("%F0%9F%8C%9D","[无聊]");

        stringToEmojiMap.put("/::D","%F0%9F%98%80");
        stringToEmojiMap.put("/::>","%F0%9F%98%83");
        stringToEmojiMap.put("/:,@P","%F0%9F%98%8F");
        stringToEmojiMap.put("/:B-)","%F0%9F%98%9D");
        stringToEmojiMap.put("/::@","%F0%9F%98%A1");
        stringToEmojiMap.put("/::-S","%F0%9F%98%A4");
        stringToEmojiMap.put("/::~","%F0%9F%98%96");
        stringToEmojiMap.put("/::(","%F0%9F%98%92");
        stringToEmojiMap.put("/::’(","%F0%9F%98%AD");
        stringToEmojiMap.put("/::’|","%F0%9F%98%A5");
        stringToEmojiMap.put("/::<","%F0%9F%98%AD");
        stringToEmojiMap.put("/::O","%F0%9F%98%B1");
        stringToEmojiMap.put("/::|","%F0%9F%98%AF");
        stringToEmojiMap.put("/:?","%E2%9D%94");
        stringToEmojiMap.put("/:–b","%F0%9F%98%B3");
        stringToEmojiMap.put("/::!","%F0%9F%98%A8");
        stringToEmojiMap.put("/:8*","%F0%9F%98%AE");
        stringToEmojiMap.put("/::-O","%F0%9F%98%AB");
        stringToEmojiMap.put("/:@x","%F0%9F%8C%9D");
        stringToEmojiMap.put("/::Z","%F0%9F%98%B4");
        stringToEmojiMap.put("/:dig", "%F0%9F%98%92");
        stringToEmojiMap.put("/::>","%F0%9F%98%86");
        stringToEmojiMap.put("/::D","%F0%9F%98%8A");
        stringToEmojiMap.put("/::(","%F0%9F%98%94");
        stringToEmojiMap.put("/::'|","%F0%9F%98%A2");
        stringToEmojiMap.put("/::Z","%F0%9F%98%AA");
        stringToEmojiMap.put("/:handclap","%F0%9F%91%8F");

        emojiIconSet.addAll(stringToEmojiMap.keySet());
    }
}
