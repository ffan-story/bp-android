package com.feifan.bp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.feifan.bp.PlatformState;
import com.feifan.material.MaterialDialog;

/**
 * Created by xuchunlei on 16/2/29.
 */
public class DialogUtil {

    // 全局Dialog
    private static AlertDialog sDialog;

    private DialogUtil() {

    }

    public static AlertDialog getDialog() {
        if(sDialog == null) {
            sDialog = new AlertDialog.Builder(PlatformState.getApplicationContext()).setCancelable(false).create();
            sDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        return sDialog;
    }

    public static void clearDialog() {
        if(sDialog != null) {
            sDialog.dismiss();
        }
    }

}
