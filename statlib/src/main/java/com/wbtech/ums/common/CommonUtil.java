/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @since Version 0.1
 * @filesource
 */
package com.wbtech.ums.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.wanda.base.utils.CollectionUtils;
import com.wbtech.ums.objects.LatitudeAndLongitude;
import com.wbtech.ums.objects.SCell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommonUtil {
  /**
   * checkPermissions
   * 
   * @param context
   * @param permission
   * @return true or false
   */
  public static boolean checkPermissions(Context context, String permission) {
    PackageManager localPackageManager = context.getPackageManager();
    return localPackageManager.checkPermission(permission, context
        .getPackageName()) == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Determine the current networking is WIFI
   * 
   * @param context
   * @return
   */
  public static boolean currentNoteworkTypeIsWIFI(Context context) {
    ConnectivityManager connectionManager = (ConnectivityManager) context.
        getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectionManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
  }


  /**
   * Judge wifi is available
   * 
   * @param inContext
   * @return
   */
  public static boolean isWiFiActive(Context inContext) {
    if (checkPermissions(inContext, "android.permission.ACCESS_WIFI_STATE")) {
      Context context = inContext.getApplicationContext();
      ConnectivityManager connectivity = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivity != null) {
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
          for (int i = 0; i < info.length; i++) {
            if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
              return true;
            }
          }
        }
      }
      return false;
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permission", "lost--->android.permission.ACCESS_WIFI_STATE");
      }

      return false;
    }
  }


  /**
   * Testing equipment networking and networking WIFI
   * 
   * @param context
   * @return true or false
   */
  public static boolean isNetworkAvailable(Context context) {
    if (checkPermissions(context, "android.permission.INTERNET")) {
      ConnectivityManager cManager = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cManager.getActiveNetworkInfo();
      if (info != null && info.isAvailable()) {
        return true;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("error", "Network error");
        }
        return false;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e(" lost  permission",
            "lost----> android.permission.INTERNET");
      }
      return false;
    }
  }

  /**
   * Get the current time format yyyy-MM-dd HH:mm:ss
   * 
   * @return
   */
  public static String getTime() {
    Date date = new Date();
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");
    return localSimpleDateFormat.format(date);
  }

  /**
   * get APPKEY
   * 
   * @param context
   * @return appkey
   */
  public static String getAppKey(Context paramContext) {
    String umsAppkey;
    try {
      PackageManager localPackageManager = paramContext
          .getPackageManager();
      ApplicationInfo localApplicationInfo = localPackageManager
          .getApplicationInfo(paramContext.getPackageName(), 128);
      if (localApplicationInfo != null) {
        String str = localApplicationInfo.metaData
            .getString("UMS_APPKEY");
        if (str != null) {
          umsAppkey = str;
          return umsAppkey.toString();
        }
        if (UmsConstants.DebugMode)
          Log.e("UmsAgent", "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
      }
    } catch (Exception localException) {
      if (UmsConstants.DebugMode) {
        Log.e("UmsAgent", "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
        localException.printStackTrace();
      }
    }
    return null;
  }

  /**
   * get currnet activity's name
   * 
   * @param context
   * @return
   */
  public static String getActivityName(Context context) {
    ActivityManager am = (ActivityManager) context
        .getSystemService(Context.ACTIVITY_SERVICE);
    if (checkPermissions(context, "android.permission.GET_TASKS")) {
      ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
      return cn.getShortClassName();
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permission", "android.permission.GET_TASKS");
      }

      return null;
    }


  }

  /**
   * get PackageName
   * 
   * @param context
   * @return
   */
  public static String getPackageName(Context context) {
    ActivityManager am = (ActivityManager) context
        .getSystemService(Context.ACTIVITY_SERVICE);

    if (checkPermissions(context, "android.permission.GET_TASKS")) {
      ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
      return cn.getPackageName();
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permission", "android.permission.GET_TASKS");
      }

      return null;
    }

  }


  /**
   * get OS number
   * 
   * @param context
   * @return
   */
  public static String getOsVersion(Context context) {
    String osVersion = "";
    if (checkPhoneState(context)) {
      osVersion = android.os.Build.VERSION.RELEASE;
      if (UmsConstants.DebugMode) {
        printLog("android_osVersion", "OsVerson" + osVersion);
      }

      return osVersion;
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("android_osVersion", "OsVerson get failed");
      }

      return null;
    }
  }

  /**
   * get deviceid
   * 
   * @param context
   *          add <uses-permission android:name="READ_PHONE_STATE" />
   * @return
   */
  public static String getDeviceID(Context context) {
    if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
      String deviceId = "";
      if (checkPhoneState(context)) {
        TelephonyManager tm = (TelephonyManager) context
            .getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
      }
      if (deviceId != null) {
        if (UmsConstants.DebugMode) {
          printLog("commonUtil", "deviceId:" + deviceId);
        }

        return deviceId;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("commonUtil", "deviceId is null");
        }

        return null;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permissioin", "lost----->android.permission.READ_PHONE_STATE");
      }

      return "";
    }
  }

  /**
   * check phone _state is readied ;
   * 
   * @param context
   * @return
   */
  public static boolean checkPhoneState(Context context) {
    PackageManager packageManager = context.getPackageManager();
    if (packageManager.checkPermission("android.permission.READ_PHONE_STATE", context
        .getPackageName()) != 0) {
      return false;
    }
    return true;
  }

  /**
   * get sdk version
   * 
   * @return
   */
  public static int getSDKVersion() {
    int osVersion;
    try {
      osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
    } catch (NumberFormatException e) {
      osVersion = 0;
    }

    return osVersion;
  }

  /**
   * get device desc
   * 
   * @param paramContext
   * @return
   */
  public static String getDeviceDesc(Context context) {
    String deviceDesc = android.os.Build.MODEL;
    return deviceDesc;
  }

  /**
   * get app version
   * 
   * @param paramContext
   * @return
   */
  public static String getAppVersion(Context context) {
    String versionCode = null;
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionCode = pi.versionName;
      Log.i("", "versionCode = " + versionCode);
      if (versionCode == null || versionCode.length() <= 0) {
        return null;
      }
    } catch (Exception e) {
      if (UmsConstants.DebugMode) {
        Log.e("UmsAgent", "Exception", e);
      }
    }
    return versionCode;
  }


  /**
   * Get the version number of the current program
   * 
   * @param context
   * @return
   */

  public static String getCurVersion(Context context) {
    String curversion = "";
    try {
      // ---get the package info---
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm
          .getPackageInfo(context.getPackageName(), 0);
      curversion = pi.versionName;
      if (curversion == null || curversion.length() <= 0) {
        return "";
      }
    } catch (Exception e) {
      if (UmsConstants.DebugMode) {
        Log.e("VersionInfo", "Exception", e);
      }

    }
    return curversion;
  }

  /**
   * Get the current send model
   * 
   * @param context
   * @return
   */
  public static int getReportPolicyMode(Context context) {
    String str = context.getPackageName();
    SharedPreferences localSharedPreferences = context
        .getSharedPreferences("ums_agent_online_setting_" + str, 0);
    int type = localSharedPreferences.getInt("ums_local_report_policy", 0);
    return type;
  }

  /**
   * Get the base station information
   * 
   * @throws Exception
   */
  public static SCell getCellInfo(Context context) throws Exception {
    SCell cell = new SCell();
    TelephonyManager mTelNet =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
    if (location == null) {
      if (UmsConstants.DebugMode) {
        Log.e("GsmCellLocation Error", "GsmCellLocation is null");
      }
      return null;
    }


    String operator = mTelNet.getNetworkOperator();
    // System.out.println("operator------>"+operator.toString());
    int mcc = Integer.parseInt(operator.substring(0, 3));
    int mnc = Integer.parseInt(operator.substring(3));
    int cid = location.getCid();
    int lac = location.getLac();

    /** 通过getNeighboringCellInfo获取BSSS */
    List<NeighboringCellInfo> infoLists = mTelNet.getNeighboringCellInfo();
    int strength = 0;
    if (!CollectionUtils.isEmpty(infoLists)) {
      for (NeighboringCellInfo info : infoLists) {
        strength += (-133 + 2 * info.getRssi());// 获取邻区基站信号强度
      }
    }
    cell.MCC = mcc;
    cell.MCCMNC = Integer.parseInt(operator);
    cell.MNC = mnc;
    cell.LAC = lac;
    cell.CID = cid;
    cell.BSSS = strength;

    return cell;
  }

  public static LatitudeAndLongitude getLatitudeAndLongitude(Context context,
      boolean mUseLocationService) {
    LatitudeAndLongitude latitudeAndLongitude = new LatitudeAndLongitude();
    if (mUseLocationService) {
      LocationManager loctionManager =
          (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      List<String> matchingProviders = loctionManager.getAllProviders();
      for (String prociderString : matchingProviders) {
        // Log.d("provider",prociderString);
        System.out.println(prociderString);
        Location location = loctionManager.getLastKnownLocation(prociderString);
        if (location != null) {
          // Log.d("ss", location.getLatitude()+"");
          latitudeAndLongitude.latitude = location.getLatitude() + "";
          latitudeAndLongitude.longitude = location.getLongitude() + "";
        } else {
          latitudeAndLongitude.latitude = "";
          latitudeAndLongitude.longitude = "";
        }
      }
    } else {
      latitudeAndLongitude.latitude = "";
      latitudeAndLongitude.longitude = "";
    }


    return latitudeAndLongitude;

  }


  /**
   * To determine whether it contains a gyroscope
   * 
   * @return
   */
  public static boolean isHaveGravity(Context context) {
    SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    if (manager == null) {
      return false;
    }
    return true;
  }

  /**
   * Get the current networking
   * 
   * @param context
   * @return WIFI or MOBILE
   */
  public static String getNetworkType(Context context) {
    TelephonyManager manager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    int type = manager.getNetworkType();
    String typeString = "UNKNOWN";
    if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
      typeString = "CDMA";
    }
    if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
      typeString = "EDGE";
    }
    if (type == TelephonyManager.NETWORK_TYPE_EVDO_0) {
      typeString = "EVDO_0";
    }
    if (type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
      typeString = "EVDO_A";
    }
    if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
      typeString = "GPRS";
    }
    if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
      typeString = "HSDPA";
    }
    if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
      typeString = "HSPA";
    }
    if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
      typeString = "HSUPA";
    }
    if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
      typeString = "UMTS";
    }
    if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
      typeString = "UNKNOWN";
    }
    return typeString;
  }

  /**
   * Determine the current network type
   * 
   * @param context
   * @return
   */
  public static boolean isNetworkTypeWifi(Context context) {
    // TODO Auto-generated method stub


    if (checkPermissions(context, "android.permission.INTERNET")) {
      ConnectivityManager cManager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cManager.getActiveNetworkInfo();

      if (info != null && info.isAvailable() && info.getTypeName().equals("WIFI")) {
        return true;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("error", "Network not wifi");
        }
        return false;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e(" lost  permission", "lost----> android.permission.INTERNET");
      }
      return false;
    }
  }

  /**
   * get mobile operators
   * 
   * @param context
   * @return
   */
  public static String getMobileOperators(Context context) {
    TelephonyManager telephonyManager = (TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE);
    String operatorString = telephonyManager.getSimOperator();
    if (operatorString == null) {
      return "未知";
    }
    if (operatorString.equals("46000") || operatorString.equals("46002")) {
      // 中国移动 1
      return "中国移动";
    } else if (operatorString.equals("46001")) {
      // 中国联通
      return "中国联通";
    } else if (operatorString.equals("46003")) {
      // 中国电信
      return "中国电信";
    }
    // error
    return "未知";
  }

  /**
   * get horizontal flag
   * 
   * @param context
   * @return
   */
  public static int getHorizontalFlag(Context context) {
    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Get the current application version number
   * 
   * @param context
   * @return
   */
  public static String getVersion(Context context) {
    String versionName = "";
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionName = pi.versionName;
      if (versionName == null || versionName.length() <= 0) {
        return "";
      }
    } catch (Exception e) {
      if (UmsConstants.DebugMode) {
        Log.e("UmsAgent", "Exception", e);
      }

    }
    return versionName;
  }

  /**
   * Get the current application version number
   * 
   * @param context
   * @return
   */
  public static String getDeviceId(Context context) {
    String versionName = "";
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
      // pi.packageName;
      // pi.

      versionName = pi.versionName;
      if (versionName == null || versionName.length() <= 0) {
        return "";
      }
    } catch (Exception e) {
      if (UmsConstants.DebugMode) {
        Log.e("UmsAgent", "Exception", e);
      }

    }
    return versionName;
  }

  /**
   * Set the output log
   * 
   * @param tag
   * @param log
   */

  public static void printLog(String tag, String log) {
    if (UmsConstants.DebugMode == true) {
      Log.d(tag, log);
    }
  }


  /**
   * get IMSI
   *
   * @param context
   *          add <uses-permission android:name="READ_PHONE_STATE" />
   * @return
   */
  public static String getIMSI(Context context) {
    if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
      String imsi = "";
      if (checkPhoneState(context)) {
        TelephonyManager tm = (TelephonyManager) context
            .getSystemService(Context.TELEPHONY_SERVICE);
        imsi = tm.getSubscriberId();
      }
      if (imsi != null) {
        if (UmsConstants.DebugMode) {
          printLog("commonUtil", "imsi:" + imsi);
        }

        return imsi;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("commonUtil", "imsi is null");
        }

        return null;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permissioin", "lost----->android.permission.READ_PHONE_STATE");
      }

      return "";
    }
  }

  /**
   * PHONE_WIFI_MAC
   *
   * @param context
   * @return
   */
  public static String getPhoneWifiMac(Context context) {
    if (checkPermissions(context, "android.permission.ACCESS_WIFI_STATE")) {
      WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      WifiInfo info = wifiMan.getConnectionInfo();
      String mac = info.getMacAddress();
      if (mac != null) {
        if (UmsConstants.DebugMode) {
          printLog("commonUtil", "mac:" + mac);
        }

        return mac;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("commonUtil", "mac is null");
        }

        return null;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permission", "lost--->android.permission.ACCESS_WIFI_STATE");
      }
      return "";
    }
  }

  /**
   * ROUTER_MAC
   *
   * @param context
   * @return
   */
  public static String getRouterMac(Context context) {
    if (checkPermissions(context, "android.permission.ACCESS_WIFI_STATE")) {
      String netMac = "";
      WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (mWifi.isWifiEnabled()) {
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        // 获取被连接网络的mac地址
        netMac = wifiInfo.getBSSID();
      }
      if (netMac != null) {
        if (UmsConstants.DebugMode) {
          printLog("commonUtil", "netMac:" + netMac);
        }

        return netMac;
      } else {
        if (UmsConstants.DebugMode) {
          Log.e("commonUtil", "netMac is null");
        }

        return null;
      }
    } else {
      if (UmsConstants.DebugMode) {
        Log.e("lost permission", "lost--->android.permission.ACCESS_WIFI_STATE");
      }
      return "";
    }
  }
}
