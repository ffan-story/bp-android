package com.bp.crash;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

public class CrashUtil {

  public static String getMyUUID(Context context){
    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    final String tmDevice, tmSerial, tmPhone, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
    String uniqueId = deviceUuid.toString();
    Log.d("debug","uuid="+uniqueId);
    return uniqueId;
  }
}
