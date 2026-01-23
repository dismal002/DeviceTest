package com.dismal.devicetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.dismal.devicetest.ui.TestConfirm;

public class TestTouchPanel extends TestUnitActivity {
    private int bit_height = 0;
    private int bit_width = 0;
    private TestConfirm mConfirm = null;
    /* access modifiers changed from: private */
    public GestureOverlayView mGov = null;
    private Intent mIntent = null;
    private String mMode = "";
    /* access modifiers changed from: private */
    public TouchPanelView mPanelView = null;
    private int mScreenWidth = 540;
    private Bitmap mTouchPot = null;
    private int mWifiState = -1;

    private void initGesture() {
        this.mGov = (GestureOverlayView) findViewById(R.id.gestures_id);
        this.mGov.setGestureColor(-65536);
        this.mGov.setGestureStrokeType(1);
        this.mGov.addOnGestureListener(new MListener(this));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            intent.getExtras().getString("mode");
        }
        Log.e("lsz", "paneltest--oncreate");
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.test_touchpanel);
        this.mScreenWidth = new DisplayMetrics().widthPixels;
        initView();
    }

    private void initView() {
        this.mConfirm = (TestConfirm) findViewById(R.id.confirm_layout);
        if (this.mConfirm != null) {
            this.mConfirm.setTestConfirm(this, TestTouchPanel.class, "TestTouchPanel", this.mMode, (String) null, (String) null, false);
        }
        
        this.mTouchPot = BitmapFactory.decodeResource(getResources(), R.drawable.touch_pot);
        this.bit_width = this.mTouchPot != null ? this.mTouchPot.getWidth() : 0;
        this.bit_height = this.mTouchPot != null ? this.mTouchPot.getHeight() : 0;
        
        initGesture();
        ViewGroup container = (ViewGroup) findViewById(R.id.touchpanel_container);
        this.mPanelView = new TouchPanelView(this);
        if (container != null) {
            container.addView(this.mPanelView, 0); // Add behind gesture overlay
        }
    }

    /* access modifiers changed from: private */
    public void setConfirmDialog() {
        GestureOverlayView gestureOverlayView = this.mGov;
        if (gestureOverlayView != null) {
            gestureOverlayView.cancelClearAnimation();
            this.mGov.clear(true);
            this.mGov.cancelLongPress();
            this.mGov.removeAllOnGestureListeners();
        }
        createDialog(this, "").show();
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return super.onKeyLongPress(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return super.onKeyUp(i, keyEvent);
    }

    class MListener implements GestureOverlayView.OnGestureListener {
        private TestTouchPanel mtt;

        public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        public MListener(TestTouchPanel testTouchPanel) {
            this.mtt = testTouchPanel;
        }

        public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            TestTouchPanel.this.mPanelView.refreshView((int) motionEvent.getX(), (int) motionEvent.getY());
        }

        public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            TestTouchPanel.this.mPanelView.refreshView((int) motionEvent.getX(), (int) motionEvent.getY());
        }

        public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                if (TestTouchPanel.this.mGov != null) {
                    TestTouchPanel.this.mGov.clear(false);
                }
                if (gestureOverlayView != null) {
                    gestureOverlayView.clear(false);
                }
            }
            TestTouchPanel.this.mPanelView.refreshView((int) motionEvent.getX(), (int) motionEvent.getY());
        }
    }

    public Dialog createDialog(Context context, String str) {
        Log.d("liuzhicheng", "Utils         createDialog  ");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.test_touchpanel_result));
        builder.setTitle(context.getResources().getString(R.string.but_test_touchpanel));
        builder.setPositiveButton(context.getResources().getString(R.string.btn_succeed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TestTouchPanel.this.setResult(100);
                dialogInterface.dismiss();
                TestTouchPanel.this.finish();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.btn_fail), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TestTouchPanel.this.setResult(50);
                dialogInterface.dismiss();
                TestTouchPanel.this.finish();
            }
        });
        return builder.create();
    }

    public class TouchPanelView extends View {
        private boolean[] bTouch;
        private int item_height = 0;
        private int item_width = 0;
        private Activity mActivity = null;
        private int mBackColor = -16777216;
        private int mColumn = 16;
        private Bitmap mDrawingBitmap = null;
        private Bitmap mLocalBitmap;
        private Paint mPaint;
        private int mPrecise = 20;
        private int mRow = 0;
        private int mTotal = 0;
        private int pic_height = 0;
        private int pic_width = 0;
        private int screen_height = 0;
        private int screen_width = 0;

        private boolean isTouchValidRegion(int i, int i2) {
            return true;
        }

        public TouchPanelView(Activity activity) {
            super(activity);
            this.mActivity = TestTouchPanel.this;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.screen_width = displayMetrics.widthPixels;
            this.screen_height = displayMetrics.heightPixels;
            int i = this.screen_width;
            if (i <= 480) {
                this.mLocalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.touchpanel_480);
            } else if (i >= 720) {
                this.mLocalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.touchpanel_720);
            } else {
                this.mLocalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.touchpanel_540);
            }
            this.item_width = this.mLocalBitmap.getWidth();
            this.item_height = this.mLocalBitmap.getHeight();
            this.mColumn = this.screen_width / this.item_width;
            this.mRow = this.screen_height / this.item_height;
            this.mTotal = this.mColumn * this.mRow;
            this.bTouch = new boolean[this.mTotal];
            for (int i2 = 0; i2 < this.mTotal; i2++) {
                this.bTouch[i2] = false;
            }
            init_picture_info();
            this.mPaint = new Paint();
        }

        private int getHitItem(int i, int i2) {
            int i3 = i / this.item_width;
            int i4 = i2 / this.item_height;
            int i5 = this.mColumn;
            if (i3 >= i5) {
                i3 = i5 - 1;
            }
            int i6 = this.mRow;
            if (i4 >= i6) {
                i4 = i6 - 1;
            }
            return (this.mColumn * i4) + i3;
        }

        private void init_picture_info() {
            int width = this.mLocalBitmap.getWidth();
            int height = this.mLocalBitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale((float) (this.item_width / width), (float) (this.item_height / height));
            Log.e("liuzhicheng", "width-->" + width + ",height-->" + height);
            this.mDrawingBitmap = Bitmap.createBitmap(this.mLocalBitmap, 0, 0, width, height, matrix, true);
        }

        public boolean isTouchFinish() {
            int i = 0;
            boolean z = false;
            while (i < this.mTotal - 1) {
                boolean[] zArr = this.bTouch;
                boolean z2 = zArr[i];
                i++;
                if (z2 != zArr[i]) {
                    return false;
                }
                z = true;
            }
            return z;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.mPaint.setColor(this.mBackColor);
            this.mPaint.setAntiAlias(true);
            canvas.drawRect(0.0f, 0.0f, (float) (this.screen_width - 1), (float) (this.screen_height - 1), this.mPaint);
            Log.e("lsz", "mtotal-->" + this.mTotal);
            for (int i = 0; i < this.mTotal; i++) {
                if (!this.bTouch[i]) {
                    int i2 = this.mColumn;
                    float f = (float) ((i % i2) * this.item_width);
                    float f2 = (float) ((i / i2) * this.item_height);
                    Log.e("lsz", "left-->" + f + ",top-->" + f2);
                    canvas.drawBitmap(this.mDrawingBitmap, f, f2, this.mPaint);
                }
            }
        }

        public void refreshView(int i, int i2) {
            if (isTouchValidRegion(i, i2)) {
                int hitItem = getHitItem(i, i2);
                if (hitItem >= 0 && hitItem < this.mTotal) {
                    this.bTouch[hitItem] = true;
                    if (isTouchFinish()) {
                        invalidate();
                        TestTouchPanel.this.setConfirmDialog();
                        return;
                    }
                }
                invalidate();
            }
        }
    }
}
