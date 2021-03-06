package com.feifan.bp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import android.view.WindowManager;
import android.widget.Toast;

import com.feifan.bp.util.LogUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuchunlei on 15/6/24.
 */
public class Utils {

    private static final String TAG = "Utils";
    private static Toast mToast;
    private static AlertDialog mDialog;
    private static Handler mhandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    private Utils() {

    }

    /**
     * 显示不重复的toast提示
     *
     * @param text
     */
    public static void showToast(Context context, int text, int duration) {
        if(context != null) {
            mhandler.removeCallbacks(r);
            if (null != mToast) {
                mToast.setText(text);
            } else {
                mToast = Toast.makeText(context, text, duration);
            }
            mhandler.postDelayed(r, 5000);
            mToast.show();
        }
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     * @param gravity 停靠位置
     */
    public static void showShortToast(Context context, String message, int gravity) {
        if(context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message 提示消息的资源ID
     * @param gravity 停靠位置
     */
    public static void showShortToast(Context context, int message, int gravity) {
        if(context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     */
    public static void showShortToast(Context context, String message) {
        if(context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     */
    public static void showShortToast(Context context, int message) {
        if(context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 显示长时间的toast提示
     *
     * @param message
     * @param gravity 停靠位置
     */
    public static void showLongToast(Context context, int message, int gravity) {
        if(context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        final Context context = PlatformState.getApplicationContext();
        if(context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前网络是否可用
     *
     * @return
     */
    public static boolean isCurrentNetworkAvailable(Context context) {
        if(context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
            } else {
                //如果仅仅是用来判断网络连接
                //则可以使用 cm.getActiveNetworkInfo().isAvailable();
                NetworkInfo[] info = cm.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static void checkPhoneNumber(String phoneNumber) throws Throwable {
        Pattern p = Pattern.compile("^[1][0-9][0-9]{9}$");
        Matcher m = p.matcher(phoneNumber);
        if (!m.matches()) {
            throw new Throwable(getString(R.string.error_message_text_phone_number_illegal));
        }
    }

    public static void checkDigitAndLetter(String value) throws Throwable {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            throw new Throwable(getString(R.string.error_message_text_search_illegal_format));
        }
    }

    /**
     * 包含0-9 || A-Z || a-z 时，返回false,否则返回true
     *
     * @param value
     * @return
     */
    public static boolean isNotDigitAndLetter(String value) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 打开浏览器
     *
     * @param url
     */
    public static Intent getSystemBrowser(String url) {
        if(!TextUtils.isEmpty(url)) {
            LogUtil.i(TAG, "getSystemBrowser() url=" + url);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            return intent;
        }
        return null;
    }

    public static boolean isChineseChar(String str) {
        boolean result = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            result = true;
        }
        return result;
    }

    /**
     * 获取sdcar路径
     *
     * @return
     */
    public static boolean isHasSdCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /**
     * 金额格式化,支持精度设置
     *
     * @param amount
     * @return
     */
    public static String formatMoney(String amount, int len) {

        if (amount == null || amount.length() < 1) {
            return "0.00";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(amount);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = result + ".00";
        } else if (result.contains(".")) {
            String[] strs = result.split("\\.");
            if (strs[1].length() == 1) {
                result = result + "0";
            }
        } else {
        }
        return result;
    }

    /**
     * 获取字符串内容
     * <pre>
     *     使用ApplicationContext，适用于没有Context的场景
     * </pre>
     *
     * @param resource
     * @return
     */
    public static String getString(int resource) {
        if (resource > 0) {
            return PlatformState.getApplicationContext().getString(resource);
        }
        return null;
    }

    /**
     * 消除登录失效弹框
     */
    public static void dismissLoginDialog(){
        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

//    /**
//     * 静态 LaunchActivity 用于获取 Handle ，发送Handle message
//     */
//    public static  LaunchActivity myLunchActivitys;

    /**
     * 输出格式化webview URL
     * @param webViewUrl
     */
    public static void logUrlFormat(String webViewUrl){
        if (!TextUtils.isEmpty(webViewUrl)){
            if (webViewUrl.contains("?")){
//                if (BuildConfig.DEBUG){
//                    Message message = new Message();
//                    message.what = 1;
//                    Bundle b = new Bundle();
//                    b.putString("MESSAGE", webViewUrl.split("[?]")[0] + "\n" + webViewUrl.split("[?]")[1].replace("&", "\n"));
//                    message.setData(b);
//                    myLunchActivitys.myHandler.sendMessage(message);
//                }
                LogUtil.i("URL","WebView URl:"+webViewUrl.split("[?]")[0] + "\n" + webViewUrl.split("[?]")[1].replace("&", "\n"));
            }else{
                LogUtil.i("URL","WebView URl:"+webViewUrl);
            }

        }
    }

    /**
     * 以安全的方式显示Toast
     * @param message
     */
    public static void showShortToastSafely(int message) {
        Toast.makeText(PlatformState.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 以安全的方式显示Toast
     * @param message
     */
    public static void showShortToastSafely(String message) {
        Toast.makeText(PlatformState.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 以安全的方式长时间显示Toast
     * @param message
     */
    public static void showLongToastSafely(int message) {
        Toast.makeText(PlatformState.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
