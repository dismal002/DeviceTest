package com.dismal.devicetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestUnitActivity extends AppCompatActivity {
    protected boolean isAutoTestMode = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isAutoTestMode = getIntent().getBooleanExtra("autotest", false);
    }

    public void onAttachedToWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.flags |= Integer.MIN_VALUE;
        window.setAttributes(attributes);
        super.onAttachedToWindow();
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 4 && this.isAutoTestMode) {
            Log.e("lsz", "OnKeyUp--backkey");
            return true;
        } else if (i == 3) {
            return true;
        } else {
            return super.onKeyUp(i, keyEvent);
        }
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (i == 4 && this.isAutoTestMode) {
            Toast.makeText(this, getResources().getString(R.string.exit_auto_test), 0).show();
            sendBroadcast(new Intent("com.devicetest.STOP_AUTO_TEST"));
            finish();
        }
        return super.onKeyLongPress(i, keyEvent);
    }
}
