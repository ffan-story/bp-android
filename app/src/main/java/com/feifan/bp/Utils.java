package com.feifan.bp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
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
        mhandler.removeCallbacks(r);
        if (null != mToast) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, duration);
        }
        mhandler.postDelayed(r, 5000);
        mToast.show();
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
     * 显示长时间的toast提示
     *
     * @param message
     * @param gravity 停靠位置
     */
    public static void showLongToast(Context context, int message, int gravity) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(gravity, 0, 0);
        toast.show();
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
     * 判断当前网络是否可用
     *
     * @return
     */
    public static boolean isCurrentNetworkAvailable(Context context) {
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
        return false;
    }

    /**
     * 是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static void checkPhoneNumber(Context context, String phoneNumber) throws Throwable {
        Pattern p = Pattern.compile("^[1][0-9][0-9]{9}$");
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
        return intent;
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
                result = result+"0";
            }
        }else{
        }
        return result;
    }

    /**
     * 获取字符串内容
     * <pre>
     *     使用ApplicationContext，适用于没有Context的场景
     * </pre>
     * @param resource
     * @return
     */
    public static String getString(int resource) {
        if(resource > 0) {
            return PlatformState.getApplicationContext().getString(resource);
        }
        return null;
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
//            if (file.getName().contains(filter)) {
//                file.delete();
//                LogUtil.i(Constants.TAG, "deleted" + file.getAbsolutePath());
//            }

        }
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
