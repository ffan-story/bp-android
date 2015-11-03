package com.wanda.stat;

import android.content.Context;

import com.wbtech.ums.UmsAgent;

import java.util.HashMap;

public class WClickAgentImpl implements WClickAgent {
  public static String URL = "http://10.199.80.182/razor/web/index.php?";

  public WClickAgentImpl() {
    UmsAgent.setBaseURL(URL);
  }

  @Override
  public void setDebug(boolean isDebug) {}

  @Override
  public void setReportPolicy(Context context, int reportPolicy) {
    if (reportPolicy == WReportPolicy.BATCH_IN_LAUNCH) {
      UmsAgent.setDefaultReportPolicy(context, 0);

    } else if (reportPolicy == WReportPolicy.INTERVAL) {
      UmsAgent.setDefaultReportPolicy(context, 1);

    }
    else if (reportPolicy == WReportPolicy.APPLOGLOGINANDLAUNCH) {
      UmsAgent.setDefaultReportPolicy(context, 2);

    }

  }

  @Override
  public void setAutoLocation(boolean auto) {
    UmsAgent.setAutoLocation(auto);
  }

  @Override
  public void onResume(Context context) {
    UmsAgent.onResume(context);
  }

  @Override
  public void onPause(Context context) {
    UmsAgent.onPause(context);
  }

  @Override
  public void onError(Context context, String errorMsg) {
    UmsAgent.onError(context, errorMsg);
  }

  @Override
  public void onEvent(Context context, String evengId) {
    UmsAgent.onEvent(context, evengId);
  }

  @Override
  public void onEvent(Context context, String event_id, String label) {
    UmsAgent.onEvent(context, event_id, label);
  }

  @Override
  public void updateOnlineConfig(Context context) {}

  @Override
  public void onEventBegin(Context context, String event_id, String label) {

  }

  @Override
  public void onEventEnd(Context context, String event_id, String label) {

  }

  @Override
  public void onEventDuration(Context context, String event_id, long duration) {

  }

  @Override
  public void onEventDuration(Context context, String event_id, String label,
      long duration) {

  }

  @Override
  public void onEventDuration(Context context, String event_id,
      HashMap<String, String> map, long duration) {

  }

  @Override
  public void onEventBegin(Context context, String event_id) {

  }

  @Override
  public void onEventEnd(Context context, String event_id) {

  }
}
