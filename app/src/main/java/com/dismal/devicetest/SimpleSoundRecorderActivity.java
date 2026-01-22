package com.dismal.devicetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;

public class SimpleSoundRecorderActivity extends TestUnitActivity {
    private static final String TAG = "SimpleSoundRecorder";
    private static final int PERMISSION_REQUEST_CODE = 127;
    
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    
    private Button btnRecord;
    private Button btnStop;
    private Button btnPlay;
    private TextView tvStatus;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_sound_recorder);
        setTitle(R.string.sound_recorder_title);
        
        btnRecord = findViewById(R.id.btn_record);
        btnStop = findViewById(R.id.btn_stop);
        btnPlay = findViewById(R.id.btn_play);
        tvStatus = findViewById(R.id.tv_status);
        
        // Set up audio file path in cache directory
        File cacheDir = getCacheDir();
        audioFilePath = new File(cacheDir, "test_recording.3gp").getAbsolutePath();
        
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    startRecording();
                } else {
                    requestPermission();
                }
            }
        });
        
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                } else if (isPlaying) {
                    stopPlaying();
                }
            }
        });
        
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });
        
        updateButtonStates();
        
        // Check permission on startup
        if (!checkPermission()) {
            requestPermission();
        }
    }
    
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Microphone permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Microphone permission denied. Cannot record audio.", Toast.LENGTH_LONG).show();
                tvStatus.setText("Permission denied");
            }
        }
    }
    
    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);
            
            mediaRecorder.prepare();
            mediaRecorder.start();
            
            isRecording = true;
            tvStatus.setText("Recording...");
            updateButtonStates();
            
            Log.d(TAG, "Recording started: " + audioFilePath);
        } catch (IOException e) {
            Log.e(TAG, "Failed to start recording", e);
            Toast.makeText(this, "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                
                isRecording = false;
                tvStatus.setText("Recording stopped. Ready to play.");
                updateButtonStates();
                
                Log.d(TAG, "Recording stopped");
            } catch (Exception e) {
                Log.e(TAG, "Error stopping recording", e);
            }
        }
    }
    
    private void startPlaying() {
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            Toast.makeText(this, "No recording found. Please record first.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            
            isPlaying = true;
            tvStatus.setText("Playing...");
            updateButtonStates();
            
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
            
            Log.d(TAG, "Playback started");
        } catch (IOException e) {
            Log.e(TAG, "Failed to start playback", e);
            Toast.makeText(this, "Failed to play recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void stopPlaying() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                
                isPlaying = false;
                tvStatus.setText("Playback stopped");
                updateButtonStates();
                
                Log.d(TAG, "Playback stopped");
            } catch (Exception e) {
                Log.e(TAG, "Error stopping playback", e);
            }
        }
    }
    
    private void updateButtonStates() {
        btnRecord.setEnabled(!isRecording && !isPlaying);
        btnStop.setEnabled(isRecording || isPlaying);
        btnPlay.setEnabled(!isRecording && !isPlaying);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
