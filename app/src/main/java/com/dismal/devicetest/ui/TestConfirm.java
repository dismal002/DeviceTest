package com.dismal.devicetest.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.dismal.devicetest.R;

public class TestConfirm extends LinearLayout {
    private Context mContext;
    private Button mFail = null;
    private boolean mIsExistConfimWidget = true;
    private Activity mOwner = null;
    private Class<?> mOwnerClass = null;
    private String mOwnerName = "";
    private String mParamKey = null;
    private String mParamVal = null;
    private Button mSuccess = null;
    private String mTestMode = "";

    public TestConfirm(Context context) {
        super(context);
        this.mContext = context;
    }

    public TestConfirm(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: private */
    public void handleClick(boolean z) {
        Log.e("lsz", "handleClick-->" + z);
        int i = z ? 100 : 50;
        Activity activity = this.mOwner;
        if (activity != null) {
            activity.setResult(i);
            this.mOwner.finish();
        }
    }

    public void initView() {
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.test_confirm, this);
        this.mFail = (Button) findViewById(R.id.confirm_btn_fail);
        this.mFail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TestConfirm.this.handleClick(false);
            }
        });
        this.mSuccess = (Button) findViewById(R.id.confirm_btn_succeed);
        this.mSuccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TestConfirm.this.handleClick(true);
            }
        });
    }

    public void setTestConfirm(Activity activity) {
        this.mOwner = activity;
        initView();
    }

    public void setTestConfirm(Activity activity, Class<?> cls, String str, String str2, String str3, String str4) {
        this.mOwner = activity;
        this.mOwnerClass = cls;
        this.mOwnerName = str;
        this.mTestMode = str2;
        this.mParamKey = str3;
        this.mParamVal = str4;
        initView();
    }

    public void setTestConfirm(Activity activity, Class<?> cls, String str, String str2, String str3, String str4, boolean z) {
        this.mIsExistConfimWidget = z;
        setTestConfirm(activity, cls, str, str2, str3, str4);
    }
}
