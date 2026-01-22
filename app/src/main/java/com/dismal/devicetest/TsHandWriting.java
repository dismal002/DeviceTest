package com.dismal.devicetest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

public class TsHandWriting extends AppCompatActivity {
    MyView mView = null;
    /* access modifiers changed from: private */
    public int mZoom = 1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.v("@M_EM/TouchScreen/HW", "onCreate start");
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        this.mView = new MyView(this);
        this.mView.setSystemUiVisibility(4096);
        setContentView(this.mView);
        Log.v("@M_EM/TouchScreen/HW", "onCreate success");
    }

    public void onResume() {
        super.onResume();
        // Writing to system files requires root, so this is disabled for regular apps
        // The functionality still works, just without logging to system files
    }

    public void onPause() {
        Log.v("@M_EM/TouchScreen/HW", "-->onPause");
        // Writing to system files requires root, so this is disabled for regular apps
        // The functionality still works, just without logging to system files
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Clean Table.");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (1 == menuItem.getItemId()) {
            this.mView.clear();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class PT {
        public Float mX;
        public Float mY;

        public PT(Float f, Float f2) {
            this.mX = f;
            this.mY = f2;
        }
    }

    public class MyView extends View {
        private boolean mCurDown;
        private ArrayList<PT> mCurLine;
        private float mCurPressure;
        private int mCurWidth;
        private int mCurX;
        private int mCurY;
        private int mHeaderBottom;
        private ArrayList<ArrayList<PT>> mLines = new ArrayList<>();
        private final Paint mPaint;
        private final Paint mTargetPaint;
        private final Paint mTextBackgroundPaint;
        private final Paint mTextLevelPaint;
        private final Paint.FontMetricsInt mTextMetrics = new Paint.FontMetricsInt();
        private final Paint mTextPaint;
        private VelocityTracker mVelocity;
        private ArrayList<VelocityTracker> mVelocityList = new ArrayList<>();

        public MyView(Context context) {
            super(context);
            new DisplayMetrics();
            DisplayMetrics displayMetrics = TsHandWriting.this.getApplicationContext().getResources().getDisplayMetrics();
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            if ((480 == i && 800 == i2) || (800 == i && 480 == i2)) {
                int unused = TsHandWriting.this.mZoom = 2;
            }
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTextSize((float) (TsHandWriting.this.mZoom * 10));
            this.mTextPaint.setARGB(255, 0, 0, 0);
            this.mTextBackgroundPaint = new Paint();
            this.mTextBackgroundPaint.setAntiAlias(false);
            this.mTextBackgroundPaint.setARGB(128, 255, 255, 255);
            this.mTextLevelPaint = new Paint();
            this.mTextLevelPaint.setAntiAlias(false);
            this.mTextLevelPaint.setARGB(192, 255, 0, 0);
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            this.mPaint.setARGB(255, 255, 255, 255);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(2.0f);
            this.mTargetPaint = new Paint();
            this.mTargetPaint.setAntiAlias(false);
            this.mTargetPaint.setARGB(192, 0, 255, 0);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(1.0f);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.mTextPaint.getFontMetricsInt(this.mTextMetrics);
            Paint.FontMetricsInt fontMetricsInt = this.mTextMetrics;
            this.mHeaderBottom = (-fontMetricsInt.ascent) + fontMetricsInt.descent + 2;
            Log.v("@M_EM/TouchScreen/HW", "Metrics: ascent=" + this.mTextMetrics.ascent + " descent=" + this.mTextMetrics.descent + " leading=" + this.mTextMetrics.leading + " top=" + this.mTextMetrics.top + " bottom=" + this.mTextMetrics.bottom);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            int i;
            PT pt;
            Canvas canvas2 = canvas;
            int width = getWidth() / 5;
            int i2 = (-this.mTextMetrics.ascent) + 1;
            float f = (float) this.mHeaderBottom;
            float f2 = f;
            canvas.drawRect(0.0f, 0.0f, (float) (width - 1), f2, this.mTextBackgroundPaint);
            float f3 = (float) i2;
            canvas2.drawText("X: " + this.mCurX, 1.0f, f3, this.mTextPaint);
            float f4 = (float) width;
            int i3 = width * 2;
            canvas.drawRect(f4, 0.0f, (float) (i3 + -1), f2, this.mTextBackgroundPaint);
            canvas2.drawText("Y: " + this.mCurY, (float) (width + 1), f3, this.mTextPaint);
            float f5 = (float) i3;
            int i4 = width * 3;
            float f6 = f5;
            canvas.drawRect(f6, 0.0f, (float) (i4 - 1), f2, this.mTextBackgroundPaint);
            canvas.drawRect(f6, 0.0f, ((this.mCurPressure * f4) + f5) - 1.0f, f2, this.mTextLevelPaint);
            Log.w("@M_EM/TouchScreen/HW", "mCurPressure = " + this.mCurPressure);
            canvas2.drawText("Pres: " + this.mCurPressure, (float) (i3 + 1), f3, this.mTextPaint);
            int i5 = width * 4;
            canvas.drawRect((float) i4, 0.0f, (float) (i5 + -1), f2, this.mTextBackgroundPaint);
            VelocityTracker velocityTracker = this.mVelocity;
            int i6 = 0;
            canvas2.drawText("XVel: " + (velocityTracker == null ? 0 : (int) (Math.abs(velocityTracker.getXVelocity()) * 1000.0f)), (float) (i4 + 1), f3, this.mTextPaint);
            canvas.drawRect((float) i5, 0.0f, (float) getWidth(), f, this.mTextBackgroundPaint);
            VelocityTracker velocityTracker2 = this.mVelocity;
            canvas2.drawText("YVel: " + (velocityTracker2 == null ? 0 : (int) (Math.abs(velocityTracker2.getYVelocity()) * 1000.0f)), (float) (i5 + 1), f3, this.mTextPaint);
            int size = this.mLines.size();
            int i7 = 0;
            while (i7 < size) {
                ArrayList arrayList = this.mLines.get(i7);
                this.mPaint.setARGB(255, i6, 255, 255);
                int size2 = arrayList.size();
                float f7 = 0.0f;
                float f8 = 0.0f;
                for (int i8 = i6; i8 < size2; i8++) {
                    PT pt2 = (PT) arrayList.get(i8);
                    if (i8 > 0) {
                        pt = pt2;
                        canvas.drawLine(f8, f7, pt2.mX.floatValue(), pt2.mY.floatValue(), this.mTargetPaint);
                        canvas2.drawPoint(f8, f7, this.mPaint);
                    } else {
                        pt = pt2;
                    }
                    f8 = pt.mX.floatValue();
                    f7 = pt.mY.floatValue();
                }
                float f9 = f7;
                VelocityTracker velocityTracker3 = this.mVelocityList.get(i7);
                if (velocityTracker3 == null) {
                    canvas2.drawPoint(f8, f9, this.mPaint);
                    i = 0;
                } else {
                    i = 0;
                    this.mPaint.setARGB(255, 255, 0, 0);
                    canvas.drawLine(f8, f9, f8 + (velocityTracker3.getXVelocity() * 16.0f), f9 + (velocityTracker3.getYVelocity() * 16.0f), this.mPaint);
                }
                if (this.mCurDown) {
                    canvas.drawLine(0.0f, (float) this.mCurY, (float) getWidth(), (float) this.mCurY, this.mTargetPaint);
                    int i9 = this.mCurX;
                    canvas.drawLine((float) i9, 0.0f, (float) i9, (float) getHeight(), this.mTargetPaint);
                    int i10 = (int) (this.mCurPressure * 255.0f);
                    this.mPaint.setARGB(255, i10, 128, 255 - i10);
                    canvas2.drawPoint((float) this.mCurX, (float) this.mCurY, this.mPaint);
                    canvas2.drawCircle((float) this.mCurX, (float) this.mCurY, (float) this.mCurWidth, this.mPaint);
                }
                i7++;
                i6 = i;
            }
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mVelocity = VelocityTracker.obtain();
                this.mVelocityList.add(this.mVelocity);
                this.mCurLine = new ArrayList<>();
                this.mLines.add(this.mCurLine);
            }
            this.mVelocity.addMovement(motionEvent);
            this.mVelocity.computeCurrentVelocity(1);
            int historySize = motionEvent.getHistorySize();
            boolean z = false;
            for (int i = 0; i < historySize; i++) {
                this.mCurLine.add(new PT(Float.valueOf(motionEvent.getHistoricalX(i)), Float.valueOf(motionEvent.getHistoricalY(i))));
            }
            this.mCurLine.add(new PT(Float.valueOf(motionEvent.getX()), Float.valueOf(motionEvent.getY())));
            if (action == 0 || action == 2) {
                z = true;
            }
            this.mCurDown = z;
            this.mCurX = (int) motionEvent.getX();
            this.mCurY = (int) motionEvent.getY();
            this.mCurPressure = motionEvent.getPressure();
            Log.w("@M_EM/TouchScreen/HW", "event.getPressure()= " + this.mCurPressure);
            this.mCurWidth = (int) (motionEvent.getSize() * ((float) (getWidth() / 3)));
            invalidate();
            return true;
        }

        public void clear() {
            Iterator<ArrayList<PT>> it = this.mLines.iterator();
            while (it.hasNext()) {
                it.next().clear();
            }
            this.mLines.clear();
            this.mVelocityList.clear();
            invalidate();
        }
    }
}
