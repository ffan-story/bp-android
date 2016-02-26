package com.feifan.bp.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import com.feifan.bp.LaunchActivity;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;

/**
 * Created by Frank on 16/2/26.
 */
public class SystemAlterDialog {

    private static AlertDialog mDialog = null;

    public static AlertDialog getInstance(final Context context, String msg) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(context)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserProfile.getInstance().clear();
                            PlatformState.getInstance().reset();
                            Intent intent = LaunchActivity.buildIntent(context);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            context.startActivity(intent);
                        }
                    }).create();
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
