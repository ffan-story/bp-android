package com.feifan.bp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.material.MaterialDialog;

/**
 * Created by xuchunlei on 16/2/29.
 */
public class DialogUtil {

    // 全局Dialog
    private static AlertDialog sDialog;
    private static Window mAlertDialogWindow;
    private static TextView mMessageView;
    private static TextView mPostiveView;

    private DialogUtil() {

    }

    public static AlertDialog getDialog() {
        if (sDialog == null) {
            sDialog = new AlertDialog.Builder(PlatformState.getApplicationContext()).setCancelable(false).create();
            sDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        return sDialog;
    }

    public static void clearDialog() {
        if (sDialog != null) {
            sDialog.dismiss();
        }
    }

    public static void setDialogStyle(String msg, View.OnClickListener onClickListener) {
        mAlertDialogWindow = sDialog.getWindow();
        View contv = LayoutInflater.from(PlatformState.getApplicationContext()).inflate(R.layout.layout_system_alterdialog, null);
        contv.setFocusable(true);
        contv.setFocusableInTouchMode(true);

        mAlertDialogWindow.setBackgroundDrawableResource(com.feifan.materialwidget.R.drawable.material_dialog_window);
        mAlertDialogWindow.setContentView(contv);
        mMessageView = (TextView) mAlertDialogWindow.findViewById(R.id.message);
        mMessageView.setText(msg);
        mPostiveView = (TextView) mAlertDialogWindow.findViewById(R.id.positive);
        mPostiveView.setOnClickListener(onClickListener);
    }

}
