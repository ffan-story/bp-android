package com.feifan.bp.browser;

import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * 匹配器
 * <pre>
 *     用来匹配浏览器相关的各种
 * </pre>
 *
 * Created by xuchunlei on 15/11/17.
 */
public class BrowserMatcher {

    /**
     * 菜单信息类
     *
     * <pre>
     *     封装了创建菜单所需要的信息
     * </pre>
     *
     */
    public static class MenuInfo {
        public int id;
        public int iconRes;
        public int titleRes;
    }

    /**
     * 通过标题匹配顶部右侧菜单项名称
     *
     * @param title 页面标题
     * @return 菜单名称
     */
    public MenuInfo matchForMenu(String title) {
//        if (getString(R.string.browser_staff_list).equals(title)) {
//            mToolbarStatus = TOOLBAR_STATUS_STAFF;
//        } else if (getString(R.string.browser_coupon_list).equals(title)) {
//            mToolbarStatus = TOOLBAR_STATUS_COUPON;
//        } else if (getString(R.string.index_commodity_text).equals(title)) {
//            mToolbarStatus = TOOLBAR_STATUS_COMMODITY;
//        } else if (getString(R.string.browser_commodity_desc).equals(title)) {
//            mToolbarStatus = TOOLBAR_STATUS_COMMODITY_DESC;
//        } else {
//            mToolbarStatus = TOOLBAR_STATUS_IDLE;
//        }

//        if (mToolbarStatus == TOOLBAR_STATUS_STAFF) {
//            inflater.inflate(R.menu.menu_staff_manage, menu);
//            (menu.findItem(R.id.action_staff)).setOnMenuItemClickListener(this);
//        } else if (mToolbarStatus == TOOLBAR_STATUS_COUPON) {
//            inflater.inflate(R.menu.menu_coupon_add, menu);
//            (menu.findItem(R.id.action_coupon)).setOnMenuItemClickListener(this);
//        } else if (mToolbarStatus == TOOLBAR_STATUS_COMMODITY) {
//            inflater.inflate(R.menu.menu_commodity_manage, menu);
//            (menu.findItem(R.id.action_commodity)).setOnMenuItemClickListener(this);
//        } else if (mToolbarStatus == TOOLBAR_STATUS_COMMODITY_DESC) {
//            inflater.inflate(R.menu.menu_commodity_manage_desc, menu);
//            (menu.findItem(R.id.action_commodity_desc)).setOnMenuItemClickListener(this);
//        } else {
//            menu.clear();
//        }
//        super.onCreateOptionsMenu(menu, inflater);

        if(title == null) {
            return null;
        }

        if(title.equals(Utils.getString(R.string.browser_staff_list))) {
            MenuInfo info = new MenuInfo();
            info.id = R.id.menu_staff_add;
            info.titleRes = R.string.browser_staff_add;
            info.iconRes = R.mipmap.menu_ic_staff;
            return info;
        }

        return null;
    }

}
