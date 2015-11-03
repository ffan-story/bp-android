package com.wbtech.ums.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dupengfei on 15-7-9.
 *
 * 数据统计需要数据的封装
 */
public class AppstatlogModel implements Serializable {


  // 设备ID OpenUdId
  private String device_id;
  // 屏幕分辨率 1024*768
  private String screen_resolution_desc;
  // 是否横屏 1：是 0：否
  private String horizontal_flag;
  // 操作系统及版本 Android 4.4.2 IOS 8
  private String os_version;
  // 网络环境 3G、CDMA、TDSCDMA
  private String network_desc;
  // sdk版本号 1.0 2.0 2.1
  private String sdk_version;
  // 移动设备厂商和型号 iPhone 6 小米3
  private String device_desc;
  // 下载来源 预先定义，如：1 豌豆荚 2 腾讯手机管家 3 91手机助手
  private String app_source;
  // app的版本号 1.0 2.0 2.1
  private String app_version;
  // appkey
  private String appkey;
  // 语言环境
  private String language;
  // 用户移动终端的IP地址
  private String IP;
  // 经纬度 大数据
  private String geo_position;
  private String mobile_operators_desc;

  private ArrayList<Operation> event_log;

  // GPS Latitude(地理位置纬度)+ Longitude（地理位置经度）
  private String GPS;
  // cell_id 基站信息
  private String cell_id;
  // IMEI 设备唯一序列号
  private String IMEI;
  // IMSI SIM卡唯一序列号
  private String IMSI;
  // PHONE_WIFI_MAC 手机WIFI_MAC地址
  private String PHONE_WIFI_MAC;
  // ROUTER_MAC 路由器mac地址
  private String ROUTER_MAC;

  // get开始
  public String getDevice_id() {
    return device_id;
  }

  public String getScreen_resolution_desc() {
    return screen_resolution_desc;
  }

  public String getHorizontal_flag() {
    return horizontal_flag;
  }

  public String getOs_version() {
    return os_version;
  }

  public String getNetwork_desc() {
    return network_desc;
  }

  public String getSdk_version() {
    return sdk_version;
  }

  public String getDevice_desc() {
    return device_desc;
  }

  public String getApp_source() {
    return app_source;
  }

  public String getApp_version() {
    return app_version;
  }

  public String getAppkey() {
    return appkey;
  }

  public String getLanguage() {
    return language;
  }

  public String getIP() {
    return IP;
  }

  public String getGeoPosition() {
    return geo_position;
  }


  public ArrayList<Operation> getEvent_log() {
    return event_log;
  }


  public String getMobile_operators_desc() {
    return mobile_operators_desc;
  }

  public String getGPS() {
    return GPS;
  }

  public String getCell_id() {
    return cell_id;
  }

  public String getIMEI() {
    return IMEI;
  }

  public String getIMSI() {
    return IMSI;
  }

  public String getPHONE_WIFI_MAC() {
    return PHONE_WIFI_MAC;
  }

  public String getROUTER_MAC() {
    return ROUTER_MAC;
  }

  // get结束



  // set开始

  public void setMobile_operators_desc(String mobile_operators_desc) {
    this.mobile_operators_desc = mobile_operators_desc;
  }

  public void setEvent_log(ArrayList<Operation> event_log) {
    this.event_log = event_log;
  }



  public void setDevice_id(String device_id) {
    this.device_id = device_id;
  }

  public void setScreen_resolution_desc(String screen_resolution_desc) {
    this.screen_resolution_desc = screen_resolution_desc;
  }

  public void setHorizontal_flag(String horizontal_flag) {
    this.horizontal_flag = horizontal_flag;
  }

  public void setOs_version(String os_version) {
    this.os_version = os_version;
  }

  public void setNetwork_desc(String network_desc) {
    this.network_desc = network_desc;
  }

  public void setSdk_version(String sdk_version) {
    this.sdk_version = sdk_version;
  }

  public void setDevice_desc(String device_desc) {
    this.device_desc = device_desc;
  }

  public void setApp_source(String app_source) {
    this.app_source = app_source;
  }

  public void setApp_version(String app_version) {
    this.app_version = app_version;
  }

  public void setAppkey(String appkey) {
    this.appkey = appkey;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setIP(String IP) {
    this.IP = IP;
  }

  public void setGeoPosition(String geo_position) {
    this.geo_position = geo_position;
  }

  public void setGPS(String GPS) {
    this.GPS = GPS;
  }

  public void setCell_id(String cell_id) {
    this.cell_id = cell_id;
  }

  public void setIMEI(String IMEI) {
    this.IMEI = IMEI;
  }

  public void setIMSI(String IMSI) {
    this.IMSI = IMSI;
  }

  public void setPHONE_WIFI_MAC(String PHONE_WIFI_MAC) {
    this.PHONE_WIFI_MAC = PHONE_WIFI_MAC;
  }

  public void setROUTER_MAC(String ROUTER_MAC) {
    this.ROUTER_MAC = ROUTER_MAC;
  }
  // set结束



}
