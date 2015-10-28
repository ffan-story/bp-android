package com.feifan.bp.logininfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;

/**
 * Created by tianjun on 2015-10-26.
 */
public class LoginInfoFragment extends BaseFragment implements View.OnClickListener {
  private OnFragmentInteractionListener mListener;

  public static LoginInfoFragment newInstance() {
    LoginInfoFragment fragment = new LoginInfoFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_login_info, container, false);
    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  protected void setupToolbar(Toolbar toolbar) {
    super.setupToolbar(toolbar);
    toolbar.setTitle(R.string.login_info);
    toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          Bundle b = new Bundle();
          b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
              LoginInfoFragment.class.getName());
          b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
              OnFragmentInteractionListener.TYPE_NAVI_CLICK);
          mListener.onFragmentInteraction(b);
        }
      }
    });
  }

  @Override
  public void onClick(View v) {

  }
}
