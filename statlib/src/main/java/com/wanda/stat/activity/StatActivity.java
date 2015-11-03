package com.wanda.stat.activity;

import android.app.Activity;

import com.wanda.stat.WStat;

public class StatActivity extends Activity {
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
