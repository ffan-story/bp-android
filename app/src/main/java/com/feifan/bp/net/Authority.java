package com.feifan.bp.net;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.R;
import com.feifan.bp.home.command.CommodityCmd;
import com.feifan.bp.home.command.MarketingCmd;
import com.feifan.bp.home.command.OrderManagementCmd;
import com.feifan.bp.home.command.RefundCmd;
import com.feifan.bp.home.command.StaffManagementCmd;
import com.feifan.bp.home.command.StatisticReportCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maning on 15/8/10.
 */
public class Authority {
    public static Map<String, Auth> AUTH_MAP;
    public static List<String> AUTH_LIST;
    public static String REFUND_ID;
    public static String HISTORY_ID;
    public static String NODE_ID;
    static {
        AUTH_MAP = new HashMap<>();
        AUTH_LIST = new ArrayList<>();
        switch (BuildConfig.CURRENT_ENVIRONMENT) {
            case SIT:
                AUTH_LIST.add("1142");
                AUTH_LIST.add("1160");
                AUTH_LIST.add("1161");
                AUTH_LIST.add("1226");
                AUTH_LIST.add("1227");
                AUTH_LIST.add("1162");
                AUTH_MAP.put("1142", new Auth("1142",
                        R.string.index_order_text, R.mipmap.index_ic_order, OrderManagementCmd.class));
                AUTH_MAP.put("1160", new Auth("1160",
                        R.string.index_report_text, R.mipmap.index_ic_report, StatisticReportCmd.class));
                AUTH_MAP.put("1161", new Auth("1161",
                        R.string.index_staff_text, R.mipmap.index_ic_staff, StaffManagementCmd.class));
                AUTH_MAP.put("1162", new Auth("1162",
                        R.string.index_refund_text, R.mipmap.index_ic_refund, RefundCmd.class));
                AUTH_MAP.put("1226", new Auth("1226",
                        R.string.index_commodity_text, R.mipmap.index_ic_commodity, CommodityCmd.class));
                AUTH_MAP.put("1227", new Auth("1227",
                        R.string.index_marketing_text, R.mipmap.index_ic_marketing, MarketingCmd.class));
                REFUND_ID = "1162";
                HISTORY_ID = "1166";
                NODE_ID = "1141";
                break;
            case PRODUCT_PRE:

                break;
            case PRODUCT:

                AUTH_LIST.add("997");
                AUTH_LIST.add("1002");
                AUTH_LIST.add("1003");
                AUTH_LIST.add("1081");
                AUTH_LIST.add("1082");
                AUTH_LIST.add("1004");
                AUTH_MAP.put("997", new Auth("997",
                        R.string.index_order_text, R.mipmap.index_ic_order, OrderManagementCmd.class));
                AUTH_MAP.put("1002", new Auth("1002",
                        R.string.index_report_text, R.mipmap.index_ic_report, StatisticReportCmd.class));
                AUTH_MAP.put("1003", new Auth("1003",
                        R.string.index_staff_text, R.mipmap.index_ic_staff, StaffManagementCmd.class));
                AUTH_MAP.put("1004", new Auth("1004",
                        R.string.index_refund_text, R.mipmap.index_ic_refund, RefundCmd.class));
                AUTH_MAP.put("1081", new Auth("1081",
                        R.string.index_commodity_text, R.mipmap.index_ic_commodity, CommodityCmd.class));
                AUTH_MAP.put("1082", new Auth("1082",
                        R.string.index_marketing_text, R.mipmap.index_ic_marketing, MarketingCmd.class));
                REFUND_ID = "1004";
                HISTORY_ID = "1005";
                NODE_ID = "996";

                break;
            default:
                AUTH_LIST.add("1142");
                AUTH_LIST.add("1160");
                AUTH_LIST.add("1161");
                AUTH_LIST.add("1162");
                AUTH_LIST.add("1226");
                AUTH_LIST.add("1227");
                AUTH_MAP.put("1142", new Auth("1142",
                        R.string.index_order_text, R.mipmap.index_ic_order, OrderManagementCmd.class));
                AUTH_MAP.put("1160", new Auth("1160",
                        R.string.index_report_text, R.mipmap.index_ic_report, StatisticReportCmd.class));
                AUTH_MAP.put("1161", new Auth("1161",
                        R.string.index_staff_text, R.mipmap.index_ic_staff, StaffManagementCmd.class));
                AUTH_MAP.put("1162", new Auth("1162",
                        R.string.index_refund_text, R.mipmap.index_ic_refund, RefundCmd.class));
                AUTH_MAP.put("1226", new Auth("1226",
                        R.string.index_commodity_text, R.mipmap.index_ic_commodity, CommodityCmd.class));
                AUTH_MAP.put("1227", new Auth("1227",
                        R.string.index_marketing_text, R.mipmap.index_ic_marketing, MarketingCmd.class));
                REFUND_ID = "1162";
                HISTORY_ID = "1166";
                NODE_ID = "1141";
                break;
        }
    }

    public static class Auth {
        public Auth(String id, int titleResId, int iconResId, Class<?> clazz) {
            this.id = id;
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.clazz = clazz;
        }
        public int titleResId;
        public int iconResId;
        public String id;
        public Class<?> clazz;

    }
}
