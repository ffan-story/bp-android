package com.feifan.bp.home.writeoff;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by konta on 15-11-2.
 */
public class CodeCheckModel extends BaseModel {
    public CodeCheckModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {

    LogUtil.i("CodeCheckModel","json===="+ json);
    }
}
