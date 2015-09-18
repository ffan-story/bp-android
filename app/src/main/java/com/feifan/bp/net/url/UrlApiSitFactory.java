package com.feifan.bp.net.url;

/**
 * Created by maning on 15/7/24.
 */
public class UrlApiSitFactory extends UrlFactory {

    @Override
    protected String getPictureUploadUrl() {
        return "http://xapi.intra.ffan.com/";
    }

    @Override
    public String getFFanHostUrl() {
        return "http://xapi.sit.ffan.com/";
    }

    @Override
    public String getH5HostUrl() {
        return "http://sop.sit.ffan.com/";
    }
}
