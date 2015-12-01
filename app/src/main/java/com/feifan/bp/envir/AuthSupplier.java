package com.feifan.bp.envir;

import com.feifan.bp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限相关常量提供者
 * <pre>
 *     设置该类源于服务端不同环境下，提供的权限ID不同导致，后期可做优化
 * </pre>
 *
 * Created by xuchunlei on 15/10/31.
 */
public class AuthSupplier {

    public interface IAuthFactory {

        /**
         * 权限过滤器
         * <pre>
         *     存储目前支持的权限项ID及其图标资源ID
         * </pre>
         * @return
         */
        Map<Integer, Integer> getAuthFilter();

        /**
         * 权限父节点ID
         * <pre>
         *     用于提供过滤参数
         * </pre>
         * @return
         */
        String getNodeId();

        /**
         * 验证历史项ID
         *
         * @return
         */
        String getHistoryId();

        /**
         * 获取权限项tab标题资源
         * <pre>
         *     存在返回资源ID，不存在返回－1
         * </pre>
         * @return
         */
        int getAuthTabTitleRes(int authId);

        /**
         * 获取权限项tab状态资源
         * @param authId
         * @return
         */
        int getAuthTabStatusRes(int authId);

        /**
         * 获取退款售后项ID
         * @return
         */
        String getRefundId();

    }

    /**
     * SIT环境权限工厂
     */
    static class SitAuthFactory implements IAuthFactory {

        private Map<Integer, Integer> mFilter = new HashMap<Integer, Integer>();

        public SitAuthFactory(){
            mFilter.put(1142, R.mipmap.index_ic_order);    // 订单管理
            mFilter.put(1160, R.mipmap.index_ic_report);   // 统计报表
            mFilter.put(1161, R.mipmap.index_ic_staff);    // 员工管理
            mFilter.put(1162, R.mipmap.index_ic_refund);   // 退款售后
            mFilter.put(1226, R.mipmap.index_ic_commodity);// 商品管理
            mFilter.put(1227, R.mipmap.index_ic_marketing);// 营销管理
            mFilter.put(1333, R.mipmap.index_ic_report);   // 对账管理
        }

        @Override
        public Map<Integer, Integer> getAuthFilter() {
            return mFilter;
        }

        @Override
        public String getNodeId() {
            return "1141";
        }

        @Override
        public String getHistoryId() {
            return "1166";
        }

        @Override
        public int getAuthTabTitleRes(int authId) {
            switch (authId) {
                case 1142:                     // 订单管理
                    return R.array.tab_title_order;
                case 1161:                     // 员工管理
                    return R.array.tab_title_staff_management;
                case 1162:                     // 退款售后
                    return R.array.tab_title_refund;
                default:
                    return -1;
            }
        }

        @Override
        public int getAuthTabStatusRes(int authId) {
            switch (authId){
                case 1142: // 订单管理
                    return R.array.tab_title_order_status;
                case 1161: // 员工管理
                    return R.array.data_type;
                case 1162: // 退款售后
                    return R.array.tab_title_refund_status;
                default:
                    return -1;
            }

        }

        @Override
        public String getRefundId() {
            return "1162";
        }

    }

    static class ProductAuthFactory implements IAuthFactory {

        private Map<Integer, Integer> mFilter = new HashMap<Integer, Integer>();

        public ProductAuthFactory(){
            mFilter.put(997, R.mipmap.index_ic_order);     // 订单管理
            mFilter.put(1002, R.mipmap.index_ic_report);   // 统计报表
            mFilter.put(1003, R.mipmap.index_ic_staff);    // 员工管理
            mFilter.put(1004, R.mipmap.index_ic_refund);   // 退款售后
            mFilter.put(1081, R.mipmap.index_ic_commodity);// 商品管理
            mFilter.put(1082, R.mipmap.index_ic_marketing);// 营销管理
            mFilter.put(1145, R.mipmap.index_ic_report);   // 对账管理
        }

        @Override
        public Map<Integer, Integer> getAuthFilter() {
            return mFilter;
        }

        @Override
        public String getNodeId() {
            return "996";
        }

        @Override
        public String getHistoryId() {
            return "1005";
        }

        @Override
        public int getAuthTabTitleRes(int authId) {
            switch (authId) {
                case 997:                     // 订单管理
                    return R.array.tab_title_order;
                case 1003:                     // 员工管理
                    return R.array.tab_title_staff_management;
                case 1004:                     // 退款售后
                    return R.array.tab_title_refund;
                default:
                    return -1;
            }
        }

        @Override
        public int getAuthTabStatusRes(int authId) {
            switch (authId){
                case 997: // 订单管理
                    return R.array.tab_title_order_status;
                case 1003: // 员工管理
                    return R.array.data_type;
                case 1004: // 退款售后
                    return R.array.tab_title_refund_status;
                default:
                    return -1;
            }
        }

        @Override
        public String getRefundId() {
            return "1004";
        }

    }

}
