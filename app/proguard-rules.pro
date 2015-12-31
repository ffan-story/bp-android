# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/maning/Library/Android/sdk/tools/proguard/proguard-android.txt
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
##########常规项##########
#指定代码的压缩级别
-optimizationpasses 5
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#避免混淆泛型
-keepattributes Signature
#忽略警告
-ignorewarning
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#########根据项目附加项##########
#项目自己的代码
-keep class  com.feifan.bp.network.BaseModel {*;}
#-keep class  * extends com.feifan.bp.network.BaseModel {*;}
#第三方jar
-keep class com.umeng.**{*;}  #友盟相关
-keepclassmembers class * {   #反射
   public <init>(org.json.JSONObject);
}
#-keep class com.android.volley.** { *; }  #Volley框架
-keep class org.apache.http.** { *; }

#记录生成的日志数据,gradle build时在本项目根目录输出
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt