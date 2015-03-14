# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/shaobin/opt/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#

# Artem Chepurnoy
-dontwarn javax.annotation.**

# should be copied to application proguard rules config
-keep class com.android.vending.billing.**

-assumenosideeffects class org.solovyev.android.checkout.Billing {
    public static void debug(...);
    public static void warning(...);
    public static void error(...);
}

-assumenosideeffects class org.solovyev.android.checkout.Check {
        *;
}