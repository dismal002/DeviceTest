package com.dismal.devicetest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.ArrayList;
import java.util.List;

public class TestBluetooth extends TestUnitActivity {
    private BluetoothAdapter mAdapter;
    private TestConfirm mConfirm;
    /* access modifiers changed from: private */
    public ArrayList<String> mDevicesInfo;
    private BroadcastReceiver mReceiver;
    private TextView tv = null;

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_bluetooth);
        setTitle(R.string.but_test_bluetooth);
        this.tv = (TextView) findViewById(R.id.bt_info_id);
        this.mConfirm = (TestConfirm) findViewById(R.id.bt_confirm);
        this.mConfirm.setTestConfirm(this, TestBluetooth.class, "", "", "", "");
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mReceiver = new MBlueToothReceiver();
        this.mDevicesInfo = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        registerReceiver(this.mReceiver, intentFilter);

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(android.Manifest.permission.BLUETOOTH_SCAN);
            permissions.add(android.Manifest.permission.BLUETOOTH_CONNECT);
        }
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        } else {
            startBluetoothOperations();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startBluetoothOperations();
            } else {
                tv.setText("Permissions denied. Bluetooth test cannot proceed.");
            }
        }
    }

    private void startBluetoothOperations() {
        if (mAdapter != null) {
            try {
                if (!mAdapter.isEnabled()) {
                    mAdapter.enable();
                    this.tv.setText(R.string.test_bt_opening);
                } else {
                    new myThread().start();
                }
            } catch (SecurityException e) {
                Log.e("TestBluetooth", "SecurityException enabling bluetooth", e);
                tv.setText("Permission error: " + e.getMessage());
            }
        } else {
            tv.setText("Bluetooth not available on this device");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             startBluetoothOperations();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mAdapter != null) {
                this.mAdapter.disable();
            }
        } catch (SecurityException e) {
            Log.e("TestBluetooth", "SecurityException disabling bluetooth", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
            Log.e("TestBluetooth", "Error unregistering receiver", e);
        }
    }

    class myThread extends Thread {
        myThread() {
        }

        public void run() {
            try {
                Thread.sleep(1000);
                TestBluetooth.this.searchBt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void searchBt() {
        try {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }
            this.mAdapter.startDiscovery();
        } catch (SecurityException e) {
            Log.e("TestBluetooth", "SecurityException in searchBt", e);
            tv.setText("Permission error: " + e.getMessage());
        }
    }

    public void showDeviceList(boolean z) {
        int size = this.mDevicesInfo.size();
        StringBuffer stringBuffer = new StringBuffer();
        if (!z) {
            stringBuffer.append(getResources().getString(R.string.test_bt_searching));
            stringBuffer.append("\n");
        }
        if (size == 0) {
            stringBuffer.append(getResources().getString(R.string.test_bt_no_devices));
            stringBuffer.append("\n");
        }
        for (int i = 0; i < size; i++) {
            stringBuffer.append(this.mDevicesInfo.get(i));
            stringBuffer.append("\n");
        }
        this.tv.setText(stringBuffer.toString());
    }

    class MBlueToothReceiver extends BroadcastReceiver {
        MBlueToothReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("lsz", "action-->" + action);
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                try {
                    if (bluetoothDevice.getBondState() != 12) {
                        String name = bluetoothDevice.getName();
                        if (name == null) name = "Unknown Device";
                        TestBluetooth.this.mDevicesInfo.add(name + "-->" + bluetoothDevice.getAddress());
                        TestBluetooth.this.showDeviceList(false);
                    }
                } catch (SecurityException e) {
                    Log.e("TestBluetooth", "SecurityException in FOUND receiver", e);
                }
            } else if (!"android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                if (!"android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                }
            } else if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0) == 12) {
                new myThread().start();
            }
        }
    }
}
