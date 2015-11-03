package com.wanda.stat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WStat {
  /*
   * stat service provider types
   */
  public static final int PROVIDER_UMENG = 1; // stat with umeng
  public static final int PROVIDER_WANDA = 2; // stat with wanda
  public static final int PROVIDER_BOTH = 3; // stat with both
  /*
   * stat service provider
   */
  private static int provider = PROVIDER_BOTH;
  private static String userId = "";
  private static String app_source = "0";

  public static String getApp_source() {
    return app_source;
  }

  public static void setApp_source(String app_source) {
    WStat.app_source = app_source;
  }

  public static int getProvider() {
    return provider;
  }

  public static void setProvider(int provider) {
    WStat.provider = provider;
  }

  @SuppressLint("UseSparseArrays")
  private static HashMap<Integer, WClickAgent> clickAgents = new HashMap<Integer, WClickAgent>();
  private static WClickAgent wandaClickAgent = new WClickAgentImpl();
  private static WClickAgent umengClickAgent = new WClickAgentUmengImpl();

  static {
    clickAgents.put(PROVIDER_UMENG, umengClickAgent);
    clickAgents.put(PROVIDER_WANDA, wandaClickAgent);
  }

  // stat in logcat
  public static void setDebug(boolean isDebug) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.setDebug(isDebug);
      }
    }

  }

  // stat policy
  public static void setReportPolicy(Context context, int reportPolicy) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.setReportPolicy(context, reportPolicy);
      }
    }
  }

  // auto add location info with stat
  public static void setAutoLocation(boolean auto) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.setAutoLocation(auto);
      }
    }
  }

  // log on activity resume
  public static void onResume(Context context) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onResume(context);
      }
    }
  }

  // log on acitivity pause
  public static void onPause(Context context) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onPause(context);
      }
    }
  }

  // submit a error log
  public static void onError(Context context, String errorMsg) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onError(context, errorMsg);
      }
    }
  }

  public static void setUserId(String userId) {
    WStat.userId = userId;
  }

  public static String getUserId() {
    return WStat.userId;
  }

  // custome event log
  public static void onEvent(Context context, String eventId) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEvent(context, eventId);

      }
    }
  }

  // custome event log
  public static void onEvent(Context context, String event_id, String label) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEvent(context, event_id, label);
      }
    }
  }

  public void onEventBegin(Context context, String event_id, String label) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventBegin(context, event_id, label);
      }
    }
  }

  public void onEventEnd(Context context, String event_id, String label) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventEnd(context, event_id, label);
      }
    }
  }

  public void onEventBegin(Context context, String event_id) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventBegin(context, event_id);
      }
    }
  }

  public void onEventEnd(Context context, String event_id) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventEnd(context, event_id);
      }
    }
  }

  public void onEventDuration(Context context, String event_id, long duration) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventDuration(context, event_id, duration);
      }
    }
  }

  public void onEventDuration(Context context, String event_id, String label,
      long duration) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventDuration(context, event_id, label, duration);
      }
    }
  }

  public void onEventDuration(Context context, String event_id,
      HashMap<String, String> map, long duration) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.onEventDuration(context, event_id, map, duration);
      }
    }
  }

  public static void updateOnlineConfig(Context context) {
    Iterator<Map.Entry<Integer, WClickAgent>> iter = clickAgents.entrySet()
        .iterator();
    while (iter.hasNext()) {
      Map.Entry<Integer, WClickAgent> entry = iter.next();
      int providerType = entry.getKey();
      WClickAgent agent = entry.getValue();
      if ((providerType & provider) != 0) {
        agent.updateOnlineConfig(context);
      }
    }
  }

  public static void setUmsBaseUrl(String url) {
    WClickAgentImpl.URL = url;
  }
}
