package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestWifi extends TestUnitActivity {
    private static final int PERMISSION_REQUEST_CODE = 124;
    /* access modifiers changed from: private */
    public static boolean bStopThread = false;
    private static Hashtable<String, Integer> mHashtable = new Hashtable<>();
    private boolean bBakupStatus = false;
    private boolean permissionsGranted = false;
    final BroadcastReceiver mBroadCast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("csh wif ", "csh action" + action);
            if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                if (intent.getIntExtra("wifi_state", 0) == 3) {
                    new myThread().start();
                }
            } else if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                TestWifi testWifi = TestWifi.this;
                List unused = testWifi.mWifiList = testWifi.mWifiManager.getScanResults();
                Message message = new Message();
                if (TestWifi.this.mWifiList != null) {
                    message.what = 1;
                    Log.e("TestWiFi", "mWifiList.size()-->" + TestWifi.this.mWifiList.size());
                    for (int i = 0; i < TestWifi.this.mWifiList.size(); i++) {
                        String str = ((ScanResult) TestWifi.this.mWifiList.get(i)).SSID;
                        int i2 = ((ScanResult) TestWifi.this.mWifiList.get(i)).level;
                        Log.e("TestWiFi", i + "--ssid-->" + str + ",level-->" + i2);
                        TestWifi.this.addWifiInfoItem(str, i2);
                    }
                    TestWifi.this.mMyHandler.sendMessage(message);
                    return;
                }
                message.what = 2;
                TestWifi.this.mMyHandler.sendMessage(message);
            }
        }
    };
    private TestConfirm mConfirm = null;
    private Intent mIntent = null;
    private IntentFilter mIntentFilter = null;
    private LinearLayout mLinearLayout = null;
    private String mMode = "";
    /* access modifiers changed from: private */
    public myHandler mMyHandler = new myHandler();
    private String mSignalText = "";
    private TextView mTv = null;
    private WifiInfo mWifiInfo = null;
    /* access modifiers changed from: private */
    public List<ScanResult> mWifiList;
    /* access modifiers changed from: private */
    public WifiManager mWifiManager = null;
    private int mWifiState = -1;
    private boolean mWifiscann = false;

    /* access modifiers changed from: private */
    public void addWifiInfoItem(String str, int i) {
        mHashtable.put(str, Integer.valueOf(i));
    }

    /* access modifiers changed from: private */
    public void createAllItems() {
        this.mTv.setVisibility(8);
        this.mLinearLayout.removeAllViews();
        Map.Entry[] sortedHashtable = getSortedHashtable(mHashtable);
        for (int i = 0; i < sortedHashtable.length; i++) {
            String obj = sortedHashtable[i].getKey().toString();
            int parseInt = Integer.parseInt(sortedHashtable[i].getValue().toString());
            if (!obj.contains("NVRAM WARNING")) {
                createItem(obj, parseInt);
            }
        }
        this.mLinearLayout.invalidate();
    }

    private void createItem(String str, int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = (displayMetrics.widthPixels / 4) + 30;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(1);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        textView.setTextSize(18.0f);
        textView.setText(str);
        linearLayout.addView(textView);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout2.setOrientation(0);
        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(new LinearLayout.LayoutParams(i2, -1));
        textView2.setPadding(4, 0, 0, 0);
        textView2.setTextColor(-16764024);
        new StringBuilder();
        String str2 = this.mSignalText;
        textView2.setText(str2 + i);
        linearLayout2.addView(textView2);
        ProgressBar progressBar = new ProgressBar(this, (AttributeSet) null, 16842872);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(displayMetrics.widthPixels - i2, -1));
        progressBar.setProgress(0);
        setProgressBarStyle(progressBar);
        progressBar.setProgress(i + 100);
        progressBar.setMax(100);
        linearLayout2.addView(progressBar);
        linearLayout.addView(linearLayout2);
        this.mLinearLayout.addView(linearLayout);
    }

    public static Map.Entry[] getSortedHashtable(Hashtable hashtable) {
        Set entrySet = hashtable.entrySet();
        Map.Entry[] entryArr = (Map.Entry[]) entrySet.toArray(new Map.Entry[entrySet.size()]);
        Arrays.sort(entryArr, new Comparator<Map.Entry>() {
            public int compare(Map.Entry entry, Map.Entry entry2) {
                return new Integer(Integer.parseInt(entry2.getValue().toString())).compareTo(new Integer(Integer.parseInt(entry.getValue().toString())));
            }
        });
        return entryArr;
    }

    private void initView() {
        this.mLinearLayout = (LinearLayout) findViewById(R.id.wifi_content_id);
        this.mTv = (TextView) findViewById(R.id.wifi_info_id);
        this.mConfirm = (TestConfirm) findViewById(R.id.wifi_confirm);
        this.mConfirm.setTestConfirm(this, TestWifi.class, "WiFi", this.mMode, (String) null, (String) null);
        this.mTv.setVisibility(0);
        this.mTv.setText(getResources().getString(R.string.test_wifi_opening));
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.mBroadCast, this.mIntentFilter);
        this.mSignalText = getResources().getString(R.string.test_wifi_signal);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        this.bBakupStatus = this.mWifiManager.isWifiEnabled();
    }

    private void setProgressBarStyle(ProgressBar progressBar) {
        LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable[] drawableArr = new Drawable[layerDrawable.getNumberOfLayers()];
        int numberOfLayers = layerDrawable.getNumberOfLayers();
        for (int i = 0; i < numberOfLayers; i++) {
            drawableArr[i] = layerDrawable.getDrawable(i);
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#FF00EA15"), Color.parseColor("#FFFDE32B"), Color.parseColor("#FF5052A7"), Color.parseColor("#FFEE0000")});
            gradientDrawable.setShape(0);
            gradientDrawable.setGradientType(0);
            gradientDrawable.setCornerRadii(new float[]{1.08422758E9f, 1.08422758E9f, 1.08422758E9f, 1.08422758E9f, 1.08422758E9f, 1.08422758E9f, 1.08422758E9f, 1.08422758E9f});
            drawableArr[i] = new ClipDrawable(gradientDrawable, 3, 1);
        }
        progressBar.setProgressDrawable(new LayerDrawable(drawableArr));
    }

    /* access modifiers changed from: private */
    public void setText(String str) {
        this.mTv.setVisibility(0);
        this.mTv.setText(str);
    }

    public boolean isWifiAlive(Context context) {
        return this.mWifiManager.isWifiEnabled();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.mMode = intent.getExtras().getString("mode");
        }
        setContentView(R.layout.test_wifi);
        initView();
        setTitle(R.string.but_test_wifi);
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            permissionsGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true;
            } else {
                mTv.setText("Location permission required for Wi-Fi scanning. Please grant permission in settings.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        boolean z;
        super.onDestroy();
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null && !(z = this.bBakupStatus)) {
            wifiManager.setWifiEnabled(z);
        }
        if (this.mIntentFilter != null) {
            unregisterReceiver(this.mBroadCast);
        }
    }

    public void onPause() {
        super.onPause();
        bStopThread = true;
    }

    public void onResume() {
        super.onResume();
        bStopThread = false;
        if (!permissionsGranted) {
            mTv.setText("Waiting for location permission...");
            return;
        }
        if (!isWifiAlive(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mTv.setText("Please enable Wi-Fi manually. Android 10+ restricts apps from toggling Wi-Fi.");
            } else {
                try {
                    this.mWifiManager.setWifiEnabled(true);
                } catch (SecurityException e) {
                    Log.e("TestWifi", "SecurityException enabling Wi-Fi", e);
                    mTv.setText("Cannot enable Wi-Fi. Please enable manually.");
                }
            }
        } else if (!this.mWifiscann) {
            this.mWifiscann = true;
            new myThread().start();
        }
    }

    class myThread extends Thread {
        myThread() {
        }

        public void run() {
            while (!TestWifi.bStopThread) {
                try {
                    Thread.sleep(1000);
                    if (ActivityCompat.checkSelfPermission(TestWifi.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        TestWifi.this.mWifiManager.startScan();
                    } else {
                        Log.e("TestWifi", "Missing location permission for Wi-Fi scan");
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                } catch (SecurityException e) {
                    Log.e("TestWifi", "SecurityException during Wi-Fi scan", e);
                    return;
                }
            }
        }
    }

    class myHandler extends Handler {
        public myHandler() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                TestWifi.this.createAllItems();
            } else if (i == 2) {
                TestWifi testWifi = TestWifi.this;
                testWifi.setText(testWifi.getResources().getString(R.string.test_wifi_no_network));
            }
        }
    }
}
