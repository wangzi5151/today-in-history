# Gson uses reflection to (de)serialize the API/cache models.
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.wangzi.todayinhistory.model.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep line numbers for readable crash reports.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
