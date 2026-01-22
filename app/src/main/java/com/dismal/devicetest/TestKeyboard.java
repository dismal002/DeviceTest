package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestKeyboard extends TestUnitActivity {
    /* access modifiers changed from: private */
    public int keyFound = 0;
    private TestConfirm mConfirm = null;
    private Intent mIntent = null;
    KeyMap[] mKeyTable;
    /* access modifiers changed from: private */
    public TextView mKeyTip = null;
    /* access modifiers changed from: private */
    public GridView mKeylayout = null;
    private String mMode = "";
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.android.powerkeytest")) {
                int indexByKeycode = TestKeyboard.this.getIndexByKeycode(26);
                if (TestKeyboard.this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                    TestKeyboard.access$108(TestKeyboard.this);
                    TestKeyboard.this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
                }
            } else if (action.equals("com.android.recentkeytest")) {
                int indexByKeycode2 = TestKeyboard.this.getIndexByKeycode(187);
                if (TestKeyboard.this.mKeylayout.getChildAt(indexByKeycode2).getVisibility() == 0) {
                    TestKeyboard.access$108(TestKeyboard.this);
                    TestKeyboard.this.mKeylayout.getChildAt(indexByKeycode2).setVisibility(4);
                }
            }
            int access$100 = TestKeyboard.this.keyFound;
            TestKeyboard testKeyboard = TestKeyboard.this;
            if (access$100 == testKeyboard.mKeyTable.length) {
                testKeyboard.mKeyTip.setText(TestKeyboard.this.getResources().getString(R.string.test_keyboard_finish));
            }
        }
    };

    static /* synthetic */ int access$108(TestKeyboard testKeyboard) {
        int i = testKeyboard.keyFound;
        testKeyboard.keyFound = i + 1;
        return i;
    }

    public TestKeyboard() {
        // Can't read system properties without system app, default to standard keys
        if (false) {
            this.mKeyTable = new KeyMap[]{new KeyMap(24, "Volume_Up"), new KeyMap(25, "Volume_Down"), new KeyMap(187, "Recent"), new KeyMap(3, "Home"), new KeyMap(4, "Back"), new KeyMap(26, "Power"), new KeyMap(27, "Camera")};
            return;
        }
        this.mKeyTable = new KeyMap[]{new KeyMap(24, "Volume_Up"), new KeyMap(25, "Volume_Down"), new KeyMap(187, "Recent"), new KeyMap(3, "Home"), new KeyMap(4, "Back"), new KeyMap(26, "Power"), new KeyMap(27, "Camera")};
    }

    private void initView() {
        setTitle(R.string.but_test_keyboard);
        this.mKeyTip = (TextView) findViewById(R.id.key_tip);
        this.mConfirm = (TestConfirm) findViewById(R.id.keyboard_confirm);
        this.mConfirm.setTestConfirm(this, TestKeyboard.class, "Keyboard", this.mMode, (String) null, (String) null);
        this.mConfirm.setFocusable(false);
        this.mConfirm.findViewById(R.id.confirm_btn_succeed).setFocusable(false);
        this.mConfirm.findViewById(R.id.confirm_btn_fail).setFocusable(false);
        this.mKeylayout = (GridView) findViewById(R.id.keyboard_gridview_id);
        this.mKeylayout.setAdapter(new TextAdapter(this));
        this.mKeylayout.setEnabled(false);
    }

    public int getIndexByKeycode(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            KeyMap[] keyMapArr = this.mKeyTable;
            if (i2 < keyMapArr.length) {
                if (keyMapArr[i2].getKeyCode() == i) {
                    i3 = i2;
                }
                i2++;
            } else {
                Log.e("TestKeyboard", "flag -- >" + i3);
                return i3;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.mMode = intent.getExtras().getString("mode");
        }
        requestWindowFeature(1);
        setContentView(R.layout.test_keyboard);
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.powerkeytest");
        intentFilter.addAction("com.android.recentkeytest");
        registerReceiver(this.mReceiver, intentFilter);
    }

    public void onResume() {
        super.onResume();
        // SystemProperties.set() requires system app, no-op for regular apps
    }

    public void onPause() {
        super.onPause();
        // SystemProperties.set() requires system app, no-op for regular apps
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        Log.e("TestKeyboard", "keyCode->" + i);
        int indexByKeycode = getIndexByKeycode(i);
        Log.e("TestKeyboard", "i---->" + indexByKeycode);
        if (i == 3) {
            Log.e("TestKeyboard", "home key pressed");
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
        } else if (i == 187) {
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
            Log.e("TestKeyboard", "menu key pressed");
        } else if (i == 24) {
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
            Log.e("TestKeyboard", "KEYCODE_VOLUME_UP key pressed");
        } else if (i == 25) {
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
            Log.e("TestKeyboard", "KEYCODE_VOLUME_DOWN key pressed");
        } else if (i == 4) {
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
            Log.e("TestKeyboard", "KEYCODE_BACK key pressed");
        } else if (i == 27) {
            if (this.mKeylayout.getChildAt(indexByKeycode).getVisibility() == 0) {
                this.keyFound++;
                this.mKeylayout.getChildAt(indexByKeycode).setVisibility(4);
            }
            Log.e("TestKeyboard", "KEYCODE_CAMERA key pressed");
        }
        if (this.keyFound == this.mKeyTable.length) {
            this.mKeyTip.setText(getResources().getString(R.string.test_keyboard_finish));
        }
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 24 || i == 25 || i == 27) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
        }
        return super.onKeyLongPress(i, keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.getAction();
        return super.onTouchEvent(motionEvent);
    }

    private class TextAdapter extends BaseAdapter {
        private Context mContext;

        public long getItemId(int i) {
            return (long) i;
        }

        public TextAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return TestKeyboard.this.mKeyTable.length;
        }

        public Object getItem(int i) {
            return TestKeyboard.this.mKeyTable[i];
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView;
            if (view == null) {
                textView = new TextView(this.mContext);
                textView.setTextSize(20.0f);
            } else {
                textView = (TextView) view;
            }
            textView.setText(TestKeyboard.this.mKeyTable[i].getKeyName());
            return textView;
        }
    }

    class KeyMap {
        int keyCode;
        String keyName;

        public KeyMap(int i, String str) {
            this.keyCode = i;
            this.keyName = str;
        }

        public int getKeyCode() {
            return this.keyCode;
        }

        public String getKeyName() {
            return this.keyName;
        }
    }
}
