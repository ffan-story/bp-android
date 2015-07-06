package com.feifan.bp.password;

import org.json.JSONObject;

public class PasswordModel {
    public String authCode="";
    public String key="";
   

    public PasswordModel(JSONObject json) { 
        if(json==null){
            return;
        }
        authCode = json.optString("authCode"); 
        key = json.optString("key"); 
    }

    @Override
    public String toString() {
        return "authCode=" + authCode+","+"key"+key ;
    }
}
