package com.feifan.bp.net.http;

import android.provider.Settings;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maning on 15/6/16.
 */
public abstract class Request {

    private HttpStack.METHOD mMethod;
    private String mUrl;
    private List<NameValuePair> mNameValueList = new ArrayList<>();

    public abstract void process(Response response);

    public HttpStack.METHOD getMethod() {
        return mMethod;
    }

    public void setMethod(HttpStack.METHOD mMethod) {
        this.mMethod = mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void putParams(String name, String value) {
        mNameValueList.add(new BasicNameValuePair(name, value));
    }

    public List<NameValuePair> getParams() {
        return mNameValueList;
    }
}
