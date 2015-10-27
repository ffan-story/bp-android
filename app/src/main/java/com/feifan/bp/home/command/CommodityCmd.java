package com.feifan.bp.home.command;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.browser.BrowserActivityNew;
import com.feifan.bp.net.UrlFactory;

/**
 * Created by maning on 15/9/16.
 */
public class CommodityCmd implements Command {
    private Context mContext;
    private String relativeUrl;

    public CommodityCmd(Context context, String url) {
        mContext = context;
        relativeUrl = url;
    }

    @Override
    public boolean handle() {
        String url = UrlFactory.urlForHtml(mContext, relativeUrl);
        if (Utils.isNetworkAvailable(mContext)) {
//            Intent intent = new Intent(mContext, BrowserActivity.class);
//            intent.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
//            mContext.startActivity(intent);

            Intent intent = new Intent(mContext, BrowserActivityNew.class);
            intent.putExtra(BrowserActivityNew.EXTRA_KEY_URL, url);
            mContext.startActivity(intent);
        } else {
            Utils.showShortToast(mContext, R.string.error_message_text_offline, Gravity.CENTER);
        }
        return false;
    }
}
