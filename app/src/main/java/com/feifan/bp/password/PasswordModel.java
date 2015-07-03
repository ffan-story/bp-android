package com.feifan.bp.password;

import org.json.JSONObject;

public class PasswordModel {
    public String token;
   

    public PasswordModel(JSONObject json) { 
        token = json.optString("token"); 
    }

    @Override
    public String toString() {
        return "token=" + token ;
    }
}
