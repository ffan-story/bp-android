package com.feifan.bp.net.url;

/**
 * Created by maning on 15/7/24.
 */
public class UrlGatewaySitFactory extends UrlFactory {

    @Override
    public String getFFanHostUrl() {
        return "http://api.sit.ffan.com/";
    }

    @Override
    public String getH5HostUrl() {
        return "http://sop.sit.ffan.com/";
    }

}
