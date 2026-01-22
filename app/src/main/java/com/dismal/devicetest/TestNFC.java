package com.dismal.devicetest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.dismal.devicetest.LoyaltyCardReader;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestNFC extends TestUnitActivity implements LoyaltyCardReader.AccountCallback {
    public static int READER_FLAGS = 143;
    /* access modifiers changed from: private */
    public IntentFilter[] intentFilters;
    /* access modifiers changed from: private */
    public boolean isInLoad;
    private boolean isOpen = false;
    /* access modifiers changed from: private */
    public LoyaltyCardReader mLoyaltyCardReader;
    private TextView mNfc = null;
    /* access modifiers changed from: private */
    public NfcAdapter nfc;
    /* access modifiers changed from: private */
    public PendingIntent pendingIntent;
    /* access modifiers changed from: private */
    public String[][] techList;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_nfc);
        setTitle(R.string.but_test_nfc);
        this.mNfc = (TextView) findViewById(R.id.test_nfc);
        this.mNfc.setText("Waiting...");
        this.mLoyaltyCardReader = new LoyaltyCardReader(this);
        ((TestConfirm) findViewById(R.id.finger_confirm)).setTestConfirm(this, TestNFC.class, "nfc", "", (String) null, "");
        initNFC();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.i("txtx", "onResume");
        this.nfc = NfcAdapter.getDefaultAdapter(this);
        enableForegroundDispatch();
        super.onResume();
    }

    private void initNFC() {
        this.techList = new String[][]{new String[]{IsoDep.class.getName()}, new String[]{MifareClassic.class.getName()}, new String[]{MifareUltralight.class.getName()}, new String[]{Ndef.class.getName()}, new String[]{NfcA.class.getName()}, new String[]{NfcB.class.getName()}, new String[]{NfcBarcode.class.getName()}, new String[]{NfcF.class.getName()}, new String[]{NfcV.class.getName()}};
        this.intentFilters = new IntentFilter[]{new IntentFilter("android.nfc.action.TECH_DISCOVERED"), new IntentFilter("android.nfc.action.TAG_DISCOVERED"), new IntentFilter("android.nfc.action.NDEF_DISCOVERED")};
        int flags = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_MUTABLE;
        }
        this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, TestNFC.class).addFlags(536870912), flags);
    }

    private void enableForegroundDispatch() {
        Log.i("txtx", "enableForegroundDispatch");
        NfcAdapter nfcAdapter = this.nfc;
        if (nfcAdapter != null) {
            this.isOpen = nfcAdapter.isEnabled();
//            if (!this.isOpen) {
//                this.nfc.enable();
//            }
            this.isInLoad = true;
            runNFCLoad();
        }
    }

    /* access modifiers changed from: private */
    public void runNFCLoad() {
        Log.i("txtx", "runNFCLoad  isInLoad=" + this.isInLoad);
        if (this.nfc != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    boolean isEnabled = TestNFC.this.nfc.isEnabled();
                    Log.i("txtx", "isOpen=" + isEnabled);
                    if (TestNFC.this.isInLoad) {
                        if (!isEnabled) {
                            TestNFC.this.runNFCLoad();
                            return;
                        }
                        Log.i("txtx", "111111111111");
                        NfcAdapter access$000 = TestNFC.this.nfc;
                        TestNFC testNFC = TestNFC.this;
                        access$000.enableForegroundDispatch(testNFC, testNFC.pendingIntent, TestNFC.this.intentFilters, TestNFC.this.techList);
                        new Bundle().putInt("presence", 50);
                        NfcAdapter access$0002 = TestNFC.this.nfc;
                        TestNFC testNFC2 = TestNFC.this;
                        access$0002.enableReaderMode(testNFC2, testNFC2.mLoyaltyCardReader, TestNFC.READER_FLAGS, (Bundle) null);
                    }
                }
            }, 50);
        }
    }

    private void disableReaderMode() {
        Log.i("txtx", "disableReaderMode isInLoad=" + this.isInLoad);
        NfcAdapter nfcAdapter = this.nfc;
        if (nfcAdapter != null && this.isInLoad) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    private void disableForegroundDispatch() {
        Log.i("txtx", "disableForegroundDispatch");
        NfcAdapter nfcAdapter = this.nfc;
        if (nfcAdapter != null) {
            if (this.isInLoad) {
                nfcAdapter.disableForegroundDispatch(this);
            }
//            if (!this.isOpen && this.nfc.isEnabled()) {
//                this.nfc.disable();
//            }
        }
    }

    public void onAccountReceived(CopyOnWriteArrayList<String> copyOnWriteArrayList) {
        Log.i("txtx", "onAccountReceived");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TestNFC.this.lambda$onAccountReceived$0$TestNFC(copyOnWriteArrayList);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: show */
    public void lambda$onAccountReceived$0$TestNFC(CopyOnWriteArrayList<String> copyOnWriteArrayList) {
        Log.i("txtx", "show");
        this.mNfc.setText("");
        if (copyOnWriteArrayList != null) {
            Iterator<String> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                TextView textView = this.mNfc;
                textView.append(it.next() + "\n");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i("txtx", "onPause");
        disableReaderMode();
        disableForegroundDispatch();
        this.nfc = null;
        this.isInLoad = false;
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.i("txtx", "onStop");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i("txtx", "onDestroy");
        super.onDestroy();
    }
}
