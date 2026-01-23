package com.dismal.devicetest;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Locale;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int[] flags = null;
    private boolean isAutoTest = false;
    private boolean[] isSuccess = new boolean[31];
    private Button exportPdfBtn;
    private Button languageBtn;
    private LanguagePreference languagePreference;
    private BroadcastReceiver stopAutoTestReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.e("lsz", "onReceive-->stopAutoTest");
            MainActivity.this.stopAutoTest();
        }
    };
    private Button targetBtn;
    private Button testAll;
    private Button testBacklight;
    private Button testBattery;
    private Button testBluetooth;
    private Button[] testBtns = new Button[31];
    private Button testCamera;
    private Button testColorBtn;
    private Button testCompass;
    private Button testDistanceSensor;
    private Button testEarphone;
    private Button testFMRadio;
    private Button testFlashLightBtn;
    private Button testGpsBtn;
    private Button testGravitySensor;
    private Button testHeadset;
    private Button testKeyboard;
    private Button testLightSensor;
    private Button testMic;
    private Button testNFC;
    private Button testHandwriting;
    private Button testOTG;
    private Button testPannel;
    private Button testSdCard;
    private Button testSim;
    private Button testSound;
    private Button testTEE;
    private Button testTEE_02;
    private Button testTelephone;
    private Button testVersion;
    private Button testVibrateBtn;
    private Button testWifi;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.flags = getIntent().getIntArrayExtra("testflag");
        if (this.flags == null) {
            this.flags = new int[31];
        }
        setContentView(R.layout.main);
        setTitle(R.string.app_name);
        this.languagePreference = new LanguagePreference(this);
        this.languageBtn = (Button) findViewById(R.id.test_languagechange);
        if (this.languageBtn != null) {
            this.languageBtn.setOnClickListener(this);
        }
        this.testAll = (Button) findViewById(R.id.test_all);
        if (this.testAll != null) {
            this.testAll.setOnClickListener(this);
        }
        this.testVersion = (Button) findViewById(R.id.test_version);
        if (this.testVersion != null) {
            this.testVersion.setOnClickListener(this);
            this.testBtns[0] = this.testVersion;
        }
        this.testColorBtn = (Button) findViewById(R.id.test_color);
        if (this.testColorBtn != null) {
            this.testColorBtn.setOnClickListener(this);
            this.testBtns[1] = this.testColorBtn;
        }
        this.testPannel = (Button) findViewById(R.id.test_touchpanel);
        if (this.testPannel != null) {
            this.testPannel.setOnClickListener(this);
            this.testBtns[2] = this.testPannel;
        }
        this.testGpsBtn = (Button) findViewById(R.id.test_gps);
        if (this.testGpsBtn != null) {
            this.testGpsBtn.setOnClickListener(this);
            this.testBtns[3] = this.testGpsBtn;
        }
        this.testSdCard = (Button) findViewById(R.id.test_sd);
        if (this.testSdCard != null) {
            this.testSdCard.setOnClickListener(this);
            this.testBtns[4] = this.testSdCard;
        }
        this.testFlashLightBtn = (Button) findViewById(R.id.test_flashlight);
        if (this.testFlashLightBtn != null) {
            this.testFlashLightBtn.setOnClickListener(this);
            this.testBtns[5] = this.testFlashLightBtn;
        }
        this.testCamera = (Button) findViewById(R.id.test_camera);
        if (this.testCamera != null) {
            this.testCamera.setOnClickListener(this);
            this.testBtns[6] = this.testCamera;
        }
        this.testWifi = (Button) findViewById(R.id.test_wifi);
        if (this.testWifi != null) {
            this.testWifi.setOnClickListener(this);
            this.testBtns[7] = this.testWifi;
        }
        this.testBluetooth = (Button) findViewById(R.id.test_bluetooth);
        if (this.testBluetooth != null) {
            this.testBluetooth.setOnClickListener(this);
            this.testBtns[8] = this.testBluetooth;
        }
        this.testBacklight = (Button) findViewById(R.id.test_backlight);
        if (this.testBacklight != null) {
            this.testBacklight.setOnClickListener(this);
            this.testBtns[9] = this.testBacklight;
        }
        this.testBattery = (Button) findViewById(R.id.test_power);
        if (this.testBattery != null) {
            this.testBattery.setOnClickListener(this);
            this.testBtns[10] = this.testBattery;
        }
        this.testKeyboard = (Button) findViewById(R.id.test_keyboard);
        if (this.testKeyboard != null) {
            this.testKeyboard.setOnClickListener(this);
            this.testBtns[11] = this.testKeyboard;
        }
        this.testEarphone = (Button) findViewById(R.id.test_earphone);
        if (this.testEarphone != null) {
            this.testEarphone.setOnClickListener(this);
            this.testBtns[12] = this.testEarphone;
        }
        this.testTelephone = (Button) findViewById(R.id.test_phone);
        if (this.testTelephone != null) {
            this.testTelephone.setOnClickListener(this);
            this.testBtns[13] = this.testTelephone;
        }
        this.testVibrateBtn = (Button) findViewById(R.id.test_vibrate);
        if (this.testVibrateBtn != null) {
            this.testVibrateBtn.setOnClickListener(this);
            this.testBtns[14] = this.testVibrateBtn;
        }
        this.testGravitySensor = (Button) findViewById(R.id.test_gravity_sensor);
        if (this.testGravitySensor != null) {
            this.testGravitySensor.setOnClickListener(this);
            this.testBtns[15] = this.testGravitySensor;
        }
        this.testLightSensor = (Button) findViewById(R.id.test_light_sensor);
        if (this.testLightSensor != null) {
            this.testLightSensor.setOnClickListener(this);
            this.testBtns[16] = this.testLightSensor;
            this.testBtns[16].setVisibility(0);
        }
        this.testDistanceSensor = (Button) findViewById(R.id.test_distance_sensor);
        if (this.testDistanceSensor != null) {
            this.testDistanceSensor.setOnClickListener(this);
            this.testBtns[17] = this.testDistanceSensor;
        }
        this.testSim = (Button) findViewById(R.id.test_sim);
        if (this.testSim != null) {
            this.testSim.setOnClickListener(this);
            this.testBtns[18] = this.testSim;
        }
        this.testSound = (Button) findViewById(R.id.test_speaker);
        if (this.testSound != null) {
            this.testSound.setOnClickListener(this);
            this.testBtns[19] = this.testSound;
        }
        this.testHeadset = (Button) findViewById(R.id.test_headset);
        if (this.testHeadset != null) {
            this.testHeadset.setOnClickListener(this);
            this.testBtns[20] = this.testHeadset;
        }
        this.testMic = (Button) findViewById(R.id.test_mic);
        if (this.testMic != null) {
            this.testMic.setOnClickListener(this);
            this.testBtns[21] = this.testMic;
        }
        this.testFMRadio = (Button) findViewById(R.id.test_fm);
        if (this.testFMRadio != null) {
            this.testFMRadio.setOnClickListener(this);
            this.testBtns[22] = this.testFMRadio;
        }
        this.testHandwriting = (Button) findViewById(R.id.test_handwriting);
        if (this.testHandwriting != null) {
            this.testHandwriting.setOnClickListener(this);
            this.testBtns[23] = this.testHandwriting;
        }
        
        this.testNFC = (Button) findViewById(R.id.test_nfc);
        if (this.testNFC != null) {
            this.testNFC.setOnClickListener(this);
            this.testBtns[24] = this.testNFC;
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
                this.testBtns[24].setVisibility(View.VISIBLE);
            } else {
                this.testBtns[24].setVisibility(View.GONE);
            }
        }
        
        this.testOTG = (Button) findViewById(R.id.test_otg);
        if (this.testOTG != null) {
            this.testOTG.setOnClickListener(this);
            this.testBtns[26] = this.testOTG;
            this.testBtns[26].setVisibility(0); // OTG visible by default
        }
        this.testCompass = (Button) findViewById(R.id.test_cmpass);
        if (this.testCompass != null) {
            this.testCompass.setOnClickListener(this);
            this.testBtns[27] = this.testCompass;
            this.testBtns[27].setVisibility(0); // Compass visible by default
        }
        
        this.testTEE = (Button) findViewById(R.id.test_tee);
        if (this.testTEE != null) {
            this.testTEE.setOnClickListener(this);
            this.testBtns[28] = this.testTEE;
            this.testTEE.setVisibility(8); // TEE missing implementation
        }
        
        this.testTEE_02 = (Button) findViewById(R.id.test_tee_02);
        if (this.testTEE_02 != null) {
            this.testTEE_02.setOnClickListener(this);
            this.testBtns[29] = this.testTEE_02;
            this.testTEE_02.setVisibility(8); // TEE_02 missing implementation
        }
        
        // Hardware detection for earphone and FM radio
        detectAndConfigureHardware();
        
        this.exportPdfBtn = (Button) findViewById(R.id.test_export_pdf);
        if (this.exportPdfBtn != null) {
            this.exportPdfBtn.setOnClickListener(this);
        }
        registerReceiver(this.stopAutoTestReceiver, new IntentFilter("android.intent.action.STOP_AUTO_TEST"));
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_about, null);
        builder.setView(dialogView);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        int i = 0;
        while (true) {
            int[] iArr = this.flags;
            if (i < iArr.length) {
                if (iArr[i] == 1) {
                    this.testBtns[i].setTextColor(-16776961);
                } else if (iArr[i] == 2) {
                    this.testBtns[i].setTextColor(-65536);
                }
                i++;
            } else {
                return;
            }
        }
    }
    
    private void detectAndConfigureHardware() {
        // Detect headphone jack
        boolean hasHeadphoneJack = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
                for (AudioDeviceInfo device : devices) {
                    if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                        device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                        hasHeadphoneJack = true;
                        break;
                    }
                }
            }
        } else {
            // For older devices, assume headphone jack exists
            hasHeadphoneJack = true;
        }
        
        // Detect FM radio - check for common FM radio packages
        boolean hasFMRadio = false;
        PackageManager pm = getPackageManager();
        String[] fmPackages = {
            "com.android.fmradio",
            "com.mediatek.fmradio",
            "com.sec.android.app.fm",
            "com.miui.fmradio"
        };
        for (String pkg : fmPackages) {
            try {
                pm.getPackageInfo(pkg, 0);
                hasFMRadio = true;
                break;
            } catch (PackageManager.NameNotFoundException e) {
                // Package not found, continue checking
            }
        }
        
        // Configure button visibility
        if (testEarphone != null) {
            testEarphone.setVisibility(hasHeadphoneJack ? View.VISIBLE : View.GONE);
        }
        if (testFMRadio != null) {
            testFMRadio.setVisibility(hasFMRadio ? View.VISIBLE : View.GONE);
        }
    }

    public void onClick(View view) {
        Log.e("lsz", "--->onclick");
        Intent intent = new Intent();
        int id = view.getId();
        int i = 0;
        if (id == R.id.test_languagechange) {
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (configuration.locale.toString().equals("zh_CN")) {
                configuration.locale = Locale.ENGLISH;
                resources.updateConfiguration(configuration, displayMetrics);
            } else {
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(configuration, displayMetrics);
            }
            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.setFlags(268468224);
            startActivity(intent2);
        } else if (id == R.id.test_all) {
            this.isAutoTest = true;
            Log.e("lsz", "--->onclick-->test_all");
            this.testBtns[0].performClick();
            Log.e("lsz", "--->testBtns[0].performClick();");
        } else if (id == R.id.test_export_pdf) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // On Android 10+ (API 29+), we use MediaStore or legacy mode and don't need runtime permissions for this
                exportReport();
            } else {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (CheckPermissions(permissions)) {
                    exportReport();
                } else {
                    requestPermissionsInternal(permissions, 1001);
                }
            }
        } else {
            // Test activities
            switch (id) {
                case R.id.test_version /*2131296277*/:
                    intent.setAction("com.devicetest.testversion");
                    i = 11;
                    break;
                case R.id.test_color /*2131296278*/:
                    intent.setAction("com.devicetest.testcolor");
                    i = 12;
                    break;
                case R.id.test_touchpanel /*2131296279*/:
                    if (!FactoryConstants.TESTTOUCHPANELGRID) {
                        intent.setAction("com.devicetest.testtouchpannel");
                    } else {
                        intent.setAction("com.devicetest.testtouchpannelgrid");
                    }
                    i = 13;
                    break;
                case R.id.test_gps /*2131296280*/:
                    intent.setAction("com.devicetest.testgps");
                    i = 21;
                    break;
                case R.id.test_sd /*2131296281*/:
                    intent.setAction("com.devicetest.testsdcard");
                    i = 22;
                    break;
                case R.id.test_flashlight /*2131296282*/:
                    intent.setAction("com.devicetest.testflashlight");
                    i = 23;
                    break;
                case R.id.test_camera /*2131296283*/:
                    intent.setAction("com.devicetest.testcamera");
                    i = 31;
                    break;
                case R.id.test_wifi /*2131296284*/:
                    intent.setAction("com.devicetest.testwifi");
                    i = 32;
                    break;
                case R.id.test_bluetooth /*2131296285*/:
                    intent.setAction("com.devicetest.testbluetooth");
                    i = 33;
                    break;
                case R.id.test_backlight /*2131296286*/:
                    intent.setAction("com.devicetest.testbacklight");
                    i = 41;
                    break;
                case R.id.test_power /*2131296287*/:
                    intent.setAction("com.devicetest.testbattery");
                    i = 42;
                    break;
                case R.id.test_keyboard /*2131296288*/:
                    intent.setAction("com.devicetest.testkeyboard");
                    i = 43;
                    break;
                case R.id.test_earphone /*2131296289*/:
                    intent.setAction("com.devicetest.testearphone");
                    i = 51;
                    break;
                case R.id.test_fm /*2131296290*/:
                    intent.setAction("com.devicetest.testfmradio");
                    i = 82;
                    break;
                case R.id.test_vibrate /*2131296291*/:
                    intent.setAction("com.devicetest.testvibrate");
                    i = 53;
                    break;
                case R.id.test_gravity_sensor /*2131296292*/:
                    intent.setAction("com.devicetest.testsensor");
                    Bundle bundle = new Bundle();
                    bundle.putString("sensor_type", "type_gravity");
                    intent.putExtras(bundle);
                    i = 61;
                    break;
                case R.id.test_light_sensor /*2131296293*/:
                    intent.setAction("com.devicetest.testsensor");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("sensor_type", "type_light");
                    intent.putExtras(bundle2);
                    i = 62;
                    break;
                case R.id.test_distance_sensor /*2131296294*/:
                    intent.setAction("com.devicetest.testsensor");
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("sensor_type", "type_distance");
                    intent.putExtras(bundle3);
                    i = 63;
                    break;
                case R.id.test_sim /*2131296295*/:
                    intent.setAction("com.devicetest.testsim");
                    i = 71;
                    break;
                case R.id.test_speaker /*2131296296*/:
                    intent.setAction("com.devicetest.testspeaker");
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("dev_type", "speaker");
                    intent.putExtras(bundle4);
                    i = 72;
                    break;
                case R.id.test_headset /*2131296297*/:
                    intent.setAction("com.devicetest.testspeaker");
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("dev_type", "headset");
                    intent.putExtras(bundle5);
                    i = 73;
                    break;
                case R.id.test_mic /*2131296298*/:
                    intent.setAction("com.devicetest.testmic");
                    i = 81;
                    break;
                case R.id.test_phone /*2131296299*/:
                    intent.setAction("com.devicetest.testphone");
                    i = 52;
                    break;
                case R.id.test_cmpass /*2131296533*/:
                    intent.setAction("com.devicetest.testcompass");
                    i = 88;
                    break;
                case R.id.test_handwriting:
                    intent.setClass(this, TsHandWriting.class);
                    i = 83;
                    break;
                case R.id.test_nfc /*2131296536*/:
                    intent.setAction("com.devicetest.testnfc");
                    i = 85;
                    break;
                case R.id.test_otg /*2131296537*/:
                    intent.setAction("com.devicetest.test_otg");
                    i = 87;
                    break;
                case R.id.test_tee /*2131296539*/:
                    intent.setAction("com.devicetest.test_tee");
                    i = 89;
                    break;
                case R.id.test_tee_02 /*2131296540*/:
                    intent.setAction("com.devicetest.test_tee_02");
                    i = 90;
                    break;
            }
        }
        
        Log.e("lsz", "--->requestCode-->" + i);
        if (i != 0) {
            intent.putExtra("autotest", this.isAutoTest);
            try {
                startActivityForResult(intent, i);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to start activity for action " + intent.getAction(), e);
                Toast.makeText(this, "Test implementation not found", Toast.LENGTH_SHORT).show();
                if (this.isAutoTest) {
                    // Skip to next test if in auto mode
                    onActivityResult(i, 0, null);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        int targetBtn2 = setTargetBtn(i);
        if (i2 == 100) {
            this.flags[targetBtn2] = 1;
            this.targetBtn.setTextColor(-16776961);
        } else if (i2 == 50) {
            this.flags[targetBtn2] = 2;
            this.targetBtn.setTextColor(-65536);
        }
        if (this.isAutoTest) {
            while (targetBtn2 < 31) {
                if (targetBtn2 == 30) {
                    Toast.makeText(this, R.string.test_complete, 1).show();
                    stopAutoTest();
                    return;
                }
                targetBtn2++;
                Button button = this.testBtns[targetBtn2];
                if (button != null && button.getVisibility() == 0) {
                    button.performClick();
                    return;
                }
            }
        }
    }

    private int setTargetBtn(int i) {
        switch (i) {
            case 11:
                this.targetBtn = this.testVersion;
                return 0;
            case 12:
                this.targetBtn = this.testColorBtn;
                return 1;
            case 13:
                this.targetBtn = this.testPannel;
                return 2;
            default:
                switch (i) {
                    case 21:
                        this.targetBtn = this.testGpsBtn;
                        return 3;
                    case 22:
                        this.targetBtn = this.testSdCard;
                        return 4;
                    case 23:
                        this.targetBtn = this.testFlashLightBtn;
                        return 5;
                    default:
                        switch (i) {
                            case 31:
                                this.targetBtn = this.testCamera;
                                return 6;
                            case 32:
                                this.targetBtn = this.testWifi;
                                return 7;
                            case 33:
                                this.targetBtn = this.testBluetooth;
                                return 8;
                            default:
                                switch (i) {
                                    case 41:
                                        this.targetBtn = this.testBacklight;
                                        return 9;
                                    case 42:
                                        this.targetBtn = this.testBattery;
                                        return 10;
                                    case 43:
                                        this.targetBtn = this.testKeyboard;
                                        return 11;
                                    default:
                                        switch (i) {
                                            case 51:
                                                this.targetBtn = this.testEarphone;
                                                return 12;
                                            case 52:
                                                this.targetBtn = this.testTelephone;
                                                return 13;
                                            case 53:
                                                this.targetBtn = this.testVibrateBtn;
                                                return 14;
                                            default:
                                                switch (i) {
                                                    case 61:
                                                        this.targetBtn = this.testGravitySensor;
                                                        return 15;
                                                    case 62:
                                                        this.targetBtn = this.testLightSensor;
                                                        return 16;
                                                    case 63:
                                                        this.targetBtn = this.testDistanceSensor;
                                                        return 17;
                                                    default:
                                                        switch (i) {
                                                            case 71:
                                                                this.targetBtn = this.testSim;
                                                                return 18;
                                                            case 72:
                                                                this.targetBtn = this.testSound;
                                                                return 19;
                                                            case 73:
                                                                this.targetBtn = this.testHeadset;
                                                                return 20;
                                                            default:
                                                                switch (i) {
                                                                    case 81:
                                                                        this.targetBtn = this.testMic;
                                                                        return 21;
                                                                    case 82:
                                                                        this.targetBtn = this.testFMRadio;
                                                                        return 22;
                                                                    case 83:
                                                                        this.targetBtn = this.testHandwriting;
                                                                        return 23;

                                                                    case 85:
                                                                        this.targetBtn = this.testNFC;
                                                                        return 25;
                                                                    case 86:
                                                                        // this.targetBtn = this.testRGBLEDS;
                                                                        return 26;
                                                                    case 87:
                                                                        this.targetBtn = this.testOTG;
                                                                        return 27;
                                                                    case 88:
                                                                        this.targetBtn = this.testCompass;
                                                                        return 28;
                                                                    case 89:
                                                                        this.targetBtn = this.testTEE;
                                                                        return 29;
                                                                    case 90:
                                                                        this.targetBtn = this.testTEE_02;
                                                                        return 30;
                                                                    default:
                                                                        return 0;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
    }

    private void exportReport() {
        TestReportExporter exporter = new TestReportExporter(this);
        String filePath = exporter.exportResults(testBtns, flags);
        if (filePath != null) {
            Toast.makeText(this, "Report exported to: " + filePath, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to export report", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean CheckPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissionsInternal(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                exportReport();
            } else {
                Toast.makeText(this, "Storage permission is required to export PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopAutoTest() {
        this.isAutoTest = false;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.stopAutoTestReceiver);
    }
}
