package com.feifan.bp.home.check;

import com.feifan.bp.TransactionFlow.TransFlowTabActivity;
import com.feifan.bp.base.BaseFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;

/**
 * 对账管理界面
 *
 * Created by tianjun on 2015-11-6.
 */
public class CheckManageFragment extends BaseFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public static CheckManageFragment newInstance() {
        CheckManageFragment fragment = new CheckManageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reconciliation_management, container, false);
        v.findViewById(R.id.reconciliation_management_view_details).setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onTitleChanged(getString(R.string.check_manage_title));
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, CheckManageFragment.class.getName());
        switch (v.getId()) {
            case R.id.reconciliation_management_view_details:
                // TODO 跳转到流水对账界面
                LogUtil.i(Constants.TAG, "From CheckManageFragment to ----Fragment");
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, TransFlowTabActivity.class.getName());

                break;
        }
        mListener.onFragmentInteraction(args);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.check_manage_title);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Bundle b = new Bundle();
                    b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
                            CheckManageFragment.class.getName());
                    b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
                            OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                    mListener.onFragmentInteraction(b);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}