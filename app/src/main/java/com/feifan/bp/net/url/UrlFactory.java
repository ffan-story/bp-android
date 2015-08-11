/**
 *
 */
package com.feifan.bp.net.url;

import android.content.Context;

import com.feifan.bp.PlatformState;
import com.feifan.bp.account.AccountManager;

/**
 * 商家后台Url抽象工厂
 *
 * @author xuchunlei
 *         create at: 2015年6月18日 下午4:05:28
 */
public abstract class UrlFactory {

    // H5页面相对路径－报表统计
    private static final String URL_PATH_REPORT = "H5App/index.html#/statistical";
    // H5页面相对路径－验证历史
    private static final String URL_PATH_HISTORY = "H5App/index.html#/goods/search_history";
    // H5页面相对路径－订单管理
    private static final String URL_PATH_ORDER = "H5App/index.html#/order";
    // H5页面相对路径－查询提货码
    private static final String URL_PATH_SEARCH = "H5App/index.html#/goods/search_result";

    protected abstract String getFFanHostUrl();

    protected abstract String getH5HostUrl();

    public String statisticReportForHtml(Context context) {
        AccountManager accountManager = AccountManager.instance(context);
        String url = getH5HostUrl().concat(URL_PATH_REPORT).
                concat("?loginToken=").
                concat(accountManager.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(accountManager.getUid())).
                concat("&appType=bpMobile");
        return url;
    }

    public String checkHistoryForHtml(Context context) {
        AccountManager accountManager = AccountManager.instance(context);
        String url = getH5HostUrl().concat(URL_PATH_HISTORY).
                concat("?loginToken=").
                concat(accountManager.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(accountManager.getUid())).
                concat("&appType=bpMobile").concat("&signStatus=2&merchantId=").
                concat(accountManager.getAuthRangeId());
        return url;
    }

    public String orderManagementForHtml(Context context) {
        AccountManager accountManager = AccountManager.instance(context);
        String url = getH5HostUrl().concat(URL_PATH_ORDER).
                concat("?loginToken=").
                concat(accountManager.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(accountManager.getUid())).
                concat("&appType=bpMobile");
        return url;
    }

    public String searchCodeForHtml(Context context, String code) {
        AccountManager accountManager = AccountManager.instance(context);
        String url = getH5HostUrl().concat(URL_PATH_SEARCH).
                concat("?loginToken=").concat(accountManager.getLoginToken()).
                concat("&uid=").concat(String.valueOf(accountManager.getUid())).
                concat("&appType=bpMobile").
                concat("&merchantId=").concat(accountManager.getAuthRangeId()).
                concat("&signNo=").concat(code);
        return url;
    }

    public String urlForHtml(Context context, String reUrl) {
        AccountManager accountManager = AccountManager.instance(context);
        String url = getH5HostUrl().concat(reUrl).
                concat("?loginToken=").
                concat(accountManager.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(accountManager.getUid())).
                concat("&appType=bpMobile");
        return url;
    }

    public String staffManagementForHtml() {
        //TODO: need url.
        return "";
    }

    public String refundForHtml() {
        //TODO: need url.
        return "";
    }

    //----for http request---//
    public String login() {
        return getFFanHostUrl() + "xadmin/login";
    }

    public String checkPermission() {
        return getFFanHostUrl() + "xadmin/userAuth";
    }

    public String checkVersion() {
        return getFFanHostUrl() + "xadmin/getversioninfo?appType=bpMobile";
    }

    public String checkPhoneNumExist() {
        return getFFanHostUrl() + "xadmin/verificationphone";
    }

    public String forgetPassword() {
        return getFFanHostUrl() + "xadmin/forgetpwd";
    }

    public String resetPassword() {
        return getFFanHostUrl() + "xadmin/editPassword";
    }

    public String sendSMS() {
        return getFFanHostUrl() + "xadmin/phoneSms";
    }

    public String checkVersionUpdate() {
        return getFFanHostUrl() + "xadmin/appVersion/bpMobile/getLatest";
    }

}