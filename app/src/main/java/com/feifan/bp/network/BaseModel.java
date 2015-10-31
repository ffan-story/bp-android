package com.feifan.bp.network;

import android.text.TextUtils;

import com.feifan.bp.Constants;
import com.feifan.bp.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求获取的数据模型基类
 * <pre>
 *     其他模型必须继承此类，并做相应扩展
 *     字段统一使用public修饰符，不使用setter/getter
 * </pre>
 * Created by xuchunlei on 15/10/31.
 */
public class BaseModel {

    protected static final String TAG = "Model";

    /**
     * 状态
     */
    public int status;
    /**
     * 消息
     */
    public String msg;

    public BaseModel(JSONObject json) {

        // FIXME 服务端设计不统一导致的复杂解析逻辑，后续会建议修正

        // 解析基础信息
        status = json.optInt("status");
        if (status == 0) {
            status = Constants.RESPONSE_CODE_SUCCESS;
            msg = json.optString("message");
        } else {
            msg = json.optString("msg");
            if (TextUtils.isEmpty(msg)) {
                msg = json.optString("message");
            }
        }

        // 解析数据信息
        if(status == Constants.RESPONSE_CODE_SUCCESS) {
            try {
                parseData(json.opt("data").toString());
            } catch (JSONException e) {
                LogUtil.w(TAG, e.getMessage());
            }
        }
        LogUtil.i(TAG, toString());
    }

    /**
     * 解析数据信息
     * <pre>
     *     子类中实现该方法
     * </pre>
     * @param json
     */
    protected void parseData(String json) throws JSONException {
        // TODO Do not do anything here
    }
}
