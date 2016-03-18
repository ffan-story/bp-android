/**
 *
 */
package com.feifan.bp.network;

import android.text.TextUtils;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.UserProfile;
import com.feifan.bp.envir.EnvironmentManager;

import java.util.Random;

/**
 * 商家后台Url抽象工厂
 *
 * @author xuchunlei
 *         create at: 2015年6月18日 下午4:05:28
 */
public abstract class UrlFactory {

    //帮助中心详情
    private static final String URL_PATH_HELP_DETAIL = "/H5App/default.html#/help/detail/";

    //退款单详情
    private static final String URL_PATH_REFUND = "/H5App/index.html#/order/detail";

    // H5页面相对路径－查询提货码
    private static final String URL_PATH_SEARCH = "H5App/index.html#/goods/search_result";

    private static final String URL_PATH_STAFF_ADD = "H5App/index.html#/staff/add";

    private static final String URL_PATH_COUPON_ADD = "H5App/default.html#/coupon/create";

    //H5页面相对路径－商品管理－添加商品
    private static final String URL_PATH_COMMODITY_MANAGE_ADD = "H5App/index.html#/commodity/select_cat_menu";

    //H5页面相对路径－闪购商品列表
    private static final String URL_PATH_COMMODITY_MANAGE_INSTANTS = "H5App/index.html#/commodity/list/";

    //H5页面相对路径－店铺分析概览
    private static final String URL_PATH_STORE_OVERVIEW = "/H5App/default.html#/analysis/overview";

    //H5页面相对路径－访客分析
    private static final String URL_PATH_VISITORS_ANALYSIS = "/H5App/default.html#/analysis/visit";

    //H5页面相对路径－店铺分析指标说明
    private static final String URL_PATH_STORE_DESCRIPTION = "/H5App/default.html#/analysis/note";

    //H5页面相对路径－核销券码详情页
    private static final String URL_PATH_CODE_COUPONE_DETAIL = "/H5App/default.html#/activity/rule/";

    // H5页面相对路径－订单详情
    private static final String URL_PATH_ORDER_DETAIL = "/H5App/index.html#/order/detail";

    //H5页面相对路径－闪购活动-审核历史
    private static final String URL_PATH_HISTORY_AUDIT = "/H5App/default.html#/lohas/audit/";

    //H5页面相对路径－闪购活动-活动详情
    private static final String URL_PATH_PROMOTION_DETAIL = "/H5App/default.html#/lohas/detail";

    private static final String URL_SOP_FFAN = "http://sop.ffan.com";


    public static String checkHistoryForHtml(String relativeUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        return urlForHtml(relativeUrl).
                concat("&signStatus=2").
                concat("&merchantId=").concat(userProfile.getAuthRangeId());
    }

    /**
     * 退款查询
     *
     * @return
     */
    public static String refundForHtml() {
        return urlForHtml(URL_PATH_REFUND).concat("&code=%s").concat("&refund=1");
    }

    /**
     * 帮助中心详情
     *
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

    /**
     * 商品管理 添加商品
     * @return
     */
    public static String getCommodityManageForHtmlUrl() {
        return urlForHtml(URL_PATH_COMMODITY_MANAGE_ADD) + "&ts=" + new Random().nextInt();
    }

    /**
     * 闪购商品列表
     *
     * @param status
     * @return
     */
    public static String getInstantsForHtmlUrl(String status){
        return urlForHtml(URL_PATH_COMMODITY_MANAGE_INSTANTS + status);
    }

    /**
     * 店铺分析
     *
     * @return
     */
    public static String storeOverviewForHtml() {
        return urlForHtml(URL_PATH_STORE_OVERVIEW);
    }

    /**
     * 核销券码详情
     *
     * @return
     */
    public static String getCodeCouponeDetail(String code) {
        return urlForHtml(URL_PATH_CODE_COUPONE_DETAIL + code);
    }

    /**
     * 获取订单详情Url
     *
     * @param order
     * @return
     */
    public static String getOrderDetailUrl(String order) {
        return urlForHtml(URL_PATH_ORDER_DETAIL) + "&code=" + order;
    }

    /**
     * /H5App/default.html#/lohas/audit?id=GP1451871058174000000&goodsCode=111&merchantId=1&storeId=1
     * 闪购活动-审核历史
     * @return
     */
    public static String getUrlPathHistoryAudit(String eventId,String goodsCode){
        return urlForHtml(URL_PATH_HISTORY_AUDIT).
                concat("&id=").concat(eventId).
                concat("&goodsCode=").concat(goodsCode).
                concat("&merchantId=").concat(UserProfile.getInstance().getMerchantId()).
                concat("&storeId=").concat(UserProfile.getInstance().getAuthRangeId());
    }

    public static String visitorsAnalysisForHtml() {
        return urlForHtml(URL_PATH_VISITORS_ANALYSIS);
    }

    public static String storeDescriptionForHtml() {
        return urlForHtml(URL_PATH_STORE_DESCRIPTION);
    }

    /**
     * 报名活动详情
     *
     * @return
     */
    public static String promotionDetailForHtml(String id) {
        return urlForHtml(URL_PATH_PROMOTION_DETAIL) + "&id=" + id;
    }

    public static String actionUrlForHtml(String reUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String url = EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(reUrl)).
                concat("&loginToken=").concat(userProfile.getLoginToken()).
                concat("&uid=").concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&version=" + BuildConfig.VERSION_CODE).
                concat("&showTabs=0");
        return url;
    }

    public static String urlForHtml(String reUrl) {
        UserProfile userProfile = UserProfile.getInstance();
        String paramStart = "?";
        if (reUrl.contains(paramStart)) {
            paramStart = "&";
        }
        return EnvironmentManager.getHostFactory().getFFanH5Host().concat(formatRelativeUrl(reUrl)).
                concat(paramStart).
                concat("loginToken=").concat(userProfile.getLoginToken()).
                concat("&uid=").concat(String.valueOf(userProfile.getUid())).
                concat("&appType=bpMobile").
                concat("&version=" + BuildConfig.VERSION_CODE).
                concat("&showTabs=0");
    }

    //----for http request---//
    public static String getLoginUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "login";
    }

    public static String getLoginConfirmUrl(){
        return EnvironmentManager.getHostFactory().getFFanH5Host()+"mapp/newlogin/tokenlogin";
    }
    public static String getAuthorizeUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "userAuth";
//        return "http://xapi.pre.ffan.com/xadmin/userAuth";
    }
    public static String getLogoutUrl(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()+"mapp/loginout";
    }

    public static String getCheckPhoneNumExistUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "verificationphone";
    }

    public static String getForgetPasswordUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "forgetpwd";
    }

    public static String getResetPasswordUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "editPassword";
    }

    public static String getSendSMSUrl() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "phoneSms";
    }

    public static String checkVersionUpdate() {
        return EnvironmentManager.getHostFactory().getXadminApiPrefix() + "appVersion/android/getLatest";
    }

    public static String getShopListUrl() {
        return EnvironmentManager.getHostFactory().getFFanApiV1Host() + "cdaservice/stores/";
    }

    public static String getFlashBuyUrl() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/transactionspecific";
    }

    //对账管理二期
    public static String getInstantSummary() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/transactionspecificflsummary";
    }

    public static String getInstantDetailList() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/transactiongroup";
    }

    //闪购报名
    public static String getPromotionListUrl() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/enroll";
    }

    public static String getCouponsUrl() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/transactionspecificcpsummary";
    }

    public static String getGoodsStatus(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/enroll/goodsstatus";
    }

    public static String getGoodsList(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/enroll/goodsbystatus";
    }

    public static String auditGoodsUrl(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/enroll/goods/audit";
    }

    public static String deleteGoodsUrl(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/enroll/goods/disabled";
    }

    //add by tianjun 2015.10.29
    public static String submitFeedBack() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/feedback";
    }

    public static String getReadMessage() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/unread";
    }

    public static String getLoginInfo() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/user";
    }

    public static String getMessgeList() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/message";
    }

    public static String getCodeQueryResult() {
        return EnvironmentManager.getHostFactory().getFFanH5Host() + "goods/coupon/searchunusecoupons";
    }

    public static String getGoodsQueryResult() {
        return EnvironmentManager.getHostFactory().getFFanH5Host() + "goods/GoodsVerification/getOrderBySignOnApp";
    }

    /**
     * 获取商品管理-闪购商品状态和数量
     *
     * @return
     */
    public static String getInstantsBuyCommodity() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/goodsindex";
//        return "http://xapi.sit.ffan.com/mapp/goodsindex";

    }

    public static String getMessgeListStatus() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/messagestatus";
    }

    public static String getHelpCenter() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "mapp/helpcenter";
    }

    public static String getMarketingContract() {
        return EnvironmentManager.getHostFactory().getFFanApiPrefix() + "mapp/cdaservice/marketingcontract";
    }

    //券码核销
    public static String getCheckCouponCode() {
        return EnvironmentManager.getHostFactory().getFFanH5Host() + "goods/coupon/checkCoupon";
    }

    //提货码核销
    public static String getCheckGoodsCode() {
        return EnvironmentManager.getHostFactory().getFFanH5Host() + "goods/GoodsVerification/useSignOnApp";
    }

    /**
     * 红包分类汇总 列表
     * @return
     */
    public static String getAnalysisRedList() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()  + "cashgift?action=collect";
    }

    /**
     * 红包核销详情
     * @return
     */
    public static String getAnalysisRedListDetail() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()  + "cashgift?action=check";
    }

    /**
     * EnvironmentManager.getHostFactory().getMAppApiPrefix() = http://api.sit.ffan.com/mapp/v1/
     *  闪购活动添加商品列表
     */
    public static String getInstEventGoodsList() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()  + "mapp/enroll/goods";
    }

    /**
     * EnvironmentManager.getHostFactory().getMAppApiPrefix() = http://api.sit.ffan.com/mapp/v1/
     *  闪购活动添加商品-设置详情
     */
    public static String getInstEventGoodsSettingDetail(String goodsCode) {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()  + "mapp/enroll/goods/"+goodsCode;
    }

    /**
     * EnvironmentManager.getHostFactory().getMAppApiPrefix() = http://api.sit.ffan.com/mapp/v1/
     *  闪购活动添加商品-提交保存
     */
    public static String getInstEventGoodsCommAndSave() {
        return EnvironmentManager.getHostFactory().getMAppApiPrefix()  + "mapp/enroll/activity";
    }

    /**
     * http://api.sit.ffan.com/mapp/v1/cashflow?action=list
     * 获取收款流水列表
     * @return
     */
    public static String getReceiptsRecordsList(){
        return EnvironmentManager.getHostFactory().getMAppApiPrefix() + "cashflow?action=list";
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
        return EnvironmentManager.getHostFactory().getFFanApiPrefix() + "uploadpicture";
    }

    public static String getSopFfanUrl() {
        return URL_SOP_FFAN;
    }

}
