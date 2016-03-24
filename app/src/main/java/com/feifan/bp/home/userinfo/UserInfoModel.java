package com.feifan.bp.home.userinfo;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录者信息数据模型
 *
 * Created by tianjun on 2015-10-30.
 */
public class UserInfoModel extends BaseModel {
    /**
     * 商户ID
     */
    public String uid;
    /**
     * 商户名称
     */
    public String name;
    /**
     * 手机号
     */
    public String phone;

    /**
     * 所属商户类型ID，门店或商户ID
     */
    public String authRangeId;

    /**
     * 所属商户类型名称（store：门店，merchant：商户）
     */
    public String authRangeType;

    /**
     * 登录权限ID，当类型为门店时，用于标示是店长或店员，198为店员，其他为店长
     */
    public String agid;
    /**
     * "技术导入(禁止修改)" －－ 所属商户名称
     */
    public String merchantName;
    /**
     * "哆啦A梦" －－所属门店名称
     */
    public String storeName;
    /**
     * "哆啦A梦（南昌红谷滩店）" －－ 所属门店名称
     */
    public String storeViewName;
    /**
     * "南昌红谷滩万达广场" －－所属广场名称
     */
    public String plazaName;
    /**
     * 省份ID
     */
    public String provinceId;
    /**
     * 城市ID
     */
    public int cityId;
    /**
     * "店长" －－商户身份 店长、店员或商户
     */
    public String identity;

    public UserInfoModel(JSONObject json) {
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
            merchantName = rangNameJSONObject.optString("merchantName");
            storeName = rangNameJSONObject.optString("storeName");
            storeViewName = rangNameJSONObject.optString("storeViewName");
            plazaName = rangNameJSONObject.optString("plazaName");
            plazaName = (plazaName == null ? "无":plazaName);

            provinceId = rangNameJSONObject.optString("provinceId");
            cityId = rangNameJSONObject.optInt("cityId");
            identity = rangNameJSONObject.optString("identity");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
