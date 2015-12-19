package com.feifan.bp.home.code;



import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.home.MessageModel;
import com.feifan.bp.home.MessageStatusModel;
import com.feifan.bp.home.ReadMessageModel;
import com.feifan.bp.home.VersionModel;
import com.feifan.bp.network.DefaultErrorListener;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;
import com.feifan.bp.network.UrlFactory;

/**
 * 核销界面控制类
 * <p/>
 * Created by xuchunlei on 15/6/19.
 */
public class CodeCtrl {

    /**
     * 券码详情
     * http://sop.ffan.com/goods/coupon/searchunusecoupons?certificateno={券码}
     * @param listener
     */
    public static void queryCouponsResult(String code, Listener listener, ErrorListener errorListener) {
        JsonRequest<CodeModel> request = new GetRequest.Builder<CodeModel>(UrlFactory.getCodeQueryResult()).errorListener(errorListener)
                .param("certificateno", code)
                .build()
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
                .build()
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
        JsonRequest<CodeCheckModel> request = new PostRequest<CodeCheckModel>(UrlFactory.getCheckCouponCode(), errorListener)
                .param("certificateno", code)
                .param("memberId", memberId)
                .header("uid", UserProfile.getInstance().getUid()+"")
                .header("loginToken", UserProfile.getInstance().getLoginToken())
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
        JsonRequest<CodeCheckModel> request = new PostRequest<CodeCheckModel>(UrlFactory.getCheckGoodsCode(), errorListener)
                .param("signNo", code)
                .param("orderNo", orderNo)
                .header("uid", UserProfile.getInstance().getUid()+"")
                .header("loginToken", UserProfile.getInstance().getLoginToken())
                .header("appType","bpMobile")
                .targetClass(CodeCheckModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }

}
