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
public class RefundCmd implements Command {
    private Context mContext;

    public RefundCmd(Context context) {
        mContext = context;
    }

    @Override
    public boolean handle() {
        String url = NetUtils.getUrlFactory().refundForHtml();
        if (Utils.isNetworkAvailable(mContext)) {
            Intent intent = new Intent(mContext, BrowserActivity.class);
            intent.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
            mContext.startActivity(intent);
        } else {
            Utils.showShortToast(mContext, R.string.error_message_text_offline, Gravity.CENTER);
        }
        return true;
    }
}
