package com.feifan.bp.home.code;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konta on 15-11-2.
 */
public class CodeModel extends BaseModel {
    private final String TAG = "CodeModel";
    public CodeModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        LogUtil.e(TAG,json);
    }


}
