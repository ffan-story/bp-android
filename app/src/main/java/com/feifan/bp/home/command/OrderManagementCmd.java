package com.feifan.bp.home.command;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.browser.TabLayoutActivity;
import com.feifan.bp.net.UrlFactory;

/**
 * Created by maning on 15/8/4.
 */
public class OrderManagementCmd implements Command {
    private Context mContext;
    private String relativeUrl;

    private String tabTitles[] = new String[]{"全部状态","待付款","POS交易关闭","交易关闭","付款成功","交易成功","交易完成"};
    private String arryUrl[] ;
    public OrderManagementCmd(Context context, String url) {
        mContext = context;
        this.relativeUrl = url;
    }

    @Override
    public boolean handle() {
        String url = UrlFactory.urlForHtml(mContext, relativeUrl);
        if (Utils.isNetworkAvailable(mContext)) {
//            Intent intent = new Intent(mContext, BrowserActivity.class);
//            intent.putExtra(BrowserActivity.EXTRA_KEY_URL, url);
//            mContext.startActivity(intent);
            arryUrl=new String[]{url,url,url,url,url,url,url};
            Intent intent = new Intent(mContext, TabLayoutActivity.class);
            intent.putExtra(TabLayoutActivity.EXTRA_KEY_URLS, arryUrl);
            intent.putExtra(TabLayoutActivity.EXTRA_KEY_TITLES, tabTitles);
            mContext.startActivity(intent);
        } else {
            Utils.showShortToast(mContext, R.string.error_message_text_offline, Gravity.CENTER);
        }
        return false;
    }
}
