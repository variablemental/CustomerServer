package com.emotibot.xychatlib.controllers;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.openapiresult.XYlibResult;
import com.emotibot.xychatlib.openapiresult.items.XYlibCommonItem;
import com.emotibot.xychatlib.utils.XYlibLogUtils;
import com.emotibot.xychatlib.utils.XYlibPreferencesUtils;
import com.emotibot.xychatlib.utils.XYlibReturnResultUtils;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibRegisterController implements View.OnClickListener{
    public static final String tag = XYlibRegisterController.class.getSimpleName();
    private XYlibChatActivity mActivity;
    private XYlibPreferencesUtils mPreference;
    private Gson mGson;

    private RelativeLayout rlRegister;
    private ProgressBar pb;
    private TextView tvState;
    private TextView tvRetry;

    public XYlibRegisterController(XYlibChatActivity chatActivity) {
        mActivity = chatActivity;
        mPreference = new XYlibPreferencesUtils(mActivity);
        mActivity.setUid(mPreference.getString(XYlibPreferencesUtils.UID));
        mGson = mActivity.getGson();

        rlRegister = (RelativeLayout)mActivity.findViewById(R.id.rl_register);
        pb = (ProgressBar) mActivity.findViewById(R.id.pb_register);
        tvState = (TextView) mActivity.findViewById(R.id.tv_regiter_state);
        tvRetry = (TextView) mActivity.findViewById(R.id.tv_retry);

        rlRegister.setOnClickListener(this);
        tvRetry.setOnClickListener(this);

        startRegister();
    }

    private void startRegister() {
        if (!TextUtils.isEmpty(mActivity.getUid())) {
            rlRegister.setVisibility(View.GONE);
            return;
        }

        rlRegister.setVisibility(View.VISIBLE);
        showProgressBar();

        register();
    }

    private void register() {
        mActivity.getNetworkHelper().register(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                String result = response.body();
                XYlibLogUtils.d(tag, "result:"+result);
                processResult(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private void processResult(String result) {
        XYlibReturnResultUtils rru = new XYlibReturnResultUtils(result);
        if (rru.getReturnValue() == 0) {
            String json = rru.getElement(0);

            Type type = new TypeToken<XYlibResult<List<XYlibCommonItem>>>() {}.getType();
            XYlibResult<List<XYlibCommonItem>> ret = mGson.fromJson(json, type);

            if (TextUtils.isEmpty(ret.getValue())
                    || !ret.getCmd().equals(URLConstants.REGISTER)) {
                hideProgressBar();
            } else {
                registerSuccess(ret.getValue());
            }
        } else {
            hideProgressBar();
        }
    }
    private void showProgressBar() {
        pb.setVisibility(View.VISIBLE);
        tvState.setText(mActivity.getResources().getText(R.string.regitering));
        tvRetry.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        pb.setVisibility(View.GONE);
        tvState.setText(mActivity.getResources().getText(R.string.regiter_fail));
        tvRetry.setVisibility(View.VISIBLE);
    }

    private void registerSuccess(String uid) {
        rlRegister.setVisibility(View.GONE);
        mActivity.setUid(uid);
        mPreference.setString(XYlibPreferencesUtils.UID, uid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_register) {
            return;
        }

        if (v.getId() == R.id.tv_retry) {
            startRegister();
        }
    }

}
