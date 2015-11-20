package com.feifan.bp.browser;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import com.feifan.bp.Constants;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
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

    /** 期望动作－打开 */
    public static final int PENDING_DO_OPEN = 1;
    /** 期望动作－关闭 */
    public static final int PENDING_DO_CLOSE = PENDING_DO_OPEN;

    // 顶部菜单项集合
    private static final Map<String, MenuInfo> sMenuStore;

    static {
        sMenuStore = new HashMap<String, MenuInfo>();
        sMenuStore.put(Utils.getString(R.string.browser_staff_list), new MenuInfo(R.id.menu_staff_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.browser_coupon_list), new MenuInfo(R.id.menu_coupon_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.index_commodity_text), new MenuInfo(R.id.menu_commodity_add, R.mipmap.menu_ic_add));
        sMenuStore.put(Utils.getString(R.string.browser_commodity_desc), new MenuInfo(R.id.menu_picture_add, R.mipmap.menu_ic_pict));
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

        return sMenuStore.get(title);
    }

}
