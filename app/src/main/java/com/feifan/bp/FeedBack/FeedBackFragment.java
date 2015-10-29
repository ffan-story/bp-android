package com.feifan.bp.FeedBack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.net.BaseRequestProcessListener;

/**
 * Created by tianjun on 2015-10-27.
 */
public class FeedBackFragment extends BaseFragment implements View.OnClickListener {
  private OnFragmentInteractionListener mListener;
  private boolean mIsConfirmEnable = true;
  private EditText mEtFeedBack;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_feedback, container, false);
    mEtFeedBack = (EditText) v.findViewById(R.id.feedback_content);
    return v;
  }

  @Override
    public void onClick(View v) {
      switch (v.getId()) {
          case R.id.feedback_submit:
              if (!mIsConfirmEnable) {
                  break;
              }
              if (mEtFeedBack.getText().length() > Constants.FEED_BACK_MAX_lENGTH) {
                  Utils.showShortToast(getActivity(), R.string.error_message_feed_back_length, Gravity.CENTER);
                  return;
              }
              mIsConfirmEnable = false;
              UserProfile manager = UserProfile.instance(getActivity());
              int uid = manager.getUid();
              String description = mEtFeedBack.getText().toString();
              FeedBackCtrl.submitFeedBack(getActivity(), String.valueOf(uid), description,
                      new BaseRequestProcessListener<FeedBackModel>(getActivity()) {
                          @Override
                          public void onResponse(final FeedBackModel feedBackModel) {
                              Log.i("", "@@@@@@ feedBackstatus = "+ feedBackModel.getStatus());
                          }
                      });
              break;
      }
  }

  public static FeedBackFragment newInstance() {
    FeedBackFragment fragment = new FeedBackFragment();
    return fragment;
  }

  @Override
  protected void setupToolbar(Toolbar toolbar) {
    super.setupToolbar(toolbar);
    toolbar.setTitle(R.string.feed_back);
    toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          Bundle b = new Bundle();
          b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
              FeedBackFragment.class.getName());
          b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
              OnFragmentInteractionListener.TYPE_NAVI_CLICK);
          mListener.onFragmentInteraction(b);
        }
      }
    });
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
}
