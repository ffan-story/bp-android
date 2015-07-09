package com.feifan.bp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.feifan.bp.R;

/**
 * Created by maning on 15/7/9.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isShowToolbar()) {
            initToolbar();
        }
    }

    @Override
    public final void setContentView(int layoutResID) {
        if (isShowToolbar()) {
            super.setContentView(R.layout.activity_base);
            RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.activity_base_layout);
            View view = LayoutInflater.from(this).inflate(layoutResID, rootLayout, false);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.toolbar);
            view.setLayoutParams(params);
            rootLayout.addView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupToolbar(mToolbar);
    }

    /**
     * Return the toolbar of this activity,
     * you can change toolbar's views or actions after activity is start-up.
     * @return
     */
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Override this method in subclass and custom toolbar.
     * This method is called after activity is start-up.
     * @param toolbar
     */
    protected void setupToolbar(Toolbar toolbar) {
    }

    /**
     * Implement by subclass and return true if you want a toolbar is on top of your activity.
     * @return
     */
    protected abstract boolean isShowToolbar();

}
