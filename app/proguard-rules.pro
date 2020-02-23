# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keepattributes *Annotation*

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class okio.** { *; }
-dontwarn okio.**

-keep class androidx.core.app.** { *; }
-keep class androidx.core.content.** { *; }
-keep interface androidx.core.app.** { *; }
-keep class androidx.core.content.** { *; }

-keep class androidx.appcompat.app.** { *; }
-keep class androidx.preference.internal.** { *; }
-keep class androidx.vectordrawable.graphics.** { *; }
-keep class androidx.cardview.** { *; }
-keep class androidx.appcompat.** { *; }
-keep interface androidx.appcompat.app.** { *; }
-keep class androidx.appcompat.widget.** { *; }
-keep class androidx.preference.internal.* { *; }
-keep class androidx.recyclerview.widget.* { *; }

-keep class androidx.legacy.app.** { *; }
-keep interface androidx.legacy.app.** { *; }
-keep class androidx.multidex.** { *; }
-keep class com.google.android.material.** { *; }
