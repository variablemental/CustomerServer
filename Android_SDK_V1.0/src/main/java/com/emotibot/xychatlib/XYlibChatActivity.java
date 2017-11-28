package com.emotibot.xychatlib;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.controllers.XYlibRegisterController;
import com.emotibot.xychatlib.controllers.XYlibVoicebtnController;
import com.emotibot.xychatlib.dialogs.XYlibLargeImageDialog;
import com.emotibot.xychatlib.dialogs.XYlibWebViewDialog;
import com.emotibot.xychatlib.helpers.XYlibNetworkHelper;
import com.emotibot.xychatlib.utils.XYlibFileUtils;

/**
 * Created by ldy on 2017/2/20.
 */

public class XYlibChatActivity extends AppCompatActivity {
    //global variables saved here
    private XYlibNetworkHelper networkHelper;
    private XYlibRegisterController registerController;
    private XYlibChatController chatController;
    private XYlibVoicebtnController voicebtnController;
    private XYlibLargeImageDialog largeImageDialog;
    private XYlibWebViewDialog webViewDialog;

    private Handler mHandler;
    private Gson mGson;
    private String mUid;

    private ActionBar actionBar;

    private String [] mLocationNeededPermission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_NoTitle);
        setTheme(R.style.Theme_NoTitle);
        setContentView(R.layout.activity_xylibchat);

        //only portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestPermission(mLocationNeededPermission);

        //注册控制器、线程和辅助类
        initVariables();

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actionBar=getActionBar();
        if(actionBar!=null){
            actionBar.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        largeImageDialog.dismiss();
        webViewDialog.dismiss();
    }

    private void initVariables() {
        mGson = new Gson();
        networkHelper = XYlibNetworkHelper.getInstance();
        chatController = new XYlibChatController(this);
        registerController = new XYlibRegisterController(this);
        voicebtnController = new XYlibVoicebtnController(this);
        mHandler = new Handler();
        if (XYlibFileUtils.fileDir == null) {
            XYlibFileUtils.fileDir = getFilesDir();
        }

        largeImageDialog = new XYlibLargeImageDialog(this);
        webViewDialog = new XYlibWebViewDialog(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    protected void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            boolean hasDenied = false;
            if (grantResults.length <= 0) {
                hasDenied = true;
            }
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    hasDenied = true;
                    break;
                }
            }
            if (hasDenied) {
                Toast.makeText(this, "权限不足,可能影响某些功能使用",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    public void onItemClick(View view){
        chatController.onItemClick(view);
    }

    public void utter(String utterance){
        chatController.utter(utterance);
    }

    public XYlibNetworkHelper getNetworkHelper() {
        return networkHelper;
    }

    public XYlibRegisterController getRegisterController() {
        return registerController;
    }

    public XYlibChatController getChatController() {
        return chatController;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public Gson getGson() {
        return mGson;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

    public XYlibLargeImageDialog getLargeImageDialog() {
        return largeImageDialog;
    }

    public XYlibWebViewDialog getWebViewDialog() {
        return webViewDialog;
    }
}
