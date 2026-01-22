package com.dismal.devicetest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.dismal.devicetest.ui.TestConfirm;

public class TestGPS2 extends TestUnitActivity {
    private static final int PERMISSION_REQUEST_CODE = 126;
    /* access modifiers changed from: private */
    public static long mTimeSlap = 65535;
    private static int minUpdateDistance = 0;
    private static int minUpdateTime = 2000;
    protected String TAG = "lsz";
    private final int UPDATE_SEARCH_LOCATION = 1;
    private final int UPDATE_SEARCH_SATELLITE = 2;
    private final int UPDATE_SEARCH_TIME = 0;
    private LocationListener bestAvailableProviderListener = new LocationListener() {
        public void onStatusChanged(String str, int i, Bundle bundle) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onStatusChanged provider->" + str + ",status->" + i);
        }

        public void onProviderEnabled(String str) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onProviderEnabled provider->" + str);
        }

        public void onProviderDisabled(String str) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onProviderDisabled provider->" + str);
        }

        public void onLocationChanged(Location location) {
            String str = TestGPS2.this.TAG;
            Log.e(str, "onLocationChanged location->" + location.getLatitude() + "|" + location.getLongitude());
            String unused = TestGPS2.this.current_longitude = String.valueOf(location.getLongitude());
            String unused2 = TestGPS2.this.currnet_latitude = String.valueOf(location.getLatitude());
            TestGPS2.this.mSearchTimeHandler.sendEmptyMessage(1);
        }
    };
    private LocationListener bestProviderListener = new LocationListener() {
        public void onStatusChanged(String str, int i, Bundle bundle) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onStatusChanged provider->" + str + ",status->" + i);
        }

        public void onProviderEnabled(String str) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onProviderEnabled provider->" + str);
        }

        public void onProviderDisabled(String str) {
            String str2 = TestGPS2.this.TAG;
            Log.e(str2, "onProviderDisabled provider->" + str);
        }

        public void onLocationChanged(Location location) {
            String str = TestGPS2.this.TAG;
            Log.e(str, "onLocationChanged location->" + location.getLatitude() + "|" + location.getLongitude());
        }
    };
    private final Criteria criteria = new Criteria();
    /* access modifiers changed from: private */
    public String current_longitude = "";
    /* access modifiers changed from: private */
    public String currnet_latitude = "";
    private TestConfirm gpsConfirm;
    
    // For Android N (API 24) and above
    private GnssStatus.Callback gnssStatusCallback = new GnssStatus.Callback() {
        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            int satelliteCount = status.getSatelliteCount();
            String str = TestGPS2.this.TAG;
            Log.e(str, "GnssSatellite num->" + satelliteCount);
            String unused = TestGPS2.this.satelliteNum = String.valueOf(satelliteCount);
            TestGPS2.this.mSearchTimeHandler.sendEmptyMessage(2);
        }
    };
    
    // For Android versions below N (API 21-23) - Fallback to deprecated GpsStatus
    @SuppressWarnings("deprecation")
    private GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            if (ActivityCompat.checkSelfPermission(TestGPS2.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            GpsStatus gpsStatus = TestGPS2.this.locationManager.getGpsStatus(null);
            if (gpsStatus != null) {
                int satelliteCount = 0;
                for (GpsSatellite satellite : gpsStatus.getSatellites()) {
                    satelliteCount++;
                }
                String str = TestGPS2.this.TAG;
                Log.e(str, "GpsSatellite num->" + satelliteCount);
                String unused = TestGPS2.this.satelliteNum = String.valueOf(satelliteCount);
                TestGPS2.this.mSearchTimeHandler.sendEmptyMessage(2);
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView gpsinfo;
    /* access modifiers changed from: private */
    public TextView latitude;
    /* access modifiers changed from: private */
    public LocationManager locationManager;
    /* access modifiers changed from: private */
    public TextView longitude;
    private LocationManager mLm = null;
    private String mProvider = null;
    /* access modifiers changed from: private */
    public Handler mSearchTimeHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                long unused = TestGPS2.mTimeSlap = System.currentTimeMillis() - TestGPS2.this.mStartTime;
                TestGPS2.this.testTime.setText(TestGPS2.this.formateTime(TestGPS2.mTimeSlap));
                TestGPS2.this.mSearchTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else if (i == 1) {
                TestGPS2.this.latitude.setText(TestGPS2.this.currnet_latitude);
                TestGPS2.this.longitude.setText(TestGPS2.this.current_longitude);
                TestGPS2.this.gpsinfo.setText(R.string.test_gps_location_success);
                TestGPS2.this.testTime.setVisibility(8);
                TestGPS2.this.mSearchTimeHandler.removeMessages(0);
            } else if (i == 2) {
                TestGPS2.this.saliteNum.setText(TestGPS2.this.satelliteNum);
            }
        }
    };
    /* access modifiers changed from: private */
    public long mStartTime;
    /* access modifiers changed from: private */
    public TextView saliteNum;
    /* access modifiers changed from: private */
    public String satelliteNum = "";
    /* access modifiers changed from: private */
    public TextView testTime;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_gps);
        setTitle(R.string.but_test_gps);
        this.gpsinfo = (TextView) findViewById(R.id.gps_info_id);
        this.testTime = (TextView) findViewById(R.id.gps_timeslap);
        this.latitude = (TextView) findViewById(R.id.gps_latitude_id);
        this.longitude = (TextView) findViewById(R.id.gps_longitude_id);
        this.saliteNum = (TextView) findViewById(R.id.gps_count_id);
        this.gpsConfirm = (TestConfirm) findViewById(R.id.gps_confirm);
        this.gpsConfirm.setTestConfirm(this, TestGPS2.class, "testgps", "", "", "");
        this.locationManager = (LocationManager) getSystemService("location");
        this.criteria.setAccuracy(1);
        this.criteria.setPowerRequirement(3);
        this.criteria.setAltitudeRequired(false);
        this.criteria.setSpeedRequired(false);
        this.criteria.setCostAllowed(true);
        this.criteria.setHorizontalAccuracy(3);
        this.criteria.setVerticalAccuracy(2);
        this.criteria.setBearingAccuracy(1);
        this.criteria.setSpeedAccuracy(1);
        
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, can proceed with GPS operations
            } else {
                gpsinfo.setText("Location permission required for GPS test.");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            gpsinfo.setText("Waiting for location permission...");
            return;
        }
        
        if (!this.locationManager.isProviderEnabled("gps")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.test_gps_open_please);
            builder.setMessage(R.string.test_gps_open_or_not);
            builder.setPositiveButton(R.string.test_gps_open_OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    TestGPS2.this.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 0);
                }
            });
            builder.setNegativeButton(R.string.test_gps_open_Cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.create().show();
        }
        if (this.locationManager.isProviderEnabled("gps")) {
            try {
                Location lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                this.testTime.setVisibility(0);
                this.saliteNum.setVisibility(0);
                this.saliteNum.setText(Integer.toString(getGpsCount()));
                this.latitude.setVisibility(0);
                this.longitude.setVisibility(0);
                if (lastKnownLocation != null) {
                    TextView textView = this.latitude;
                    textView.setText(lastKnownLocation.getLatitude() + "");
                    TextView textView2 = this.longitude;
                    textView2.setText(lastKnownLocation.getLongitude() + "");
                }
                this.gpsinfo.setVisibility(0);
                this.gpsinfo.setText(R.string.test_gps_search);
                this.mProvider = "gps";
                mTimeSlap = 0;
                registerListener();
                this.mStartTime = System.currentTimeMillis();
                this.mSearchTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException in onResume", e);
                gpsinfo.setText("Permission error: " + e.getMessage());
            }
        }
    }

    private int getGpsCount() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // For Android N+, count will be updated via callback
                return 0;
            } else {
                // For older Android versions, use deprecated GpsStatus
                @SuppressWarnings("deprecation")
                GpsStatus gpsStatus = this.locationManager.getGpsStatus(null);
                if (gpsStatus != null) {
                    int count = 0;
                    for (GpsSatellite satellite : gpsStatus.getSatellites()) {
                        count++;
                    }
                    return count;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting GPS count", e);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mSearchTimeHandler.removeMessages(0);
    }

    /* access modifiers changed from: private */
    public String formateTime(long j) {
        String str;
        long j2 = j / 1000;
        long j3 = (j - (1000 * j2)) / 10;
        long j4 = j2 / 60;
        long j5 = j2 - (j4 * 60);
        long j6 = j4 / 60;
        long j7 = j4 - (60 * j6);
        String str2 = "00";
        if (j6 >= 10) {
            str = String.format("%01d", new Object[]{Long.valueOf(j6)});
        } else if (j6 > 0) {
            str = String.format("%02d", new Object[]{Long.valueOf(j6)});
        } else {
            str = str2;
        }
        if (j7 >= 10) {
            str2 = String.format("%01d", new Object[]{Long.valueOf(j7)});
        } else if (j7 > 0) {
            str2 = String.format("%02d", new Object[]{Long.valueOf(j7)});
        }
        String format = String.format("%02d", new Object[]{Long.valueOf(j5)});
        String.format("%02d", new Object[]{Long.valueOf(j3)});
        return str + ":" + str2 + ":" + format;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void registerListener() {
        unregisterAllListeners();
        
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing location permission");
            return;
        }
        
        try {
            String bestProvider = this.locationManager.getBestProvider(this.criteria, false);
            String bestProvider2 = this.locationManager.getBestProvider(this.criteria, true);
            String str = this.TAG;
            Log.e(str, "bestProvider->" + bestProvider + ";bestAvailableProvider ->" + bestProvider2);
            if (bestProvider == null) {
                Log.e(this.TAG, "no best provider exist on device.");
            } else if (bestProvider.equals(bestProvider2)) {
                this.locationManager.requestLocationUpdates(bestProvider2, (long) minUpdateTime, (float) minUpdateDistance, this.bestAvailableProviderListener);
            } else {
                this.locationManager.requestLocationUpdates(bestProvider, (long) minUpdateTime, (float) minUpdateDistance, this.bestProviderListener);
                if (bestProvider2 != null) {
                    this.locationManager.requestLocationUpdates(bestProvider2, (long) minUpdateTime, (float) minUpdateDistance, this.bestAvailableProviderListener);
                } else {
                    for (String requestLocationUpdates : this.locationManager.getAllProviders()) {
                        this.locationManager.requestLocationUpdates(requestLocationUpdates, 0, 0.0f, this.bestProviderListener);
                    }
                    Log.e(this.TAG, "No Location Providers currently available.");
                }
            }
            
            // Use appropriate GPS status listener based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android N (API 24) and above - use GnssStatus
                this.locationManager.registerGnssStatusCallback(this.gnssStatusCallback);
            } else {
                // Android Lollipop/Marshmallow (API 21-23) - use deprecated GpsStatus
                @SuppressWarnings("deprecation")
                boolean registered = this.locationManager.addGpsStatusListener(this.gpsStatusListener);
                Log.d(TAG, "GpsStatusListener registered: " + registered);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException in registerListener", e);
        }
    }

    private void unregisterAllListeners() {
        try {
            this.locationManager.removeUpdates(this.bestProviderListener);
            this.locationManager.removeUpdates(this.bestAvailableProviderListener);
            
            // Use appropriate GPS status listener based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android N (API 24) and above - use GnssStatus
                this.locationManager.unregisterGnssStatusCallback(this.gnssStatusCallback);
            } else {
                // Android Lollipop/Marshmallow (API 21-23) - use deprecated GpsStatus
                @SuppressWarnings("deprecation")
                boolean dummy = true; // Suppress warning for the next line
                this.locationManager.removeGpsStatusListener(this.gpsStatusListener);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error unregistering listeners", e);
        }
    }
}
