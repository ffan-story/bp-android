package com.feifan.bp.salesmanagement;

/**
 * 报名活动模型
 *
 * Created by Frank on 15/12/18.
 */
public class ActivityModel {

    private String activityName;//活动名称
    private String activityType;//活动类型
    private String signMerchantNum;//已报名商家数量
    private String activityStartDate;//活动开始时间
    private String activityEndDate;//活动结束时间

    public ActivityModel(String activityName, String activityType, String signMerchantNum, String activityStartDate, String activityEndDate) {
        this.activityName = activityName;
        this.activityType = activityType;
        this.signMerchantNum = signMerchantNum;
        this.activityStartDate = activityStartDate;
        this.activityEndDate = activityEndDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getSignMerchantNum() {
        return signMerchantNum;
    }

    public void setSignMerchantNum(String signMerchantNum) {
        this.signMerchantNum = signMerchantNum;
    }

    public String getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(String activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public String getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(String activityEndDate) {
        this.activityEndDate = activityEndDate;
    }
}
