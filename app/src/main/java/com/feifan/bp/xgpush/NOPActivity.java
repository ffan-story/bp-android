package com.feifan.bp.xgpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.feifan.bp.UserProfile;

public class NOPActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final UserProfile profile = UserProfile.getInstance();
        if (profile != null && profile.getUid() != -1) {
             startActivity(intent);
        }else {
            
        }
    }

}
