package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestSound extends TestUnitActivity {
    private final String EARPHONE = "earphone";
    private final String HEADSET = "headset";
    private final String SPEAKER = "speaker";
    private TextView mAsk = null;
    private AudioManager mAud = null;
    private TestConfirm mConfirm = null;
    private boolean mEarphone = false;
    private TextView mEarphoneInfo = null;
    private IntentFilter mFilter;
    private String mMode = "";
    /* access modifiers changed from: private */
    public String mParam = "";
    private MediaPlayer mPlayer = null;
    private MyReceiver mReceiver;
    private int mStreamMusic = 0;
    private TextView mTestType = null;
    private String mVideoFilePath;

    private void initView() {
        this.mConfirm = (TestConfirm) findViewById(R.id.snd_confirm);
        this.mConfirm.setTestConfirm(this, TestSound.class, "Headset", "", "dev_type", "");
        this.mTestType = (TextView) findViewById(R.id.snd_test_type);
        this.mAsk = (TextView) findViewById(R.id.snd_ask);
        this.mEarphoneInfo = (TextView) findViewById(R.id.snd_earphone_info);
    }

    /* access modifiers changed from: private */
    public void playMusic() {
        if (this.mPlayer == null) {
            File videoFile = getVideoFile();
            if (videoFile != null) {
                this.mVideoFilePath = videoFile.getAbsolutePath().toString();
                if (!new File(this.mVideoFilePath).exists()) {
                    return;
                }
            }
            try {
                this.mPlayer = new MediaPlayer();
                this.mPlayer.setAudioStreamType(3);
                this.mPlayer.setDataSource(this.mVideoFilePath);
                Log.d("liuzhicheng", "mVideoFilePath===" + this.mVideoFilePath);
                this.mPlayer.setLooping(true);
                this.mPlayer.prepare();
            } catch (Exception e) {
                Log.d("liuzhicheng", "playMusic---e-->" + e);
                e.printStackTrace();
            }
        }
        Log.e("lsz", "playMusic---mParam-->" + this.mParam);
        Log.e("liuzhicheng", "playMusic---mPlayer-->" + this.mPlayer);
        if (this.mParam.equals("headset")) {
            Log.e("lsz", "playMusic---headset-->");
            this.mAud.setSpeakerphoneOn(false);
            setTitle(R.string.but_test_headset);
            this.mAud.setMode(2);
            this.mStreamMusic = this.mAud.getStreamVolume(0);
            Log.e("wst", "onCreate- mStreamMusic  ->" + this.mStreamMusic);
            this.mAud.setStreamVolume(0, 6, 0);
        } else {
            Log.e("lsz", "playMusic---speaker-->");
            this.mAud.setSpeakerphoneOn(true);
            setTitle(R.string.but_test_speaker);
            this.mAud.setMode(0);
            this.mStreamMusic = this.mAud.getStreamVolume(3);
            this.mAud.setStreamVolume(3, 15, 0);
        }
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /* access modifiers changed from: private */
    public void stopMusic() {
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }

    private File getVideoFile() {
        File file = new File(getExternalCacheDir(), ".test_sound_loud_for_headset.mp3");
        try {
            inputstreamtofile(getAssets().open("test_sound_loud_for_headset.mp3"), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void inputstreamtofile(InputStream inputStream, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[8192];
            while (true) {
                int read = inputStream.read(bArr, 0, 8192);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    inputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mParam = extras.getString("dev_type");
        }
        this.mAud = (AudioManager) getSystemService("audio");
        setContentView(R.layout.test_sound);
        this.mFilter = new IntentFilter();
        this.mFilter.addAction("android.intent.action.HEADSET_PLUG");
        this.mReceiver = new MyReceiver();
        initView();
    }

    public void onResume() {
        super.onResume();
        playMusic();
    }

    public void onPause() {
        if (this.mPlayer != null) {
            stopMusic();
        }
        if (this.mParam.equals("headset")) {
            Log.e("wst", "onCreate-onPause mStreamMusic  ->" + this.mStreamMusic);
            this.mAud.setStreamVolume(0, this.mStreamMusic, 0);
            this.mAud.setMode(0);
        } else {
            this.mAud.setStreamVolume(3, this.mStreamMusic, 0);
        }
        super.onPause();
    }

    class MyReceiver extends BroadcastReceiver {
        MyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                int intExtra = intent.getIntExtra("state", 0);
                if (intExtra == 0) {
                    if (TestSound.this.mParam.equals("headset")) {
                        TestSound.this.stopMusic();
                    }
                } else if (intExtra == 1 && TestSound.this.mParam.equals("headset")) {
                    TestSound.this.playMusic();
                }
            }
        }
    }
}
