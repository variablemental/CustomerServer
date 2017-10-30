package com.emotibot.xychatlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.emotibot.xychatlib.helpers.XYlibAnimationLowSDKHelper;

/**
 * Created by ldy on 2017/2/20.
 */

public class XYlibFloatingTriggerButton extends ImageButton implements View.OnClickListener, View.OnTouchListener{
    String tag = "ondraw";

    public XYlibFloatingTriggerButton(Context context) {
        super(context);
    }

    public XYlibFloatingTriggerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XYlibFloatingTriggerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(dip2px(getContext(), 50), dip2px(getContext(), 50));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setOnClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), XYlibChatActivity.class);
        getContext().startActivity(intent);
    }

    float dX, dY;
    float containerRawX, containerRawY;
    float minDX, minDY, min = 60.0f;
    int width, height;
    int screenWidth, screenHeight;
    long startTime;
    int paddingLeft, paddingRight, paddingTop, paddingBottom;
    XYlibAnimationLowSDKHelper lhelper = new XYlibAnimationLowSDKHelper();

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ) {
            return lhelper.processTouch(view, event, this);
        }

        return processTouch(view, event);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean processTouch(View view, MotionEvent event) {
        getSizes(event);
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

                view.animate()
                        .x(endX)
                        .y(endY)
                        .setDuration(0)
                        .start();
                return false;
            case MotionEvent.ACTION_UP:
                long interval = System.currentTimeMillis() - startTime;
                float endX1, endY1;
                endX1 = getX();
                endY1 = getY();

                if (endX1 <= screenWidth/2 && endY1 <= screenHeight/2) {
                    if (endX1 <= endY1) {
                        endX1 = 0.0f + paddingLeft;
                    } else {
                        endY1 = 0.0f + paddingTop;
                    }
                } else if (endX1 > screenWidth/2 && endY1 <= screenHeight/2 ) {
                    if (screenWidth - endX1 <= endY1) {
                        endX1 = screenWidth - width - paddingRight;
                    } else {
                        endY1 = 0.0f + paddingTop;
                    }
                } else if (endX1 <= screenWidth/2 && endY1 > screenHeight/2) {
                    if (endX1 <= screenHeight - endY1) {
                        endX1 = 0.0f + paddingLeft;
                    } else {
                        endY1 = screenHeight - height - paddingBottom;
                    }
                } else {
                    if (screenWidth - endX1 <= screenHeight - endY1) {
                        endX1 = screenWidth - width - paddingRight;
                    } else {
                        endY1 = screenHeight - height - paddingBottom;
                    }
                }

                if (endX1 < 0) {
                    endX1 = 0.0f + paddingLeft;
                }

                if (endX1 > screenWidth - width) {
                    endX1 = screenWidth - width - paddingRight;
                }

                if (endY1 < 0) {
                    endY1 = 0.0f + paddingTop;
                }

                if (endY1 > screenHeight - height) {
                    endY1 = screenHeight - height - paddingBottom;
                }

                view.animate()
                        .x(endX1)
                        .y(endY1)
                        .setDuration(100)
                        .start();

                if (interval < 200) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void getSizes(MotionEvent event) {
        if (width > 0) {
            return;
        }

        containerRawX = event.getRawX() - event.getX();
        containerRawY = event.getRawY() - event.getY();
        width = getWidth();
        height = getHeight();

        minDX = width/2;
        minDY = height/2;

        View parent = (View)getParent();
        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();

        View v = (View)getParent();
        paddingLeft = v.getPaddingLeft() > 0 ? v.getPaddingLeft() : 0;
        paddingRight = v.getPaddingRight() > 0 ? v.getPaddingRight() : 0;
        paddingTop = v.getPaddingTop() > 0 ? v.getPaddingTop() : 0;
        paddingBottom = v.getPaddingBottom() > 0 ? v.getPaddingBottom() : 0;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
