package com.feifan.bp.password;

import org.json.JSONObject;

public class PasswordModel {
    public String token="";
   

    public PasswordModel(JSONObject json) { 
        if(json==null){
            return;
        }
        token = json.optString("token"); 
    }

    @Override
    public String toString() {
        return "token=" + token ;
    }
}
