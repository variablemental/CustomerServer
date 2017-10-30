package com.emotibot.xychatlib.helpers;

import android.view.MotionEvent;
import android.view.View;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import com.emotibot.xychatlib.XYlibFloatingTriggerButton;
import com.emotibot.xychatlib.nineoldandroids.view.ViewHelper;

/**
 * Created by ldy on 2017/3/15.
 */

public class XYlibAnimationLowSDKHelper {
    float dX, dY;
    float containerRawX, containerRawY;
    float minDX, minDY, min = 60.0f;
    int width, height;
    int screenWidth, screenHeight;
    long startTime;

    public boolean processTouch(final View view, final MotionEvent event, XYlibFloatingTriggerButton btn) {
        getSizes(event, btn);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();

                dX = event.getX();
                dY = event.getY();

                if (dX < min) {
                    dX = minDX;
                }

                if (dY < min) {
                    dY = minDY;
                }

                return false;
            case MotionEvent.ACTION_MOVE:
                float endX, endY;
                endX = event.getRawX() - dX - containerRawX;
                endY = event.getRawY() - dY - containerRawY;

                if (endX*-1.0f > width/2) {
                    endX = width*-1.0f/2;
                }

                if (endY*-1.0f > height/2) {
                    endY = height*-1.0f/2;
                }

                animate(view).x(endX).y(endY).setDuration(0).start();
                return false;
            case MotionEvent.ACTION_UP:
                long interval = System.currentTimeMillis() - startTime;
                float endX1, endY1;
                endX1 = ViewHelper.getX(view);
                endY1 = ViewHelper.getY(view);

                if (endX1 <= screenWidth/2 && endY1 <= screenHeight/2) {
                    if (endX1 <= endY1) {
                        endX1 = 0.0f;
                    } else {
                        endY1 = 0.0f;
                    }
                } else if (endX1 > screenWidth/2 && endY1 <= screenHeight/2 ) {
                    if (screenWidth - endX1 <= endY1) {
                        endX1 = screenWidth - width;
                    } else {
                        endY1 = 0.0f;
                    }
                } else if (endX1 <= screenWidth/2 && endY1 > screenHeight/2) {
                    if (endX1 <= screenHeight - endY1) {
                        endX1 = 0.0f;
                    } else {
                        endY1 = screenHeight - height;
                    }
                } else {
                    if (screenWidth - endX1 <= screenHeight - endY1) {
                        endX1 = screenWidth - width;
                    } else {
                        endY1 = screenHeight - height;
                    }
                }

                if (endX1 < 0) {
                    endX1 = 0.0f;
                }

                if (endX1 > screenWidth - width) {
                    endX1 = screenWidth - width;
                }

                if (endY1 < 0) {
                    endY1 = 0.0f;
                }

                if (endY1 > screenHeight - height) {
                    endY1 = screenHeight - height;
                }

                animate(view).x(endX1).y(endY1).setDuration(100).start();

                if (interval < 200) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void getSizes(MotionEvent event, XYlibFloatingTriggerButton btn) {
        if (width > 0) {
            return;
        }

        containerRawX = event.getRawX() - event.getX();
        containerRawY = event.getRawY() - event.getY();
        width = btn.getWidth();
        height = btn.getHeight();

        minDX = width/2;
        minDY = height/2;

        View parent = (View)btn.getParent();
        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
    }
}
