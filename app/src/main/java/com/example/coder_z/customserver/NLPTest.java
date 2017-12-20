package com.example.coder_z.customserver;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\12\4 0004.
 */

public class NLPTest {
    //设置APPID/AK/SK
    public static final String APP_ID = "10494043";
    public static final String API_KEY = "6N7xGqFziFYcNBm8l5nHrzQC";
    public static final String SECRET_KEY = "g9MuiUhPi1icFuyTKiTxTYWRTGHEanCo";

    public static void main(String[] args) throws JSONException{
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
       // client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String text = "百度是一家高科技公司";
        JSONObject res = client.lexer(text);
        System.out.println(res.toString(2));

    }
}