/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @since Version 0.1
 * @filesource
 */
package com.wbtech.ums.common;

public class UmsConstants {
  public static boolean DebugMode = true;
  public static long kContinueSessionMillis = 30000L;
  public static final Object saveOnlineConfigMutex = new Object();
  public static final String eventUrl = "/ums/postEvent";
  public static final String errorUrl = "/ums/postErrorLog";
  public static final String clientDataUrl = "/ums/postClientData";
  public static final String updataUrl = "/ums/getApplicationUpdate";
  public static final String activityUrl = "/ums/postActivityLog";
  public static final String onlineConfigUrl = "/ums/getOnlineConfiguration";
  public static final String uploadUrl = "/ums/uploadLog";
  public static String preUrl = "";

  public static String  APPSTATLOGURL = "/ffan/v1/appstatlog";


  /**
   * 日志上传type
   * 默认 10
   * 一卡通 1
   * 实时上传 2
   */
  public static String POST_TYPE_DEFAULT = "10";
  public static String POST_TYPE_ONECARD = "1";
  public static String POST_TYPE_PROMPTLY = "2";


  /**
   * 定量操作的次数限制
   */
  public static int QUANTITATIVE_NUMBER = 30;

  /**
   * 定时操作的间隔事件
   *
   */
  public static int TIMING_INTERVAL = 60*1000;
}
