package com.feifan.bp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

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
}
