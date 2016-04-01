package com.feifan.bp.xgpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;


public class NotificationUtils {
  public static final String PUSH_MESSAGE_NOTIFICATION_ID = "push_notification_id";
  private static final String NOTIFICATION_ID_KEY = "notification_id";

  public static final int BASE_NOTIFICATION_ID = 30000;

  public static int showNotification(Context context,
                                     Intent notificationIntent, String content, int notificationIcon) {
    return showNotification(context, notificationIntent, content,
        notificationIcon, true);
  }

  public static int showNotification(Context context,
                                     Intent notificationIntent, String content, int notificationIcon,
                                     int notificationId) {
    return showNotification(context, notificationIntent, content,
        notificationIcon, true, false, false, notificationId);
  }

  public static int showNotification(Context context,
                                     Intent notificationIntent, String content, int notificationIcon,
                                     boolean vibrate) {
    int notificationId = getNextNotificationId(context);
    return showNotification(context, notificationIntent, content,
        notificationIcon, vibrate, false, false, notificationId);
  }

  public static int showNotification(Context context,
                                     PendingIntent notificationIntent, String content,
                                     int notificationIcon) {
    return showNotification(context, notificationIntent, content,
        notificationIcon, true);
  }

  public static int showNotification(Context context,
                                     PendingIntent notificationIntent, String content,
                                     int notificationIcon, int notificationId) {
    return showNotification(context, notificationIntent, content,
        notificationIcon, true, false, false, notificationId);
  }

  public static int showNotification(Context context,
                                     PendingIntent notificationIntent, String content,
                                     int notificationIcon, boolean vibrate) {
    int notificationId = getNextNotificationId(context);
    return showNotification(context, notificationIntent, content,
        notificationIcon, vibrate, false, false, notificationId);
  }

  public static int showNotification(Context context,
                                     Intent notificationIntent, String content, int notificationIcon,
                                     boolean vibrate, boolean autoCancel, boolean onGoing,
                                     int notificationId) {
    int nid = notificationId;
    //-1 表示自动生成Id
    if (notificationId == -1) {
      nid = getNextNotificationId(context);
    }
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    notificationIntent.putExtra(PUSH_MESSAGE_NOTIFICATION_ID,
        nid);
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    return showNotification(context, contentIntent, content,
        notificationIcon, vibrate, autoCancel, onGoing, nid);
  }

  public static int showNotification(Context context,
                                     PendingIntent contentIntent, String content, int notificationIcon,
                                     boolean vibrate, boolean autoCancel, boolean onGoing,
                                     int notificationId) {
    int nid = notificationId;
    if (notificationId == -1) {
      nid = getNextNotificationId(context);
    }
    NotificationCompat.Builder builder = new NotificationCompat.Builder(
        context).setTicker(content).setWhen(System.currentTimeMillis())
            .setContentTitle(content).setSmallIcon(notificationIcon)
            .setContentIntent(contentIntent).setAutoCancel(autoCancel)
            .setOngoing(onGoing);
    if (vibrate) {
      long[] pattern = {20, 150, 100, 150};
      builder.setVibrate(pattern);
    }
    Notification notification = builder.build();
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notification.defaults |= Notification.DEFAULT_VIBRATE;
    notification.defaults |= Notification.DEFAULT_SOUND;
    NotificationManager notificationMgr = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    notificationMgr.notify(nid, notification);
    return nid;
  }

  // Each incoming message gets its own notification. We have to use a new
  // unique notification id
  // for each one.
  private static int getNextNotificationId(Context context) {
    SharedPreferences sp = PreferenceManager
        .getDefaultSharedPreferences(context.getApplicationContext());
    int notificationId = sp.getInt(NOTIFICATION_ID_KEY, 0);
    ++notificationId;
    if (notificationId > BASE_NOTIFICATION_ID) {
      notificationId = 1; // wrap around before it gets dangerous
    }

    // Save the updated notificationId in SharedPreferences
    SharedPreferences.Editor editor = sp.edit();
    editor.putInt(NOTIFICATION_ID_KEY, notificationId);
    editor.apply();

    return notificationId;
  }
}
