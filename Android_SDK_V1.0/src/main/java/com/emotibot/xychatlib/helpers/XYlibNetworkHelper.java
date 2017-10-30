package com.emotibot.xychatlib.helpers;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.XYlibOpenApi;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.utils.XYlibFileUtils;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibNetworkHelper {
    public static final int TIMEOUT = 30;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static XYlibNetworkHelper instance;

    public XYlibNetworkHelper() {
        init();
    }

    public static XYlibNetworkHelper getInstance() {
        if (instance == null) {
            instance = new XYlibNetworkHelper();
        }

        return instance;
    }

    private void init() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstants.HOST)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    public void register(Callback<String> callback) {
        XYlibOpenApi apiService = retrofit.create(XYlibOpenApi.class);
        Call<String> call = apiService.callOpenApi(XYlibConfig.APPID, URLConstants.REGISTER);
        call.enqueue(callback);
    }

    public void sendTextMsg(String uid, String msg, String location, Callback<String> callback) {
        sendMsg(uid, msg, location, URLConstants.FORMAT_TEXT, URLConstants.FORMAT_TEXT, callback);
    }

    public void sendMsg(String uid, String msg, String location, String oformat, String iformat,
                        Callback<String> callback) {
        XYlibOpenApi apiService = retrofit.create(XYlibOpenApi.class);
        Call<String> call = apiService.callOpenApi(XYlibConfig.APPID, URLConstants.CHAT,
                                                uid, msg, location, oformat, iformat);
        call.enqueue(callback);
    }

    public void sendMsg(String uid, String msg, String location, MultipartBody.Part file,
                        String oformat, String iformat, Callback<String> callback) {
        RequestBody oformat1 = RequestBody.create(MediaType.parse("text/plain"), oformat);
        RequestBody iformat1 = RequestBody.create(MediaType.parse("text/plain"), iformat);
        XYlibOpenApi apiService = retrofit.create(XYlibOpenApi.class);
        Call<String> call = apiService.callOpenApi(XYlibConfig.APPID, URLConstants.CHAT,
                uid, msg, location, file, oformat1, iformat1);
        call.enqueue(callback);
    }

    public File downloadFile(String url, String destPath) {
        XYlibOpenApi apiService = retrofit.create(XYlibOpenApi.class);
        Call<ResponseBody> call = apiService.downloadFile(url);
        File result = null;
        try {
            result = XYlibFileUtils.saveToSDCard(call.execute().body().bytes(), destPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
