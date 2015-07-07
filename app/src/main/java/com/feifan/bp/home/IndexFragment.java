package com.feifan.bp.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformHelper;
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
    private static final String URL_PATH_REPORT = "h5app/index.html#/statistical";

    private OnFragmentInteractionListener mListener;

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
        ((IconClickableEditText)v.findViewById(R.id.index_search_input)).setOnIconClickListener(this);
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
        switch (v.getId()){
            case R.id.index_scan:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;
            case R.id.index_history:
                // TODO call scan history with mListener here 注意使用回调进行实际的启动活动操作
                LogUtil.w(IndexFragment.class.getSimpleName(), "history activity not found");
                break;
            case R.id.index_report:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, PlatformHelper.getManagedH5Url(URL_PATH_REPORT));
                break;
            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    @Override
    public void onRightClicked(View v) {
        switch (v.getId()) {
            case R.id.index_search_input:
                Log.e(IndexFragment.class.getSimpleName(), "search is clicked");
                break;
        }
    }
}
