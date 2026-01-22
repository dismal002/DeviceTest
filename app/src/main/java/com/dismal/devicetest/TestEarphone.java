package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;

public class TestEarphone extends TestUnitActivity {
    /* access modifiers changed from: private */
    public static boolean mEarphone = false;
    private static boolean mThreadRunning = false;
    private byte[] buffer;
    private int bufferSize;
    private AudioManager mAud;
    private final int mChannel = 2;
    private TestConfirm mConfirm;
    private IntentFilter mFilter;
    private MyReceiver mReceiver;
    private AudioRecord mRecord;
    private int mStreamVoiceCall = 1;
    private float mTempWave = 0.0f;
    private Thread mThread = null;
    private AudioTrack mTrack;
    private float mWave = 0.0f;
    /* access modifiers changed from: private */
    public TextView tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_earphone);
        setTitle(R.string.but_test_earphone);
        this.tv = (TextView) findViewById(R.id.earphone_info);
        this.mConfirm = (TestConfirm) findViewById(R.id.earphone_confirm);
        this.mConfirm.setTestConfirm(this, TestEarphone.class, "", "", "", "");
        this.mFilter = new IntentFilter();
        this.mFilter.addAction("android.intent.action.HEADSET_PLUG");
        this.mReceiver = new MyReceiver();
        Log.e("lsz", "onCreate-->" + mEarphone);
        this.mAud = (AudioManager) getSystemService("audio");
        this.mStreamVoiceCall = this.mAud.getStreamVolume(0);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.mAud.setSpeakerphoneOn(false);
        mThreadRunning = true;
        initAM();
        registerReceiver(this.mReceiver, this.mFilter);
        this.mThread = new Thread(new myThread());
        this.mThread.start();
        Log.e("lsz", "onResume-->" + mEarphone);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        mThreadRunning = false;
        unregisterReceiver(this.mReceiver);
        AudioRecord audioRecord = this.mRecord;
        if (audioRecord != null) {
            audioRecord.stop();
            this.mRecord.release();
            this.mRecord = null;
        }
        if (this.mTrack != null) {
            try {
                this.mThread.join(2000);
                this.mTrack.stop();
                this.mTrack.release();
                this.mTrack = null;
            } catch (InterruptedException unused) {
                this.mTrack.stop();
            }
        }
        this.mAud.setStreamVolume(0, this.mStreamVoiceCall, 0);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mAud.setMode(0);
        super.onDestroy();
    }

    private void initAM() {
        mEarphone = this.mAud.isWiredHeadsetOn();
        Log.e("lsz", "mEarphone-->" + mEarphone);
        if (mEarphone) {
            this.tv.setText(R.string.test_sound_earphone_in);
        } else {
            this.tv.setText(R.string.test_sound_earphone_out);
        }
        this.bufferSize = AudioRecord.getMinBufferSize(44100, 12, 2);
        this.bufferSize = Math.max(this.bufferSize, AudioTrack.getMinBufferSize(44100, 12, 2));
        this.bufferSize = Math.max(this.bufferSize, 1024);
        int i = this.bufferSize;
        this.buffer = new byte[i];
        this.mRecord = new AudioRecord(1, 44100, 12, 2, i);
        this.mTrack = new AudioTrack(0, 44100, 12, 2, this.bufferSize, 1);
        this.mAud.setStreamVolume(0, 1, 0);
        this.mTrack.setPlaybackRate(44100);
    }

    class MyReceiver extends BroadcastReceiver {
        MyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                int intExtra = intent.getIntExtra("state", 0);
                if (intExtra == 0) {
                    TestEarphone.this.tv.setText(R.string.test_sound_earphone_out);
                    boolean unused = TestEarphone.mEarphone = false;
                } else if (intExtra == 1) {
                    TestEarphone.this.tv.setText(R.string.test_sound_earphone_in);
                    boolean unused2 = TestEarphone.mEarphone = true;
                }
            }
        }
    }

    class myThread implements Runnable {
        myThread() {
        }

        public void run() {
            TestEarphone.this.micLoopBack();
        }
    }

    /* access modifiers changed from: package-private */
    public void micLoopBack() {
        AudioRecord audioRecord = this.mRecord;
        if (audioRecord != null && this.mTrack != null && audioRecord != null && this.buffer != null) {
            audioRecord.startRecording();
            this.mTrack.play();
            while (true) {
                int i = 0;
                int read = this.mRecord.read(this.buffer, 0, this.bufferSize);
                this.mTempWave = 0.0f;
                if (read > 0) {
                    this.mTrack.write(this.buffer, 0, read);
                }
                while (i < read) {
                    if (!Thread.currentThread().isInterrupted() && mThreadRunning) {
                        float f = this.mTempWave;
                        byte[] bArr = this.buffer;
                        this.mTempWave = f + ((float) (bArr[i] * bArr[i]));
                        i++;
                    } else {
                        return;
                    }
                }
                this.mWave = (this.mTempWave / ((float) read)) * 10.0f;
            }
        }
    }
}
