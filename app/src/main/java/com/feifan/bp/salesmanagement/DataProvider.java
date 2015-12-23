package com.feifan.bp.salesmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 假数据,用于测试.
 *
 * Created by Frank on 15/12/18.
 */
public final class DataProvider {

    private DataProvider() {
    }

    private static final Random RANDOM = new Random();

    public static List<ActivityModel> getRandomData(int size) {
        List<ActivityModel> activityList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            activityList.add(createActivity());
        }
        return activityList;
    }

    public static ActivityModel createActivity() {
        return new ActivityModel("北京通州周周抢夏日狂欢","闪购活动","50","2015.8.1 10:00:00","2015.8.8 10:00:00");
    }
}
