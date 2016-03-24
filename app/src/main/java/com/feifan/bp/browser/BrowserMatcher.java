package com.feifan.bp.browser;

import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.envir.EnvironmentManager;

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



    // 顶部菜单项集合
    private static final Map<String, MenuInfo> sMenuStore;

    // 顶部菜单权限集合
    private static final Map<String, String> sRightStore;

    static {
        sMenuStore = new HashMap<String, MenuInfo>();
        sMenuStore.put(Utils.getString(R.string.browser_staff_list), new MenuInfo(R.id.menu_staff_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.browser_coupon_list), new MenuInfo(R.id.menu_coupon_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.index_commodity_text), new MenuInfo(R.id.menu_commodity_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.browser_commodity_desc), new MenuInfo(R.id.menu_picture_add, R.mipmap.menu_ic_pict));
        sMenuStore.put(Utils.getString(R.string.index_refund_text), new MenuInfo(R.id.menu_refund_start,Constants.NO_INTEGER, R.string.refund_menu_start_text));

        sRightStore = new HashMap<String, String>();
        sRightStore.put(Utils.getString(R.string.index_refund_text), EnvironmentManager.getAuthFactory().getRefundRightId());
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
     * 通过标题匹配顶部右侧菜单项信息
     *
     * @param title 页面标题
     * @return 菜单信息
     */
    public MenuInfo matchForMenu(String title) {

        if(title == null) {
            return null;
        }

        return sMenuStore.get(getRightTitle(title));
    }

    /**
     * 获取权限认证过的Title
     * @param title
     * @return
     */
    private String getRightTitle(String title) {
        String right = sRightStore.get(title);
        if(right != null) {        //需要进行权限认证
            if(!UserProfile.getInstance().getRightString().contains(right)) {
                return null;
            }
        }
        return title;
    }

}
