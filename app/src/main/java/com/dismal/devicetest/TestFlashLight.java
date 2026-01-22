package com.dismal.devicetest;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.dismal.devicetest.ui.TestConfirm;

public class TestFlashLight extends TestUnitActivity {
    private String TAG = "TestFlashlight";
    private Camera mCamera;
    private TestConfirm mConfirm = null;
    private boolean mIsBackFlashLight = true;
    private String mMode = "";
    /* access modifiers changed from: private */
    public myHandler mMyHandler = new myHandler();
    private Thread mThread = null;
    private Button mToggleFlashlight = null;
    /* access modifiers changed from: private */
    public boolean status = false;

    /* access modifiers changed from: private */
    public void CloseFlashlightOff() {
        this.status = false;
        if (this.mIsBackFlashLight) {
            changeFlashLight(1, false);
        } else {
            changeFlashLight(0, false);
        }
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    /* access modifiers changed from: private */
    public void OpenFlashlightOn() {
        this.status = true;
        Log.v("wst", "=================mCamera====" + this.mCamera);
        if (this.mCamera == null) {
            try {
                if (this.mIsBackFlashLight) {
                    changeFlashLight(1, true);
                } else {
                    changeFlashLight(0, true);
                }
                Log.v("wst", "=========sss========mIsBackFlashLight====" + this.mIsBackFlashLight);
            } catch (Exception unused) {
                Log.e("wst", "camera open failed!!");
            }
        }
    }

    public void changeFlashLight(Integer num, boolean z) {
        try {
            CameraManager cameraManager = (CameraManager) getSystemService("camera");
            for (String str : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str);
                Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Log.v("wst", "--dd--flashAvailable-----------------------===" + bool);
                Integer num2 = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                Log.v("wst", "--dd--lensFacing-----------------------===" + num2);
                if (bool != null && bool.booleanValue() && num2 != null && num2 == num) {
                    cameraManager.setTorchMode(str, z);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initFlashlightView() {
        setTitle(R.string.but_test_flashlight);
        this.mToggleFlashlight = (Button) findViewById(R.id.toggleFlashlight);
        setToggleFlashLightEnable(this.status);
        this.mToggleFlashlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("wst", "=================status====" + TestFlashLight.this.status);
                if (TestFlashLight.this.status) {
                    boolean unused = TestFlashLight.this.status = false;
                    TestFlashLight.this.CloseFlashlightOff();
                } else {
                    boolean unused2 = TestFlashLight.this.status = true;
                    TestFlashLight.this.OpenFlashlightOn();
                }
                TestFlashLight testFlashLight = TestFlashLight.this;
                testFlashLight.setToggleFlashLightEnable(testFlashLight.status);
            }
        });
        this.mConfirm = (TestConfirm) findViewById(R.id.flashlight_confirm);
        this.mConfirm.setTestConfirm(this, TestFlashLight.class, "Flashlight", this.mMode, (String) null, (String) null);
    }

    /* access modifiers changed from: private */
    public void setToggleFlashLightEnable(boolean z) {
        Button button = this.mToggleFlashlight;
        if (button != null) {
            if (z) {
                button.setText("Close");
                this.mToggleFlashlight.setBackgroundColor(-16711936);
                return;
            }
            button.setText("Open");
            this.mToggleFlashlight.setBackgroundColor(-16711681);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_flashlight);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mMode = extras.getString("mode");
        }
        initFlashlightView();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public void onPause() {
        this.status = false;
        CloseFlashlightOff();
        setToggleFlashLightEnable(false);
        super.onPause();
    }

    public void onResume() {
        this.mThread = new Thread(new turnonFlashlight());
        this.mThread.start();
        super.onResume();
    }

    class turnonFlashlight implements Runnable {
        turnonFlashlight() {
        }

        public void run() {
            TestFlashLight.this.OpenFlashlightOn();
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putBoolean("show_hide_button_state", true);
            message.setData(bundle);
            TestFlashLight.this.mMyHandler.sendMessage(message);
            try {
                Thread.sleep(100);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class myHandler extends Handler {
        public myHandler() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                TestFlashLight.this.setToggleFlashLightEnable(message.getData().getBoolean("show_hide_button_state"));
            }
        }
    }
}
