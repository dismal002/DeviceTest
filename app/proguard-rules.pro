# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Google Guava classes
-keep class com.google.common.** { *; }
-dontwarn com.google.common.**

# Keep ZXing classes
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**
