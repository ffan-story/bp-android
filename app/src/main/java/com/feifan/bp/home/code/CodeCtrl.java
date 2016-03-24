package com.feifan.bp.home.code;



import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.PostRequest;
import com.feifan.bp.base.network.UrlFactory;

import java.util.Random;

/**
 * 核销界面控制类
 * <p/>
 * Created by xuchunlei on 15/6/19.
 */
public class CodeCtrl {

    /**
     * 券码详情－用于扫码或输码
     * http://sop.ffan.com/goods/coupon/searchunusecoupons?certificateno={券码}
     * @param listener
     */
    public static void queryCouponsResult(String code, Listener listener, ErrorListener errorListener) {
        JsonRequest<CodeModel> request = new PostRequest<CodeModel>(UrlFactory.getCodeQueryResult(), errorListener)
                .param("certificateno", code)
                .param("checkDevice", String.valueOf(1)) //表示要进行验证
                .header("uid", String.valueOf(UserProfile.getInstance().getUid()))
                .header("loginToken", UserProfile.getInstance().getLoginToken())
                .header("appType", "bpMobile")
                .targetClass(CodeModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    /**
     * 提货码详情 10 位
     * http://sop.ffan.com/goods/GoodsVerification/getOrderBySignOnApp?signNo={提货码}
     * @param code
     * @param listener
     * @param errorListener
     */
    public static void queryGoodsResult(String code, Listener listener, ErrorListener errorListener) {
        JsonRequest<GoodsModel> request = new GetRequest.Builder<GoodsModel>(UrlFactory.getGoodsQueryResult()).errorListener(errorListener)
                .param("signNo", code)
                .param("app_verification_native", String.valueOf(new Random().nextInt()))
                .build()
                .header("uid", String.valueOf(UserProfile.getInstance().getUid()))
                .header("loginToken", UserProfile.getInstance().getLoginToken())
                .header("appType", "bpMobile")
                .targetClass(GoodsModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }


    /**
     * post
     * 券码核销
     * @param code
     * @param memberId
     * @param listener
     */
    public static void checkCouponCode(String code,  String memberId, Listener listener,ErrorListener errorListener) {
        String uid = String.valueOf(UserProfile.getInstance().getUid());
        String loginToken = UserProfile.getInstance().getLoginToken();

        JsonRequest<CodeCheckModel> request = new PostRequest<CodeCheckModel>(
                UrlFactory.getCheckCouponCode()
                          .concat("?uid=" + uid)
                          .concat("&loginToken=" + loginToken)
                          .concat("&appType=bpMobile")
                          .concat("&app_verification_native=" + new Random().nextInt()), errorListener)
                .param("certificateno", code)
                .param("memberId", memberId)
                .header("uid", uid)
                .header("loginToken", loginToken)
                .header("appType", "bpMobile")
                .targetClass(CodeCheckModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }


    /**
     * post
     * 提货码核销
     * @param code
     * @param orderNo
     * @param listener
     */
    public static void checkGoodsCode(String code,  String orderNo, Listener listener,ErrorListener errorListener) {
        String uid = String.valueOf(UserProfile.getInstance().getUid());
        String loginToken = UserProfile.getInstance().getLoginToken();

        JsonRequest<CodeCheckModel> request = new PostRequest<CodeCheckModel>(
                UrlFactory.getCheckGoodsCode()
                          .concat("?uid=" + uid)
                          .concat("&loginToken=" + loginToken)
                          .concat("&appType=bpMobile")
                          .concat("&app_verification_native=" + new Random().nextInt()),
                          errorListener)
                .param("signNo", code)
                .param("orderNo", orderNo)
                .header("uid", uid)
                .header("loginToken", loginToken)
                .header("appType", "bpMobile")
                .targetClass(CodeCheckModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
