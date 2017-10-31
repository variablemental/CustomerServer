package com.emotibot.xychatlib.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibCommonResultUtils {
    public static final String TYPE = "type";
    public static final String CMD = "cmd";
    public static final String VALUE = "value";
    public static final String DATA = "data";
    public static final String ITEMS = "items";
    JSONObject jsonObject;


    public XYlibCommonResultUtils(String json) {
        init(json);
    }

    private void init(String json) {
        try {
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            jsonObject = null;
        }
    }

    public String getType() {
        return getJsonValue(TYPE);
    }

    public String getCmd() {
        return getJsonValue(CMD);
    }

    public String getValue() {
        return getJsonValue(VALUE);
    }

    private String getJsonValue(String col) {
        if (jsonObject == null) {
            return "";
        }

        try {
            return jsonObject.getString(col);
        } catch (Exception e) {
            return "";
        }
    }

    public String getDataString() {
        if (jsonObject == null) {
            return "";
        }

        try {
            JSONObject jObj = jsonObject.getJSONObject(DATA);
            return jObj.toString();
        } catch (Exception e) {
            return "";
        }
    }

//    public int getDataListSize() {
//        if (jsonObject == null) {
//            return 0;
//        }
//
//        try {
//            JSONArray jArr;
//            if (getCmd().equals(OpenApiUtils.Command.CONCERT.toString())
//                    || getCmd().equals(OpenApiUtils.Command.NEWS.toString())) {
//                jArr = jsonObject.getJSONObject(DATA).getJSONArray(ITEMS);
//            } else {
//                jArr = jsonObject.getJSONArray(DATA);
//            }
//            return jArr.length();
//        } catch (Exception e) {
//            return 0;
//        }
//
//    }
}
