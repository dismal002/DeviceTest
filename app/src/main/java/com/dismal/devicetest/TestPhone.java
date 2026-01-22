package com.dismal.devicetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.dismal.devicetest.ui.TestConfirm;

public class TestPhone extends TestUnitActivity implements View.OnClickListener {
    private Button btn_10010;
    private Button btn_10086;
    private Button btn_112;
    private Button dial_sim1;
    private Button dial_sim2;
    private TestConfirm mPhoneConfirm;
    private EditText phone_edit;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_phone);
        setTitle(R.string.but_test_phone);
        this.mPhoneConfirm = (TestConfirm) findViewById(R.id.phone_confirm);
        this.mPhoneConfirm.setTestConfirm(this, TestPhone.class, "testphone", "", "", "");
        this.phone_edit = (EditText) findViewById(R.id.phone_edit);
        this.btn_10010 = (Button) findViewById(R.id.phone_10010);
        this.btn_10086 = (Button) findViewById(R.id.phone_10086);
        this.btn_112 = (Button) findViewById(R.id.phone_112);
        this.btn_10010.setOnClickListener(this);
        this.btn_10086.setOnClickListener(this);
        this.btn_112.setOnClickListener(this);
        this.dial_sim1 = (Button) findViewById(R.id.phone_dial_sim1);
        this.dial_sim1.setOnClickListener(this);
        this.dial_sim2 = (Button) findViewById(R.id.phone_dial_sim2);
        this.dial_sim2.setOnClickListener(this);
    }

    private void makeCall(int i) {
        Intent intent;
        String obj = this.phone_edit.getText().toString();
        Log.i("lsz", "makeCall: " + obj);
        Uri parse = Uri.parse("tel:" + obj);
        if ("112".equals(obj)) {
            intent = new Intent("com.android.phone.EmergencyDialer.DIAL", parse);
        } else {
            intent = new Intent("android.intent.action.CALL", parse);
        }
        intent.putExtra("android.phone.extra.ACTUAL_NUMBER_TO_DIAL", obj);
        intent.putExtra("com.android.phone.extra.video", 0);
        intent.putExtra("com.android.phone.extra.slot", i);
        Log.i("ysm", "go");
        startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_10086 /*2131296352*/:
                this.phone_edit.setText(R.string.test_phone_10086);
                return;
            case R.id.phone_10010 /*2131296353*/:
                this.phone_edit.setText(R.string.test_phone_10010);
                return;
            case R.id.phone_112 /*2131296354*/:
                this.phone_edit.setText(R.string.test_phone_112);
                return;
            case R.id.phone_dial_sim1 /*2131296355*/:
                makeCall(0);
                return;
            case R.id.phone_dial_sim2 /*2131296356*/:
                makeCall(1);
                return;
            default:
                return;
        }
    }
}
