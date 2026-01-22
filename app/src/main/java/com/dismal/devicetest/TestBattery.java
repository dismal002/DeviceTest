package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestBattery extends TestUnitActivity {
    private TestConfirm mConfirm = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            TestBattery.this.updateBatteryStats();
            sendEmptyMessageDelayed(1, 1000);
        }
    };
    /* access modifiers changed from: private */
    public TextView mHealthText = null;
    private Intent mIntent = null;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                int intExtra = intent.getIntExtra("plugged", 0);
                if (intExtra == 0) {
                    TestBattery.this.mPlugText.setText(R.string.battery_info_power_unplugged);
                } else if (intExtra == 2) {
                    TestBattery.this.mPlugText.setText(R.string.battery_info_power_usb);
                } else {
                    TestBattery.this.mPlugText.setText(R.string.battery_info_power_ac);
                }
                int intExtra2 = intent.getIntExtra("level", 0);
                TestBattery.this.mLevelText.setText(intExtra2 + "");
                int intExtra3 = intent.getIntExtra("scale", 0);
                TextView access$300 = TestBattery.this.mScaleText;
                access$300.setText(intExtra3 + "");
                int intExtra4 = intent.getIntExtra("voltage", 0);
                TestBattery.this.mVolText.setText(intExtra4 + " " + TestBattery.this.getResources().getString(R.string.battery_info_voltage_units));
                int intExtra5 = intent.getIntExtra("temperature", 0);
                String string = TestBattery.this.getResources().getString(R.string.battery_info_temperature_units);
                TestBattery.this.mTempText.setText((intExtra5 / 10) + "." + (intExtra5 % 10) + " " + string);
                new StringBuilder().append("");
                TestBattery.this.mTechText.setText(intent.getStringExtra("technology"));
                int intExtra6 = intent.getIntExtra("status", 2);
                Log.e("TestPower", "status--->" + intent.getIntExtra("status", 1));
                if (intExtra6 == 2) {
                    TestBattery.this.mStatusText.setText(R.string.battery_info_status_charging);
                } else if (intExtra6 == 4) {
                    TestBattery.this.mStatusText.setText(R.string.battery_info_status_not_charging);
                } else if (intExtra6 == 5) {
                    TestBattery.this.mStatusText.setText(R.string.battery_info_status_full);
                }
                if (intExtra == 0) {
                    try {
                        TestBattery.this.mStatusText.setText(R.string.battery_info_status_not_charging);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int intExtra7 = intent.getIntExtra("health", 1);
                if (intExtra7 == 2) {
                    TestBattery.this.mHealthText.setText(R.string.battery_info_health_good);
                    return;
                }
                Log.e("TestPower", "healthStatus--->" + intExtra7);
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView mLevelText = null;
    private String mMode = "";
    /* access modifiers changed from: private */
    public TextView mPlugText = null;
    /* access modifiers changed from: private */
    public TextView mScaleText = null;
    /* access modifiers changed from: private */
    public TextView mStatusText = null;
    /* access modifiers changed from: private */
    public TextView mTechText = null;
    /* access modifiers changed from: private */
    public TextView mTempText = null;
    private TextView mTimeText = null;
    /* access modifiers changed from: private */
    public TextView mVolText = null;

    private void initView() {
        setTitle(R.string.but_test_power);
        this.mConfirm = (TestConfirm) findViewById(R.id.power_confirm);
        this.mConfirm.setTestConfirm(this, TestBattery.class, "Power", this.mMode, (String) null, (String) null);
    }

    /* access modifiers changed from: private */
    public void updateBatteryStats() {
        this.mTimeText.setText(DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_power);
        initView();
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    }

    public void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        unregisterReceiver(this.mIntentReceiver);
    }

    public void onResume() {
        super.onResume();
        this.mStatusText = (TextView) findViewById(R.id.power_status_id);
        this.mPlugText = (TextView) findViewById(R.id.power_plug_id);
        this.mLevelText = (TextView) findViewById(R.id.power_level_id);
        this.mScaleText = (TextView) findViewById(R.id.power_scale_id);
        this.mHealthText = (TextView) findViewById(R.id.power_health_id);
        this.mTempText = (TextView) findViewById(R.id.power_temp_id);
        this.mTechText = (TextView) findViewById(R.id.power_tech_id);
        this.mTimeText = (TextView) findViewById(R.id.power_time_id);
        this.mVolText = (TextView) findViewById(R.id.power_vol_id);
        this.mHandler.sendEmptyMessageDelayed(1, 1000);
        registerReceiver(this.mIntentReceiver, this.mIntentFilter);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.getAction();
        return super.onTouchEvent(motionEvent);
    }
}
