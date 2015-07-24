package com.feifan.bp.net.url;

/**
 * Created by maning on 15/7/24.
 */
public class UrlProductPreFactory implements IUrlFactory {

    @Override
    public String getFFanHostUrl() {
        return "https://api.ffan.com/";
    }

    @Override
    public String getH5HostUrl() {
        return "http://sop.pre.ffan.com/";
    }

}
