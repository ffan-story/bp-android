package com.feifan.bp;

import com.feifan.statlib.FmsConstants;

/**
 * 统计埋点ID
 * Created by apple on 15-12-8.
 */
public class Statistics {
    public final static String USER_OPEN_APP            = "USER_OPEN_APP";		    //  用户启动APP
    public final static String CLOSE_APP                = "CLOSE_APP";	            //  关闭APP
    public final static String FB_HOME_SEARCHCODE       = "FB_HOME_SEARCHCODE";     //	核销码搜索	
    public final static String FB_HOME_SCANCODE         = "FB_HOME_SCANCODE";       //	扫码验证	
    public final static String FB_HOME_VERIFY           = "FB_HOME_VERIFY";	        //	验证历史	
    public final static String FB_HOME_ORDERMANA        = "FB_HOME_ORDERMANA";	    //	订单管理	
    public final static String FB_HOME_STAT             = "FB_HOME_STAT";	        //	统计报表	
    public final static String FB_HOME_STAFFMANA        = "FB_HOME_STAFFMANA";	    //	员工管理	
    public final static String FB_HOME_RETURN	        = "FB_HOME_RETURN";         //	退款售后	
    public final static String FB_HOME_GOODSMANA	    = "FB_HOME_GOODSMANA";      //	商品管理	
    public final static String FB_HOME_SALEMANA	        = "FB_HOME_SALEMANA";       //	营销管理	
    
    public final static String FB_HOME_HOME	            = "FB_HOME_HOME";           //	首页 首页
    public final static String FB_HOME_MESSAGE	        = "FB_HOME_MESSAGE";        //	首页 消息
    public final static String FB_HOME_SETTING	        = "FB_HOME_SETTING";        //	首页 设置

    public final static String FB_STAFFMANA_ADD	        = "FB_STAFFMANA_ADD";       //  员工管理	    添加员工
    public final static String FB_GOODSMANA_PUB	        = "FB_GOODSMANA_PUB";       //  商品管理	    发布按钮

    public final static String FB_SETTING_FEEDBACK	    = "FB_SETTING_FEEDBACK";    //  设置页面	    意见反馈

    public final static String FB_HOME_STOREANA	        = "FB_HOME_STOREANA";       //  商户APP首页  点击店铺分析
    public final static String FB_STOREANA_OVERVIEW	    = "FB_STOREANA_OVERVIEW";   //  店铺分析页面	概览
    public final static String FB_STOREANA_VISITORANA   = "FB_STOREANA_VISITORANA"; //  店铺分析页面	访客分析

    public final static String FB_VERIFY_VERIFY         = "FB_VERIFY_VERIFY";       //  核销详情    确认核销

    public final static String FB_SALEMANA_COUPON       = "FB_SALEMANA_COUPON";     //  营销管理    优惠券管理
    public final static String FB_SALEMANA_REGIS        = "FB_SALEMANA_REGIS";      //  营销管理    活动报名

    public final static String FB_HOME_FINA	            = "FB_HOME_FINA";           //  商户APP首页 	点击对账管理
    public final static String FB_FINA_FLASHBUY         = "FB_FINA_FLASHBUY";       //  交易流水页面	闪购
    public final static String FB_FINA_GENCOUPON        = "FB_FINA_GENCOUPON";      //  交易流水页面	通用券

    public final static String FB_PROMTION_ANA        = "FB_PROMTION_ANA";      //  营销分析

    /**
     * 更新客户端数据
     * 发送统计日志时作为业务相关的客户端参数数据
     * @param profile
     */
    public static void updateClientData(UserProfile profile){
        FmsConstants.sClientDataMap.clear();
        FmsConstants.sClientDataMap.put("user_id", profile.getUid());
        if(!profile.isStoreUser()) {        // 商户ID
            FmsConstants.sClientDataMap.put("merchant_id", profile.getAuthRangeId());
        }else {                             // 门店ID
            FmsConstants.sClientDataMap.put("store_id", profile.getAuthRangeId());
        }
        if(profile.getCityId() != Constants.NO_INTEGER) {  // 城市ID
            FmsConstants.sClientDataMap.put("city_id", profile.getCityId());
        }

    }
}
