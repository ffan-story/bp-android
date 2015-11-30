/**
 *
 */
package com.feifan.bp.network;

import android.text.TextUtils;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.UserProfile;
import com.feifan.bp.envir.EnvironmentManager;

/**
 * 商家后台Url抽象工厂
 *
 * @author xuchunlei
 *         create at: 2015年6月18日 下午4:05:28
 */
public abstract class UrlFactory {

    //帮助中心详情
    private static final String URL_PATH_HELP_DETAIL= "/H5App/default.html#/help/detail/";

    //退款单详情
    private static final String URL_PATH_REFUND = "/H5App/index.html#/order/detail";

    // H5页面相对路径－查询提货码
    private static final String URL_PATH_SEARCH = "H5App/index.html#/goods/search_result";

    private static final String URL_PATH_STAFF_ADD = "H5App/index.html#/staff/add";

    private static final String URL_PATH_COUPON_ADD = "H5App/default.html#/coupon/create";

    private static final String URL_PATH_COMMODITY_MANAGE = "H5App/index.html#/commodity/select_cat_menu";

    private static final String URL_SOP_FFAN = "http://sop.ffan.com";


    public static String checkHistoryForHtml(String relativeUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        return urlForHtml(relativeUrl).
                concat("&signStatus=2").
                concat("&merchantId=").concat(userProfile.getAuthRangeId());
    }


    public static String searchCodeForHtml() {
        UserProfile userProfile = UserProfile.getInstance();
        return urlForHtml(URL_PATH_SEARCH).
                concat("&merchantId=").concat(userProfile.getAuthRangeId()).
                concat("&signNo=").concat("%s");
    }

    /**
     * 退款查询
     * @return
     */
    public static String refundForHtml() {
        return urlForHtml(URL_PATH_REFUND).concat("&code=%s").concat("&refund=1");
    }

    /**
     * 帮助中心详情
     * @param id
     * @return
     */
    public static String helpCenterDetailForHtml(String id) {
        return urlForHtml(URL_PATH_HELP_DETAIL + id);
    }

    public static String staffAddForHtml() {
        return urlForHtml(URL_PATH_STAFF_ADD);
    }

    public static String couponAddForHtml() {
        return urlForHtml(URL_PATH_COUPON_ADD);
    }

    public static String commodityManageForHtml() {
        return urlForHtml(URL_PATH_COMMODITY_MANAGE);
    }

    public static String actionUrlForHtml(String reUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(reUrl)).
                concat("&loginToken=").concat(userProfile.getLoginToken()).
                concat("&uid=").concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&version="+BuildConfig.VERSION_CODE).
                concat("&showTabs=0");
        return url;
    }

    public static String urlForHtml(String reUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(reUrl)).
                concat("?loginToken=").concat(userProfile.getLoginToken()).
                concat("&uid=").concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&version="+BuildConfig.VERSION_CODE).
                concat("&showTabs=0");
        return url;
    }

    public static String staffManagementForHtml() {
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
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "xadmin/appVersion/android/getLatest";
    }

    public static String refundCount() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "trade/webrefunds";
    }

    public static String getShopListUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiV1Host() + "cdaservice/stores/";
    }

    public static String getFlashBuyUrl(){
        return EnvironmentManager.getHostFactory().getFFanApiHost()+"mapp/v1/mapp/transactionspecific";
    }

    public static String getCouponsUrl(){
        return EnvironmentManager.getHostFactory().getFFanApiHost()+"mapp/v1/mapp/transactionspecificcpsummary";
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

    public static String getHelpCenter() {
        return EnvironmentManager.getHostFactory().getFFanApiHost() + "/mapp/helpcenter";
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

    public static String getSopFfanUrl() {
        return URL_SOP_FFAN;
    }

}
