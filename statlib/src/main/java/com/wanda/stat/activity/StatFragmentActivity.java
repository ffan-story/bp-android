package com.wanda.stat.activity;

import android.support.v4.app.FragmentActivity;

import com.wanda.stat.WStat;

public class StatFragmentActivity extends FragmentActivity {
  @Override
  protected void onResume() {
    super.onResume();
    WStat.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    WStat.onPause(this);
  }
}
