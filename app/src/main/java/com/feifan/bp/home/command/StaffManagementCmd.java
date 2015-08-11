package com.feifan.bp.home.command;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import com.feifan.bp.BrowserActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.net.NetUtils;

/**
 * Created by maning on 15/8/4.
 */
public class StaffManagementCmd implements Command {
    private Context mContext;
    private String relativeUrl;

    public StaffManagementCmd(Context context, String url) {
        mContext = context;
        relativeUrl = url;
    }

    @Override
    public boolean handle() {
        String url = NetUtils.getUrlFactory().urlForHtml(mContext, relativeUrl);
        if (Utils.isNetworkAvailable(mContext)) {
            BrowserActivity.startActivity(mContext, url, true);
        } else {
            Utils.showShortToast(mContext, R.string.error_message_text_offline, Gravity.CENTER);
        }
        return true;
    }
}