package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuchunlei on 15/6/24.
 */
public class Utils {

    private static final String TAG = "Utils";

    private Utils() {

    }


    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     * @param gravity 停靠位置
     */
    public static void showShortToast(Context context, String message, int gravity) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message 提示消息的资源ID
     * @param gravity 停靠位置
     */
    public static void showShortToast(Context context, int message, int gravity) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     */
    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示短时间的toast提示
     *
     * @param message
     */
    public static void showShortToast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
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
    public static void checkPhoneNumber(Context context, String phoneNumber) throws Throwable {
        Pattern p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
        Matcher m = p.matcher(phoneNumber);
        if (!m.matches()) {
            throw new Throwable(context.getString(R.string.error_message_text_phone_number_illegal));
        }
    }

    public static void checkDigitAndLetter(Context context, String value) throws Throwable {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            throw new Throwable(context.getString(R.string.error_message_text_search_illegal_format));
        }
    }

    /**
     * 删除文件
     *
     * @param file   删除的文件或目录
     * @param filter 过滤字符串，文件名中包含该字符串的文件都将被删除
     */
    public static void deleteFile(File file, String filter) {
        if (file.exists()) {
            if (file.isFile() && file.getName().contains(filter)) {
                file.delete();
                LogUtil.i(Constants.TAG, "deleted" + file.getAbsolutePath());
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i], filter);
                }
            }
            if (file.getName().contains(filter)) {
                file.delete();
                LogUtil.i(Constants.TAG, "deleted" + file.getAbsolutePath());
            }

        }
    }

    /**
     * 打开浏览器
     *
     * @param url
     */
    public static Intent getSystemBrowser(String url) {
        LogUtil.i(TAG, "getSystemBrowser() url=" + url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        return intent;
    }
}
