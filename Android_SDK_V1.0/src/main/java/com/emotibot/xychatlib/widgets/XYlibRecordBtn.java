package com.emotibot.xychatlib.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.helpers.XYlibDialogHelperIml;
import com.emotibot.xychatlib.interfaces.XYlibDialogHelper;
import com.emotibot.xychatlib.utils.XYlibAudioHelper;
import com.emotibot.xychatlib.utils.XYlibFileUtils;
import com.emotibot.xychatlib.utils.XYlibLogUtils;
import com.emotibot.xychatlib.utils.XYlibMediaUtils;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibRecordBtn extends Button implements XYlibAudioHelper.AudioStateListener {
    private static final String TAG = XYlibRecordBtn.class.getSimpleName();
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    private static final int DISTANCE_Y_CANCEL = 10;
    private int mCurState = STATE_NORMAL;
    private boolean isRecording = false;
    private float mTime = 0;
    private boolean mReady = false;
    private boolean isPrepared = false;
    private boolean afterDownAction = false;
    private boolean isShowingAlert = false;
    private Thread voiceLevelThread = new Thread();

    private XYlibDialogHelper mDialogManager;
    private XYlibAudioHelper mAudioManager;

    private int itemBackgroundNormal;
    private int itemBackgroundPressed;

    private ColorStateList itemTextColorNormal;
    private ColorStateList itemTextColorPressed;

    private boolean showButtonText;

    public XYlibRecordBtn(Context context) {
        this(context,null);
    }

    public XYlibRecordBtn(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYlibRecordBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RecordBtn);
        itemBackgroundNormal = a.getResourceId(R.styleable.RecordBtn_itemBackgroundNormal, R.drawable.bg_press_speak_normal);
        itemBackgroundPressed = a.getResourceId(R.styleable.RecordBtn_itemBackgroundPressed, R.drawable.bg_press_speak_pressed);

        itemTextColorNormal = a.getColorStateList(R.styleable.RecordBtn_itemTextColorNormal);
        itemTextColorPressed = a.getColorStateList(R.styleable.RecordBtn_itemTextColorNormal);

        showButtonText = a.getBoolean(R.styleable.RecordBtn_showButtonText,false);

        a.recycle();
        setBackgroundResource(itemBackgroundNormal);
        mDialogManager = new XYlibDialogHelperIml(context);

        String dir = XYlibFileUtils.buildVoiceChatPath();
        mAudioManager = XYlibAudioHelper.getInstance(dir);
        mAudioManager.setAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    private Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            if(afterDownAction){
                XYlibLogUtils.i(TAG, "long click");
                mReady = true;
                showRecording();
                isPrepared = mAudioManager.prepareAudio();
                if(!isPrepared){
                    handler.sendEmptyMessageDelayed(MSM_RECORDER_NOT_PREPARED,600);
                }
            }else {
                XYlibLogUtils.i(TAG, "not reach long click");
            }

        }
    };

    public void setDialogManager(XYlibDialogHelper dialogManager){
        mDialogManager = dialogManager;
    }

    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while(isRecording){
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    handler.sendEmptyMessage(MSM_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRecording = false;
                }
            }
        }
    };
    private static final int MSM_AUDIO_PREPARED = 0x110;
    private static final int MSM_VOICE_CHANGED = 0x111;
    private static final int MSM_DIALOG_DISMISS = 0x112;
    private static final int MSM_REACH_MAX_DURATION = 0x113;
    private static final int MSM_RECORDER_NOT_PREPARED = 0x114;
    private static final int MSM_RECORDER_NOT_PERMITTED = 0x115;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSM_AUDIO_PREPARED:
                    XYlibLogUtils.i(TAG,"handler prepared");
                    if (!afterDownAction) {
                        break;
                    }
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    voiceLevelThread = new Thread(mGetVoiceLevelRunnable);
                    voiceLevelThread.start();
                    break;
                case MSM_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSM_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    isShowingAlert = false;
                    break;
                case MSM_REACH_MAX_DURATION:
                    MotionEvent motionEvent = MotionEvent.obtain(1234,0,MotionEvent.ACTION_UP,0,0,0);
                    onTouchEvent(motionEvent);
                    break;
                case MSM_RECORDER_NOT_PREPARED:
                    MotionEvent motionEvent1 = MotionEvent.obtain(1234,0,MotionEvent.ACTION_UP,0,0,0);
                    onTouchEvent(motionEvent1);
                    break;
                case MSM_RECORDER_NOT_PERMITTED:
                    MotionEvent motionEvent2 = MotionEvent.obtain(1234,0,MotionEvent.ACTION_CANCEL,0,0,0);
                    onTouchEvent(motionEvent2);
                    break;

            }
        }
    };

    public interface RecordBtnListener
    {
        void onAudioRecordStart();
        void onAudioRecordFinish(float seconds, String filePath);
        void onReachMaxRecordDuration();
    }

    private RecordBtnListener mListener;

    public void setRecordBtnListener(RecordBtnListener listener)
    {
        mListener = listener;
    }

    public void reset(){
        mReady = false;
        isPrepared = false;
        mTime = 0;
        voiceLevelThread.interrupt();
        isRecording = false;
        changeState(STATE_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        if(isShowingAlert){
            return super.onTouchEvent(event);
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                reset();
                XYlibLogUtils.i("TAG","ACTION_DOWN------mTime:"+mTime);
                XYlibMediaUtils.reset();
                if (mListener != null) {
                    mListener.onAudioRecordStart();
                }
                afterDownAction = true;
                postDelayed(longClickRunnable,50);
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if(afterDownAction){
                    if(wantToCancel(x,y)){
                        changeState(STATE_WANT_TO_CANCEL);
                    }else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                XYlibLogUtils.i("TAG","ACTION_UP------");
                boolean insideAfterDownAction = afterDownAction;
                afterDownAction = false;
                XYlibLogUtils.d(TAG,"");
                if(mReady && isPrepared){
                    if(mTime < 0.6f || !isRecording){
                        XYlibLogUtils.i(TAG, "action up short" + mReady);
                        showTooShort();
                        mAudioManager.cancel();
                    }else if(mCurState == STATE_WANT_TO_CANCEL)
                    {
                        mAudioManager.cancel();
                        mDialogManager.dismissDialog();
                    }else if(mCurState == STATE_RECORDING){
                        mAudioManager.release();
                        if(mListener != null){
                            mListener.onAudioRecordFinish(mTime, mAudioManager.getCurrentFilePath());
                        }
                        mDialogManager.dismissDialog();
                    }

                }else {
                    if(insideAfterDownAction){
                        if(!mReady){
                            showTooShort();
                        }else if(!isPrepared){
                            showNotPrepared();
                        }
                    }
                    mAudioManager.cancel();
                    reset();
                    return super.onTouchEvent(event);
                }

                reset();
                break;
            case MotionEvent.ACTION_CANCEL:
                XYlibLogUtils.i(TAG, "Event:cancel");
                afterDownAction = false;
                reset();
                mAudioManager.cancel();
                mDialogManager.dismissDialog();
                if(event.getDownTime() == 1234){
                    Toast.makeText(getContext(),"打开麦克风失败，可能录音权限被关闭", Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void showNotPrepared(){
        isShowingAlert = true;
        mDialogManager.setRecordingDialog();
        mDialogManager.notPrepared();
        mDialogManager.showRecordingDialog();
        XYlibLogUtils.i("MainPageActivity","showNotPrepared performed:");
        handler.sendEmptyMessageDelayed(MSM_DIALOG_DISMISS,1000);
    }

    public void showTooShort(){
        isShowingAlert = true;
        mDialogManager.setRecordingDialog();
        mDialogManager.tooShort();
        mDialogManager.showRecordingDialog();
        XYlibLogUtils.i("MainPageActivity","showTooShort performed:");
        handler.sendEmptyMessageDelayed(MSM_DIALOG_DISMISS,1000);
    }

    public void showRecording(){
        mDialogManager.setRecordingDialog();
        mDialogManager.recording();
        mDialogManager.showRecordingDialog();
        XYlibLogUtils.i("MainPageActivity","showRecording performed:");
    }


    public boolean wantToCancel(int x,int y){
        if(x < 0 || x > getWidth()){
            return true;
        }
        if(y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL){
            return true;
        }
        return false;
    }

    public void changeState(int state){
//        XYlibLogUtils.i(TAG,"state:--Cur:"+mCurState+" new:"+state);
        if(mCurState != state){
//            XYlibLogUtils.i(TAG,"state:"+state);
            mDialogManager.setRecordingDialog();
            mCurState = state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(itemBackgroundNormal);
                    if(showButtonText){
                        setText("按住 说话");
                    }
                    setTextColor(getResources().getColor(R.color.sendText));

                    break;
                case STATE_RECORDING:
                    setBackgroundResource(itemBackgroundPressed);
                    if(showButtonText) {
                        setText("松开 发送");
                    }
                    if(itemTextColorNormal != null){
                        setTextColor(itemTextColorNormal);
                    }else {
                        setTextColor(getResources().getColor(R.color.sendText));
                    }

                    mDialogManager.recording();
                    break;
                case STATE_WANT_TO_CANCEL:
                    if(showButtonText) {
                        setText("松开 取消");
                    }
                    setBackgroundResource(itemBackgroundPressed);
                    if(itemTextColorPressed != null){
                        setTextColor(itemTextColorPressed);
                    }else {
                        setTextColor(getResources().getColor(R.color.sendText));
                    }
                    mDialogManager.wantToCancel();
                    break;
            }
        }

    }

    @Override
    public void reachMaxDuration() {
//        XYlibLogUtils.i(TAG,"reachMaxDuration");
        handler.sendEmptyMessage(MSM_REACH_MAX_DURATION);
        if(mListener!=null){
            mListener.onReachMaxRecordDuration();
        }
    }

    @Override
    public void wellPrepared() {
//        XYlibLogUtils.i(TAG,"wellPrepared");
        handler.sendEmptyMessage(MSM_AUDIO_PREPARED);
    }

    @Override
    public void recordPermissionDenied() {
//        XYlibLogUtils.i(TAG,"----------Permission Denied");
        handler.sendEmptyMessage(MSM_RECORDER_NOT_PERMITTED);
    }
}
