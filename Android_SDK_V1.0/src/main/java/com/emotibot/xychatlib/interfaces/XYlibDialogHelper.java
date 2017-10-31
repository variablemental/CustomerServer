package com.emotibot.xychatlib.interfaces;

/**
 * Created by ldy on 2017/2/27.
 */

public interface XYlibDialogHelper {
    void showRecordingDialog();
    void setRecordingDialog();
    void dismissDialog();
    void wantToCancel();
    void recording();
    void tooShort();
    void notPrepared();
    void updateVoiceLevel(int level);
}
