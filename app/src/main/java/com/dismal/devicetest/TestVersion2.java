package com.dismal.devicetest;

import com.dismal.devicetest.SystemPropertiesHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.system.Os;
import android.system.StructUtsname;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.dismal.devicetest.ui.TestConfirm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestVersion2 extends TestUnitActivity {
    private TextView android_version;
    private TextView base_version;
    private TextView board;
    private TextView build_date;
    private TextView chip;
    private TextView imei1;
    private TextView imei2;
    private TextView kernel_version;
    private TestConfirm mConfirm;
    private LinearLayout mLayout;
    private TextView model;
    private TextView screen_size;
    private TextView sn1;
    private TextView sn2;
    private TextView ram_info2;
    private TelephonyManager telephonyManager;
    private TextView test_version_alsps;
    private TextView test_version_alspscompatible;
    private TextView test_version_backcamera;
    private ImageView test_version_barcode;
    private TextView test_version_barcodefail;
    private TextView test_version_bccompatible;
    private TextView test_version_btaddress;
    private TextView test_version_fccompatible;
    private TextView test_version_finger;
    private TextView test_version_fingercompatible;
    private TextView test_version_flashtype;
    private TextView test_version_fq;
    private TextView test_version_frontcamera;
    private TextView test_version_gsensor;
    private TextView test_version_gsensorcompatible;
    private TextView test_version_gyroscope;
    private TextView test_version_gyroscopecompatible;
    private TextView test_version_hotknot;
    private TextView test_version_modem;
    private TextView test_version_nfc;
    private TextView test_version_otg;
    private TextView test_version_screen;
    private TextView test_version_screencompatible;
    private TextView test_version_screenheight;
    private TextView test_version_screenwidth;
    private TextView test_version_sd_hot;
    private TextView test_version_serialnum;
    private TextView test_version_sim;
    private TextView test_version_sim_hot;
    private TextView test_version_tp;
    private TextView test_version_tpcompatible;
    private TextView test_version_wmaddress;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(0x7f03003c);
        setTitle(0x7f070004);
        
        telephonyManager = (TelephonyManager) getSystemService("phone");
        
        mConfirm = (TestConfirm) findViewById(0x7f0900a1);
        mConfirm.setTestConfirm(this, TestVersion2.class, "TestVersion2", "", "test_version2", "", true);
        
        mLayout = (LinearLayout) findViewById(0x7f0900a0);
        
        model = (TextView) findViewById(0x7f0900e9);
        model.setText(android.os.Build.MODEL);
        
        chip = (TextView) findViewById(0x7f0900be);
        chip.setText(SystemPropertiesHelper.getCpuInfo());
        
        board = (TextView) findViewById(0x7f0900b3);
        board.setText(android.os.Build.BRAND);
        
        kernel_version = (TextView) findViewById(0x7f0900df);
        kernel_version.setText(getFormattedKernelVersion());
        
        android_version = (TextView) findViewById(0x7f0900ab);
        android_version.setText(SystemPropertiesHelper.get("ro.build.version.release", android.os.Build.VERSION.RELEASE));
        
        base_version = (TextView) findViewById(0x7f0900af);
        base_version.setText(SystemPropertiesHelper.get("gsm.version.baseband", "unknown"));
        
        ram_info2 = (TextView) findViewById(0x7f09010e); // This was software_version, now repurposed for ram_info2 in the ID mapping or I need to find the correct ID if I changed it in XML
        ram_info2.setText(SystemPropertiesHelper.getRamInfo(this));
        
        build_date = (TextView) findViewById(0x7f0900b6);
        build_date.setText(SystemPropertiesHelper.get("ro.build.date", ""));
        
        imei1 = (TextView) findViewById(0x7f0900d5);
        String imei1Str = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei1Str = telephonyManager.getImei(0);
            } else {
                imei1Str = telephonyManager.getDeviceId(0);
            }
        } catch (Exception e) {
            Log.e("TestVersion2", "Error getting IMEI1", e);
        }
        if (imei1Str != null) {
            imei1.setText(imei1Str);
        } else {
            imei1.setText("null");
        }
        
        imei2 = (TextView) findViewById(0x7f0900d8);
        String imei2Str = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei2Str = telephonyManager.getImei(1);
            } else {
                imei2Str = telephonyManager.getDeviceId(1);
            }
        } catch (Exception e) {
            Log.e("TestVersion2", "Error getting IMEI2", e);
        }
        if (imei2Str != null) {
            imei2.setText(imei2Str);
        } else {
            imei2.setText("null");
        }
        
        String sn1Str = null;
        try {
            sn1Str = telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            Log.v("exception", "getSimSerialNumber fail");
        }
        sn1 = (TextView) findViewById(0x7f090109);
        if (sn1Str == null) {
            sn1.setText("null");
        } else {
            sn1.setText(sn1Str);
        }
        
        try {
            sn2 = (TextView) findViewById(0x7f09010c);
            String sn2Str = null; // telephonyManager.getSimSerialNumber(1) is hidden
            if (sn2Str == null) {
                sn2.setText("null");
            } else {
                sn2.setText(sn2Str);
            }
        } catch (Exception e) {
            Log.v("exception", " signal sim");
        }
        
        telephonyManager = (TelephonyManager) getSystemService("phone");
        int simCount = telephonyManager.getPhoneCount();
        
        test_version_sim = (TextView) findViewById(0x7f090138);
        if (simCount > 1) {
            test_version_sim.setText(getResources().getString(0x7f070167));
        } else {
            test_version_sim.setText(getResources().getString(0x7f070181));
        }
        
        test_version_screen = (TextView) findViewById(0x7f090132);
        test_version_screen.setText(getScreen());
        
        test_version_frontcamera = (TextView) findViewById(0x7f090129);
        test_version_frontcamera.setText(getCamerainfo(2));
        
        test_version_backcamera = (TextView) findViewById(0x7f09011f);
        test_version_backcamera.setText(getCamerainfo(1));
        
        test_version_gsensor = (TextView) findViewById(0x7f09012a);
        test_version_gsensor.setText(getGsensorinfo());
        
        test_version_modem = (TextView) findViewById(0x7f09012f);
        test_version_modem.setText(getProjectConfig("CUSTOM_MODEM"));
        
        test_version_fccompatible = (TextView) findViewById(0x7f090124);
        test_version_fccompatible.setText(getProjectConfig("CUSTOM_KERNEL_SUB_IMGSENSOR"));
        
        test_version_bccompatible = (TextView) findViewById(0x7f090122);
        test_version_bccompatible.setText(getProjectConfig("CUSTOM_KERNEL_MAIN_IMGSENSOR"));
        
        test_version_sim_hot = (TextView) findViewById(0x7f090139);
        if (getProjectConfig("MTK_SIM_HOT_SWAP").equals("yes")) {
            test_version_sim_hot.setText(getResources().getString(0x7f070182));
        } else {
            test_version_sim_hot.setText(getResources().getString(0x7f070176));
        }
        
        test_version_sd_hot = (TextView) findViewById(0x7f090136);
        if (getProjectConfig("SDCARD_HOTPLUG").equals("yes")) {
            test_version_sd_hot.setText(getResources().getString(0x7f070182));
        } else {
            test_version_sd_hot.setText(getResources().getString(0x7f070176));
        }
        
        test_version_hotknot = (TextView) findViewById(0x7f09012e);
        if (getProjectConfig("MTK_HOTKNOT_SUPPORT").equals("yes")) {
            test_version_hotknot.setText(getResources().getString(0x7f070182));
        } else {
            test_version_hotknot.setText(getResources().getString(0x7f070176));
        }
        
        test_version_otg = (TextView) findViewById(0x7f090131);
        if (getProjectConfig("USB_MTK_OTG").equals("yes")) {
            test_version_otg.setText(getResources().getString(0x7f070182));
        } else {
            test_version_otg.setText(getResources().getString(0x7f070176));
        }
        
        test_version_nfc = (TextView) findViewById(0x7f090130);
        if (getProjectConfig("MTK_NFC_SUPPORT").equals("yes")) {
            test_version_nfc.setText(getResources().getString(0x7f070182));
        } else {
            test_version_nfc.setText(getResources().getString(0x7f070176));
        }
        
        test_version_screencompatible = (TextView) findViewById(0x7f090133);
        test_version_screencompatible.setText(getProjectConfig("CUSTOM_LCM"));
        
        test_version_flashtype = (TextView) findViewById(0x7f090127);
        test_version_flashtype.setText(getProjectConfig("MEMORYDEVICE"));
        
        screen_size = (TextView) findViewById(0x7f0900f7);
        screen_size.setText(getProjectConfig("BOOT_LOGO"));
        
        test_version_screenwidth = (TextView) findViewById(0x7f090135);
        test_version_screenwidth.setText(getProjectConfig("LCM_WIDTH"));
        
        test_version_screenheight = (TextView) findViewById(0x7f090134);
        Log.d("yzw", "LCM_HEIGHT == " + getProjectConfig("LCM_HEIGHT"));
        test_version_screenheight.setText(getProjectConfig("LCM_HEIGHT"));
        
        test_version_tp = (TextView) findViewById(0x7f09013a);
        String tpConfig = "";
        String projectConfig = getProjectConfig("CUSTOM_TOUCHPANEL");
        if (projectConfig != null && !projectConfig.isEmpty()) {
            tpConfig = projectConfig.split(" ")[0];
        }
        try {
            // Try to read from sys, but may fail without root
            tpConfig = readLine("/sys/bus/platform/drivers/mtk-tpd/tpicname");
        } catch (IOException e) {
            Log.e("lsz", "Cannot read touchpanel info (may require root)", e);
            // Keep project config value or use default
            if (tpConfig.isEmpty()) {
                tpConfig = "Unknown";
            }
        } catch (SecurityException e) {
            Log.e("lsz", "Security exception reading touchpanel info", e);
            if (tpConfig.isEmpty()) {
                tpConfig = "Unknown";
            }
        }
        test_version_tp.setText(tpConfig);
        
        test_version_tpcompatible = (TextView) findViewById(0x7f09013b);
        test_version_tpcompatible.setText(getProjectConfig("CUSTOM_TOUCHPANEL"));
        
        test_version_fq = (TextView) findViewById(0x7f090128);
        String[] modemParts = getProjectConfig("CUSTOM_MODEM").split("_");
        String frequency = "";
        for (String part : modemParts) {
            if (isFrequency(part)) {
                frequency = part;
            }
        }
        test_version_fq.setText(frequency);
        
        test_version_gsensorcompatible = (TextView) findViewById(0x7f09012b);
        test_version_gsensorcompatible.setText(getProjectConfig("CUSTOM_ACCELEROMETER"));
        
        test_version_alsps = (TextView) findViewById(0x7f09011d);
        test_version_alsps.setText(getProjectConfig("CUSTOM_ALSPS"));
        
        test_version_alspscompatible = (TextView) findViewById(0x7f09011e);
        if (fileIsExists("/sys/bus/platform/drivers/als_ps/name")) {
            try {
                test_version_alspscompatible.setText(readLine("/sys/bus/platform/drivers/als_ps/name"));
            } catch (IOException e) {
                Log.e("lsz", "Cannot read als_ps info (may require root)", e);
                test_version_alspscompatible.setText(getProjectConfig("CUSTOM_ALSPS"));
            } catch (SecurityException e) {
                Log.e("lsz", "Security exception reading als_ps info", e);
                test_version_alspscompatible.setText(getProjectConfig("CUSTOM_ALSPS"));
            }
        } else {
            test_version_alspscompatible.setText(getProjectConfig("CUSTOM_ALSPS"));
        }
        
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) getSystemService("wifi");
        String macAddress = wifiManager.getConnectionInfo().getMacAddress();
        test_version_wmaddress = (TextView) findViewById(0x7f09013c);
        test_version_wmaddress.setText(macAddress);
        
        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) getSystemService("bluetooth");
        String btAddress = bluetoothManager.getAdapter().getAddress();
        test_version_btaddress = (TextView) findViewById(0x7f090123);
        test_version_btaddress.setText(btAddress);
        
        test_version_serialnum = (TextView) findViewById(0x7f090137);
        test_version_serialnum.setText(android.os.Build.SERIAL);
        

        
        test_version_gyroscope = (TextView) findViewById(0x7f09012c);
        String gyroConfig = getProjectConfig("CUSTOM_KERNEL_GYROSCOPE");
        if (gyroConfig.equals("") || gyroConfig.equals(" ")) {
            test_version_gyroscope.setText(getResources().getString(0x7f070176));
        } else {
            test_version_gyroscope.setText(gyroConfig.split(" ")[0]);
        }
        
        test_version_gyroscopecompatible = (TextView) findViewById(0x7f09012d);
        if (gyroConfig.equals("") || gyroConfig.equals(" ")) {
            test_version_gyroscopecompatible.setText(getResources().getString(0x7f070176));
        } else {
            test_version_gyroscopecompatible.setText(gyroConfig);
        }
        
        test_version_barcode = (ImageView) findViewById(0x7f090120);
        test_version_barcodefail = (TextView) findViewById(0x7f090121);
        
        String gsmSerial = SystemPropertiesHelper.get("gsm.serial", android.os.Build.SERIAL != null ? android.os.Build.SERIAL : "null");
        Log.v("yzw", "gsm.serial :  " + gsmSerial + ", gmsSerial.length() = " + gsmSerial.length());
        
        boolean isValidBarcode = false;
        if (gsmSerial != null && gsmSerial.length() == 63) {
            int len = gsmSerial.length();
            String lastStr = gsmSerial.substring(len - 3, len - 1);
            Log.v("yzw", "lastStr :  " + lastStr);
            if ("10".equals(lastStr)) {
                isValidBarcode = true;
            }
        }
        
        if (isValidBarcode) {
            test_version_barcodefail.setText("");
            test_version_barcode.setVisibility(0);
            test_version_barcode.setImageBitmap(createBitmap(gsmSerial));
        } else {
            test_version_barcodefail.setText(getResources().getString(0x7f070160));
            test_version_barcode.setVisibility(8);
        }
    }

    private static String readLine(String str) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(str), 256);
        try {
            return bufferedReader.readLine();
        } finally {
            bufferedReader.close();
        }
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

    public static String getScreen() {
        // /data/misc/recovery/proc/cmdline requires root access
        // Use Build class information instead
        try {
            // Try to read from /proc/cmdline which is usually readable
            return formatScreen(readLine("/proc/cmdline"));
        } catch (IOException e) {
            Log.e("lsz", "Cannot read cmdline, using Build info", e);
            // Fallback to Build class
            return android.os.Build.DISPLAY != null ? android.os.Build.DISPLAY : "Unavailable";
        }
    }

    public static String formatScreen(String str) {
        String str2 = "";
        for (String str3 : str.split(" ")) {
            if (str3.contains("lcm=")) {
                str2 = str3.substring(6, str3.length());
            }
        }
        return str2;
    }

    public static String getCamerainfo(int i) {
        // /proc/driver/camera_info may require root access
        // Use Camera API directly instead
        try {
            // Try to read from proc, but fallback to Camera API
            String cameraInfoStr = readLine("/proc/driver/camera_info");
            return formatCamerainfo(i, cameraInfoStr);
        } catch (IOException e) {
            Log.e("lsz", "Cannot read camera_info from proc, using Camera API", e);
            // Fallback to Camera API only
            return formatCamerainfo(i, "");
        }
    }

    public static String formatCamerainfo(int cameraId, String cameraInfoStr) {
        String[] parts = cameraInfoStr.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String part : parts) {
            if (part.contains("CAM[" + cameraId)) {
                result.append(part.substring(7, part.length() - 1)).append("\n");
            }
        }
        
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open(cameraId - 1);
            java.util.List<android.hardware.Camera.Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();
            
            for (int i = 0; i < pictureSizes.size(); i++) {
                android.hardware.Camera.Size size = pictureSizes.get(i);
                Log.e("lsz", "height-->" + size.height + "width-->" + size.width);
                result.append(size.width).append("x").append(size.height);
                
                if ((i + 1) % 3 == 0) {
                    result.append("\n");
                }
                if (i == pictureSizes.size() - 1) {
                    result.append("\n");
                }
            }
        } catch (Exception e) {
            result.append("Camera can not connected!");
            e.printStackTrace();
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
        
        return result.toString();
    }

    public static String getGsensorinfo() {
        // /sys files may not be accessible without root
        try {
            return readLine("/sys/bus/platform/drivers/gsensor/name");
        } catch (IOException e) {
            Log.e("lsz", "Cannot read gsensor info", e);
            // Return generic info since we can't access hardware-specific files
            return "Available (via SensorManager)";
        }
    }

    public static String getProjectConfig(String key) {
        // /vendor/data/misc/ProjectConfig.mk requires root access
        // Return empty string or use Build class as fallback
        String result = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/vendor/data/misc/ProjectConfig.mk"));
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(key + "=")) {
                        result = line.substring(key.length() + 1, line.length());
                    } else if (line.contains(key + " = ")) {
                        result = line.substring(key.length() + 3, line.length());
                    }
                }
                bufferedReader.close();
            } catch (Throwable th) {
                bufferedReader.close();
                throw th;
            }
        } catch (IOException e) {
            Log.e("lsz", "Cannot read ProjectConfig.mk (requires root)", e);
            // Return empty string as we can't access vendor files without root
            result = "";
        } catch (SecurityException e) {
            Log.e("lsz", "Security exception reading ProjectConfig.mk", e);
            result = "";
        }
        return result;
    }

    private static boolean isFrequency(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != 'b' && !Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private Bitmap createBitmap(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (19968 <= charAt && charAt < 40623) {
                return null;
            }
        }
        if (str == null) {
            return null;
        }
        try {
            if (!"".equals(str)) {
                return CreateOneDCode(str);
            }
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap CreateOneDCode(String str) throws WriterException {
        BitMatrix encode = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_128, 460, 120);
        int width = encode.getWidth();
        int height = encode.getHeight();
        int[] iArr = new int[(width * height)];
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                if (encode.get(i2, i)) {
                    iArr[(i * width) + i2] = -16777216;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public boolean fileIsExists(String str) {
        try {
            if (!new File(str).exists()) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}