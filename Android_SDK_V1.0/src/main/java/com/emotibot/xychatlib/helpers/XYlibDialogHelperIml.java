package com.emotibot.xychatlib.helpers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.interfaces.XYlibDialogHelper;
import com.emotibot.xychatlib.utils.XYlibLogUtils;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibDialogHelperIml implements XYlibDialogHelper {
    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;
    private View mSeparator;
    private Context mContext;

    public XYlibDialogHelperIml(Context mContext){
        this.mContext = mContext;
    }

    public void showRecordingDialog(){
        setRecordingDialog();
        mDialog.show();
    }

    public void setRecordingDialog(){
        if(mDialog == null){
            mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
            mDialog.setContentView(R.layout.dialog_recording);
            mIcon = (ImageView) mDialog.findViewById(R.id.mIcon);
            mVoice = (ImageView) mDialog.findViewById(R.id.mVoice);
            mLable = (TextView) mDialog.findViewById(R.id.mLable);
            mSeparator = mDialog.findViewById(R.id.separator);
        }
    }

    public void dismissDialog(){
        try{
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            XYlibLogUtils.i("TAG","IllegalArgumentException");
        }

    }

    public void wantToCancel(){
        mIcon.setImageResource(R.drawable.record_cancel);
        mSeparator.setVisibility(View.GONE);
        mVoice.setVisibility(View.GONE);
        mLable.setTextColor(mContext.getResources().getColor(R.color.text_record_cancel));
        mLable.setText(mContext.getString(R.string.record_cancel));
    }

    public void recording(){
        mIcon.setImageResource(R.drawable.record_mic);
        mSeparator.setVisibility(View.VISIBLE);
        mVoice.setVisibility(View.VISIBLE);
        mLable.setTextColor(mContext.getResources().getColor(R.color.text_record_normal));
        mLable.setText(mContext.getString(R.string.record_recording));
    }

    public void tooShort(){
        mIcon.setImageResource(R.drawable.record_short);
        mSeparator.setVisibility(View.GONE);
        mVoice.setVisibility(View.GONE);
        mLable.setTextColor(mContext.getResources().getColor(R.color.text_record_normal));
        mLable.setText(mContext.getString(R.string.record_short));
    }

    public void notPrepared(){
        mIcon.setImageResource(R.drawable.record_short);
        mSeparator.setVisibility(View.GONE);
        mVoice.setVisibility(View.GONE);
        mLable.setTextColor(mContext.getResources().getColor(R.color.text_record_normal));
        mLable.setText(mContext.getString(R.string.record_not_prepared));
    }

    public void updateVoiceLevel(int level)
    {
        if(mDialog!=null&&mDialog.isShowing())
        {
            int resId = mContext.getResources().getIdentifier("record_v"+level, "drawable", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
