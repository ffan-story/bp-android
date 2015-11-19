package com.feifan.bp.browser;

import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 匹配器
 * <pre>
 *     用来匹配浏览器相关的各种
 * </pre>
 *
 * Created by xuchunlei on 15/11/17.
 */
public class BrowserMatcher {

    private static final Map<String, MenuInfo> sMenuStore;

    static {
        sMenuStore = new HashMap<String, MenuInfo>();
        sMenuStore.put(Utils.getString(R.string.browser_staff_list), new MenuInfo(R.id.menu_staff_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.browser_coupon_list), new MenuInfo(R.id.menu_coupon_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.index_commodity_text), new MenuInfo(R.id.menu_commodity_add, R.mipmap.menu_ic_add));
    }

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

        public MenuInfo(int id, int iconRes, int titleRes) {
            this.id = id;
            this.iconRes = iconRes;
            this.titleRes = titleRes;
        }

        public MenuInfo(int id, int iconRes) {
            this.id = id;
            this.iconRes = iconRes;
            this.titleRes = R.string.no_title_text;
        }
    }

    /**
     * 通过标题匹配顶部右侧菜单项名称
     *
     * @param title 页面标题
     * @return 菜单名称
     */
    public MenuInfo matchForMenu(String title) {
//       if (getString(R.string.browser_commodity_desc).equals(title)) {
//            mToolbarStatus = TOOLBAR_STATUS_COMMODITY_DESC;
//        } else {
//            mToolbarStatus = TOOLBAR_STATUS_IDLE;
//        }

//       if (mToolbarStatus == TOOLBAR_STATUS_COMMODITY_DESC) {
//            inflater.inflate(R.menu.menu_commodity_manage_desc, menu);
//            (menu.findItem(R.id.action_commodity_desc)).setOnMenuItemClickListener(this);
//        } else {
//            menu.clear();
//        }

        if(title == null) {
            return null;
        }

        return sMenuStore.get(title);
    }

}
