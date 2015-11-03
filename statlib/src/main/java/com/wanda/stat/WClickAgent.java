package com.wanda.stat;

import android.content.Context;

import java.util.HashMap;

public interface WClickAgent {

  public void setDebug(boolean isDebug);

  public void setReportPolicy(Context context, int reportPolicy);

  public void setAutoLocation(boolean auto);

  public void updateOnlineConfig(Context context);

  public void onResume(Context context);

  public void onPause(Context context);

  public void onError(Context context, String errorMsg);

  public void onEvent(Context context, String evengId);

  public void onEvent(Context context, String event_id, String label);

  public void onEventBegin(Context context, String event_id, String label);

  public void onEventEnd(Context context, String event_id, String label);

  public void onEventBegin(Context context, String event_id);

  public void onEventEnd(Context context, String event_id);

  public void onEventDuration(Context context, String event_id, long duration);

  public void onEventDuration(Context context, String event_id, String label, long duration);

  public void onEventDuration(Context context, String event_id, HashMap<String, String> map,
                              long duration);
}
