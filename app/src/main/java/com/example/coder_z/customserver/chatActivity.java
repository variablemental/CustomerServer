package com.example.coder_z.customserver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.dialogs.XYlibWebViewDialog;
import com.emotibot.xychatlib.helpers.XYlibNetworkHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017\12\11 0011.
 */

public class chatActivity extends Activity {

    private ChatController chatController;
    private XYlibNetworkHelper networkHelper;
    private XYlibWebViewDialog webViewDialog;
    private String mUid;

    private String [] mLocationNeededPermission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xylibchat);
        chatController = new ChatController(this);
        findViewById(com.emotibot.xychatlib.R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public XYlibNetworkHelper getNetworkHelper() {
        return networkHelper;
    }

    private void requestPermission(String needPermission[]) {
        ArrayList<String> requestedPermission = new ArrayList<String>();
        for (int i = 0; i < needPermission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, needPermission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                requestedPermission.add(needPermission[i]);
            }
        }

        if (requestedPermission.size() > 0) {
            String permissions[] = new String[requestedPermission.size()];
            for (int i = 0; i < permissions.length; i++) {
                permissions[i] = requestedPermission.get(i);
            }

            ActivityCompat.requestPermissions(this, permissions,
                    1);
        }
    }

    public XYlibWebViewDialog getWebViewDialog() {
        return webViewDialog;
    }
    public String getUid() {
        return mUid;
    }
    public ChatController getChatController() {
        return chatController;
    }



}
