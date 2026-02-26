# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.squareup.okhttp.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn okio.**

#Retrofit
-keepattributes Exceptions
-keepattributes Signature
-dontwarn retrofit2.Platform$Java8
-dontnote retrofit2.Platform

-keepattributes *Annotation*
-keep class okhttp3.* { *; }
-keep interface okhttp3.* { *; }
-dontwarn okhttp3.**
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.codehaus.mojo.animal_sniffer.*
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn javax.annotation.**

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }
-keep class com.github.chrisbanes.** { *; }
-dontwarn com.github.chrisbanes.**
-keep class com.github.denzcoskun.** { *; }
-dontwarn com.github.denzcoskun.**
