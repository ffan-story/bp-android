package com.feifan.bp.biz.check;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.financialreconciliation.FinancialSummaryFragment;
import com.feifan.bp.biz.commoditymanager.BrandFragment;
import com.feifan.bp.transactionflowing.fragment.CouponListViewFragment;
import com.feifan.bp.transactionflowing.fragment.InstantBuyFragment;
import com.feifan.bp.util.LogUtil;

/**
 * 对账管理界面
 * <p/>
 * Created by tianjun on 2015-11-6.
 */
public class CheckManageFragment extends BaseFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public static CheckManageFragment newInstance() {
        CheckManageFragment fragment = new CheckManageFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onTitleChanged(getString(R.string.check_manage_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reconciliation_management, container, false);
        ((RelativeLayout) v.findViewById(R.id.check_manager_transaction_flow_rl)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.check_manager_financial_reconciliation_rl)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.check_manager_inventory_query_rl)).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, CheckManageFragment.class.getName());
        Bundle fragmentArgs;
        Intent intent;
        switch (v.getId()) {
            case R.id.check_manager_transaction_flow_rl:
                // TODO 跳转到流水对账界面
                LogUtil.i(Constants.TAG, "From CheckManageFragment to ----Fragment");
                fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                        .addFragment(InstantBuyFragment.class.getName(), getString(R.string.falsh_buy))
                        .addFragment(CouponListViewFragment.class.getName(), getString(R.string.coupons))
                        .build();
                intent = PlatformTabActivity.buildIntent(getContext(), getString(R.string.reconciliation_management_transaction_flow), fragmentArgs);
                startActivity(intent);
                break;
            case R.id.check_manager_financial_reconciliation_rl:
                // TODO 跳转到财务对账页面
                fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                        .addFragment(FinancialSummaryFragment.class.getName(), "乐付")
                        .addFragment(BrandFragment.class.getName(), "券")
                        .addFragment(BrandFragment.class.getName(),getString(R.string.falsh_buy))
                        .build();
                intent = PlatformTabActivity.buildIntent(getContext(), "财务对账", fragmentArgs);
                startActivity(intent);
                break;
            case R.id.check_manager_inventory_query_rl:
                Utils.showToast(getActivity(), R.string.check_manage_system_construction, Toast.LENGTH_SHORT);
                break;
        }
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
