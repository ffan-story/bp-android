package com.feifan.bp.home.Model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class MenuListModel extends ArrayList<MenuModel> {

    public MenuListModel(JSONArray json) {
        for(int i = 0;i < json.length();i++) {
            JSONArray menus = json.optJSONObject(i).optJSONArray("menu");
            for(int j = 0;j < menus.length();j++) {
                add(new MenuModel(menus.optJSONObject(j)));
            }
        }
    }

    @Override
    public String toString() {
        return "size=" + size();
    }
}
