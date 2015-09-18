package com.feifan.bp.net.url;

/**
 * Created by maning on 15/7/24.
 */
public class UrlProductPreFactory extends UrlFactory {
    @Override
    protected String getPictureUploadUrl() {
        return "https://api.pre.ffan.com/ffan/v1/";
    }

    @Override
    public String getFFanHostUrl() {
        return "https://api.ffan.com/";
    }

    @Override
    public String getH5HostUrl() {
        return "http://sop.pre.ffan.com/";
    }

}
