package com.feifan.bp.net.http;

/**
 * Created by maning on 15/6/16.
 */
abstract public class HttpStack {

    public enum METHOD{
        GET,
        POST
    }

    abstract public Response connect(Request request);


}
