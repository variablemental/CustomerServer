package com.emotibot.xychatlib.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibReturnResultUtils {
    public static final String RETURN = "return";
    public static final String DATA = "data";
    public static final String RETURN_MESSAGE = "return_message";
    public static final String EMOTION = "emotion";
    public static final String VALUE = "value";
    public static final String MESSAGE = "message";
    String jsonStr;
    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONArray emotionJsonArray;

    public XYlibReturnResultUtils(String jsonStr) {
        this.jsonStr = jsonStr;
        init();
    }

    private void init() {
        initJsonObject();
        initJsonArray();
    }

    private void initJsonObject() {
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = null;
        }
    }

    private void initJsonArray() {
        if (jsonObject == null) {
            jsonArray = null;
        } else {
            try {
                jsonArray = jsonObject.getJSONArray(DATA);
            } catch (Exception e) {
                e.printStackTrace();
                jsonArray = null;
            }
        }
    }

    public int getReturnValue() {
        if (jsonObject == null) {
            return -1;
        }

        try {
            return jsonObject.getInt(RETURN);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getErrMsg() {
        if (jsonObject == null) {
            return "";
        }

        try {
            if (jsonObject.has(RETURN_MESSAGE)) {
                return jsonObject.getString(RETURN_MESSAGE);
            } else if (jsonObject.has(MESSAGE)) {
                return jsonObject.getString(MESSAGE);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取json array中的index上的元素,并且转为string类型返回
    public String getElement(int index) {
        if (jsonArray == null) {
            return null;
        }

        try {
            return jsonArray.getString(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //以string类型,返回所有的array list中的元素
    public List<String> getAllElement() {
        ArrayList<String> result = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getEmotion() {
        if (jsonObject == null) {
            return "";
        }

        try {
            emotionJsonArray = jsonObject.getJSONArray(EMOTION);
            if (emotionJsonArray.length() == 0) {
                return "";
            }

            return emotionJsonArray.getJSONObject(0).getString(VALUE);
        } catch (Exception e) {
            return "";
        }

    }
}
