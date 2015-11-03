package com.wbtech.ums.util;

import com.wbtech.ums.model.AppstatlogModel;

import org.json.JSONException;

/**
 * Created by dupengfei on 15/8/5.
 * <p/>
 * 上传之后会把本地文本删除，下次读取的时候会读空，所以需要一个默认的没有数据对象 组装一个默认的AppstatlogModel对象
 */


public class InstallAppInfo {

  /**
   * 返回一个appstatlogModel对象，不包括event
   */

  public static AppstatlogModel getDefultAppInfo() {

    GetDeviceInfoUtil deviceInfoUtil = new GetDeviceInfoUtil();
    AppstatlogModel appstatlogModel = new AppstatlogModel();
    appstatlogModel.setDevice_id(deviceInfoUtil.getDevice_id());
    appstatlogModel.setIP(GetDeviceInfoUtil.getLocalIpAddress());
    appstatlogModel.setGeoPosition(deviceInfoUtil.getGeoPosition());
    appstatlogModel.setLanguage(deviceInfoUtil.getLanguage());
    appstatlogModel.setAppkey(deviceInfoUtil.getAppKey());
    appstatlogModel.setApp_source(deviceInfoUtil.getApp_source());
    appstatlogModel.setApp_version(deviceInfoUtil.getAppVersion());
    appstatlogModel.setHorizontal_flag(deviceInfoUtil.getHorizontalFlag());
    appstatlogModel.setNetwork_desc(deviceInfoUtil.getNetwork_desc());
    appstatlogModel.setOs_version(deviceInfoUtil.getOsVersion());
    appstatlogModel.setSdk_version(deviceInfoUtil.getSDKVersion());
    appstatlogModel.setScreen_resolution_desc(deviceInfoUtil.getScreen_resolution_desc());
    appstatlogModel.setDevice_desc(deviceInfoUtil.getDeviceDesc());

    appstatlogModel.setGPS(deviceInfoUtil.getGps());
    try {
      appstatlogModel.setCell_id(deviceInfoUtil.getCellId());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    appstatlogModel.setIMEI(deviceInfoUtil.getImei());
    appstatlogModel.setIMSI(deviceInfoUtil.getImsi());
    appstatlogModel.setPHONE_WIFI_MAC(deviceInfoUtil.getPhoneWifiMac());
    appstatlogModel.setROUTER_MAC(deviceInfoUtil.getRouterMac());

    return appstatlogModel;
  }

}
