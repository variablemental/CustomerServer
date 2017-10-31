package com.emotibot.xychatlib.viewholders;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.tasks.XYlibImageDownloadTask;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.utils.XYlibFileUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibRobotImageVH extends XYlibBaseViewHolder {
    String ext_gif = ".gif";

    TextView timeView;
    SimpleDraweeView headView;
    ImageView contentView;
    SimpleDraweeView contentViewGif;
    XYlibChatMessage chatMessage;

    public XYlibRobotImageVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        contentView = (ImageView) v.findViewById(R.id.contentView);
        contentViewGif = (SimpleDraweeView) v.findViewById(R.id.contentViewGif);
        timeView = (TextView) v.findViewById(R.id.timeView);
        headView = (SimpleDraweeView) v.findViewById(R.id.headView);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatMessage != null) {
                    String url = chatMessage.getMsg();
                    String filename = URLUtil.guessFileName(url, null, null);
                    if (filename != null && !filename.endsWith(ext_gif)) {
                        mActivity.getLargeImageDialog().setImage(chatMessage.getMsg());
                        mActivity.getLargeImageDialog().show();
                    }
                }
            }
        });
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        this.chatMessage = chatMessage;

        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);

        clear();

        String url = chatMessage.getMsg();
        String filename = URLUtil.guessFileName(url, null, null);

        if (TextUtils.isEmpty(filename)) {
            mActivity.getChatController().showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }

        if (filename.endsWith(ext_gif)) {
            contentViewGif.setVisibility(View.VISIBLE);
        } else {
            contentView.setVisibility(View.VISIBLE);
        }

        String path = XYlibFileUtils.buildImageChatPath() + filename;
        File file = new File(path);
        if (file.exists()) {
            showImage(file);
        } else {
            new XYlibImageDownloadTask(mActivity.getChatController(), path){
                @Override
                protected void onPostExecute(File result) {
                    if (result == null) {
                        contentView.setImageResource(R.drawable.loading_pic);
                        return;
                    }

                    showImage(result);
                }
            }.execute(url);
        }
    }

    private void clear() {
        contentView.setVisibility(View.GONE);
        contentViewGif.setVisibility(View.GONE);
        contentView.setImageResource(R.drawable.loading_pic);
        contentViewGif.setImageURI(XYlibFileUtils.resIdToUri(contentView.getContext(),
                                                                R.drawable.loading_pic));
    }

    private void showImage(File file) {
        if (file.getAbsolutePath().endsWith(ext_gif)) {
            showGif(file);
        } else {
            showOtherPic(file);
        }
    }

    private void showGif(File file) {
        Uri uri = Uri.fromFile(file);
        contentViewGif.setController(buildController(uri, contentViewGif.getController()));
    }

    private void showOtherPic(File file) {
        contentView.setImageURI(Uri.parse("file:///" + file));
    }

    private DraweeController buildController(Uri uri, final DraweeController oldController) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(oldController)
                .setAutoPlayAnimations(true)
                .build();
        return controller;
    }
}
