package com.dismal.devicetest;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dismal.devicetest.ui.TestConfirm;

public class TestLED extends TestUnitActivity {
    private final int NOTIFICATION_ID = 1000;
    private NotificationManager mNotificationManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_led);
        setTitle(R.string.but_test_led);
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        ((Button) findViewById(R.id.red)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        ((Button) findViewById(R.id.green)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        ((Button) findViewById(R.id.blue)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        ((TestConfirm) findViewById(R.id.led_confirm)).setTestConfirm(this, TestLED.class, "LED", "", (String) null, "");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager != null) {
            notificationManager.cancel(1000);
            this.mNotificationManager = null;
        }
    }
}
