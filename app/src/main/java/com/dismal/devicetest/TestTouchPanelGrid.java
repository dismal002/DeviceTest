package com.dismal.devicetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import java.util.ArrayList;

public class TestTouchPanelGrid extends TestUnitActivity {
    /* access modifiers changed from: private */
    public int RECT_HEIGHT = 40;
    /* access modifiers changed from: private */
    public int RECT_WIDTH = 40;
    /* access modifiers changed from: private */
    public int last_rect_hight = 0;
    /* access modifiers changed from: private */
    public int last_rect_width = 0;
    private Handler mHandler = new TouchTestActivityHandle();
    public ArrayList<RectFill> mRect = new ArrayList<>();
    /* access modifiers changed from: private */
    public int mRectSize = 0;
    /* access modifiers changed from: private */
    public int num_h_g = 0;
    /* access modifiers changed from: private */
    public int num_s_g = 0;
    /* access modifiers changed from: private */
    public int screenHeight;
    /* access modifiers changed from: private */
    public int screenWidth;
    MyView v = null;

    class TouchTestActivityHandle extends Handler {
        TouchTestActivityHandle() {
        }

        public void handleMessage(Message message) {
            if (message.what == 0) {
                TestTouchPanelGrid.this.finish();
            }
        }
    }

    private void initDate() {
        int i = this.screenHeight;
        int i2 = this.RECT_HEIGHT;
        this.num_h_g = i / i2;
        int i3 = this.screenWidth;
        int i4 = this.RECT_WIDTH;
        this.num_s_g = i3 / i4;
        this.num_h_g--;
        this.num_s_g--;
        this.last_rect_width = i3 - (this.num_s_g * i4);
        this.last_rect_hight = i - (this.num_h_g * i2);
        Log.d("qsl", "========last_rect_hight======" + this.last_rect_hight + "==last_rect_width==" + this.last_rect_width);
    }

    private void initRectFill() {
        int i = 0;
        while (i < this.screenWidth) {
            RectFill rectFill = new RectFill(i, 0);
            rectFill.setRectFillFlag(false);
            this.mRect.add(rectFill);
            RectFill rectFill2 = new RectFill(i, this.RECT_HEIGHT * (this.num_h_g / 2));
            rectFill2.setRectFillFlag(false);
            this.mRect.add(rectFill2);
            RectFill rectFill3 = new RectFill(i, this.num_h_g * this.RECT_HEIGHT);
            rectFill3.setRectFillFlag(false);
            this.mRect.add(rectFill3);
            i += this.RECT_WIDTH;
        }
        int i2 = this.RECT_HEIGHT;
        while (i2 < this.screenHeight - this.RECT_HEIGHT) {
            RectFill rectFill4 = new RectFill(0, i2);
            rectFill4.setRectFillFlag(false);
            this.mRect.add(rectFill4);
            RectFill rectFill5 = new RectFill(this.RECT_WIDTH * (this.num_s_g / 2), i2);
            rectFill5.setRectFillFlag(false);
            this.mRect.add(rectFill5);
            RectFill rectFill6 = new RectFill(this.num_s_g * this.RECT_WIDTH, i2);
            rectFill6.setRectFillFlag(false);
            this.mRect.add(rectFill6);
            i2 += this.RECT_HEIGHT;
        }
        this.mRectSize = this.mRect.size();
    }

    public class MyView extends View {
        ArrayList<PT> curLine;
        private boolean mCurDown;
        private int mCurX;
        private int mCurY;
        public ArrayList<ArrayList<PT>> mLines = new ArrayList<>();
        private final Paint mPaint = new Paint();
        private final Paint mRectPaint;
        private final Paint mTargetPaint;

        public MyView(Context context) {
            super(context);
            this.mPaint.setARGB(255, 80, 80, 80);
            this.mRectPaint = new Paint();
            this.mRectPaint.setARGB(255, 0, 255, 0);
            this.mTargetPaint = new Paint();
            this.mTargetPaint.setAntiAlias(false);
            this.mTargetPaint.setARGB(255, 0, 0, 0);
            this.mTargetPaint.setStyle(Paint.Style.STROKE);
            this.mTargetPaint.setStrokeWidth(2.0f);
            int unused = TestTouchPanelGrid.this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int unused2 = TestTouchPanelGrid.this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            Log.d("qsl", "======MyView=====" + TestTouchPanelGrid.this.screenWidth + "===" + TestTouchPanelGrid.this.screenHeight + "===" + (TestTouchPanelGrid.this.screenHeight / 30));
            if (TestTouchPanelGrid.this.screenWidth == 480) {
                int unused3 = TestTouchPanelGrid.this.RECT_HEIGHT = 30;
                int unused4 = TestTouchPanelGrid.this.RECT_WIDTH = 30;
            } else if (TestTouchPanelGrid.this.screenWidth == 540) {
                int unused5 = TestTouchPanelGrid.this.RECT_HEIGHT = 34;
                int unused6 = TestTouchPanelGrid.this.RECT_WIDTH = 36;
            } else if (TestTouchPanelGrid.this.screenWidth == 640) {
                int unused7 = TestTouchPanelGrid.this.RECT_HEIGHT = 40;
                int unused8 = TestTouchPanelGrid.this.RECT_WIDTH = 38;
            } else if (TestTouchPanelGrid.this.screenWidth == 720) {
                int unused9 = TestTouchPanelGrid.this.RECT_HEIGHT = 40;
                int unused10 = TestTouchPanelGrid.this.RECT_WIDTH = 40;
            }
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            for (int i = 0; i < TestTouchPanelGrid.this.mRectSize; i++) {
                RectFill rectFill = TestTouchPanelGrid.this.mRect.get(i);
                if (rectFill.getRectFillFlag()) {
                    if (rectFill.y + TestTouchPanelGrid.this.RECT_HEIGHT > TestTouchPanelGrid.this.num_h_g * TestTouchPanelGrid.this.RECT_HEIGHT) {
                        int i2 = rectFill.x;
                        canvas.drawRect((float) i2, (float) rectFill.y, (float) (i2 + TestTouchPanelGrid.this.RECT_WIDTH), (float) (rectFill.y + TestTouchPanelGrid.this.last_rect_hight), this.mRectPaint);
                    } else if (rectFill.x + TestTouchPanelGrid.this.RECT_WIDTH > TestTouchPanelGrid.this.num_s_g * TestTouchPanelGrid.this.RECT_WIDTH) {
                        int i3 = rectFill.x;
                        canvas.drawRect((float) i3, (float) rectFill.y, (float) (i3 + TestTouchPanelGrid.this.last_rect_width), (float) (rectFill.y + TestTouchPanelGrid.this.RECT_HEIGHT), this.mRectPaint);
                    } else {
                        int i4 = rectFill.x;
                        canvas.drawRect((float) i4, (float) rectFill.y, (float) (i4 + TestTouchPanelGrid.this.RECT_WIDTH), (float) (rectFill.y + TestTouchPanelGrid.this.RECT_HEIGHT), this.mRectPaint);
                    }
                }
            }
            for (int i5 = 0; i5 < this.mLines.size(); i5++) {
                ArrayList arrayList = this.mLines.get(i5);
                float f = 0.0f;
                float f2 = 0.0f;
                for (int i6 = 0; i6 < arrayList.size(); i6++) {
                    PT pt = (PT) arrayList.get(i6);
                    if (i6 > 0) {
                        canvas.drawLine(f, f2, pt.x.floatValue(), pt.y.floatValue(), this.mTargetPaint);
                    }
                    f = pt.x.floatValue();
                    f2 = pt.y.floatValue();
                }
            }
            canvas.drawLine(0.0f, (float) TestTouchPanelGrid.this.RECT_HEIGHT, (float) TestTouchPanelGrid.this.screenWidth, (float) TestTouchPanelGrid.this.RECT_HEIGHT, this.mPaint);
            canvas.drawLine(0.0f, (float) (TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)), (float) TestTouchPanelGrid.this.screenWidth, (float) (TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)), this.mPaint);
            canvas.drawLine(0.0f, (float) ((TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)) + TestTouchPanelGrid.this.RECT_HEIGHT), (float) TestTouchPanelGrid.this.screenWidth, (float) ((TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)) + TestTouchPanelGrid.this.RECT_HEIGHT), this.mPaint);
            canvas.drawLine(0.0f, (float) (TestTouchPanelGrid.this.num_h_g * TestTouchPanelGrid.this.RECT_HEIGHT), (float) TestTouchPanelGrid.this.screenWidth, (float) (TestTouchPanelGrid.this.num_h_g * TestTouchPanelGrid.this.RECT_HEIGHT), this.mPaint);
            canvas.drawLine((float) TestTouchPanelGrid.this.RECT_WIDTH, 0.0f, (float) TestTouchPanelGrid.this.RECT_WIDTH, (float) TestTouchPanelGrid.this.screenHeight, this.mPaint);
            canvas.drawLine((float) (TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)), 0.0f, (float) (TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)), (float) TestTouchPanelGrid.this.screenHeight, this.mPaint);
            canvas.drawLine((float) ((TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)) + TestTouchPanelGrid.this.RECT_WIDTH), 0.0f, (float) ((TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)) + TestTouchPanelGrid.this.RECT_WIDTH), (float) TestTouchPanelGrid.this.screenHeight, this.mPaint);
            canvas.drawLine((float) (TestTouchPanelGrid.this.num_s_g * TestTouchPanelGrid.this.RECT_WIDTH), 0.0f, (float) (TestTouchPanelGrid.this.num_s_g * TestTouchPanelGrid.this.RECT_WIDTH), (float) TestTouchPanelGrid.this.screenHeight, this.mPaint);
            for (int i7 = 0; i7 <= TestTouchPanelGrid.this.num_s_g; i7++) {
                canvas.drawLine((float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), 0.0f, (float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), (float) TestTouchPanelGrid.this.RECT_WIDTH, this.mPaint);
                canvas.drawLine((float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), (float) (TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)), (float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), (float) ((TestTouchPanelGrid.this.RECT_HEIGHT * (TestTouchPanelGrid.this.num_h_g / 2)) + TestTouchPanelGrid.this.RECT_HEIGHT), this.mPaint);
                canvas.drawLine((float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), (float) (TestTouchPanelGrid.this.num_h_g * TestTouchPanelGrid.this.RECT_HEIGHT), (float) (TestTouchPanelGrid.this.RECT_WIDTH * i7), (float) TestTouchPanelGrid.this.screenHeight, this.mPaint);
            }
            for (int i8 = 0; i8 <= TestTouchPanelGrid.this.num_h_g; i8++) {
                canvas.drawLine(0.0f, (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), (float) TestTouchPanelGrid.this.RECT_HEIGHT, (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), this.mPaint);
                canvas.drawLine((float) (TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)), (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), (float) ((TestTouchPanelGrid.this.RECT_WIDTH * (TestTouchPanelGrid.this.num_s_g / 2)) + TestTouchPanelGrid.this.RECT_HEIGHT), (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), this.mPaint);
                canvas.drawLine((float) (TestTouchPanelGrid.this.num_s_g * TestTouchPanelGrid.this.RECT_WIDTH), (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), (float) TestTouchPanelGrid.this.screenWidth, (float) (TestTouchPanelGrid.this.RECT_HEIGHT * i8), this.mPaint);
            }
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.curLine = new ArrayList<>();
                this.mLines.add(this.curLine);
            }
            for (int i = 0; i < motionEvent.getHistorySize(); i++) {
                this.curLine.add(new PT(Float.valueOf(motionEvent.getHistoricalX(i)), Float.valueOf(motionEvent.getHistoricalY(i))));
            }
            this.curLine.add(new PT(Float.valueOf(motionEvent.getX()), Float.valueOf(motionEvent.getY())));
            if (action == 2) {
                this.mCurDown = true;
                this.mCurX = (int) motionEvent.getX();
                this.mCurY = (int) motionEvent.getY();
                TestTouchPanelGrid.this.judgeRectFill(this.mCurX, this.mCurY);
                invalidate();
            }
            Log.v("wst", "i====" + action);
            Log.v("wst", "allRectFilled()====" + TestTouchPanelGrid.this.allRectFilled());
            if (action == 1 && TestTouchPanelGrid.this.allRectFilled()) {
                TestTouchPanelGrid.this.popupWindow();
            }
            return true;
        }
    }

    public boolean allRectFilled() {
        for (int i = 0; i < this.mRectSize; i++) {
            if (!this.mRect.get(i).getRectFillFlag()) {
                return false;
            }
        }
        return true;
    }

    public void judgeRectFill(int i, int i2) {
        int i3;
        for (int i4 = 0; i4 < this.mRectSize; i4++) {
            RectFill rectFill = this.mRect.get(i4);
            int i5 = rectFill.x;
            if (i >= i5 && i <= i5 + this.RECT_WIDTH && i2 >= (i3 = rectFill.y) && i2 <= i3 + this.RECT_HEIGHT) {
                rectFill.setRectFillFlag(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.v = new MyView(this);
        setContentView(this.v);
        initDate();
        initRectFill();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public class PT {
        public Float x;
        public Float y;

        public PT(Float f, Float f2) {
            this.x = f;
            this.y = f2;
        }
    }

    public class RectFill {
        private boolean isFillFlag = false;
        public int x;
        public int y;

        public RectFill(int i, int i2) {
            this.x = i;
            this.y = i2;
        }

        public boolean getRectFillFlag() {
            return this.isFillFlag;
        }

        public void setRectFillFlag(boolean z) {
            this.isFillFlag = z;
        }
    }

    public void popupWindow() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.popup4touchpanel, (ViewGroup) null);
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -2, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        ((Button) inflate.findViewById(R.id.tp_btn_succeed)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (popupWindow.isShowing()) {
                    TestTouchPanelGrid.this.setResult(100);
                    popupWindow.dismiss();
                    TestTouchPanelGrid.this.finish();
                    return;
                }
                popupWindow.showAsDropDown(view);
            }
        });
        ((Button) inflate.findViewById(R.id.tp_btn_fail)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (popupWindow.isShowing()) {
                    TestTouchPanelGrid.this.setResult(50);
                    popupWindow.dismiss();
                    TestTouchPanelGrid.this.finish();
                    return;
                }
                popupWindow.showAsDropDown(view);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        popupWindow.showAsDropDown(inflate, (displayMetrics.widthPixels - inflate.getWidth()) / 2, ((displayMetrics.heightPixels - inflate.getHeight()) / 2) - 60);
    }
}
