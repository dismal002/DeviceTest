package com.dismal.devicetest;

import android.content.Intent;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.Timer;
import java.util.TimerTask;

public class TestSensor extends TestUnitActivity implements SensorEventListener {
    private Sensor accelerometerSensor;
    private ImageView arrowImageView;
    private Bundle bundle = null;
    private ImageView compassImageView;
    private long curTime = 0;
    private float currentDegree = 0.0f;
    private TextView currentvalue_text;
    private Sensor distanceSensor;
    private TextView distance_text;
    private TextView info_text;
    private long lastTime = 0;
    private float last_x;
    private Sensor lightSensor;
    private TextView lightlevel_text;
    private TestConfirm mConfirm = null;
    private Intent mIntent = null;
    private boolean mIsDeviceOpen = false;
    private String mMode = "";
    private LinearLayout mSensorLayout;
    private SensorManager mSensorManager;
    private Timer mTimer = null;
    Button msetth = null;
    private TextView precision_text;
    private RotateAnimation ra = null;
    private boolean rest = false;
    private int sensor_type;
    private TextView sensor_type_text;
    private TextView threshold_text;
    private TextView threshold_text_reset;
    private float x;
    private TextView xyz_text;
    private float y;
    private float z;

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void initSensorView(Bundle bundle2) {
        this.currentvalue_text = (TextView) findViewById(R.id.text_current);
        this.threshold_text = (TextView) findViewById(R.id.text_threshold);
        this.threshold_text_reset = (TextView) findViewById(R.id.text_threshold_reset);
        this.sensor_type_text = (TextView) findViewById(R.id.text_sensor_type);
        this.msetth = (Button) findViewById(R.id.threshold_set_btn);
        this.xyz_text = (TextView) findViewById(R.id.text_xyz);
        this.arrowImageView = (ImageView) findViewById(R.id.img_arrow);
        this.compassImageView = (ImageView) findViewById(R.id.img_compass);
        this.info_text = (TextView) findViewById(R.id.text_info);
        this.precision_text = (TextView) findViewById(R.id.text_precision);
        this.lightlevel_text = (TextView) findViewById(R.id.text_light_level);
        this.distance_text = (TextView) findViewById(R.id.text_distance);
        this.mConfirm = (TestConfirm) findViewById(R.id.sensor_confirm);
        this.mSensorLayout = (LinearLayout) findViewById(R.id.sensor_layout);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        setComponentViableAndSensorListener(bundle2.getString("sensor_type"));
        this.msetth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    public void handleAcceleRometer() {
        float f = this.x;
        float f2 = 0.0f;
        boolean z2 = true;
        if (f < -3.5f || f > 3.5f) {
            float f3 = this.x;
            if (f3 > 3.5f) {
                f2 = 90.0f;
            } else if (f3 <= -3.5f) {
                f2 = 270.0f;
            } else {
                z2 = false;
            }
        } else if (this.y < -2.5f) {
            f2 = 180.0f;
        }
        if (z2) {
            rotateArrow(f2);
        }
    }

    public void handleDistance() {
        if (((double) this.x) == 0.0d) {
            this.distance_text.setText(R.string.test_sensor_handin);
            getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bkcolor_blue));
            return;
        }
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bkcolor_black));
        this.distance_text.setText(getResources().getString(R.string.test_sensor_handout));
    }

    public void handleLight() {
        this.curTime = System.currentTimeMillis();
        float abs = this.curTime != this.lastTime ? (Math.abs(this.x - this.last_x) / ((float) (this.curTime - this.lastTime))) * 100.0f : 0.0f;
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.test_sensor_lmd));
        sb.append(Float.toString(abs));
        sb.append("\n");
        sb.append(getResources().getString(R.string.test_sensor_data));
        sb.append(Float.toString(this.x) + "  lux");
        this.precision_text.setText(sb.toString());
        this.last_x = this.x;
        this.lastTime = this.curTime;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getResources().getString(R.string.test_sensor_light_level));
        sb2.append(Float.toString(this.x) + "  lux");
        this.lightlevel_text.setText(sb2.toString());
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle2) {
        super.onCreate(bundle2);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.mMode = intent.getExtras().getString("mode");
            if (this.mMode == null) {
                this.mMode = "";
            }
        }
        setContentView(R.layout.test_sensor);
        if (extras != null) {
            initSensorView(extras);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mSensorManager.unregisterListener(this);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        startTimer();
        int i = this.sensor_type;
        if (i == 0) {
            SensorManager sensorManager = this.mSensorManager;
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(1), 1);
        } else if (i == 1) {
            SensorManager sensorManager2 = this.mSensorManager;
            sensorManager2.registerListener(this, sensorManager2.getDefaultSensor(1), 1);
        } else if (i == 2) {
            SensorManager sensorManager3 = this.mSensorManager;
            sensorManager3.registerListener(this, sensorManager3.getDefaultSensor(5), 0);
        } else if (i == 3) {
            SensorManager sensorManager4 = this.mSensorManager;
            sensorManager4.registerListener(this, sensorManager4.getDefaultSensor(8), 1);
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr = sensorEvent.values;
        this.x = fArr[0];
        if (fArr.length > 1) {
            this.y = fArr[1];
        }
        float[] fArr2 = sensorEvent.values;
        if (fArr2.length > 2) {
            this.z = fArr2[2];
        }
        int type = sensorEvent.sensor.getType();
        Log.e("liuzhicheng", "x--->" + this.x + ",y==" + this.y + ",z-->" + this.z);
        if (type == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.test_sensor_x));
            sb.append(Float.toString(this.x));
            sb.append(getResources().getString(R.string.test_sensor_y));
            sb.append(Float.toString(this.y));
            sb.append(getResources().getString(R.string.test_sensor_z));
            sb.append(Float.toString(this.z));
            String sb2 = sb.toString();
            Log.e("lsz", "sb--->" + sb.toString());
            this.xyz_text.setText(sb2);
            handleAcceleRometer();
        } else if (type == 5) {
            handleLight();
        } else if (type == 8) {
            handleDistance();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
        }
        this.mTimer = null;
        this.mSensorManager.unregisterListener(this);
        super.onStop();
    }

    public void rotateArrow(float f) {
        if (this.arrowImageView != null) {
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postRotate(f, (float) (this.arrowImageView.getWidth() / 2), (float) (this.arrowImageView.getHeight() / 2));
            this.arrowImageView.setScaleType(ImageView.ScaleType.MATRIX);
            this.arrowImageView.setImageMatrix(matrix);
        }
    }

    public void setComponentViableAndSensorListener(String str) {
        this.mSensorLayout.removeAllViews();
        if (str.equals("type_distance")) {
            this.sensor_type = 3;
            setTitle(R.string.but_test_distance_sensor);
            this.mConfirm.setTestConfirm(this, TestSensor.class, "DistanceSensor", this.mMode, "sensor_type", "type_distance");
            this.mSensorLayout.addView(this.info_text);
            this.info_text.setText(R.string.test_sensor_putinabove2);
            this.distanceSensor = this.mSensorManager.getDefaultSensor(8);
            this.distanceSensor.getVersion();
            Log.e("lsz", "sensor vendor-->" + this.distanceSensor.getVendor() + " sensor version-->" + this.distanceSensor.getVersion());
            this.mSensorLayout.addView(this.distance_text);
            this.distance_text.setVisibility(0);
        } else if (str.equals("type_gravity")) {
            this.sensor_type = 0;
            this.mSensorLayout.addView(this.xyz_text);
            this.mSensorLayout.addView(this.info_text);
            setTitle(R.string.but_test_gravity_sensor);
            this.mSensorLayout.addView(this.arrowImageView);
            this.arrowImageView.setVisibility(0);
            this.mConfirm.setTestConfirm(this, TestSensor.class, "GravitySensor", this.mMode, "sensor_type", "type_gravity");
            this.info_text.setText(R.string.test_sensor_gravity_info);
            this.accelerometerSensor = this.mSensorManager.getDefaultSensor(1);
        } else if (str.equals("type_magnetic")) {
            this.sensor_type = 1;
            setTitle(R.string.but_test_magnetic_sensor);
            this.mSensorLayout.addView(this.compassImageView);
            this.compassImageView.setVisibility(0);
            this.mConfirm.setTestConfirm(this, TestSensor.class, "MagneticSensor", this.mMode, "sensor_type", "type_magnetic");
            this.info_text.setText(getResources().getString(R.string.test_sensor_znzshow));
            this.mSensorManager.getDefaultSensor(3);
        } else if (str.equals("type_light")) {
            this.sensor_type = 2;
            setTitle(R.string.but_test_light_sensor);
            this.mSensorLayout.addView(this.info_text);
            this.info_text.setText(R.string.test_sensor_putinlight);
            this.info_text.setVisibility(0);
            this.mSensorLayout.addView(this.precision_text);
            this.precision_text.setVisibility(0);
            this.mSensorLayout.addView(this.lightlevel_text);
            this.lightlevel_text.setVisibility(0);
            this.mConfirm.setTestConfirm(this, TestSensor.class, "LightSensor", this.mMode, "sensor_type", "type_light");
            this.lightSensor = this.mSensorManager.getDefaultSensor(5);
        }
    }

    public void startTimer() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    new Message().what = 0;
                }
            }, 0, 500);
        }
    }
}
