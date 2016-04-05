package com.feifan.bp.xgpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.login.LoginFragment;

/**
 * 这个是一个空的activity目的就是在用户点击notification的时候，进入此界面判断跳到哪个个界面
 */
public class NOPActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final UserProfile profile = UserProfile.getInstance();
        if (profile != null && profile.getUid() != -1) {
            intent.setClass(this, PlatformTopbarActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent.setClass(this, PlatformTopbarActivity.class);
            intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO,LoginFragment.class.getName());
            intent.putExtra(Constants.EXTRA_KEY_TITLE, getString(R.string.login_login_text));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PlatformState.getApplicationContext().startActivity(intent);
            finish();
        }
    }

}
