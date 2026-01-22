package com.dismal.devicetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestMic extends TestUnitActivity {
    private Button mChangesoundrecorder = null;
    private TestConfirm mConfirm = null;
    private Intent mIntent = null;
    private String mMode = "";
    private TextView recorder_uri;

    private void initView() {
        setTitle(R.string.but_test_mic);
        this.mChangesoundrecorder = (Button) findViewById(R.id.but_changesoundrecorder);
        this.mConfirm = (TestConfirm) findViewById(R.id.msound_confirm);
        this.mConfirm.setTestConfirm(this, TestMic.class, "Camera", this.mMode, (String) null, (String) null);
        this.recorder_uri = (TextView) findViewById(R.id.soundrecorder_uri);
        this.mChangesoundrecorder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TestMic.this, SimpleSoundRecorderActivity.class);
                TestMic.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_mic2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mMode = extras.getString("mode");
        }
        initView();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri uri;
        Log.e("lsz", "requestCode-->" + i + ",resultCode->data-->" + intent);
        if (i == 1234 && i2 == -1 && (uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI")) != null) {
            this.recorder_uri.setText(String.valueOf(uri));
        }
        super.onActivityResult(i, i2, intent);
    }
}
