/**
 *
 */
package com.feifan.bp.network;

import android.content.Context;
import android.text.TextUtils;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.UserProfile;
import com.feifan.bp.envir.EnvironmentManager;

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

    private static final String URL_PATH_STAFF_ADD = "H5App/index.html#/staff/add";

    private static final String URL_PATH_COUPON_ADD = "H5App/default.html#/coupon/create";

    private static final String URL_PATH_COMMODITY_MANAGE = "H5App/index.html#/commodity/select_cat_menu";


    public static String statisticReportForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_REPORT).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }

    public static String checkHistoryForHtml(String relativeUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(relativeUrl)).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").concat("&signStatus=2&merchantId=").
                concat(userProfile.getAuthRangeId()).
                concat("&showTabs=0");
        return url;
    }

    public static String orderManagementForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_ORDER).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }

    public static String searchCodeForHtml(String code) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_SEARCH).
                concat("?loginToken=").concat(userProfile.getLoginToken()).
                concat("&uid=").concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&merchantId=").concat(userProfile.getAuthRangeId()).
                concat("&signNo=").concat(code).
                concat("&showTabs=0");
        return url;
    }

    public static String urlForHtml(String reUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(reUrl)).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }

    public static String staffAddForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_STAFF_ADD).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }

    public static String couponAddForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_COUPON_ADD).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }

    public static String commodityManageForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(URL_PATH_COMMODITY_MANAGE).
                concat("?loginToken=").
                concat(userProfile.getLoginToken()).
                concat("&uid=").
                concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&showTabs=0");
        return url;
    }


    public static String getActionH5Url(String actionUri) {
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().
                concat(actionUri).
                concat("&showTabs=0");
        return url;
    }

    public static String staffManagementForHtml() {
        //TODO: need url.
        return "";
    }

    public static String refundForHtml() {
        //TODO: need url.
        return "";
    }

    //----for http request---//
    public static String getLoginUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/login";
    }

    public static String getAuthorizeUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/userAuth";
    }

    public static String getCheckVersionUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/getversioninfo?appType=bpMobile";
    }

    public static String getCheckPhoneNumExistUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/verificationphone";
    }

    public static String getForgetPasswordUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/forgetpwd";
    }

    public static String getResetPasswordUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/editPassword";
    }

    public static String getSendSMSUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/phoneSms";
    }

    public static String checkVersionUpdate() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/appVersion/bpMobile/getLatest";
    }

    public static String refundCount() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "trade/webrefunds";
    }

    public static String getShopListUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiV1Host() + "cdaservice/stores/";
    }

    //add by tianjun 2015.10.29
    public static String submitFeedBack() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "mapp/v1/mapp/feedback";
    }

    public static String getLoginInfo(){
        //return "http://api.sit.ffan.com/mapp/v1/mapp/user";
        return EnvironmentManager.getHostFactory().getFFanApiHost() +"mapp/v1/mapp/user";
    }

    public static String getMessgeList() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "mapp/v1/mapp/message";
    }

    public static String getMessgeListStatus() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "mapp/v1/mapp/messagestatus";
    }

    private static String formatRelativeUrl(String relativeUrl) {
        if (TextUtils.isEmpty(relativeUrl)) {
            return relativeUrl;
        }

        if (relativeUrl.charAt(0) == '/' && relativeUrl.length() > 0) {
            relativeUrl = relativeUrl.substring(1, relativeUrl.length());
        }
        return relativeUrl;
    }

    public static String uploadPicture() {
        return EnvironmentManager.getHostFactory().getFFanPicHost() + "uploadpicture";
    }

}
