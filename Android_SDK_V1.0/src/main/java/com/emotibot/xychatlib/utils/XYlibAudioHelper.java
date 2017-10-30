package com.emotibot.xychatlib.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.emotibot.xychatlib.constants.URLConstants;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibAudioHelper {
    public interface AudioStateListener
    {
        void reachMaxDuration();
        void wellPrepared();
        void recordPermissionDenied();
    }

    private static XYlibAudioHelper mInstance;
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private boolean isPrepared = false;
    private String mCurFilePath;
    private AudioStateListener mListener;
    private boolean isStarted = false;

    private int permissionDetectVoiceLv = 0;

    private XYlibAudioHelper(String dir){
        mDir = dir;
    }

    public static XYlibAudioHelper getInstance(String dir){
        if(mInstance == null){
            synchronized (XYlibAudioHelper.class){
                if(mInstance == null){
                    mInstance = new XYlibAudioHelper(dir);
                }
            }
        }
        return mInstance;
    }

    public boolean prepareAudio(){

        try {
            //check permission
            if (!validateMicAvailability()) {
                return false;
            }
            isPrepared = false;
            File dir = new File(mDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String fileName = generateFileName();
            File file = new File(dir,fileName);
            mCurFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOnInfoListener(onInfoListener);
            mMediaRecorder.setOutputFile(mCurFilePath);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setMaxDuration(URLConstants.MAX_DURATION);
            //mMediaRecorder.setMaxDuration(10*1000);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isStarted = true;
            //isRecordingWorking();
            isPrepared = true;
//            LogUtils.i("TAG","pre");
            if(mListener != null)
            {
                mListener.wellPrepared();
            }
            return true;

        } catch (IllegalStateException|IOException e) {
            e.printStackTrace();
            XYlibLogUtils.i("TAG","error");
            return false;
        } catch (SecurityException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private MediaRecorder.OnInfoListener onInfoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                if(mListener!=null){
                    mListener.reachMaxDuration();
                }
            }
        }
    };

    public void setAudioStateListener(AudioStateListener listener){
        mListener = listener;
    }

    private String generateFileName(){
        return UUID.randomUUID().toString()+".amr";
    }

    public void release()
    {
        isStarted = false;
        if(mMediaRecorder != null){
            try{
                mMediaRecorder.stop();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (RuntimeException e) {
                // TODO: handle exception
                e.printStackTrace();
            }catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void cancel()
    {
        release();
        if(mCurFilePath!=null)
        {
            File file = new File(mCurFilePath);
            file.delete();
            mCurFilePath = null;
        }

    }

    public String getCurrentFilePath() {
        // TODO Auto-generated method stub
        return mCurFilePath;
    }

    public int getVoiceLevel(int maxLevel)
    {
        if(isPrepared)
        {
            int level = 1;
            try {
                if(mMediaRecorder != null)
                    level = maxLevel * mMediaRecorder.getMaxAmplitude()/10000+1;
                    if (level > maxLevel) {
                        level = maxLevel;
                    }
                    return level;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public static boolean validateMicAvailability(){
        boolean ret = true;
        AudioRecord recorder = null;

        try{
            recorder =
                    new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_DEFAULT, 44100);
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){
                return false;
            }

            recorder.startRecording();
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
                recorder.stop();
                return false;
            }
            recorder.stop();
        } catch (SecurityException ex) {
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        finally{
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
        }

        return ret;
    }

}
