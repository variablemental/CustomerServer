package com.emotibot.xychatlib.controllers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.widgets.XYlibRecordBtn;

/**
 * Created by ldy on 2017/2/27.
 */

public class XYlibVoicebtnController implements View.OnClickListener {
    public static final int TEXT_INPUT = 1;
    public static final int VOICE_INPUT = 2;

    private XYlibChatActivity mActivty;
    private ImageButton ibVoiceBtn;
    private EditText etInput;
    private XYlibRecordBtn recordBtn;
    private Button btnSend;
    private int inputState = VOICE_INPUT;
    private InputMethodManager inputManager;

    public XYlibVoicebtnController(XYlibChatActivity activity) {
        mActivty = activity;

        inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
    }

    private void initView() {
        ibVoiceBtn = (ImageButton) mActivty.findViewById(R.id.voice_btn);
        etInput = (EditText) mActivty.findViewById(R.id.et_input);
        recordBtn = (XYlibRecordBtn) mActivty.findViewById(R.id.press_talk_btn);
        btnSend = (Button) mActivty.findViewById(R.id.send_btn);
        ibVoiceBtn.setOnClickListener(this);
    }

    private void voiceBtnClicked() {
        if (recordBtn.getVisibility() == View.VISIBLE) {
            inputState = VOICE_INPUT;
        } else {
            inputState = TEXT_INPUT;
        }

        switch (inputState) {
            case VOICE_INPUT:
                inputState = TEXT_INPUT;

                showTextInput();
                break;
            case TEXT_INPUT:
                inputState = VOICE_INPUT;

                hideKeyboard();
                showVoiceInput();
                break;
        }
    }

    private void showTextInput() {
        etInput.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        ibVoiceBtn.setImageResource(R.drawable.icon_micphone);

        recordBtn.setVisibility(View.GONE);
    }

    private void showVoiceInput() {
        recordBtn.setVisibility(View.VISIBLE);
        ibVoiceBtn.setImageResource(R.drawable.icon_keyboard);

        etInput.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        inputManager.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.voice_btn) {
            voiceBtnClicked();
        }
    }
}
