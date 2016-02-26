package com.feifan.bp.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Frank on 16/2/26.
 */
public class SystemAlterDialog {

    private static AlertDialog mDialog = null;

    public static AlertDialog getInstance(final Context context) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(context).setCancelable(false).create();
            mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//设置系统级别AlterDialog
        }
        return mDialog;
    }

    public static void dismissSystemDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
