package com.feifan.statlib;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xuchunlei on 15/12/8.
 */
class DeviceInfo {

    private static final String TAG = "DeviceInfo";
    private static Context context;
    private static Location location;
    private static TelephonyManager telephonyManager;
    private static LocationManager locationManager;
    private static BluetoothAdapter bluetoothAdapter;
    private static SensorManager sensorManager;
    /**
     * id : 1142
     * name : 订单管理
     * url : H5App/index.html#/order
     */

    private int id;
    private String name;
    private String url;

    public static void init(Context context) {
        DeviceInfo.context = context;

        try {
            telephonyManager = (TelephonyManager) (context
                    .getSystemService(Context.TELEPHONY_SERVICE));
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
        }
        getLocation();
    }

    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        StatLog.i(TAG, "getLanguage()=" + language);
        if (language == null)
            return "";
        return language;
    }

    public static String getResolution() {

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        StatLog.i(TAG, "getResolution()=" + displaysMetrics.widthPixels + "x"
                + displaysMetrics.heightPixels);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static String getDeviceProduct() {
        String result = Build.PRODUCT;
        StatLog.i(TAG, "getDeviceProduct()=" + result);
        if (result == null)
            return "";
        return result;
    }

    public static boolean getBluetoothAvailable() {
        if (bluetoothAdapter == null)
            return false;
        else
            return true;
    }

    private static boolean isSimulator() {
        if (getDeviceIMEI().equals("000000000000000"))
            return true;
        else
            return false;
    }

    public static boolean getGravityAvailable() {
        try {
            // This code getSystemService(Context.SENSOR_SERVICE);
            // often hangs out the application when it runs in Android Simulator.
            // so in simulator, this line will not be run.
            if (isSimulator())
                sensorManager = null;
            else
                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            StatLog.i(TAG, "getGravityAvailable()");
            return (sensorManager == null) ? false : true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getOsVersion() {
        String result = "Android " + Build.VERSION.RELEASE;

        StatLog.i(TAG, "getOsVersion()=" + result);
        if (result == null)
            return "";

        return result;
    }

    /**
     * Returns a constant indicating the device phone type. This indicates the
     * type of radio used to transmit voice calls.
     *
     * @return PHONE_TYPE_NONE //0 PHONE_TYPE_GSM //1 PHONE_TYPE_CDMA //2
     *         PHONE_TYPE_SIP //3
     */
    public static int getPhoneType() {
        if (telephonyManager == null)
            return -1;
        int result = telephonyManager.getPhoneType();
        StatLog.i(TAG, "getPhoneType()=" + result);
        return result;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                StatLog.e(TAG,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSubscriberId();
            StatLog.i(TAG, "getIMSI()=" + result);
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }

        return result;
    }

    public static String getWifiMac() {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wifiManager.getConnectionInfo();
            String result = wi.getMacAddress();
            if (result == null)
                result = "";
            StatLog.i(TAG, "getWifiMac()=" + result);
            return result;
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
            return "";
        }

    }

    public static String getDeviceTime() {
        try {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);
            String result = localSimpleDateFormat.format(date);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer == null)
                manufacturer = "";
            String model = Build.MODEL;
            if (model == null)
                model = "";

            if (model.startsWith(manufacturer)) {
                return capitalize(model).trim();
            } else {
                return (capitalize(manufacturer) + " " + model).trim();
            }
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static String getNetworkTypeWIFI2G3G() {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            String type = ni.getTypeName().toLowerCase(Locale.US);
            if (!type.equals("wifi")) {
                type = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getExtraInfo();
            }
            return type;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean getWiFiAvailable() {
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                StatLog.e(TAG,
                        "ACCESS_WIFI_STATE permission should be added into AndroidManifest.xml.");
                return false;
            }
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getTypeName().equals("WIFI")
                                && info[i].isConnected()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDeviceIMEI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                StatLog.e(TAG,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getDeviceId();
            if (result == null)
                result = "";
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return result;
    }

    private static String getSSN() {
        String result = "";
        try {

            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                StatLog.e(TAG,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSimSerialNumber();
            if (result == null)
                result = "";
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return result;
    }

    public static String getDeviceId() {
        String result = null;
        try {
            String imei = getDeviceIMEI();
            String imsi = getIMSI();
            String salt = CommonUtil.getSALT(context);

            result = CommonUtil.md5Appkey(imei + imsi + salt);

        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return result;
    }

    public static String getLatitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLatitude());
    }

    public static String getLongitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLongitude());

    }

    public static String getGPSAvailable() {
        if (location == null)
            return "false";
        else
            return "true";
    }

    private static void getLocation() {
        StatLog.i(TAG, "getLocation");
        try {
            List<String> matchingProviders = locationManager.getAllProviders();
            for (String prociderString : matchingProviders) {
                location = locationManager.getLastKnownLocation(prociderString);
                if (location != null)
                    break;
            }
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
        }
    }

    public static String getMCCMNC() {
        String result = "";
        try {

            String operator = telephonyManager.getNetworkOperator();
            if (operator == null)
                result = "";
            else
                result = operator;
        } catch (Exception e) {
            result = "";
            StatLog.e(TAG, e.toString());
        }
        return result;
    }

    public static int getSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return version;
    }



    /**
     * Capitalize the first letter
     *
     * @param s model,manufacturer
     * @return Capitalize the first letter
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
