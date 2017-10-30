package com.emotibot.xychatlib.dialogs;

import android.app.Dialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibWebViewDialog {
    Dialog dialog;
    XYlibChatActivity mActivity;
    WebView mWebView;


    public XYlibWebViewDialog(XYlibChatActivity mActivity) {
        this.mActivity = mActivity;
        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(mActivity, R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.dialog_webview);

        dialog.findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        mWebView = (WebView) dialog.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http")) {
                    return false;
                }

                mWebView.loadUrl(url);

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public void loadWebPage(String url) {
        mWebView.loadUrl(url);
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
