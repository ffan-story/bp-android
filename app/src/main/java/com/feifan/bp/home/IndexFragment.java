package com.feifan.bp.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformHelper;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.widget.IconClickableEditText;
import com.feifan.bp.scanner.CodeScannerActivity;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 *
 *
 */
public class IndexFragment extends Fragment implements View.OnClickListener, IconClickableEditText.OnIconClickListener {

    // H5页面相对路径－报表统计
    private static final String URL_PATH_REPORT = "H5App/index.html#/statistical";
    // H5页面相对路径－验证历史
    private static final String URL_PATH_HISTORY = "H5App/index.html#/goods/search_history";
    // H5页面相对路径－订单管理
    private static final String URL_PATH_ORDER = "H5App/index.html#/order";
    // H5页面相对路径－查询提货码
    private static final String URL_PATH_SEARCH = "H5App/index.html#/goods/search_result";

    private OnFragmentInteractionListener mListener;

    // views
    private IconClickableEditText mCodeEdt;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        v.findViewById(R.id.index_scan).setOnClickListener(this);
        v.findViewById(R.id.index_history).setOnClickListener(this);
        v.findViewById(R.id.index_report).setOnClickListener(this);
        v.findViewById(R.id.index_order).setOnClickListener(this);
        mCodeEdt = (IconClickableEditText)v.findViewById(R.id.index_search_input);
        mCodeEdt.setOnIconClickListener(this);
        return v;
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
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;
            case R.id.index_history:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO,
                        PlatformHelper.getManagedH5Url(URL_PATH_HISTORY) + "&signStatus=2&merchantId="
                        + PlatformState.getInstance().getUserProfile().getAuthRangeId());
                break;
            case R.id.index_report:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, PlatformHelper.getManagedH5Url(URL_PATH_REPORT));
                break;
            case R.id.index_order:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, PlatformHelper.getManagedH5Url(URL_PATH_ORDER));
                break;
            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    @Override
    public void onRightClicked(View v) {

        if(TextUtils.isEmpty(mCodeEdt.getText())) {
            return;
        }
        String code = mCodeEdt.getText().toString();
        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.index_search_input:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO,
                        PlatformHelper.getManagedH5Url(URL_PATH_SEARCH)
                        + "&merchantId=" + PlatformState.getInstance().getUserProfile().getAuthRangeId()
                        + "&signNo=" + code);
                break;
            default:
                break;
        }
        mListener.onFragmentInteraction(args);
    }
}
