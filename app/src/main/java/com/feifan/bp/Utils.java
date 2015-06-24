package com.feifan.bp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuchunlei on 15/6/24.
 */
public class Utils {

    private Utils() {

    }

    /**
     * 显示短时间的toast提示
     * @param message
     * @param gravity 停靠位置
     */
    public static void showShortToast(String message, int gravity) {

        Toast toast = Toast.makeText(PlatformState.getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 显示短时间的toast提示
     * @param message 提示消息的资源ID
     * @param gravity 停靠位置
     */
    public static void showShortToast(int message, int gravity) {

        Toast toast = Toast.makeText(PlatformState.getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 显示短时间的toast提示
     * @param message
     */
    public static void showShortToast(String message) {
        Toast.makeText(PlatformState.getApplicationContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示短时间的toast提示
     * @param message
     */
    public static void showShortToast(int message) {
        Toast.makeText(PlatformState.getApplicationContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)PlatformState.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
     * @param phoneNumber
     * @return
     */
    public static void checkPhoneNumber(String phoneNumber) throws Throwable{
        Pattern p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
        Matcher m = p.matcher(phoneNumber);
        if(!m.matches()) {
            throw new Throwable(PlatformState.getApplicationContext().getString(R.string.error_message_text_phone_number_illegal));
        }
    }
}
