package com.example.coder_z.customserver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by coder-z on 17-10-25.
 */

public class RecognitionActivity extends AppCompatActivity {

    Runnable networkTask=new Runnable() {
        @Override
        public void run() {
            try {
                reco_result = RecognitionResult.detect(bytes);
                Message msg=new Message();
                Bundle bundle=new Bundle();
                bundle.putString(NOTICE_MESSAGE,reco_result);
                msg.setData(bundle);
                handler.sendMessage(msg);
                //Toast.makeText(RecognitionActivity.this, reco_result, Toast.LENGTH_LONG).show();
                System.out.println(reco_result);
            }catch (Exception e){
                Toast.makeText(RecognitionActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

    private static final String NOTICE_MESSAGE="NOTICE_MESSAGE";
    private static final int TAKE_PICTURE=1;
    private byte[] bytes=null;
    private String reco_result;
    private Button mPhotoButton;
    private ImageView mImageView;
    private TextView mNotice;
    private Handler handler=null;

    public void onCreate(Bundle onSavedStateInstance){
        super.onCreate(onSavedStateInstance);
        setContentView(R.layout.recognition_activity);
        mPhotoButton=(Button)findViewById(R.id.take_photo);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"),TAKE_PICTURE);
            }
        });
        mImageView=(ImageView)findViewById(R.id.photo_view);
        mNotice=(TextView)findViewById(R.id.recognition_notice);
        handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle b=message.getData();
                String str=b.getString(NOTICE_MESSAGE);
                mNotice.setText(str);
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==TAKE_PICTURE){
            if(resultCode==RESULT_OK){
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                mImageView.setImageBitmap(bitmap);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                bytes=baos.toByteArray();
                new Thread(networkTask).start();    //后台线程调用API识别
                }
        }
    }






}
