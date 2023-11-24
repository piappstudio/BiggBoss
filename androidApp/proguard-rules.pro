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
-keepattributes SourceFile,LineNumberTable

# For Google drive
-keep class com.google.api.services.drive.** {*;}
-keep class com.google.api.client.** {*;}
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keep class com.google.** { *;}
-keep interface com.google.** { *;}
-dontwarn com.google.**

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.biggboss.shared.model.** {*;}

-keep class com.crashlytics.** { *;}
-keep public class * extends android.app.Activity

-keep class io.kamel.** {*;}

-keep class java.time.** { *; }

-dontwarn org.slf4j.impl.StaticLoggerBinder