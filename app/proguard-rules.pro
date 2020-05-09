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
#-printconfiguration ~/tmp/full-r8-config.txt

-keep class com.google.a.b.w.** { *; }


-keep class com.iexamcenter.calendarweather.saxrssreader.** {
    public *;
}
-keep public class org.jsoup.** {
    public *;
}
-assumenosideeffects class android.util.Log { *; }

-assumenosideeffects class System {
    public static *** out.println(...);
}
#-keep class com.iexamcenter.calendarweather.model.MyQuoteListSave
-keep class com.iexamcenter.calendarweather.response.**
-keep class com.iexamcenter.calendarweather.request.**
-keep class com.iexamcenter.calendarweather.retro.**
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# for retrofit

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
-dontwarn okio.**
# Okio
-keep class com.google.j2objc.annotations.** { *; }
-dontwarn   com.google.j2objc.annotations.**
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-dontwarn java.lang.invoke.*
-keep class com.iexamcenter.calendarweather.response.**
-keep class com.android.vending.billing.**
-keep class com.android.billingclient.api.**

