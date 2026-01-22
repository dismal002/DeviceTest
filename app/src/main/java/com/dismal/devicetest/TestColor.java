package com.dismal.devicetest;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestColor extends TestUnitActivity {
    private int currentState = 0;
    private int mNum = 0;
    private TestConfirm tc = null;
    private TextView tv = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.test_color);
        this.tv = (TextView) findViewById(R.id.test_color_text1);
        this.tc = (TestConfirm) findViewById(R.id.color_confirm);
        this.tc.setTestConfirm(this, TestColor.class, "LCD", "", (String) null, "");
    }

    private void changeColor(int i) {
        int i2 = i % 5;
        this.tv.setText("");
        Log.e("TestColor", "-->" + i2);
        if (i2 == 0) {
            this.tv.setBackgroundColor(-65536);
        } else if (i2 == 1) {
            this.tv.setBackgroundColor(-16711936);
        } else if (i2 == 2) {
            this.tv.setBackgroundColor(-16776961);
        } else if (i2 == 3) {
            this.tv.setBackgroundColor(-16777216);
        } else if (i2 == 4) {
            this.tv.setBackgroundColor(-1);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            this.mNum++;
            changeColor(this.mNum);
            int i = this.mNum;
            if (i > 0 && i % 5 == 0) {
                this.tc.setVisibility(0);
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
