package com.dismal.devicetest;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.system.Os;
import android.system.StructUtsname;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import com.dismal.devicetest.SystemPropertiesHelper;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestVersion extends TestUnitActivity {

    private TextView android_version;
    private TextView base_version;
    private TextView board;
    private TextView build_date;
    private TextView camera_model;
    private TextView chip;
    private TextView imei1;
    private TextView imei2;
    private TextView kernel_version;
    private TestConfirm mConfirm;
    private LinearLayout mLayout;
    private TextView model;
    private TextView motherboard;
    private TextView screen_size;
    private TextView sn1;
    private TextView sn2;
    private TextView ram_info;
    private TelephonyManager telephonyManager;

    @Override
    public void onCreate(Bundle bundle) {
        String str;
        String str2;
        String str3;
        super.onCreate(bundle);
        setContentView(R.layout.test_version);
        setTitle(R.string.but_test_version);
        this.telephonyManager = (TelephonyManager) getSystemService("phone");
        this.mConfirm = (TestConfirm) findViewById(R.id.version_confirm);
        this.mConfirm.setTestConfirm(this, TestVersion.class, "TestVersion", "", "test_version", "", true);
        this.mLayout = (LinearLayout) findViewById(R.id.version_layout);
        this.model = (TextView) findViewById(R.id.model);
        // Use actual device model from Build class
        String str4 = Build.MODEL;
        if (str4 == null || str4.isEmpty()) {
            str4 = Build.DEVICE;
        }
        if (str4 == null || str4.isEmpty()) {
            str4 = SystemPropertiesHelper.get("ro.product.model", "Unknown");
        }
        this.model.setText(str4);
        this.chip = (TextView) findViewById(R.id.chip);
        // Use actual hardware/CPU information from system properties
        str = SystemPropertiesHelper.get("ro.board.platform", "");
        if (str == null || str.isEmpty()) {
            str = SystemPropertiesHelper.get("ro.hardware", "");
        }
        if (str == null || str.isEmpty()) {
            str = Build.HARDWARE;
        }
        if (str == null || str.isEmpty()) {
            str = SystemPropertiesHelper.get("ro.chipname", "");
        }
        if (str == null || str.isEmpty()) {
            str = "Unknown";
        }
        Log.d("yzw", "CPU/Hardware: " + str);
        this.chip.setText(SystemPropertiesHelper.getCpuInfo());
        this.board = (TextView) findViewById(R.id.board);
        // Use actual board information from Build class
        String str5 = Build.BOARD;
        if (str5 == null || str5.isEmpty()) {
            str5 = SystemPropertiesHelper.get("ro.product.board", "");
        }
        if (str5 == null || str5.isEmpty()) {
            str5 = Build.HARDWARE;
        }
        if (str5 == null || str5.isEmpty()) {
            str5 = "Unknown";
        }
        this.board.setText(str5);
        this.camera_model = (TextView) findViewById(R.id.camera_mode);
        this.camera_model.setText(getCameraSize());
        this.kernel_version = (TextView) findViewById(R.id.kernel_version);
        this.kernel_version.setText(getFormattedKernelVersion());
        this.android_version = (TextView) findViewById(R.id.android_version);
        // Use actual Android version from Build class
        str2 = Build.VERSION.RELEASE;
        if (str2 == null || str2.isEmpty()) {
            str2 = SystemPropertiesHelper.get("ro.build.version.release", "Unknown");
        }
        // Append SDK version for more complete information
        str2 = str2 + " (API " + Build.VERSION.SDK_INT + ")";
        this.android_version.setText(str2);
        this.base_version = (TextView) findViewById(R.id.base_version);
        this.base_version.setText(SystemPropertiesHelper.get("gsm.version.baseband", "unknown"));
        this.ram_info = (TextView) findViewById(R.id.ram_info);
        this.ram_info.setText(SystemPropertiesHelper.getRamInfo(this));
        this.build_date = (TextView) findViewById(R.id.build_date);
        this.build_date.setText(SystemPropertiesHelper.get("ro.build.date", ""));
        this.screen_size = (TextView) findViewById(R.id.screen_size);
        // Use actual screen dimensions from DisplayMetrics
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        int i4 = displayMetrics.widthPixels;
        int i5 = displayMetrics.heightPixels;
        // Calculate density and DPI information
        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;
        str3 = i4 + "x" + i5 + " (" + densityDpi + "dpi, density: " + String.format("%.2f", density) + ")";
        Log.d("xiedingbangTestVersion", "Screen size: " + str3);
        this.screen_size.setText(str3);
        this.imei1 = (TextView) findViewById(R.id.imei1);
        String deviceId = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                deviceId = this.telephonyManager.getImei(0);
            } else {
                deviceId = this.telephonyManager.getDeviceId(0);
            }
        } catch (Exception e) {
            Log.e("TestVersion", "Exception getting IMEI1", e);
        }
        if (deviceId != null) {
            this.imei1.setText(deviceId);
        } else {
            this.imei1.setText("null");
        }
        this.imei2 = (TextView) findViewById(R.id.imei2);
        String deviceId2 = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                deviceId2 = this.telephonyManager.getImei(1);
            } else {
                deviceId2 = this.telephonyManager.getDeviceId(1);
            }
        } catch (Exception e) {
            Log.e("TestVersion", "Exception getting IMEI2", e);
        }
        if (deviceId2 != null) {
            this.imei2.setText(deviceId2);
        } else {
            this.imei2.setText("null");
        }
        // Default to 2 SIMs, can't read custom settings without system app
        int i7 = 2;
        if (i7 == 1) {
            ((TextView) findViewById(R.id.imei1_title)).setText("IMEI: ");
            ((LinearLayout) findViewById(R.id.version_layout_imei2)).setVisibility(8);
        }
        String simSerialNumber = null;
        try {
            simSerialNumber = this.telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            Log.v("exception", "getSimSerialNumber fail");
        }
        this.sn1 = (TextView) findViewById(R.id.sn1);
        if (simSerialNumber == null) {
            this.sn1.setText("null");
        } else {
            this.sn1.setText(simSerialNumber);
        }
        try {
            this.sn2 = (TextView) findViewById(R.id.sn2);
            String simSerialNumber2 = null;
            if (this.sn2 != null) {
                this.sn2.setText("null");
            }
        } catch (Exception unused) {
            Log.v("exception", " signal sim");
        }
        if (i7 == 1) {
            ((TextView) findViewById(R.id.sn1_title)).setText("SN: ");
            ((LinearLayout) findViewById(R.id.version_layout_sn2)).setVisibility(8);
        }
        String str7 = SystemPropertiesHelper.get("vendor.gsm.serial", Build.SERIAL != null ? Build.SERIAL : "null");
        this.motherboard = (TextView) findViewById(R.id.motherboard);
        Log.v("lzc", "gsm.serial :  " + str7 + ", gmsSerial.length() = " + str7.length());
        this.motherboard.setText(str7);
    }

    public String getFormattedKernelVersion() {
        return formatKernelVersion(getBaseContext(), Os.uname());
    }

    public String formatKernelVersion(Context context, StructUtsname structUtsname) {
        if (structUtsname == null) {
            return "Unavailable";
        }
        Matcher matcher = Pattern.compile("(#\\d+) (?:.*?)?((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)").matcher(structUtsname.version);
        if (!matcher.matches()) {
            Log.e("txtx", "Regex did not match on uname version " + structUtsname.version);
            return "Unavailable";
        }
        return structUtsname.release + "\n" + matcher.group(1) + " " + matcher.group(2);
    }

    private String getCameraSize() {
        int numberOfCameras = Camera.getNumberOfCameras();
        StringBuilder sb = new StringBuilder();
        Log.e("lsz", "num-->" + numberOfCameras);
        for (int i = 0; i < numberOfCameras; i++) {
            Log.e("lsz", "i-->" + i);
            if (i == 0) {
                sb.append("Back Camera:\n");
            } else if (i == 1) {
                sb.append("Front Camera:\n");
            }
            Camera camera = null;
            try {
                camera = Camera.open(i);
                List<Camera.Size> supportedPictureSizes = camera.getParameters().getSupportedPictureSizes();
                for (int i2 = 0; i2 < supportedPictureSizes.size(); i2++) {
                    Camera.Size size = supportedPictureSizes.get(i2);
                    Log.e("lsz", "height-->" + size.height + "width-->" + size.width);
                    sb.append(size.width).append("x").append(size.height);
                    if ((i2 + 1) % 3 == 0) {
                        sb.append("\n");
                    }
                    if (i2 == supportedPictureSizes.size() - 1) {
                        sb.append("\n");
                    }
                }
            } catch (Exception e) {
                sb.append("Camera can not connected!");
                e.printStackTrace();
            } finally {
                if (camera != null) {
                    camera.release();
                }
            }
        }
        Log.e("lsz", sb.toString());
        return sb.toString();
    }
}
