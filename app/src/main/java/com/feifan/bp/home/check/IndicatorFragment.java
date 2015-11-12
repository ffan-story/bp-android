package com.feifan.bp.home.check;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;

/**
 * Created by tianjun on 2015-11-9.
 */
public class IndicatorFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    public static IndicatorFragment newInstance() {
        IndicatorFragment fragment = new IndicatorFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onTitleChanged(getString(R.string.indicator_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.indicator_details, container, false);

        TextView mMoreTradingDetails = (TextView) v.findViewById(R.id.indicator_more_trading_flow);
        CharSequence str = getText(R.string.indicator_more_trading_flow);
        SpannableString spannableString1 = new SpannableString(str);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Toast.makeText(getActivity(), "who hit me", Toast.LENGTH_SHORT).show();
                BrowserActivity.startActivity(getActivity(), "http://sop.ffan.com");
            }
        }, 12, str.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.accent)), 12, str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMoreTradingDetails.setText(spannableString1);
        mMoreTradingDetails.setMovementMethod(LinkMovementMethod.getInstance());
        return v;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.indicator_title);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
//                    Bundle b = new Bundle();
//                    b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
//                            IndicatorFragment.class.getName());
//                    b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
//                            OnFragmentInteractionListener.TYPE_NAVI_CLICK);
//                    mListener.onFragmentInteraction(b);
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
