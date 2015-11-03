package com.wanda.stat;

import android.content.Context;

import java.util.HashMap;

// import com.umeng.analytics.MobclickAgent;
// import com.umeng.analytics.ReportPolicy;

public class WClickAgentUmengImpl implements WClickAgent {
  public WClickAgentUmengImpl() {}

  @Override
  public void setDebug(boolean isDebug) {
    // MobclickAgent.setDebugMode(isDebug);
  }

  @Override
  public void setReportPolicy(Context context, int reportPolicy) {
    // if(reportPolicy == WReportPolicy.BATCH_IN_LAUNCH){
    // MobclickAgent.setDefaultReportPolicy(context, ReportPolicy.BATCH_AT_LAUNCH);
    //
    // }else if(reportPolicy == WReportPolicy.INTERVAL){
    // MobclickAgent.setDefaultReportPolicy(context, ReportPolicy.BATCH_BY_INTERVAL);
    // }
  }

  @Override
  public void setAutoLocation(boolean auto) {
    // MobclickAgent.setAutoLocation(auto);
  }

  @Override
  public void onResume(Context context) {
    // MobclickAgent.onResume(context);
  }

  @Override
  public void onPause(Context context) {
    // MobclickAgent.onPause(context);
  }

  @Override
  public void onError(Context context, String errorMsg) {
    // MobclickAgent.onError(context, errorMsg);
  }

  @Override
  public void onEvent(Context context, String evengId) {
    // MobclickAgent.onEvent(context, evengId);
  }

  @Override
  public void onEvent(Context context, String event_id, String label) {
    // MobclickAgent.onEvent(context, event_id, label);
  }

  @Override
  public void updateOnlineConfig(Context context) {
    // MobclickAgent.updateOnlineConfig(context);
  }

  @Override
  public void onEventBegin(Context context, String event_id, String label) {
    // MobclickAgent.onEventBegin(context, event_id, label);
  }

  @Override
  public void onEventEnd(Context context, String event_id, String label) {
    // MobclickAgent.onEventEnd(context, event_id, label);
  }

  @Override
  public void onEventDuration(Context context, String event_id, long duration) {
    // MobclickAgent.onEventDuration(context, event_id, duration);
  }

  @Override
  public void onEventDuration(Context context, String event_id, String label,
      long duration) {
    // MobclickAgent.onEventDuration(context, event_id, label, duration);

  }

  @Override
  public void onEventDuration(Context context, String event_id,
      HashMap<String, String> map, long duration) {
    // MobclickAgent.onEventDuration(context, event_id, map, duration);
  }

  @Override
  public void onEventBegin(Context context, String event_id) {
    // MobclickAgent.onEventBegin(context, event_id);
  }

  @Override
  public void onEventEnd(Context context, String event_id) {
    // MobclickAgent.onEventEnd(context, event_id);
  }
}
