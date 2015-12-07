package com.feifan.bp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.feifan.bp.Constants;
import com.feifan.bp.R;

/**
 * Created by xuchunlei on 15/12/4.
 */
public abstract class PlatformFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.topbar_menu, menu);
        MenuInfo info = getMenuInfo();
        if(info != null) {
            MenuItem item = menu.add(Menu.NONE, info.id, 1, info.titleRes);
            if(info.iconRes != Constants.NO_INTEGER) {
                item.setIcon(info.iconRes);
            }
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setOnMenuItemClickListener(this);
        }
    }

    protected MenuInfo getMenuInfo() {
        return null;
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
}
