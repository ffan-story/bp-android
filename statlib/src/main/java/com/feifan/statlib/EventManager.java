package com.feifan.statlib;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map.Entry;
import java.util.Set;

import com.feifan.statlib.FmsAgent.SendPolicy;

/**
 * 统计事件管理器
 *
 * Created by xuchunlei on 15/12/8.
 */
class EventManager {

    private final String TAG = "EventManager";

    private Context context;
    private String eventid;

    private final String EVENT_URL = "";

    public EventManager(Context context, String eventid) {
        this.context = context;
        this.eventid = eventid;
    }

    public void postEventInfo() {
        JSONObject localJSONObject;
        try {
            localJSONObject = prepareEventJSON();
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            return;
        }
        if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                && CommonUtil.isNetworkAvailable(context)) {

            StringBuffer paramsBuffer = new StringBuffer();
            String params = paramsBuffer.append("type=20").append("&")
                                        .append("content=" + localJSONObject.toString())
                                        .toString();
            String result = NetworkUtil.Post(FmsConstants.urlPrefix + EVENT_URL, params);
            StatLog.i(TAG, "Post " + eventid + " response " + result);
            ResponseMessage message = NetworkUtil.parse(result);
            if (message == null) {
                CommonUtil.saveInfoToFile("eventInfo", localJSONObject, context);
                return;
            }
            // TODO dispose ResponseMessage here
            if(message.status != ResponseMessage.RESPONSE_STATUS_SUCCESS) {
                StatLog.e(TAG, "Error Code=" + message.status + ",Message=" + message.msg);
                //                    CommonUtil.saveInfoToFile("eventInfo", localJSONObject, context);
            }
        } else {
            CommonUtil.saveInfoToFile("eventInfo", localJSONObject, context);
            return;
        }
    }

    private JSONObject prepareEventJSON() {

        JSONObject localJSONObject = new JSONObject();
        try {
            // 设备信息
            localJSONObject.put("IMEI", DeviceInfo.getDeviceIMEI());
            localJSONObject.put("IMSI", DeviceInfo.getIMSI());
            localJSONObject.put("PHONE_WIFI_MAC", DeviceInfo.getWifiMac());
            localJSONObject.put("device_id", DeviceInfo.getDeviceId());
            // TODO ROUTER_MAC
            // TODO cell_id
            localJSONObject.put("screen_resolution_desc", DeviceInfo.getResolution());
            // TODO horizontal_flag
            localJSONObject.put("os_version", DeviceInfo.getOsVersion());
            localJSONObject.put("mobile_operators_desc", DeviceInfo.getMCCMNC());
            localJSONObject.put("network_desc", DeviceInfo.getNetworkTypeWIFI2G3G());
            localJSONObject.put("sdk_version", DeviceInfo.getSDKVersion());
            localJSONObject.put("device_desc", DeviceInfo.getDeviceName());
            localJSONObject.put("app_source", AppInfo.getAppSource());
            localJSONObject.put("app_version", AppInfo.getAppVersion());
            localJSONObject.put("appkey", AppInfo.getAppKey());
            localJSONObject.put("language", DeviceInfo.getLanguage());
            // TODO IP
            localJSONObject.put("geo_position", DeviceInfo.getLongitude() + "," + DeviceInfo.getLatitude());

            // 事件日志
            JSONArray logArray = new JSONArray();
            JSONObject log = new JSONObject();

            // 操作日志－公共
            Set<Entry<String, Object>> entrySet = FmsConstants.sClientDataMap.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                log.put(entry.getKey(), entry.getValue());
            }

            // 操作日志－事件
            log.put("event_id", eventid);
            log.put("event_time", DeviceInfo.getDeviceTime());
            logArray.put(log);

            localJSONObject.put("event_log", logArray);
            StatLog.i(TAG, localJSONObject.toString());
        } catch (JSONException e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return localJSONObject;
    }
}
