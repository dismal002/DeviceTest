package com.dismal.devicetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dismal.devicetest.ui.TestConfirm;

public class TestFMRadio extends TestUnitActivity {
    private TestConfirm fmConfirm;
    private Button openFM;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_fm1);
        setTitle(R.string.but_test_fm);
        this.openFM = (Button) findViewById(R.id.but_openfm);
        this.fmConfirm = (TestConfirm) findViewById(R.id.fm_confirm);
        this.fmConfirm.setTestConfirm(this, TestFMRadio.class, "testsoundrecorder", "", "", "");
        this.openFM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName("com.android.fmradio", "com.android.fmradio.FmMainActivity");
                TestFMRadio.this.startActivityForResult(intent, 12345);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
