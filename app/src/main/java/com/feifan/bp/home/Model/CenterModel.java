package com.feifan.bp.home.Model;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class CenterModel {

    public int id;
    public String primaryName;
    public String secondaryName;
    public String logoSrc;

    @Override
    public String toString() {
        return "id=" + id + ",primaryName=" + primaryName + ",secondaryName=" + secondaryName + ",logoSrc=" + logoSrc;
    }
}
