package com.bp.crash;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class SystemUtil {
  private static final String META_CHANNEL = "CHANNEL";
  private static final String BUILD_PROP_FILE = "/system/build.prop";
  private static final String PROP_NAME_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
  private static String packageVersionName = null;
  private static int versionCode = 0;

  private SystemUtil() {}

  public static boolean aboveApiLevel(int sdkInt) {
    return getApiLevel() >= sdkInt;
  }

  public static int getApiLevel() {
    return Build.VERSION.SDK_INT;
  }

  public static boolean isBlur() {
    try {
      return Build.BRAND.toLowerCase().contains("blur");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isZTE() {
    try {
      return Build.BRAND.toLowerCase().contains("zte");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isGalaxyS2() {
    try {
      String lowerCaseModel = Build.MODEL.toLowerCase();
      return lowerCaseModel.contains("gt-i9100")
          || lowerCaseModel.contains("gt-i9108")
          || lowerCaseModel.contains("gt-i9103");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isNexusS() {
    try {
      String lowerCaseModel = Build.MODEL.toLowerCase();
      return lowerCaseModel.contains("nexus s");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isKindleFire() {
    try {
      return Build.MODEL.toLowerCase().contains("kindle fire");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isMIUI() {
    File buildPropFile = new File(BUILD_PROP_FILE);
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(buildPropFile));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(PROP_NAME_MIUI_VERSION_CODE)) {
          return true;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean isSDCardMounted() {
    return Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED);
  }

  public static boolean isExternalSDCardMounted() {
    if (Build.VERSION.SDK_INT < 11) {
      return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    } else {
      return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
          && !Environment.isExternalStorageEmulated();
    }
  }


  public static String getSecureAndroidID(Context context) {
    return Secure
        .getString(context.getContentResolver(), Secure.ANDROID_ID);
  }

  public static String getSdkVersion() {
    try {
      return Build.VERSION.SDK;
    } catch (Exception e) {
      e.printStackTrace();
      return String.valueOf(getSdkVersionInt());
    }
  }

  public static String getSdkReleaseVersion() {
    try {
      return Build.VERSION.RELEASE;
    } catch (Exception e) {
      e.printStackTrace();
      return getSdkVersion();
    }
  }

  public static int getSdkVersionInt() {
    try {
      return Build.VERSION.SDK_INT;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * if the external storage device which is emulated, that mean the devices
   * does not have real external storage ,result includes that devices.
   * 
   * @return
   */
  public static long getAvailableExternalStorage() {
    try {
      File file = Environment.getExternalStorageDirectory();
      if (file != null && file.exists()) {
        StatFs sdFs = new StatFs(file.getPath());
        if (sdFs != null) {
          long sdBlockSize = sdFs.getBlockSize();
          long sdAvailCount = sdFs.getAvailableBlocks();
          return sdAvailCount * sdBlockSize;
        }
      }
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static long getTotalExternalMemorySize() {
    try {
      File file = Environment.getExternalStorageDirectory();
      if (file != null && file.exists()) {
        StatFs sdFs = new StatFs(file.getPath());
        if (sdFs != null) {
          long sdBlockSize = sdFs.getBlockSize();
          long sdTotalCount = sdFs.getBlockCount();
          return sdTotalCount * sdBlockSize;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static long getAvailableInternalStorage() {
    File file = Environment.getDataDirectory();
    if (file != null && file.exists()) {
      StatFs sdFs = new StatFs(file.getPath());
      if (sdFs != null) {
        long sdBlockSize = sdFs.getBlockSize();
        long sdAvailCount = sdFs.getAvailableBlocks();
        return sdAvailCount * sdBlockSize;
      }
    }
    return 0;
  }

  public static long getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    if (path != null && path.exists()) {
      StatFs stat = new StatFs(path.getPath());
      long blockSize = stat.getBlockSize();
      long totalBlocks = stat.getBlockCount();
      return totalBlocks * blockSize;
    }
    return 0;
  }

  public static boolean checkSdCardStatusOk() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  public static boolean checkAvailableInternalStorage(long size) {
    long availabelStorage = SystemUtil.getAvailableInternalStorage();
    // if apkSize is -1 , do not check
    if (size < 0) {
      return true;
    }
    if (availabelStorage <= 0) {
      return false;
    }
    return availabelStorage >= size;
  }

  public static boolean checkAvailableExternalStorage(long size) {
    long availabelStorage = SystemUtil.getAvailableExternalStorage();
    // if apkSize is -1 , do not check
    if (size < 0) {
      return true;
    }
    if (availabelStorage <= 0) {
      return false;
    }
    return availabelStorage >= size;
  }

  public static boolean checkSpaceEnough(String path, InstallOption installOpition) {

    if (TextUtils.isEmpty(path) || installOpition == null) {
      return false;
    }
    if (installOpition == InstallOption.AUTO) {
      return true;
    }
    File file = new File(path);
    if (installOpition == InstallOption.INTERNAL) {
      return SystemUtil.checkAvailableInternalStorage(file.length());
    }
    if (installOpition == InstallOption.EXTERNAL) {
      return SystemUtil.checkAvailableStorage(file.length());
    }

    return false;
  }

  public static boolean checkAvailableStorage(long size) {
    long availabelStorage = SystemUtil.getAvailableExternalStorage();
    // if apkSize is -1 , do not check
    if (size < 0) {
      return true;
    }
    if (availabelStorage <= 0) {
      return false;
    }
    return availabelStorage >= size;
  }

  public static int getVersionCode(Context context) {
    if (versionCode != 0) {
      return versionCode;
    }
    PackageInfo packageInfo;
    try {
      packageInfo = context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0);
      versionCode = packageInfo.versionCode;
      return versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static void sleepZero() {
    try {
      Thread.sleep(0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String getNonNullModel() {
    if (TextUtils.isEmpty(Build.MODEL)) {
      return "";
    } else {
      return Build.MODEL;
    }
  }

  public static IBinder invokeGetService(String name) {
    Method method;
    try {
      method = Class.forName("android.os.ServiceManager").getMethod(
          "getService", String.class);
    } catch (SecurityException e) {
      e.printStackTrace();
      return null;
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      return null;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    IBinder binder;
    try {
      binder = (IBinder) method.invoke(null, name);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return null;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
    return binder;
  }

  public static String getImei(Context context) {
    try {
      TelephonyManager telephonyManager = (TelephonyManager) context
          .getSystemService(Context.TELEPHONY_SERVICE);
      return telephonyManager.getDeviceId();
    } catch (Exception e) {
      // In some devices, we are not able to get device id, and may cause some exception,
      // so catch it.
      return "";
    }
  }

  public static String getVersionName(Context context) {
    if (packageVersionName == null) {
      PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), 0);
      if (packageInfo != null) {
        packageVersionName = packageInfo.versionName;
      } else {
        packageVersionName = "";
      }

    }
    return packageVersionName;
  }

  public static PackageInfo getPackageInfo(Context context, String packageName, int flag) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = packageManager.getPackageInfo(packageName, flag);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      // In some ROM, there will be a PackageManager has died exception. So we catch it here.
      e.printStackTrace();
    }
    return packageInfo;
  }

  @TargetApi(9)
  public static boolean isPrimaryExternalStorageRemoveable() {
    if (getApiLevel() >= 9) {
      return Environment.isExternalStorageRemovable();
    } else {
      return true;
    }
  }


  public static int getScreentHeight(WindowManager windowManager) {
    int heightPixels = 0;
    Display defaultDisplay = windowManager.getDefaultDisplay();
    if (SystemUtil.aboveApiLevel(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        && !SystemUtil.aboveApiLevel(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
      try {
        heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(defaultDisplay);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (SystemUtil.aboveApiLevel(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
      android.graphics.Point realSize = new android.graphics.Point();
      defaultDisplay.getRealSize(realSize);
      heightPixels = realSize.y;
    } else {
      DisplayMetrics metrics = new DisplayMetrics();
      defaultDisplay.getMetrics(metrics);
      heightPixels = metrics.heightPixels;
    }
    return heightPixels;
  }

  public static int getScreentWidth(WindowManager windowManager) {
    int widthPixels = 0;
    Display defaultDisplay = windowManager.getDefaultDisplay();
    if (SystemUtil.aboveApiLevel(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        && !SystemUtil.aboveApiLevel(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
      try {
        widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(defaultDisplay);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (SystemUtil.aboveApiLevel(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
      android.graphics.Point realSize = new android.graphics.Point();
      defaultDisplay.getRealSize(realSize);
      widthPixels = realSize.x;
    } else {
      DisplayMetrics metrics = new DisplayMetrics();
      defaultDisplay.getMetrics(metrics);
      widthPixels = metrics.widthPixels;
    }
    return widthPixels;
  }

  public static boolean hasSoftKeys(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }
    return false;
  }

  public static enum InstallOption {
    AUTO, EXTERNAL, INTERNAL, ERROR
  }

  /**
   * check if the mobile has been rooted
   * 
   * @exception java.io.IOException
   * @return the mobile has been rooted
   * @author TQS
   */
  public static boolean isRooted() {
    boolean rooted = false;
    boolean hasSuFile = false;
    String command = "ls -l /%s/su";
    File su = new File("/system/bin/su");
    if (su.exists()) {
      hasSuFile = true;
      command = String.format(command, "system/bin");
    } else {
      su = new File("/system/xbin/su");
      if (su.exists()) {
        hasSuFile = true;
        command = String.format(command, "system/xbin");
      } else {
        su = new File("/data/bin/su");
        if (su.exists()) {
          hasSuFile = true;
          command = String.format(command, "data/bin");
        }
      }
    }

    if (hasSuFile == true) {
      rooted = true;
    }

    return rooted;
  }


  public static String getSystemDisplayId() {
    if (TextUtils.isEmpty(Build.DISPLAY)) {
      return "";
    } else {
      return Build.DISPLAY;
    }
  }

  public static String getBrand() {
    if (TextUtils.isEmpty(Build.BRAND)) {
      return "";
    } else {
      return Build.BRAND;
    }
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  public static int getMetricsSize(WindowManager windowManager) {
    if (windowManager == null) {
      return 0;
    }
    DisplayMetrics metrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(metrics);
    return metrics.densityDpi;
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  public static String getDpi(WindowManager windowManager) {
    if (windowManager == null) {
      return "";
    }
    int densityDpi = getMetricsSize(windowManager);
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "ldpi";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhpdi";
      case DisplayMetrics.DENSITY_XXHIGH:
      default:
        return "xxhdpi";
    }
  }

  public static String getMetaChannel(Context context) {
    String channel = "";
    try {
      Bundle data = context.getPackageManager().getApplicationInfo(
          context.getPackageName(), PackageManager.GET_META_DATA).metaData;
      if (data != null) {
        channel = data.getString(META_CHANNEL);
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return channel;
  }

}
