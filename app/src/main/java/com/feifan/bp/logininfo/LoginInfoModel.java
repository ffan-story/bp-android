package com.feifan.bp.logininfo;

import android.text.TextUtils;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tianjun on 2015-10-30.
 */
public class LoginInfoModel extends BaseModel {
    /**
     * 商户ID
     */
    private String uid;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属商户类型ID，门店或商户ID
     */
    private String authRangeId;

    /**
     * 所属商户类型名称（store：门店，merchant：商户）
     */
    private String authRangeType;

    /**
     * 登录权限ID，当类型为门店时，用于标示是店长或店员，198为店员，其他为店长
     */
    private String agid;
    /**
     * "技术导入(禁止修改)" －－ 所属商户名称
     */
    private String merchantName;
    /**
     * "哆啦A梦" －－所属门店名称
     */
    private String storeName;
    /**
     * "哆啦A梦（南昌红谷滩店）" －－ 所属门店名称
     */
    private String storeViewName;
    /**
     * "南昌红谷滩万达广场" －－所属广场名称
     */
    private String plazaName;
    /**
     * 省份ID
     */
    private String provinceId;
    /**
     * 城市ID
     */
    private String cityId;
    /**
     * "店长" －－商户身份 店长、店员或商户
     */
    private String identity;

    public LoginInfoModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);

        JSONObject data = new JSONObject(json);

        uid = data.optString("uid");
        name = data.optString("name");
        phone = data.optString("phone");
        authRangeId = data.optString("auth_range_id");
        authRangeType = data.optString("auth_range_type");
        agid = data.optString("agid");
        try {
            JSONObject rangNameJSONObject = data.getJSONObject("rangeName");
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("merchantName"))) {
                merchantName = rangNameJSONObject.optString("merchantName");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("storeName"))) {
                storeName = rangNameJSONObject.optString("storeName");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("storeViewName"))) {
                storeViewName = rangNameJSONObject.optString("storeViewName");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("plazaName"))) {
                plazaName = rangNameJSONObject.optString("plazaName");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("provinceId"))) {
                provinceId = rangNameJSONObject.optString("provinceId");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("cityId"))) {
                cityId = rangNameJSONObject.optString("cityId");
            }
            if (!TextUtils.isEmpty(rangNameJSONObject.optString("identity"))) {
                identity = rangNameJSONObject.optString("identity");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAuthRangeId() {
        return authRangeId;
    }

    public String getAuthRangeType() {
        return authRangeType;
    }

    public String getAgid() {
        return agid;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreViewName() {
        return storeViewName;
    }

    public String getPlazaName() {
        return plazaName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public String toString() {
        return "uid=" + uid + " ,name=" + name + " ,phone=" + phone + " ,authRangeId=" + authRangeId
                + ",authRangeType=" + authRangeType
                + ",agid=" + agid + " ,merchantName=" + merchantName + ",storeName=" + storeName
                + " ,storeViewName=" + storeViewName + ",plazaName=" + plazaName + ",provinceId="
                + provinceId
                + " ,cityId=" + cityId + ",identity=" + identity;
    }
}
