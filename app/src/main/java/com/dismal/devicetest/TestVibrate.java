package com.dismal.devicetest;

import android.os.Bundle;
import android.os.Vibrator;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.Timer;
import java.util.TimerTask;

public class TestVibrate extends TestUnitActivity {
    private TestConfirm mConfirm = null;
    private String mMode = "";
    private Timer mTimer = null;
    private Vibrator mVibrator = null;

    private void initView() {
        this.mConfirm = (TestConfirm) findViewById(R.id.vib_confirm);
        this.mConfirm.setTestConfirm(this, TestVibrate.class, "Vibrate", this.mMode, (String) null, (String) null);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mMode = extras.getString("mode");
        }
        setContentView(R.layout.test_vibrate);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        initView();
        setTitle(R.string.but_test_vibrate);
        startTimer();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        stopVibrate();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        startTimer();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        stopVibrate();
    }

    public void startTimer() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    TestVibrate.this.startVibrate();
                }
            }, 0, 2000);
        }
    }

    public void startVibrate() {
        this.mVibrator.cancel();
        this.mVibrator.vibrate(1000);
    }

    public void stopVibrate() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
        }
        this.mTimer = null;
        this.mVibrator.cancel();
    }
}
