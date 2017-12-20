package com.example.coder_z.customserver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017\11\8 0008.
 */

public class OCRActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,SurfaceHolder.Callback{

    private final static int REQUEST_CODE_OCR=101;
    private final static int REQUEST_CODE_TAKE_PICTURE=102;
    private final static int REQUEST_CODE_FACE=103;
    private final static String TAG_OCR="OCR";
    private final static String TAG_PIC="图像识别";
    private final static String TAG_FACE="人脸识别";

    private static String indicator=TAG_OCR;

    private boolean hasGotToken=false;
    private AlertDialog.Builder alertDialog;
    private Button mPhotoButton;
    private Button mModeButton;
    private RadioGroup mRadioGroup;
    private TextView mNotice;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {//照相动作回调用的pictureCallback

        //在这里可以获得拍照后的图片数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //byte[]数组data就是图片数据，可以在这里对图片进行处理

            switch (indicator){
                case TAG_OCR:
                    Intent intent = new Intent(OCRActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            new File(getApplicationContext().getFilesDir(), "pic.jpg").getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                            CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent,REQUEST_CODE_OCR);
                    break;
                case TAG_PIC:
                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                    RecognitionResult.process(OCRActivity.this,bitmap);
                    break;
                case TAG_FACE:
                    //#Todo
                    break;
            }

            mCamera.startPreview();//恢复预览
        }
    };



    public void onCreate(Bundle onSavedStateInstance){
        super.onCreate(onSavedStateInstance);
        setContentView(R.layout.recognition_activity);
        alertDialog=new AlertDialog.Builder(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.reco_radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);
        mNotice=(TextView)findViewById(R.id.recognition_notice);
        mModeButton=(Button)findViewById(mRadioGroup.getCheckedRadioButtonId());


        mPhotoButton=(Button)findViewById(R.id.take_photo);
/*        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }

                Button b=(Button)findViewById(mRadioGroup.getCheckedRadioButtonId());
                String indicator=b.getText().toString();
                switch (indicator){
                    case TAG_OCR:
                        Intent intent = new Intent(OCRActivity.this, CameraActivity.class);
                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                new File(getApplicationContext().getFilesDir(), "pic.jpg").getAbsolutePath());
                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                                CameraActivity.CONTENT_TYPE_GENERAL);
                        startActivityForResult(intent,REQUEST_CODE_OCR);
                        break;
                    case TAG_PIC:
                        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"),REQUEST_CODE_TAKE_PICTURE);
                        break;
                    case TAG_FACE:
                        //#Todo
                        break;

                }

            }
        });*/
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });
        initAccessTokenWithAkSk();
        initPic();
        mSurfaceView=(SurfaceView)findViewById(R.id.camera_surface);
        mSurfaceHolder=mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

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

            case REQUEST_CODE_TAKE_PICTURE:
                RecognitionResult.onResult(OCRActivity.this,data);
                break;

            case REQUEST_CODE_FACE:

                //#Todo
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.main:
                Button b=(Button)findViewById(mRadioGroup.getCheckedRadioButtonId());
                String indicator1=b.getText().toString();
                Toast.makeText(this,indicator1,Toast.LENGTH_LONG).show();
                indicator=indicator1;
                break;
            case R.id.recognition:
                Button b2=(Button)findViewById(mRadioGroup.getCheckedRadioButtonId());
                String indicator2=b2.getText().toString();
                Toast.makeText(this,indicator2,Toast.LENGTH_LONG).show();
                indicator=indicator2;
                break;
            case R.id.browser:
                Button b3=(Button)findViewById(mRadioGroup.getCheckedRadioButtonId());
                String indicator3=b3.getText().toString();
                Toast.makeText(this,indicator3,Toast.LENGTH_LONG).show();
                indicator=indicator3;
                break;
        }
    }

    private void initPic(){
        RecognitionResult.setmNotice((TextView)findViewById(R.id.recognition_notice));
        RecognitionResult.initHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();//使用静态方法open初始化camera对象，默认打开的是后置摄像头
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);//设置在surfaceView上显示预览
            mCamera.setDisplayOrientation(90);

            Camera.Parameters parameters = mCamera.getParameters();//得到一个已有的（默认的）参数

            Display display = this.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;

            int[] bestResolution=getBestResolution(parameters, screenHeight, screenWidth);
            parameters.setPreviewSize(bestResolution[0], bestResolution[1]);

            parameters.setRotation(90);//设置照相生成的图片的方向，后面有详细说明
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//设置闪光灯模式为关
            mCamera.setParameters(parameters);//将参数赋给camera

            mCamera.startPreview();//开始预览
        } catch (IOException e) {
            //在异常处理里释放camera并置为null
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


    private int[] getBestResolution(Camera.Parameters parameters, int width, int height) {
        int[] bestResolution = new int[2];//int数组，用来存储最佳宽度和最佳高度
        int bestResolutionWidth = -1;//最佳宽度
        int bestResolutionHeight = -1;//最佳高度

        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获得设备所支持的分辨率列表
        int difference = 99999;//最小差值，初始化市需要设置成一个很大的数

        //遍历sizeList，找出与期望分辨率差值最小的分辨率
        for (int i = 0; i < sizeList.size(); i++) {
            int differenceWidth = Math.abs(width - sizeList.get(i).width);//求出宽的差值
            int differenceHeight = Math.abs(height - sizeList.get(i).height);//求出高的差值

            //如果它们两的和，小于最小差值
            if ((differenceWidth + differenceHeight) < difference) {
                difference = (differenceWidth + differenceHeight);//更新最小差值
                bestResolutionWidth = sizeList.get(i).width;//赋值给最佳宽度
                bestResolutionHeight = sizeList.get(i).height;//赋值给最佳高度
            }
        }

        //最后将最佳宽度和最佳高度添加到数组中
        bestResolution[0] = bestResolutionWidth;
        bestResolution[1] = bestResolutionHeight;
        return bestResolution;//返回最佳分辨率数组
    }
}
