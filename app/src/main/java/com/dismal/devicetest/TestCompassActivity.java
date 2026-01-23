package com.dismal.devicetest;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.Locale;

public class TestCompassActivity extends TestUnitActivity {
    private final float MAX_ROATE_DEGREE = 1.0f;
    TextView mAltitudeTextView;
    LinearLayout mAngleLayout;
    private boolean mChinease;
    View mCompassView;
    protected Runnable mCompassViewUpdater = new Runnable() {
        public void run() {
            TestCompassActivity testCompassActivity = TestCompassActivity.this;
            if (testCompassActivity.mPointer != null && !testCompassActivity.mStopDrawing) {
                if (TestCompassActivity.this.mDirection != TestCompassActivity.this.mTargetDirection) {
                    float access$200 = TestCompassActivity.this.mTargetDirection;
                    if (access$200 - TestCompassActivity.this.mDirection > 180.0f) {
                        access$200 -= 360.0f;
                    } else if (access$200 - TestCompassActivity.this.mDirection < -180.0f) {
                        access$200 += 360.0f;
                    }
                    float access$100 = access$200 - TestCompassActivity.this.mDirection;
                    if (Math.abs(access$100) > 1.0f) {
                        access$100 = access$100 > 0.0f ? 1.0f : -1.0f;
                    }
                    TestCompassActivity testCompassActivity2 = TestCompassActivity.this;
                    float unused = testCompassActivity2.mDirection = testCompassActivity2.normalizeDegree(testCompassActivity2.mDirection + ((access$200 - TestCompassActivity.this.mDirection) * TestCompassActivity.this.mInterpolator.getInterpolation(Math.abs(access$100) > 1.0f ? 0.4f : 0.3f)));
                    TestCompassActivity testCompassActivity3 = TestCompassActivity.this;
                    testCompassActivity3.mPointer.updateDirection(testCompassActivity3.mDirection);
                }
                TestCompassActivity.this.updateDirection();
                TestCompassActivity testCompassActivity4 = TestCompassActivity.this;
                testCompassActivity4.mHandler.postDelayed(testCompassActivity4.mCompassViewUpdater, 20);
            }
        }
    };
    private TestConfirm mConfirm = null;
    /* access modifiers changed from: private */
    public float mDirection;
    LinearLayout mDirectionLayout;
    protected final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public AccelerateInterpolator mInterpolator;
    private String mMode = "";
    private Sensor mOrientationSensor;
    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            TestCompassActivity testCompassActivity = TestCompassActivity.this;
            float unused = testCompassActivity.mTargetDirection = testCompassActivity.normalizeDegree(sensorEvent.values[0] * -1.0f);
        }
    };
    CompassView mPointer;
    private Sensor mPressureSensor;
    private SensorEventListener mPressureSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            TestCompassActivity testCompassActivity = TestCompassActivity.this;
            testCompassActivity.mAltitudeTextView.setText(testCompassActivity.getString(R.string.altitude, new Object[]{Integer.valueOf((int) testCompassActivity.calculateAltitude(sensorEvent.values[0]))}));
            TestCompassActivity testCompassActivity2 = TestCompassActivity.this;
            testCompassActivity2.mPressureTextView.setText(testCompassActivity2.getString(R.string.pressure, new Object[]{Float.valueOf(sensorEvent.values[0] / 10.0f)}));
        }
    };
    TextView mPressureTextView;
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public boolean mStopDrawing;
    /* access modifiers changed from: private */
    public float mTargetDirection;
    private Button mbuttonadjust;

    /* access modifiers changed from: private */
    public float calculateAltitude(float f) {
        return ((1013.25f - f) * 100.0f) / 12.7f;
    }

    /* access modifiers changed from: private */
    public float normalizeDegree(float f) {
        return (f + 720.0f) % 360.0f;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.compass_layout);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        initResources();
        initServices();
        this.mConfirm = (TestConfirm) findViewById(R.id.compass_confirm);
        this.mConfirm.setTestConfirm(this, TestCompassActivity.class, "TestCompassActivity", this.mMode, (String) null, (String) null, true);
        this.mbuttonadjust = (Button) findViewById(R.id.confirm_btn_adjust);
        this.mbuttonadjust.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TestCompassActivity.this, Imageflowadjust.class);
                TestCompassActivity.this.startActivity(intent);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Sensor sensor = this.mOrientationSensor;
        if (sensor != null) {
            this.mSensorManager.registerListener(this.mOrientationSensorEventListener, sensor, 1);
        }
        Sensor sensor2 = this.mPressureSensor;
        if (sensor2 != null) {
            this.mSensorManager.registerListener(this.mPressureSensorEventListener, sensor2, 1);
        }
        this.mStopDrawing = false;
        this.mHandler.postDelayed(this.mCompassViewUpdater, 20);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mStopDrawing = true;
        if (this.mOrientationSensor != null) {
            this.mSensorManager.unregisterListener(this.mOrientationSensorEventListener);
        }
        if (this.mPressureSensor != null) {
            this.mSensorManager.unregisterListener(this.mPressureSensorEventListener);
        }
    }

    private void initResources() {
        this.mDirection = 0.0f;
        this.mTargetDirection = 0.0f;
        this.mInterpolator = new AccelerateInterpolator();
        this.mStopDrawing = true;
        this.mChinease = TextUtils.equals(Locale.getDefault().getLanguage(), "zh");
        this.mCompassView = findViewById(R.id.view_compass);
        this.mPointer = (CompassView) findViewById(R.id.compass_pointer);
        this.mAltitudeTextView = (TextView) findViewById(R.id.textview_altitude);
        this.mPressureTextView = (TextView) findViewById(R.id.textview_pressure);
        this.mDirectionLayout = (LinearLayout) findViewById(R.id.layout_direction);
        this.mAngleLayout = (LinearLayout) findViewById(R.id.layout_angle);
        this.mPointer.setImageResource(this.mChinease ? R.mipmap.compass_cn : R.mipmap.compass);
    }

    private void initServices() {
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mOrientationSensor = this.mSensorManager.getDefaultSensor(3);
        this.mPressureSensor = this.mSensorManager.getDefaultSensor(6);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);
    }

    /* access modifiers changed from: private */
    public void updateDirection() {
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        this.mDirectionLayout.removeAllViews();
        this.mAngleLayout.removeAllViews();
        float normalizeDegree = normalizeDegree(this.mTargetDirection * -1.0f);
        ImageView imageView4 = null;
        if (normalizeDegree > 22.5f && normalizeDegree < 157.5f) {
            imageView2 = new ImageView(this);
            imageView2.setImageResource(this.mChinease ? R.mipmap.e_cn : R.mipmap.e);
            imageView2.setLayoutParams(layoutParams);
            imageView = null;
        } else if (normalizeDegree <= 202.5f || normalizeDegree >= 337.5f) {
            imageView2 = null;
            imageView = null;
        } else {
            ImageView imageView5 = new ImageView(this);
            imageView5.setImageResource(this.mChinease ? R.mipmap.w_cn : R.mipmap.w);
            imageView5.setLayoutParams(layoutParams);
            imageView = imageView5;
            imageView2 = null;
        }
        if (normalizeDegree > 112.5f && normalizeDegree < 247.5f) {
            ImageView imageView6 = new ImageView(this);
            imageView6.setImageResource(this.mChinease ? R.mipmap.s_cn : R.mipmap.s);
            imageView6.setLayoutParams(layoutParams);
            ImageView imageView7 = imageView6;
            imageView3 = null;
            imageView4 = imageView7;
        } else if (((double) normalizeDegree) < 67.5d || normalizeDegree > 292.5f) {
            imageView3 = new ImageView(this);
            imageView3.setImageResource(this.mChinease ? R.mipmap.n_cn : R.mipmap.n);
            imageView3.setLayoutParams(layoutParams);
        } else {
            imageView3 = null;
        }
        if (this.mChinease) {
            if (imageView2 != null) {
                this.mDirectionLayout.addView(imageView2);
            }
            if (imageView != null) {
                this.mDirectionLayout.addView(imageView);
            }
            if (imageView4 != null) {
                this.mDirectionLayout.addView(imageView4);
            }
            if (imageView3 != null) {
                this.mDirectionLayout.addView(imageView3);
            }
        } else {
            if (imageView4 != null) {
                this.mDirectionLayout.addView(imageView4);
            }
            if (imageView3 != null) {
                this.mDirectionLayout.addView(imageView3);
            }
            if (imageView2 != null) {
                this.mDirectionLayout.addView(imageView2);
            }
            if (imageView != null) {
                this.mDirectionLayout.addView(imageView);
            }
        }
        int i = (int) normalizeDegree;
        boolean z = false;
        if (i >= 100) {
            this.mAngleLayout.addView(getNumberImage(i / 100));
            i %= 100;
            z = true;
        }
        if (i >= 10 || z) {
            this.mAngleLayout.addView(getNumberImage(i / 10));
            i %= 10;
        }
        this.mAngleLayout.addView(getNumberImage(i));
        ImageView imageView8 = new ImageView(this);
        imageView8.setImageResource(R.mipmap.degree);
        imageView8.setLayoutParams(layoutParams);
        this.mAngleLayout.addView(imageView8);
    }

    private ImageView getNumberImage(int i) {
        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        switch (i) {
            case 0:
                imageView.setImageResource(R.mipmap.number_0);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.number_1);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.number_2);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.number_3);
                break;
            case 4:
                imageView.setImageResource(R.mipmap.number_4);
                break;
            case 5:
                imageView.setImageResource(R.mipmap.number_5);
                break;
            case 6:
                imageView.setImageResource(R.mipmap.number_6);
                break;
            case 7:
                imageView.setImageResource(R.mipmap.number_7);
                break;
            case 8:
                imageView.setImageResource(R.mipmap.number_8);
                break;
            case 9:
                imageView.setImageResource(R.mipmap.number_9);
                break;
        }
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}
