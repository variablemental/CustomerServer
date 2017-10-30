package com.emotibot.xychatlib.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/2/24.
 */

public class XYlibRobotResWaitVH extends XYlibBaseViewHolder {
    private ImageView ivDot1, ivDot2, ivDot3;
    private int mCount;
    private int mPos;
    private XYlibChatMessage mCm;
    private int intervalTime = 500;

    private View mView;

    public XYlibRobotResWaitVH(View view) {
        super(view);
        mView = view;
        mCount = 0;
    }

    protected void initViews(View v) {
        ivDot1 = (ImageView) v.findViewById(R.id.ivDot1);
        ivDot2 = (ImageView) v.findViewById(R.id.ivDot2);
        ivDot3 = (ImageView) v.findViewById(R.id.ivDot3);
    }

    public void bindView(XYlibChatMessage chatMessage, final XYlibChatActivity activity, int pos) {
        mActivity = activity;
        mPos = pos;
        mCm = chatMessage;

        if (mCount == 0) {
            show0();
            activity.getHandler().postDelayed(runnable, intervalTime);
        }
//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ivDot1, "alpha", 0f, 1f);
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ivDot2, "alpha", 0f, 1f);
//        ObjectAnimator anim3 = ObjectAnimator.ofFloat(ivDot3, "alpha", 0f, 1f);
//
//        final AnimatorSet animSet = new AnimatorSet();
//        animSet.addListener(new AnimatorListenerAdapter() {
//                                 @Override
//                                 public void onAnimationStart(Animator animation) {
//
//                                 }
//
//                                 @Override
//                                 public void onAnimationEnd(Animator animation) {
//                                     animSet.start();
//                                 }
//                             });
//        animSet.play(anim3).after(anim2);
//        animSet.play(anim2).after(anim1);
//        animSet.setDuration(500);
//        animSet.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mCount++;
            int lastPos = mActivity.getChatController().getAdapter().getItemCount() - 1;
            if (mPos == lastPos
                    && mCm.getMsgType() == XYlibChatMessageUtils.TYPING) {
                if (mCount % 4 == 0) {
                    show0();
                } else if (mCount % 4 == 1) {
                    show1();
                } else if (mCount % 4 == 2) {
                    show2();
                } else {
                    show3();
                }
                mView.invalidate();
                mActivity.getHandler().postDelayed(runnable, intervalTime);
            } else {
                show0();
            }
        }
    };

    private void show0() {
        ivDot1.setImageResource(R.drawable.dot_trans);
        ivDot2.setImageResource(R.drawable.dot_trans);
        ivDot3.setImageResource(R.drawable.dot_trans);
    }
    private void show1() {
        ivDot1.setImageResource(R.drawable.dot_black);
        ivDot2.setImageResource(R.drawable.dot_trans);
        ivDot3.setImageResource(R.drawable.dot_trans);
    }
    private void show2() {
        ivDot1.setImageResource(R.drawable.dot_black);
        ivDot2.setImageResource(R.drawable.dot_black);
        ivDot3.setImageResource(R.drawable.dot_trans);
    }
    private void show3(){
        ivDot1.setImageResource(R.drawable.dot_black);
        ivDot2.setImageResource(R.drawable.dot_black);
        ivDot3.setImageResource(R.drawable.dot_black);
    }

}
