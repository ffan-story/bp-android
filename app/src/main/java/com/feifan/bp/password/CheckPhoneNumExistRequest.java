package com.feifan.bp.password;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.net.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class CheckPhoneNumExistRequest extends Request<PasswordModel> {

    private static final String TAG = CheckPhoneNumExistRequest.class.getSimpleName();

    private static final String URL_FORMAT = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/verificationphone?phone=%s";
 

    private Listener<PasswordModel> mListener;

    public CheckPhoneNumExistRequest(Listener<PasswordModel> listener, ErrorListener errorListener, String phone) {
        super(Method.GET, String.format(URL_FORMAT, phone), errorListener);
        mListener = listener;
    }

     
    @Override
    protected Response<PasswordModel> parseNetworkResponse(NetworkResponse networkResponse) {
        String jsonStr = null;
        try {
            jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject json = new JSONObject(jsonStr);

            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new PasswordModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {
                LogUtil.w(TAG, "error status:" + jsonStr);
                return Response.error(new VolleyError(json.optString("msg")));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtil.w(TAG, "Response:" + jsonStr);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(PasswordModel passwordModel) {
        mListener.onResponse(passwordModel);
    }

}
