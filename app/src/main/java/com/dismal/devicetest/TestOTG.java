package com.dismal.devicetest;

import android.content.Context;
import android.os.Bundle;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbDevice;
import java.util.HashMap;
import java.util.Iterator;
import android.text.TextUtils;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestOTG extends TestUnitActivity {
    /* access modifiers changed from: private */
    public Context mContext;
    private final Object mListener = null; // Removed StorageEventListener
    private UsbManager mUsbManager;
    /* access modifiers changed from: private */
    public TextView mTextView = null;
    private TestConfirm otgConfirm;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_otg);
        setTitle(R.string.test_otg);
        this.mTextView = (TextView) findViewById(R.id.tv_test_otg);
        this.otgConfirm = (TestConfirm) findViewById(R.id.test_otg);
        this.otgConfirm.setTestConfirm(this, TestOTG.class, "test_otg", "", "", "");
        this.mContext = this;
        this.mUsbManager = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        checkOTG();
    }

    private void checkOTG() {
        if (this.mUsbManager != null) {
            HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
            showInfo(!deviceList.isEmpty(), this.mTextView);
        }
    }

    public boolean isUSBOTGPlaceholder() {
        return false;
    }

    /* access modifiers changed from: private */
    public void showInfo(boolean z, TextView textView) {
        Context context;
        if (textView != null && (context = this.mContext) != null) {
            if (z) {
                textView.setText(context.getString(R.string.test_usb_otg_insert));
            } else {
                textView.setText(context.getString(R.string.test_usb_otg_not_insert));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        checkOTG();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }
}
