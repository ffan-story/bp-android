package com.feifan.bp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by mengmeng on 16/3/21.
 */
public class SystemUtil {
    private static final String FEIFAN_BP = "com.feifan.bp";

    /**
     * get top activity name
     * @param context
     * @return
     */
    public static String getTopActivityName(Context context){
        if(context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            if (manager != null) {
                List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
                if (runningTaskInfos != null) {
                    if (runningTaskInfos != null && runningTaskInfos.size() > 0 && runningTaskInfos.get(0) != null && runningTaskInfos.get(0).topActivity != null) {
                        return runningTaskInfos.get(0).topActivity.getClassName();
                    }
                }
            }
        }
        return "";
    }

    /**
     * top activity is belong feifan bp
     * @param context
     * @return
     */
    public static boolean isBPActivities(Context context){
        if(context != null){
            String activityName = getTopActivityName(context);
            if(!TextUtils.isEmpty(activityName)){
                if(activityName.contains(FEIFAN_BP)){
                    return true;
                }
            }
        }
        return false;
    }
}
