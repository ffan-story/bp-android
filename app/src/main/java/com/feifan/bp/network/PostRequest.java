package com.feifan.bp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.UserProfile;
import com.feifan.bp.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * POST请求
 * Created by xuchunlei on 15/10/26.
 */
public class PostRequest<T extends BaseModel> extends JsonRequest<T> {

    private Map<String, String> mParams = new HashMap<>();

    public PostRequest(String url, ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mParams.put("appType", "bpMobile");
        mParams.put("clientType", "Android");
        mParams.put("version", String.valueOf(BuildConfig.VERSION_CODE));
//            mParams.put("clientAgent", )
        mParams.put("uid", String.valueOf(UserProfile.getInstance().getUid()));
        mParams.put("agid", UserProfile.getInstance().getAuthRangeId());
        mParams.put("loginToken", UserProfile.getInstance().getLoginToken());
    }

    public PostRequest<T> param(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        LogUtil.i(TAG, mParams.toString());
        return mParams;
    }
}
