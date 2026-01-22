package com.dismal.devicetest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class Imageflowadjust extends TestUnitActivity {
    private Sensor mOrientationSensor;
    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {
        int Zcount = 0;
        float lastZ = 0.0f;

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            float f = sensorEvent.values[1];
            float f2 = this.lastZ;
            if (f2 == 0.0f) {
                this.lastZ = f;
            } else if (f2 > 0.0f && f < 0.0f) {
                this.Zcount++;
                this.lastZ = f;
            } else if (this.lastZ < 0.0f && f > 0.0f) {
                this.Zcount++;
                this.lastZ = f;
            }
            Log.e("xx", "Zcount ==== " + this.Zcount);
            if (this.Zcount >= 5) {
                Intent intent = new Intent();
                intent.setClass(Imageflowadjust.this, TestCompassActivity.class);
                Imageflowadjust.this.startActivity(intent);
                Toast.makeText(Imageflowadjust.this.getApplicationContext(), "Adjust completed", 1).show();
            }
        }
    };
    private SensorManager mSensorManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.image_flow);
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_1), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_2), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_3), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_4), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_5), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_6), 200);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.calibrate_7), 200);
        animationDrawable.setOneShot(false);
        ((ImageView) findViewById(R.id.iv_flow)).setBackground(animationDrawable);
        animationDrawable.start();
        initServices();
    }

    private void initServices() {
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mOrientationSensor = this.mSensorManager.getDefaultSensor(3);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mOrientationSensor != null) {
            this.mSensorManager.unregisterListener(this.mOrientationSensorEventListener);
        }
        super.onPause();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Sensor sensor = this.mOrientationSensor;
        if (sensor != null) {
            this.mSensorManager.registerListener(this.mOrientationSensorEventListener, sensor, 1);
        }
    }
}
