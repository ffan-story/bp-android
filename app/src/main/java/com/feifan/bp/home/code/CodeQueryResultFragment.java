package com.feifan.bp.home.code;


import android.app.AlertDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.home.storeanalysis.SimpleBrowserFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.statlib.FmsAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by konta on 2015/12/17.
 */
public class CodeQueryResultFragment extends ProgressFragment {
    public static final String CODE = "code";
    public static final String EXTRA_KEY_URL = "url";
    public static final String TAG = "CodeQueryResultFragment";
    private String code ;
    private String errMsg ;
    private List<String> goodsList = new ArrayList<String>();

    View view;
    /**
     * 错误信息
     */
    private TextView tv_error_text;

    private Button btn_code_use;
    private LinearLayout ll_goods_code_result;
    private RelativeLayout rl_ticket_code_result;
    private TextView tv_ticket_code;
    private TextView tv_ticket_code_time;
    private TextView tv_ticket_code_timeout;
    private TextView tv_ticket_code_status;
    private TextView tv_ticket_code_title1;
    private TextView tv_ticket_code_title2;
    private TextView tv_goods_order;
    private TextView tv_goods_branch;
    private TextView tv_goods_status;
    private TextView tv_goods_total_money;
    private TextView tv_goods_integrate_money;
    private TextView tv_goods_actual_money;
    private RelativeLayout ll_code_rule;
    private ListView lv_goods_info;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        code = getArguments().getString(CODE);
    }


    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        stub.setLayoutResource(R.layout.fragment_code_query_result);
        View view = stub.inflate();
        tv_error_text = (TextView) view.findViewById(R.id.tv_error_text);
        btn_code_use = (Button) view.findViewById(R.id.btn_code_use);
        //核销券
        rl_ticket_code_result= (RelativeLayout) view.findViewById(R.id.rl_ticket_code_result);
        tv_ticket_code = (TextView) view.findViewById(R.id.tv_ticket_code);
        tv_ticket_code_time = (TextView) view.findViewById(R.id.tv_ticket_code_time);
        tv_ticket_code_timeout = (TextView) view.findViewById(R.id.tv_ticket_code_timeout);
        tv_ticket_code_status = (TextView) view.findViewById(R.id.tv_ticket_code_status);
        tv_ticket_code_title1 = (TextView) view.findViewById(R.id.tv_ticket_code_title1);
        tv_ticket_code_title2 = (TextView) view.findViewById(R.id.tv_ticket_code_title2);
        ll_code_rule = (RelativeLayout) view.findViewById(R.id.ll_code_rule);
        //提货码
        ll_goods_code_result= (LinearLayout) view.findViewById(R.id.ll_goods_code_result);
        tv_goods_order = (TextView) view.findViewById(R.id.tv_goods_order);
        tv_goods_branch = (TextView) view.findViewById(R.id.tv_goods_branch);
        tv_goods_status = (TextView) view.findViewById(R.id.tv_goods_status);
        lv_goods_info = (ListView) view.findViewById(R.id.lv_goods_info);
        tv_goods_total_money = (TextView) view.findViewById(R.id.tv_goods_total_money);
        tv_goods_integrate_money = (TextView) view.findViewById(R.id.tv_goods_integrate_money);
        tv_goods_actual_money = (TextView) view.findViewById(R.id.tv_goods_actual_money);

        ll_code_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(EXTRA_KEY_URL, UrlFactory.getCodeCouponeDetail()+code);
                PlatformTopbarActivity.startActivity(getActivity(), SimpleBrowserFragment.class.getName(), getString(R.string.indicator_title), args);

            }
        });
        btn_code_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, null, false);
                builder.setView(view);
                builder.show();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });
        lv_goods_info.setAdapter(new MyAdapter());
        return view;
    }


    @Override
    protected void requestData() {
        if (!TextUtils.isEmpty(code)){
            if (Utils.isNetworkAvailable(getActivity())) {
                setContentEmpty(false);
                CodeCtrl.codeCooperQueryResult(code, new Response.Listener<CodeModel>() {
                    @Override
                    public void onResponse(CodeModel codeModel) {
                        setContentShown(true);
                    }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            setContentShown(true);
                    }

                });
            } else {

                setContentEmpty(true);
            }
        }

    }

    private boolean checkCode(String code) {



//        if(checkCode(code)){
//            LogUtil.e(TAG,code);
//            tv_error_text.setVisibility(View.GONE);
//            if(code.length() == 10){//提货码
//                ll_goods_code_result.setVisibility(View.VISIBLE);
//                rl_ticket_code_result.setVisibility(View.GONE);
//            }else{//券码
//                rl_ticket_code_result.setVisibility(View.VISIBLE);
//                ll_goods_code_result.setVisibility(View.GONE);
//            }
//        }else{
//            tv_error_text.setVisibility(View.VISIBLE);
//            rl_ticket_code_result.setVisibility(View.GONE);
//            ll_goods_code_result.setVisibility(View.GONE);
//            tv_error_text.setText(errMsg);
//        }


        Boolean isCodeCorrect = true;
        if (code.length() < 10){
            errMsg = "验证码至少需要10位";
            isCodeCorrect = false;
        }else if (!code.matches("^[0-9]*$")){
            errMsg = "验证码含有非法字符";
            isCodeCorrect = false;
        }
        return isCodeCorrect;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
