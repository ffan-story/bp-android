package com.wbtech.ums.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.wanda.base.config.GlobalConfig;
import com.wanda.stat.WStat;
import com.wbtech.ums.common.CommonUtil;
import com.wbtech.ums.model.EventLogIds;
import com.wbtech.ums.model.Operation;
import com.wbtech.ums.objects.LatitudeAndLongitude;
import com.wbtech.ums.objects.SCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by dupengfei on 15-7-10.
 */
public class GetDeviceInfoUtil {

  private Context mcontext;


  public GetDeviceInfoUtil() {

    this.mcontext = GlobalConfig.getAppContext();
  }

  public String getDevice_id() {

    TelephonyManager tm = (TelephonyManager) (mcontext
        .getSystemService(Context.TELEPHONY_SERVICE));
    return tm.getDeviceId() == null ? "" : tm.getDeviceId();
  }


  public static String getLocalIpAddress() {
    String ipAddress = null;
    try {
      List<NetworkInterface> interfaces = Collections
          .list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface iface : interfaces) {
        if (iface.getDisplayName().equals("eth0")) {
          List<InetAddress> addresses = Collections.list(iface
              .getInetAddresses());
          for (InetAddress address : addresses) {
            if (address instanceof Inet4Address) {
              ipAddress = address.getHostAddress();
            }
          }
        } else if (iface.getDisplayName().equals("wlan0")) {
          List<InetAddress> addresses = Collections.list(iface
              .getInetAddresses());
          for (InetAddress address : addresses) {
            if (address instanceof Inet4Address) {
              ipAddress = address.getHostAddress();
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return ipAddress;
  }

  public String getLanguage() {

    return Locale.getDefault().getLanguage();
  }

  public String getAppKey() {

    return CommonUtil.getAppKey(mcontext);
  }


  public String getApp_source() {

    return WStat.getApp_source();
  }

  public String getAppVersion() {

    return CommonUtil.getAppVersion(mcontext);
  }

  public String getHorizontalFlag() {

    return CommonUtil.getHorizontalFlag(mcontext) + "";
  }


  public String getNetwork_desc() {

    return CommonUtil.isNetworkTypeWifi(mcontext) ? "Wifi" : CommonUtil.getNetworkType(mcontext);
  }

  public String getOsVersion() {

    return "Android" + CommonUtil.getOsVersion(mcontext);
  }

  public String getSDKVersion() {

    return CommonUtil.getSDKVersion() + "";
  }

  public String getScreen_resolution_desc() {

    WindowManager manager = (WindowManager) mcontext
        .getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics displaysMetrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(displaysMetrics);

    return displaysMetrics.widthPixels + " * "
        + displaysMetrics.heightPixels;
  }


  public String getDeviceDesc() {

    return CommonUtil.getDeviceDesc(mcontext);
  }

  public ArrayList<Operation> getOperations(String event_id) {

    ArrayList<Operation> operations = new ArrayList<Operation>();
    Operation operation = new Operation();
    operation.setEvent_id(event_id);
    operation.setEvent_time(CommonUtil.getTime());
    operation.setUser_id(WStat.getUserId());

    // 应对20150803需求，事件加上城市，商圈等Id
    EventLogIds eventLogIds = EventLogIds.getInstance();
    operation.setCity_id(eventLogIds.getCity_id());
    operation.setPlaza_id(eventLogIds.getPlaza_id());
    operation.setMerchant_id(eventLogIds.getMerchant_id());
    operation.setProduct_id(eventLogIds.getProduct_id());
    operation.setBrandId(eventLogIds.getBrandId());
    operation.setBrandStoryId(eventLogIds.getBrandstoryId());
    //电影
    operation.setFilm_id(eventLogIds.getFilm_id());
    operation.setCinema_id(eventLogIds.getCinema_id());
    operation.setAliasName(eventLogIds.getAliasName());
    operation.setRound_id(eventLogIds.getRound_id());
    operation.setOrderby_id(eventLogIds.getOrderby_id());

    operations.add(operation);

    return operations;

  }

  public String getGeoPosition() {
    LatitudeAndLongitude coordinates = CommonUtil.getLatitudeAndLongitude(mcontext,
        true);
    String latitude = coordinates.latitude;
    String longitude = coordinates.longitude;
    if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
      return null;
    } else {
      return latitude + "," + longitude;
    }
  }

  public String getGps() {
    LatitudeAndLongitude coordinates = CommonUtil.getLatitudeAndLongitude(mcontext,
        true);
    String latitude = coordinates.latitude;
    String longitude = coordinates.longitude;
    if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
      return null;
    } else {
      return latitude + "+" + longitude;
    }
  }

  public String getCellId() throws JSONException {
    JSONObject json = null;
    try {
      SCell sCell = CommonUtil.getCellInfo(mcontext);
      // 以下内容是把得到的信息组合成json体，然后发送给我的服务器，获取经纬度信息
      if (sCell != null) {
        JSONObject item = new JSONObject();
        item.put("cid", sCell.CID);
        item.put("lac", sCell.LAC);
        item.put("mnc", sCell.MNC);
        item.put("mcc", sCell.MCC);
        item.put("strength", sCell.BSSS);

        JSONArray cells = new JSONArray();
        cells.put(0, item);

        json = new JSONObject();
        json.put("cells", cells);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return json + "";
  }

  public String getImei() {
    return CommonUtil.getDeviceID(mcontext);
  }

  public String getImsi() {
    return CommonUtil.getIMSI(mcontext);
  }

  public String getPhoneWifiMac() {

    return CommonUtil.getPhoneWifiMac(mcontext);
  }

  public String getRouterMac() {
    return CommonUtil.getRouterMac(mcontext);
  }


}
