package com.dismal.devicetest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import com.dismal.devicetest.ui.TestConfirm;

public class TestBacklight extends TestUnitActivity {
    private boolean mAutoLevel = true;
    private View mBG = null;
    private TestConfirm mConfirm = null;
    private String mMode = "";
    private SeekBar mSeekBar = null;

    private void initView() {
        this.mBG = findViewById(R.id.bk_bg);
        this.mBG.setBackgroundResource(R.drawable.bk_bg);
        this.mConfirm = (TestConfirm) findViewById(R.id.backlight_confirm);
        this.mSeekBar = (SeekBar) findViewById(R.id.bk_seek);
        this.mConfirm.setTestConfirm(this, TestBacklight.class, "Backlight", this.mMode, (String) null, (String) null);
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TestBacklight.setBacklightLevel(TestBacklight.this, i + 20);
            }
        });
    }

    private void restoreBacklight() {
        if (this.mAutoLevel) {
            startAutoBrightness(this);
        }
    }

    private void saveBacklight() {
        Log.i("ysm", "TestBacklight >>> saveBacklight");
        this.mAutoLevel = isAutoBrightness(getContentResolver());
    }

    public static void setBacklightLevel(Activity activity, int i) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.screenBrightness = Float.valueOf((float) i).floatValue() * 0.003921569f;
        activity.getWindow().setAttributes(attributes);
    }

    private void setBacklightLevelBySeek() {
        int progress = this.mSeekBar.getProgress();
        stopAutoBrightness(this);
        setBacklightLevel(this, progress);
    }

    public static void startAutoBrightness(Activity activity) {
        // Settings.System.putInt requires WRITE_SETTINGS permission and user approval
        // Use Settings.System.canWrite() to check if we can write
        if (android.provider.Settings.System.canWrite(activity)) {
            try {
                Settings.System.putInt(activity.getContentResolver(), "screen_brightness_mode", 1);
            } catch (SecurityException e) {
                android.util.Log.e("TestBacklight", "Cannot write settings", e);
            }
        } else {
            // Request permission from user
            android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        }
    }

    public boolean isAutoBrightness(ContentResolver contentResolver) {
        try {
            Settings.System.getInt(contentResolver, "screen_brightness_mode");
            return false;
        } catch (Settings.SettingNotFoundException e) {
            while (true) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sendBroadcast(new Intent("com.curvedservice.stop.receiver"));
        this.isAutoTestMode = getIntent().getBooleanExtra("autotest", false);
        setContentView(R.layout.test_backlight);
        setTitle(R.string.but_test_backlight);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        restoreBacklight();
        sendBroadcast(new Intent("com.curvedservice.start.receiver"));
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        setBacklightLevelBySeek();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        saveBacklight();
        setBacklightLevelBySeek();
    }

    public void stopAutoBrightness(Activity activity) {
        // Settings.System.putInt requires WRITE_SETTINGS permission and user approval
        if (android.provider.Settings.System.canWrite(activity)) {
            try {
                Settings.System.putInt(activity.getContentResolver(), "screen_brightness_mode", 0);
            } catch (SecurityException e) {
                android.util.Log.e("TestBacklight", "Cannot write settings", e);
            }
        }
    }
}
