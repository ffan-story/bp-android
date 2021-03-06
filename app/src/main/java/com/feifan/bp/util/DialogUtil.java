package com.feifan.bp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.login.LoginFragment;

/**
 * Created by xuchunlei on 16/2/29.
 */
public class DialogUtil {

    // Cookie对话框
    private static AlertDialog sCookieDialog;

    // Error对话框
    private static AlertDialog sErrorDialog;

    private DialogUtil() {

    }

    /**
     * 显示Cookie过期对话框
     * @param msg
     */
    public static void showCookieDialog(String msg) {
        if (sCookieDialog == null) {
            sCookieDialog = new AlertDialog.Builder(PlatformState.getApplicationContext()).setCancelable(false).create();
            sCookieDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        if(!sCookieDialog.isShowing()) {
            sCookieDialog.show();

            //更改对话框样式为Material Design
            View v = customize(sCookieDialog.getWindow());

            //设置内容和动作
            TextView messageV = (TextView) v.findViewById(R.id.message);
            messageV.setText(msg);
            v.findViewById(R.id.positive).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.closeCookieDialog();
                    // 清理登录数据
                    UserProfile.getInstance().clear();
                    PlatformState.getInstance().reset();
//                    Intent intent = LaunchActivity.buildIntent(PlatformState.getApplicationContext());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                    PlatformState.getApplicationContext().startActivity(intent);
                    Intent intent = new Intent(PlatformState.getApplicationContext(),PlatformTopbarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, LoginFragment.class.getName());
                    intent.putExtra(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.login_login_text));
                    PlatformState.getApplicationContext().startActivity(intent);
                    Activity activity = PlatformState.getInstance().getCurrentActivity();
                    if(activity != null ) {
                        activity.finish();
                    }
                }
            });
        }
    }

    /**
     * 关闭Cookie过期对话框
     */
    public static void closeCookieDialog() {
        if (sCookieDialog != null) {
            sCookieDialog.dismiss();
            sCookieDialog = null;
        }
    }

    /**
     * 显示错误提示对话框
     */
    public static void showErrorDialog(String msg) {
        if(sErrorDialog == null) {
            sErrorDialog = new AlertDialog.Builder(PlatformState.getApplicationContext()).create();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                sErrorDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }else {
                sErrorDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        if(!sErrorDialog.isShowing()) {
            // 必须先显示，才能进行自定义设置
            sErrorDialog.show();

            View v = customize(sErrorDialog.getWindow());
            TextView messageV = (TextView)v.findViewById(R.id.message);
            messageV.setText(msg);
            v.findViewById(R.id.positive).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    sErrorDialog.dismiss();
                    Activity activity = PlatformState.getInstance().getCurrentActivity();
                    if(activity != null && !(activity instanceof LaunchActivity)) {
                        activity.finish();
                    }
                }
            });
        }
    }

    /**
     * 关闭错误对话框
     */
    public static void closeErrorDialog() {
        if(sErrorDialog != null) {
            sErrorDialog.dismiss();
            sErrorDialog = null;
        }
    }

    private static View customize(Window w) {
        View contentV = LayoutInflater.from(PlatformState.getApplicationContext()).inflate(R.layout.layout_system_alterdialog, null);
        contentV.setFocusable(true);
        contentV.setFocusableInTouchMode(true);

        w.setBackgroundDrawableResource(com.feifan.materialwidget.R.drawable.material_dialog_window);
        w.setContentView(contentV);
        return contentV;
    }

}
