package com.example.coder_z.customserver;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * Created by coder-z on 17-10-25.
 */

public class HomeActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {


    private TabHost tabHost;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        actionBar=getActionBar();
        if(actionBar!=null){
            actionBar.show();
        }
        tabHost=getTabHost();



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


        return super.onCreateOptionsMenu(menu);
    }

    //Actionbar点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.bar_side:


                return true;
            case R.id.bar_robot:

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
