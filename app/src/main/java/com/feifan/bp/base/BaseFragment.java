package com.feifan.bp.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Created by maning on 15/7/9.
 */
public class BaseFragment extends Fragment {

    protected Toolbar getToolbar() {
        Activity a = getActivity();
        if (a instanceof BaseActivity) {
            return ((BaseActivity)a).getToolbar();
        }
        return null;
    }
}
