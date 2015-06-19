package com.feifan.bp.home.Model;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class MenuModel {

    public int id;
    public String name;
    public String url;

    public MenuModel(JSONObject json) {
        id = json.optInt("id");
        name = json.optString("name");
        url = json.optString("url");
    }
}
