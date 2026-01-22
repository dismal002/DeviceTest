package com.dismal.devicetest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dismal.devicetest.ui.TestConfirm;

public class TestCamera extends TestUnitActivity {
    private static final int PERMISSION_REQUEST_CODE = 125;
    private Button mChangeCamera = null;
    private TestConfirm mConfirm = null;
    private Intent mIntent = null;
    private String mMode = "";

    private void initView() {
        setTitle(R.string.but_test_camera);
        this.mChangeCamera = (Button) findViewById(R.id.but_changecamera);
        this.mConfirm = (TestConfirm) findViewById(R.id.cam_confirm);
        this.mConfirm.setTestConfirm(this, TestCamera.class, "Camera", this.mMode, (String) null, (String) null);
        this.mChangeCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checkCameraPermission()) {
                    launchCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void launchCamera() {
        try {
            startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 1001);
        } catch (Exception e) {
            Log.e("TestCamera", "Error launching camera", e);
            Toast.makeText(this, "Failed to launch camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_camera);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mMode = extras.getString("mode");
        }
        initView();
        
        // Check permission on startup
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted. You can now use the camera.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied. Cannot access camera.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        Log.e("lsz", "requestCode-->" + i + ",resultCode->data-->" + intent);
        super.onActivityResult(i, i2, intent);
    }
}
