package com.emotibot.xychatlib;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import com.emotibot.xychatlib.constants.URLConstants;

/**
 * Created by ldy on 2017/2/22.
 */

public interface XYlibOpenApi {
    @POST(URLConstants.BASE_PATH)
    Call<String> callOpenApi(@Query(URLConstants.APPID) String appID,
                             @Query(URLConstants.CMD) String cmd,
                             @Query(URLConstants.USERID) String uid,
                             @Query(URLConstants.TEXT) String msg,
                             @Query(URLConstants.LOCATION) String location,
                             @Query(URLConstants.OFORMAT) String oformat,
                             @Query(URLConstants.IFORMAT) String informat);

    @Multipart
    @POST(URLConstants.BASE_PATH)
    Call<String> callOpenApi(
            @Query(URLConstants.APPID) String appID, @Query(URLConstants.CMD) String cmd,
            @Query(URLConstants.USERID) String uid, @Query(URLConstants.TEXT) String msg,
            @Query(URLConstants.LOCATION) String location,
            //@Part(URLConstants.FILE) RequestBody description,
            @Part MultipartBody.Part file,
            @Part(URLConstants.OFORMAT) RequestBody oformat,
            @Part(URLConstants.IFORMAT) RequestBody informat
    );


    @POST(URLConstants.BASE_PATH)
    Call<String> callOpenApi(@Query(URLConstants.APPID) String appID,
                             @Query(URLConstants.CMD) String cmd);

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
