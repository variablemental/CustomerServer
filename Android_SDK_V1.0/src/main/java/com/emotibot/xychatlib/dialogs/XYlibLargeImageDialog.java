package com.emotibot.xychatlib.dialogs;

import android.app.Dialog;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import java.io.File;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.tasks.XYlibImageDownloadTask;
import com.emotibot.xychatlib.utils.XYlibFileUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibLargeImageDialog {
    Dialog dialog;
    XYlibChatActivity mActivity;
    ImageView iv;

    public XYlibLargeImageDialog(XYlibChatActivity mActivity) {
        this.mActivity = mActivity;

        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(mActivity, R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.dialog_large_image);
        iv = (ImageView) dialog.findViewById(R.id.iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }

    public void setImage(String url) {
        iv.setImageResource(R.drawable.loading_pic);
        String filename = URLUtil.guessFileName(url, null, null);
        if (TextUtils.isEmpty(filename)) {
            return;
        }

        String path = XYlibFileUtils.buildImageChatPath() + filename;
        File file = new File(path);
        if (file.exists()) {
            iv.setImageURI(Uri.parse("file:///" + file));
        } else {
            new XYlibImageDownloadTask(mActivity.getChatController(), path){
                @Override
                protected void onPostExecute(File result) {
                    if (result == null) {
                        iv.setImageResource(R.drawable.loading_pic);
                        return;
                    }

                    iv.setImageURI(Uri.parse("file:///" + result));
                }
            }.execute(url);
        }
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
