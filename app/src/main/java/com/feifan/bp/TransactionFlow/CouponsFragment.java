package com.feifan.bp.TransactionFlow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.widget.SegmentedGroup;
import com.feifan.material.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 15/11/6.
 */
public class CouponsFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    private SegmentedGroup segmentedGroup;
    private RadioButton rb_last1,rb_last2,rb_other;
    private TextView title1,content1,title2,content2;

    // add by tianjun 2015.11.10
    private MaterialDialog mDialog;
    private Spinner mAccountPeriodSpinner;
    private ArrayAdapter<String> mAdapter;

    public CouponsFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coupons, null);
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
        rb_last1 = (RadioButton) v.findViewById(R.id.last1);
        rb_last2 = (RadioButton) v.findViewById(R.id.last2);
        rb_other = (RadioButton) v.findViewById(R.id.other);
        title1 = (TextView) v.findViewById(R.id.item_1).findViewById(R.id.title);
        title2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.title);
        content1 = (TextView) v.findViewById(R.id.item_1).findViewById(R.id.content);
        content2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.content);

        rb_last1.setChecked(true);
        getCouponsData("1","","");
        segmentedGroup.setOnCheckedChangeListener(this);

        return v;
    }

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

    private void getCouponsData(String type,String month,String storeId){
        ((TransFlowTabActivity) getActivity()).showProgressBar(true);
        //测试
        String url = "http://api.sit.ffan.com/mapp/v1/mapp/transactionspecificcpsummary";

        JsonRequest<CpSummaryModel> request = new GetRequest.Builder<CpSummaryModel>(url)
                .param("type", type)
                .param("month", month)
                .param("storeId",storeId)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                })
                .build()
                .targetClass(CpSummaryModel.class)
                .listener(new Response.Listener<CpSummaryModel>() {
                    @Override
                    public void onResponse(CpSummaryModel model) {
                        title1.setText(model.total_text);
                        title2.setText(model.totalAmount_text);
                        content1.setText(model.total);
                        content2.setText(model.totalAmount);

                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.transaction_flow);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.last1:
                break;
            case R.id.last2:
                break;
            case R.id.other:
                initDialog();
                break;
        }
    }
}
