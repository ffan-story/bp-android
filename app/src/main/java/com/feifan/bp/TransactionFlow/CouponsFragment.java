package com.feifan.bp.TransactionFlow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.check.IndicatorFragment;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.util.TimeUtils;
import com.feifan.bp.widget.MonPicker;
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
public class CouponsFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,MenuItem.OnMenuItemClickListener{
    private SegmentedGroup segmentedGroup;
    private RadioButton rb_last1,rb_last2,rb_other;
    private TextView title1,content1,title2,content2;

    private MaterialDialog mDialog;
    private MonPicker picker;
    private String selectData;
    private Boolean mCheckFlag = false;
    private String mStoreId;

    public CouponsFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mStoreId = UserProfile.getInstance().getAuthRangeId();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coupons, null);
        segmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmentedGroup);
        rb_last1 = (RadioButton) v.findViewById(R.id.last1);
        rb_last2 = (RadioButton) v.findViewById(R.id.last2);
        rb_other = (RadioButton) v.findViewById(R.id.other);
        rb_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckFlag){
                    initDialog();
                }
            }
        });
        title1 = (TextView) v.findViewById(R.id.item_1).findViewById(R.id.title);
        title2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.title);
        content1 = (TextView) v.findViewById(R.id.item_1).findViewById(R.id.content);
        content2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.content);

        rb_last1.setChecked(true);
        getCouponsData("1", "");
        segmentedGroup.setOnCheckedChangeListener(this);

        return v;
    }

    private void initDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_month_pick, null);
        picker = (MonPicker) view.findViewById(R.id.month_picker);
        mDialog = new MaterialDialog(getActivity()).setContentView(view)
                .setPositiveButton(R.string.date_self_define_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectData = picker.getYear()+"-"+DataFormat(picker.getMonth()+1);
                        LogUtil.i("fangke","selectData===========>"+selectData);
                        getCouponsData("", selectData);
                        mCheckFlag = true;
                        mDialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.date_self_define_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCheckFlag = true;
                        mDialog.dismiss();
                    }
                });
        mDialog.show();
    }

    private String DataFormat(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return String.valueOf(data);
        }
    }

    private void getCouponsData(String type,String month){
        ((TransFlowTabActivity) getActivity()).showProgressBar(true);
        //测试
        String url = UrlFactory.getCouponsUrl();

        JsonRequest<CpSummaryModel> request = new GetRequest.Builder<CpSummaryModel>(url)
                .param("type", type)
                .param("month", month)
                .param("storeId",mStoreId)
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
                        content2.setText(Utils.formatMoney(model.totalAmount,2));

                        ((TransFlowTabActivity) getActivity()).hideProgressBar();
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        menu.findItem(R.id.check_menu_directions).setOnMenuItemClickListener(this);
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.last1:
                getCouponsData("1","");
                break;
            case R.id.last2:
                getCouponsData("2","");
                break;
            case R.id.other:
                initDialog();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(getActivity(), PlatformTopbarActivity.class);
        intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, IndicatorFragment.class.getName());
        startActivity(intent);
        return false;
    }
}
