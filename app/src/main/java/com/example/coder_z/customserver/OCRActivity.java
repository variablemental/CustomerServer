package com.example.coder_z.customserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;

import java.io.File;

/**
 * Created by Administrator on 2017\11\8 0008.
 */

public class OCRActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_OCR=101;

    private boolean hasGotToken=false;
    private AlertDialog.Builder alertDialog;
    private Button mPhotoButton;
    public void onCreate(Bundle onSavedStateInstance){
        super.onCreate(onSavedStateInstance);
        setContentView(R.layout.recognition_activity);
        alertDialog=new AlertDialog.Builder(this);
        mPhotoButton=(Button)findViewById(R.id.take_photo);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(OCRActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        new File(getApplicationContext().getFilesDir(), "pic.jpg").getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent,REQUEST_CODE_OCR);
            }
        });
        initAccessTokenWithAkSk();
    }

    //初始化百度的AK和SK
    public void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;

            }
            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        },getApplicationContext(), OCRUtil.AK, OCRUtil.SK);
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_CODE_OCR:
                GeneralParams param = new GeneralParams();
                //是否检测图像朝向，朝向是指输入图像是正常方向、逆时针旋转90/180/270度
                param.setDetectDirection(true);
                //是否返回文字外接多边形顶点位置，不支持单字位置。默认为false
                param.setVertexesLocation(true);
                //是否定位单字符位置，big：不定位单字符位置，默认值；small：定位单字符位置
                param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL);
                //存储位置
                param.setImageFile(new File( new File(getApplicationContext().getFilesDir(), "pic.jpg").getAbsolutePath()));
                //识别，成功后结果在OnResult执行
                OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
                    @Override
                    public void onResult(GeneralResult result) {
                        StringBuilder sb = new StringBuilder();
                        // Word类包含位置信息
                        for (WordSimple wordSimple : result.getWordList()) {
                            Word word = (Word) wordSimple;
                            sb.append(word.getWords());
                            sb.append("\n");
                        }
                        alertText(OCRUtil.Result_Title,sb.toString());
                    }

                    @Override
                    public void onError(OCRError error) {
                        alertText(OCRUtil.Failure_Title,error.getMessage());
                    }
                });
                break;

        }

    }

    //检查AK,SK是否验证有效。
    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(this, "Access Token 获取失败", Toast.LENGTH_LONG);
        }
        return hasGotToken;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }
}
