package com.feifan.bp.home.check;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.TransactionFlow.TransFlowTabActivity;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.material.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reconciliation_management, container, false);
        ((TextView) v.findViewById(R.id.reconciliation_management_view_details)).setOnClickListener(this);
        return v;
    }

    // add by tianjun 2015.11.10
    private MaterialDialog mDialog;
    private Spinner mAccountPeriodSpinner;
    private ArrayAdapter<String> mAdapter;

    private void initDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.data_selection_dialog, null);
        mAccountPeriodSpinner = (Spinner) view.findViewById(R.id.date_self_define_account_spinner);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getDateList(5));
        mAdapter.setDropDownViewResource(R.layout.spinner_item);
        mAccountPeriodSpinner.setAdapter(mAdapter);
        mAccountPeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                Toast.makeText(getActivity(), "你选择的月份是：" + spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDialog = new MaterialDialog(getActivity()).setContentView(view)
                .setPositiveButton(R.string.date_self_define_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.date_self_define_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
        mDialog.show();
    }

    public String getCurrentData() {
        Date dNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
        String currentDate = sdf.format(dNow);
        return currentDate;
    }

    public List<String> getDateList(int month) {
        Date dNow = new Date();
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
        String startDate;
        List<String> timeList = new ArrayList<String>();
        for (int i = 0; i < month; i++) {
            calendar.add(calendar.MONTH, -i);
            dBefore = calendar.getTime();
            startDate = sdf.format(dBefore);
            System.out.println(startDate);
            timeList.add(startDate);
            calendar.add(calendar.MONTH, i);
        }
        return timeList;
    }
    //end.

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
