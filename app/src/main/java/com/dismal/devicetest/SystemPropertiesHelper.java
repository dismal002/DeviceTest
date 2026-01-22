package com.dismal.devicetest;

import android.os.Build;

/**
 * Helper class to replace SystemProperties calls that require system app privileges.
 * Uses Build class and other standard Android APIs instead.
 */
public class SystemPropertiesHelper {
    
    /**
     * Get system property value, using Build class as fallback
     */
    public static String get(String key, String defaultValue) {
        // Try to get from Build class first
        switch (key) {
            case "ro.product.model":
                return Build.MODEL != null ? Build.MODEL : defaultValue;
            case "ro.board.platform":
            case "ro.hardware":
                return Build.HARDWARE != null ? Build.HARDWARE : defaultValue;
            case "ro.product.board":
                return Build.BOARD != null ? Build.BOARD : defaultValue;
            case "ro.build.version.release":
                return Build.VERSION.RELEASE != null ? Build.VERSION.RELEASE : defaultValue;
            case "ro.build.date":
                return Build.TIME > 0 ? new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(new java.util.Date(Build.TIME)) : defaultValue;
            case "gsm.version.baseband":
                // Baseband version is not available without system app, return default
                return defaultValue;
            case "ro.build.fingerprint":
            case "ro.product.customer.version":
                // Custom build versions not available without system app
                return "";
            case "vendor.gsm.serial":
            case "gsm.serial":
                // Serial number from Build class
                return Build.SERIAL != null ? Build.SERIAL : defaultValue;
            case "ro.chipname":
                // Try to get from Build
                return Build.HARDWARE != null ? Build.HARDWARE : defaultValue;
            default:
                return defaultValue;
        }
    }
    
    /**
     * Get boolean system property
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, defaultValue ? "true" : "false");
        return "true".equals(value) || "1".equals(value);
    }
    
    /**
     * Get CPU information from /proc/cpuinfo
     */
    public static String getCpuInfo() {
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("/proc/cpuinfo"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Hardware") || line.contains("Processor")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        return parts[1].trim();
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            android.util.Log.e("SystemPropertiesHelper", "Error reading cpuinfo", e);
        }
        return Build.HARDWARE;
    }

    /**
     * Get RAM information using ActivityManager
     */
    public static String getRamInfo(android.content.Context context) {
        try {
            android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                activityManager.getMemoryInfo(mi);
                long totalMemMB = mi.totalMem / 1024 / 1024;
                long totalMemGB = Math.round((double)mi.totalMem / (1024 * 1024 * 1024));
                // Ensure at least 1GB if it's close to it
                if (totalMemGB == 0 && totalMemMB > 512) {
                    totalMemGB = 1;
                }
                return totalMemGB + " GB (" + totalMemMB + " MB)";
            }
        } catch (Exception e) {
            android.util.Log.e("SystemPropertiesHelper", "Error getting memory info", e);
        }
        return "Unknown";
    }

    /**
     * Set system property (not available without system app, so this is a no-op)
     */
    public static void set(String key, String value) {
        // Setting system properties requires system app privileges
        // This is a no-op for regular apps
        android.util.Log.d("SystemPropertiesHelper", "set() called but requires system app: " + key + " = " + value);
    }
}
