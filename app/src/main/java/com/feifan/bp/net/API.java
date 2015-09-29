package com.feifan.bp.net;

import com.feifan.bp.BuildConfig;

/**
 * Created by maning on 15/8/5.
 */
public class API {

    private static String FFAN_HOST;
    private static String HTML5_HOST;

    static {
        switch (BuildConfig.CURRENT_ENVIRONMENT) {
            case SIT_GATEWAY:
                FFAN_HOST = "http://api.sit.ffan.com/";
                HTML5_HOST = "http://sop.sit.ffan.com/";
                break;
            case SIT_API:
                FFAN_HOST = "http://xapi.sit.ffan.com/";
                HTML5_HOST = "http://sop.sit.ffan.com/";
                break;
            case PRODUCT_PRE:
                FFAN_HOST = "https://api.ffan.com/";
                HTML5_HOST = "http://sop.pre.ffan.com/";
                break;
            case PRODUCT:
                FFAN_HOST = "https://api.ffan.com/";
                HTML5_HOST = "https://sop.ffan.com/";
                break;
            default:
                FFAN_HOST = "http://api.sit.ffan.com/";
                HTML5_HOST = "http://sop.sit.ffan.com/";
                break;
        }
    }

//    public interface Html5API {
//
//    }
//
//    public interface HttpAPI {
//        String LOGIN_URL = FFAN_HOST + "xadmin/login";
//        String CHECK_VERSION_URL = FFAN_HOST + "xadmin/getversioninfo?appType=bpMobile";
//        String CHECK_PHONE_NUM_URL = FFAN_HOST + "xadmin/verificationphone";
//        String FORGET_PASSWORD_URL = FFAN_HOST + "xadmin/forgetpwd";
//        String RESET_PASSWORD_URL = FFAN_HOST + "xadmin/editPassword";
//        String SEND_SMS_URL = FFAN_HOST + "xadmin/phoneSms";
//    }
}
