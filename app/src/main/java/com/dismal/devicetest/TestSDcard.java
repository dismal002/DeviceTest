package com.dismal.devicetest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import java.io.File;

public class TestSDcard extends TestUnitActivity {
    private File EXTERNAL_STORAGE2_DIRECTORY = null;
    private boolean isAutoTestMode;
    private boolean mActivityStop = false;
    private int mAndroidVersion = Integer.parseInt(Build.VERSION.SDK);
    private Button mBtnDel;
    private Button mBtnDisplay;
    private Button mBtnSava;
    private LinearLayout mBtn_layout;
    private TestConfirm mConfirm = null;
    private String mContent = "";
    private Context mContext = null;
    private String mEditCont = "";
    private String mMode = "";
    private TextView mPhoneCardDetail;
    private TextView mPhoneCardStatus;
    private TextView mSdDetail;
    private EditText mSdEdit;
    private TextView mSdStatus;
    private StorageManager mStorageManager = null;
    private File phoneCardDir;
    private File sdCardDir;

    private static boolean isSDExistWhenSwap() {
        return false;
    }

    public String getExternalStorage2State() {
        return "removed";
    }

    public TestSDcard() {
        if (this.mAndroidVersion >= 16) {
            this.EXTERNAL_STORAGE2_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/storage/sdcard1");
        } else {
            this.EXTERNAL_STORAGE2_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/mnt/sdcard2");
        }
    }

    private void initSdcardView() {
        this.mPhoneCardStatus = (TextView) findViewById(R.id.test_phonecard_status);
        this.mPhoneCardDetail = (TextView) findViewById(R.id.test_phonecard_detail);
        this.mSdStatus = (TextView) findViewById(R.id.test_sd_status);
        this.mSdDetail = (TextView) findViewById(R.id.test_sd_detail);
        this.mSdEdit = (EditText) findViewById(R.id.test_sd_edit);
        this.mBtn_layout = (LinearLayout) findViewById(R.id.btn_hor_layout);
        this.mBtnSava = (Button) findViewById(R.id.test_sd_btn_save);
        this.mBtnDisplay = (Button) findViewById(R.id.test_sd_btn_display);
        this.mBtnDel = (Button) findViewById(R.id.test_sd_btn_del);
        this.mSdEdit.setVisibility(4);
        this.mBtn_layout.setVisibility(4);
        this.mConfirm = (TestConfirm) findViewById(R.id.sdcard_confirm);
        this.mConfirm.setTestConfirm(this, TestSDcard.class, "Sdcard", this.mMode, (String) null, (String) null);
        Log.d("TAG", "initSdcardView");
        String externalStorageState = Environment.getExternalStorageState();
        this.phoneCardDir = Environment.getExternalStorageDirectory();
        Log.e("TAG", "mountState-111->" + externalStorageState);
        if ("mounted".equals(externalStorageState)) {
            readPhoneCardDetail();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.test_sd_totalsize));
            sb.append("0KB");
            sb.append("\n");
            sb.append(getResources().getString(R.string.test_sd_freesize));
            sb.append("0KB");
            if (isSDExistWhenSwap()) {
                this.mSdStatus.setText(getResources().getString(R.string.test_sd_sdnotinsert));
                this.mSdDetail.setText(sb.toString());
            } else {
                this.mPhoneCardStatus.setText(getResources().getString(R.string.test_sd_phonenotinsert));
                this.mPhoneCardDetail.setText(sb.toString());
            }
        }
        this.mStorageManager = (StorageManager) getSystemService("storage");
        this.sdCardDir = this.EXTERNAL_STORAGE2_DIRECTORY;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            java.util.List<android.os.storage.StorageVolume> volumes = this.mStorageManager.getStorageVolumes();
            for (android.os.storage.StorageVolume volume : volumes) {
                if (volume.isRemovable()) {
                    try {
                        // getPath() is hidden but often available via reflection or in older SDKs
                        java.lang.reflect.Method getPathMethod = volume.getClass().getMethod("getPath");
                        String path = (String) getPathMethod.invoke(volume);
                        if (path != null) {
                            this.sdCardDir = new File(path);
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "Failed to get path via reflection", e);
                    }
                }
            }
        }
        String storageState = Environment.getStorageState(this.sdCardDir);
        Log.e("TAG", "mountState-222->" + storageState);
        if ("mounted".equals(storageState)) {
            readSdcardDetail();
        } else {
            this.mSdStatus.setText(getResources().getString(R.string.test_sd_sdnotinsert));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getResources().getString(R.string.test_sd_totalsize));
            sb2.append("0KB");
            sb2.append("\n");
            sb2.append(getResources().getString(R.string.test_sd_freesize));
            sb2.append("0KB");
            this.mSdDetail.setText(sb2.toString());
            if (isSDExistWhenSwap()) {
                this.mPhoneCardStatus.setText(getResources().getString(R.string.test_sd_phonenotinsert));
                this.mPhoneCardDetail.setText(sb2.toString());
            } else {
                this.mSdStatus.setText(getResources().getString(R.string.test_sd_sdnotinsert));
                this.mSdDetail.setText(sb2.toString());
            }
        }
        this.mSdEdit.setVisibility(4);
        this.mBtn_layout.setVisibility(4);
        this.mBtnSava.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mBtnDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mBtnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    /* access modifiers changed from: package-private */
    public File getDirectory(String str, String str2) {
        return new File(str2);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.mMode = intent.getExtras().getString("mode");
            if (this.mMode != null) {
                this.isAutoTestMode = true;
            }
        }
        setContentView(R.layout.test_sd);
        initSdcardView();
        setTitle(R.string.but_test_sd);
        this.mContext = this;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Log.i("TAG", "onStop called.");
        this.mActivityStop = true;
        "mounted".equals(getExternalStorage2State());
    }

    public void readPhoneCardDetail() {
        StatFs statFs = new StatFs(this.phoneCardDir.getPath());
        long blockSize = (long) statFs.getBlockSize();
        long blockCount = (long) statFs.getBlockCount();
        long availableBlocks = (long) statFs.getAvailableBlocks();
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.test_sd_totalsize));
        long j = (blockCount * blockSize) / 1048576;
        Long.toString(j);
        // Can't read system properties without system app, use actual calculated value
        String l = Long.toString(j);
        sb.append(l);
        sb.append("MB");
        sb.append("\n");
        sb.append(getResources().getString(R.string.test_sd_freesize));
        String l2 = Long.toString((availableBlocks * blockSize) / 1048576);
        sb.append(l2);
        sb.append("MB");
        this.mPhoneCardDetail.setText(sb.toString());
        if (isSDExistWhenSwap()) {
            this.mSdDetail.setText(sb.toString());
            this.mSdStatus.setText(getResources().getString(R.string.test_sd_sdinsert));
        } else {
            this.mPhoneCardDetail.setText(sb.toString());
            this.mPhoneCardStatus.setText(getResources().getString(R.string.test_sd_phoneinsert));
        }
        Log.e("TAG", "blockSize-->" + blockSize + ",totalSzie->" + l + ",availableSize->" + l2);
    }

    public void readSdcardDetail() {
        StatFs statFs = new StatFs(this.sdCardDir.getPath());
        long blockSize = (long) statFs.getBlockSize();
        long blockCount = (long) statFs.getBlockCount();
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.test_sd_totalsize));
        sb.append(Long.toString((blockCount * blockSize) / 1048576));
        sb.append("MB");
        sb.append("\n");
        sb.append(getResources().getString(R.string.test_sd_freesize));
        sb.append(Long.toString((blockSize * ((long) statFs.getAvailableBlocks())) / 1048576));
        sb.append("MB");
        if (isSDExistWhenSwap()) {
            this.mPhoneCardDetail.setText(sb.toString());
            this.mPhoneCardStatus.setText(getResources().getString(R.string.test_sd_phoneinsert));
            return;
        }
        this.mSdDetail.setText(sb.toString());
        this.mSdStatus.setText(getResources().getString(R.string.test_sd_sdinsert));
    }
}
