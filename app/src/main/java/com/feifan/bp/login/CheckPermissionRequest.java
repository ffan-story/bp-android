package com.feifan.bp.login;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/8/6.
 */
public class CheckPermissionRequest extends BaseRequest<PermissionModel> {

    private static final String URL = NetUtils.getUrlFactory().checkPermission();

    public CheckPermissionRequest(Parameters params,
                                  OnRequestProcessListener<PermissionModel> listener) {
        super(Method.GET, URL, params, listener);
    }


    @Override
    protected PermissionModel onGetModel(JSONObject json) {
        return new PermissionModel(json);
    }

    public static class Params extends Parameters {
        @HttpParams(type = HttpParams.Type.URL)
        private String uid;

        @HttpParams(type = HttpParams.Type.URL)
        private String nodeid;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setNodeid(String nodeid) {
            this.nodeid = nodeid;
        }
    }


}
