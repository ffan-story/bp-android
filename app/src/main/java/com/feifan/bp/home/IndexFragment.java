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

        switch (v.getId()){
            case R.id.index_scan:
                // TODO call scan activity with mListener here 注意使用回调进行实际的启动活动操作
//                LogUtil.w(IndexFragment.class.getSimpleName(), "scan activity not found");
                Intent intent = new Intent(getActivity(), CodeScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.index_history:
                // TODO call scan history with mListener here 注意使用回调进行实际的启动活动操作
                LogUtil.w(IndexFragment.class.getSimpleName(), "history activity not found");
                break;
        }
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
