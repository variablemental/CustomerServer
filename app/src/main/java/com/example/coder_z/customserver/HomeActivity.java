package com.example.coder_z.customserver;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TabHost;

import com.emotibot.xychatlib.XYlibChatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by coder-z on 17-10-25.
 */

public class HomeActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String Home_Tag="Home";
    private static final String Recognition_Tag="Recognition";
    private static final String Me_Tag="Me";
    private static final String Setting_Tag="Setting";

    private TabHost tabHost;
    private ActionBar actionBar;
    private RadioGroup mRadioGroup;

    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        actionBar=getActionBar();
        if(actionBar!=null){
            actionBar.show();
        }

        mRadioGroup=(RadioGroup)findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);
        tabHost=getTabHost();
        tabHost.addTab(tabHost.newTabSpec(Home_Tag).setIndicator("0").setContent(new Intent(HomeActivity.this,MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Recognition_Tag).setIndicator("1").setContent(new Intent(HomeActivity.this,OCRActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Me_Tag).setIndicator("2").setContent(new Intent(HomeActivity.this,MiniActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Setting_Tag).setIndicator("3").setContent(new Intent(HomeActivity.this,SettingActivity.class)));
        tabHost.setCurrentTab(0);

    }

    //Actionbar创建
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.bar_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.bar_search);
        SearchView searchView=(SearchView) searchItem.getActionView();
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info=searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        //scale=3.0 dp=pxValue / scale + 0.5f=pxValue / 3.5

        return super.onCreateOptionsMenu(menu);
    }

    //Actionbar点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.bar_side:

                Log.d(Home_Tag,"decetion is on !");
                Intent i=new Intent(HomeActivity.this,chatActivity.class);
                startActivity(i);
                return true;
            case R.id.bar_robot:
                Log.d(Home_Tag,"robot is on !");
                Intent intent=new Intent(HomeActivity.this,XYlibChatActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.main:
                tabHost.setCurrentTab(0);
                break;
            case R.id.recognition:
                tabHost.setCurrentTab(1);
                break;
            case R.id.browser:
                tabHost.setCurrentTab(2);
                break;
            case R.id.setting:
                tabHost.setCurrentTab(3);
                break;
        }
    }
}
