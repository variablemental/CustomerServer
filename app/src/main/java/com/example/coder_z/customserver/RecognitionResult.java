package com.example.coder_z.customserver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.util.Base64Util;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

/**
 * 图像主体检测
 */
public class RecognitionResult {

    static Runnable networkTask=new Runnable() {
        @Override
        public void run() {
            try {
                reco_result = RecognitionResult.detect(bytes);
                Message msg=new Message();
                Bundle bundle=new Bundle();
                bundle.putString(NOTICE_MESSAGE,reco_result);
                msg.setData(bundle);
                handler.sendMessage(msg);
                Toast.makeText(mContext, reco_result, Toast.LENGTH_LONG).show();
                System.out.println(reco_result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private static final String TAG="RecognitionResult";
    private static Handler handler=null;
    private static final String NOTICE_MESSAGE="NOTICE_MESSAGE";
    private static String reco_result;
    private static byte[] bytes=null;
    static private TextView mNotice;
    private static Context mContext;

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    private static String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/object_detect";
    public static String detect() {
        // 请求url
        try {
            // 本地文件路径
            String filePath = "F:\\1.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);

            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            return detectprocess(imgParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String detect(byte[] bytes) {
        try {
            String imgStr = Base64Util.encode(bytes);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            return detectprocess(imgParam);
        }catch (Exception e) {
            Log.e(TAG,"detection creck!");
        }
        return  null;
    }

    private static String detectprocess(String imgParam) throws Exception{
         //&with_face 可带人脸的图像
         String param = "image=" + imgParam + "&with_face=" + 1;

        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
        String accessToken = AuthService.getAuth();
        String result = HttpUtil.post(url, accessToken, param);
        System.out.println(result);
        return result;
    }

    public static void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle b=message.getData();
                String str=b.getString(NOTICE_MESSAGE);
                mNotice.setText(str);
            }
        };
    }

    public static void setmNotice(TextView notice){
        mNotice=notice;
    }

    public static void onResult(Context context, Intent data){
        Bitmap bitmap=(Bitmap) data.getExtras().get("data");
        process(context,bitmap);
    }

    public static void process(Context context,Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        bytes=baos.toByteArray();
        mContext=context;
        new Thread(networkTask).start();    //后台线程调用API识别
    }

    public static void main(String[] args) {
       System.out.println(RecognitionResult.detect());
    }
}