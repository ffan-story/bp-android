package com.feifan.bp.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import com.feifan.bp.CodeScannerActivity;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.base.OnTabLifetimeListener;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.home.check.CheckManageFragment;
import com.feifan.bp.home.code.CodeQueryResultFragment;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.home.commoditymanager.BrandFragment;
import com.feifan.bp.home.commoditymanager.InstantsBuyFragment;
import com.feifan.bp.home.storeanalysis.visitorsAnalysisFragment;
import com.feifan.bp.home.userinfo.UserInfoFragment;
import com.feifan.bp.login.AuthListModel.AuthItem;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.salesmanagement.IndexSalesManageFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.BadgerTextView;
import com.feifan.statlib.FmsAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener, OnTabLifetimeListener {

    private static final String TAG = "IndexFragment";
    private static final String STATISTICAL_PATH = "http://sop.sit.ffan.com/H5App/index.html#/statistical";

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    // views
//    private IconClickableEditText mCodeEdt;
    private EditText mCodeEditText;

    public static final String USER_TYPE = "1";
    private String storeId = "";
    private String merchantId = "";

    private BadgerTextView mRefundMenu;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getToolbar().setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        // 获取未读提示状态
        if (UserProfile.getInstance().isStoreUser()) {
            storeId = UserProfile.getInstance().getAuthRangeId();
        } else {
            merchantId = UserProfile.getInstance().getAuthRangeId();
        }
        HomeCtrl.getUnReadtatus(merchantId, storeId, USER_TYPE, new Response.Listener<ReadMessageModel>() {
            @Override
            public void onResponse(ReadMessageModel readMessageModel) {
                int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                PlatformState.getInstance().updateUnreadStatus(refundId, readMessageModel.refundCount > 0);
                refreshRefund();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_index, container, false);
        v.findViewById(R.id.index_scan).setOnClickListener(this);
        v.findViewById(R.id.index_history).setOnClickListener(this);
        v.findViewById(R.id.login_info_icon).setOnClickListener(this);
        if (UserProfile.getInstance().getAuthRangeType().equals("merchant")) {
            getShopData();
        }
        mCodeEditText = (EditText) v.findViewById(R.id.et_code_edit);
        mCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 4 && i != 9 && i != 14 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 5 || sb.length() == 10 || sb.length() == 15) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }

                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            mCodeEditText.setText(sb.subSequence(0, sb.length() - 1));
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mCodeEditText.setText(sb.toString());
                    mCodeEditText.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        v.findViewById(R.id.index_search_btn).setOnClickListener(this);

        List<AuthItem> list = UserProfile.getInstance().getAuthList();
        LogUtil.i(TAG, "auth list=" + list);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.index_function_container);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new IndexAdapter(list);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    /**
     * 获取门店数据
     */
    private void getShopData() {
        String merchantId = UserProfile.getInstance().getAuthRangeId();
        String url = UrlFactory.getShopListUrl();
        LogUtil.i(TAG, "Url = " + url);
        LogUtil.i(TAG, "merchantId = " + merchantId);

        JsonRequest<StoreModel> request = new GetRequest.Builder<StoreModel>(url + merchantId)
                .build()
                .targetClass(StoreModel.class)
                .listener(new Response.Listener<StoreModel>() {
                    @Override
                    public void onResponse(StoreModel storeModel) {
                        UserProfile userProfile = UserProfile.getInstance();
                        userProfile.setStoreList(storeModel.getStoreList());
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
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

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                //统计埋点  扫码验证
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SCANCODE);

                if (!UserProfile.getInstance().isStoreUser()) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
                    return;
                }
                String urlStr = UrlFactory.searchCodeForHtml();
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                args.putString(CodeScannerActivity.INTERATION_KEY_URL, urlStr);
                break;
            case R.id.login_info_icon:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, UserInfoFragment.class.getName());
                break;
            case R.id.index_history:
                //统计埋点  验证历史
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_VERIFY);

                if (Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
                    if (!UserProfile.getInstance().getHistoryUrl().equals(Constants.NO_STRING)) {
                        String url = UrlFactory.checkHistoryForHtml(UserProfile.getInstance().getHistoryUrl()) + "&status=";
                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserTabActivity.class.getName());
                        args.putString(BrowserTabActivity.EXTRA_KEY_URL, url);
                        args.putStringArray(BrowserTabActivity.EXTRA_KEY_STATUS, getActivity().getResources().getStringArray(R.array.data_type));
                        args.putStringArray(BrowserTabActivity.EXTRA_KEY_TITLES, getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));
                    } else {
                        Utils.showShortToast(getActivity(), R.string.error_message_permission_limited, Gravity.CENTER);
                        return;
                    }
                } else {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }
                break;

            case R.id.index_search_btn:
                //统计埋点  核销码搜索
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SEARCHCODE);

                if (!UserProfile.getInstance().isStoreUser()) {
                    Toast.makeText(getActivity(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
                    mCodeEditText.setText("");
                    return;
                }

                String code = mCodeEditText.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(code)) {
                    Utils.showShortToast(getActivity(), R.string.chargeoff_code_empty, Gravity.CENTER);
                    return;
                }
                int mIntCodeLength = code.trim().length();
                if (mIntCodeLength<Constants.CODE_LENGTH_TEN) {
                    args.putString(ErrorFragment.EXTRA_KEY_ERROR_MESSAGE, getActivity().getApplicationContext().getString(R.string.error_message_text_sms_code_length_min));
                    PlatformTopbarActivity.startActivity(getActivity(), ErrorFragment.class.getName(),
                            getActivity().getApplicationContext().getString(R.string.query_result), args);
                    return;
                }else{
                    if (!Utils.isNetworkAvailable(getActivity())) {
                        Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                        return;
                    }
                    if (mIntCodeLength==Constants.CODE_LENGTH_TEN){//等于10位提货吗
                        args.putBoolean(CodeQueryResultFragment.EXTRA_KEY_IS_COUPON, false);
                    }else if (mIntCodeLength>Constants.CODE_LENGTH_TEN) {//大于10位券码
                        args.putBoolean(CodeQueryResultFragment.EXTRA_KEY_IS_COUPON, true);
                    }
                    args.putString(CodeQueryResultFragment.CODE, code);
                    PlatformTopbarActivity.startActivity(getActivity(), CodeQueryResultFragment.class.getName(),
                            getActivity().getApplicationContext().getString(R.string.query_result), args);
                    mCodeEditText.setText("");
                    return;
                }
            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    @Override
    public void onEnter() {
        LogUtil.i(TAG, "Home enter into IndexFragment!");
        if (mRefundMenu != null) {
            refreshRefund();
        }
    }

    private void refreshRefund() {
        if(mRefundMenu != null) {
            if (!TextUtils.isEmpty(EnvironmentManager.getAuthFactory().getRefundId())){
                int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                if (PlatformState.getInstance().getUnreadStatus(refundId)) {
                    mRefundMenu.showBadger();
                } else {
                    mRefundMenu.hideBadger();
                }
            }
        }
    }

    class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

        private List<AuthItem> mList = new ArrayList<>();
        private final int mIconSize;

        {
            mIconSize = ContextCompat.getDrawable(getContext(), R.mipmap.index_ic_order).getMinimumWidth();
        }

        public IndexAdapter(List<AuthItem> list) {
            mList.addAll(list);
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public IndexViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.index_function_item, viewGroup, false);
            IndexViewHolder holder = new IndexViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final IndexViewHolder indexViewHolder, int i) {
            final AuthItem item = mList.get(i);
            Integer iconRes = EnvironmentManager.getAuthFactory().getAuthFilter().get(item.id);
            if (iconRes == null) {
                return;
            }

            final Drawable t = ContextCompat.getDrawable(getContext(), iconRes);
            t.setBounds(0, 0, mIconSize, mIconSize);

            indexViewHolder.t.setCompoundDrawables(null, t, null, null);

            indexViewHolder.t.setText(item.name);
            indexViewHolder.t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdded()) {
                        if (item.url != null && item.url.length() != 0) {
                            String url = UrlFactory.urlForHtml(item.url);
                            if (Utils.isNetworkAvailable(getContext())) {
                                //统计埋点
                                switch (item.id) {
                                    case 1142:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_ORDERMANA);
                                        break;// 订单管理
                                    case 1160:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STAT);
                                        break;// 统计报表
                                    case 1161:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STAFFMANA);
                                        break;// 员工管理
                                    case 1162:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_RETURN);
                                        break;// 退款售后
                                    case 1226:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_GOODSMANA);
                                        break;// 商品管理
                                    case 1227:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SALEMANA);
                                        break;// 营销管理
                                    case 1445:
                                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STOREANA);
                                        break;// 店铺分析
                                }
                                if (EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id) != -1 && EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id) != -1) {
                                    String titleName = "";
                                    if (!url.contains("/staff?") && !item.name.equals("员工管理")) {
                                        titleName = item.name;
                                    }
                                    BrowserTabActivity.startActivity(getContext(), url + "&status=",
                                            getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id)),
                                            getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id)),
                                            titleName);
                                } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getStoreAnalysisId())) {//TODO 跳转到店铺分析界面
                                    Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                                            .addFragment(SimpleBrowserFragment.class.getName(), "概览")
                                            .addArgument(SimpleBrowserFragment.class.getName(), SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                                            .addFragment(visitorsAnalysisFragment.class.getName(), "访客分析")
                                            .addArgument(visitorsAnalysisFragment.class.getName(), visitorsAnalysisFragment.EXTRA_KEY_URL, UrlFactory.visitorsAnalysisForHtml())
                                            .build();
                                    Intent intent = PlatformTabActivity.buildIntent(getContext(), "店铺分析", fragmentArgs);
                                    startActivity(intent);
                                }
                                else if(item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getCommodityManagerId())){//TODO 跳转到商品管理页面
                                    Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                                            .addFragment(InstantsBuyFragment.class.getName(), getString(R.string.commodity_instants_buy))
                                            .addArgument(InstantsBuyFragment.class.getName(), InstantsBuyFragment.EXTRA_KEY_URL,UrlFactory.storeOverviewForHtml())
                                            .addFragment(BrandFragment.class.getName(), getString(R.string.commodity_brand))
                                            .addArgument(BrandFragment.class.getName(), BrandFragment.EXTRA_KEY_URL,UrlFactory.visitorsAnalysisForHtml())
                                            .build();
                                    Intent intent = PlatformTabActivity.buildIntent(getContext(), getString(R.string.index_commodity_text), fragmentArgs);
                                    startActivity(intent);
                                }
                                else if (item.id == 1227) {
                                    Bundle args = new Bundle();
                                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, IndexSalesManageFragment.class.getName());
                                    mListener.onFragmentInteraction(args);
                                } else {
                                    BrowserActivity.startActivity(getContext(), url);
                                }
                            } else {
                                Utils.showShortToast(getContext(), R.string.error_message_text_offline, Gravity.CENTER);
                            }
                        } else {
                            //统计埋点 对账管理
                            FmsAgent.onEvent(getActivity(), Statistics.FB_HOME_FINA);
                            Bundle args = new Bundle();
                            args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                            args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CheckManageFragment.class.getName());
                            mListener.onFragmentInteraction(args);
                        }
                    }
                }
            });

            // 退款售后菜单项，用于未读提示
            if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId())) {
                if (mRefundMenu != null) { //再次显示界面时，首先清除状态
                    mRefundMenu.hideBadger();
                }
                mRefundMenu = indexViewHolder.t;
                refreshRefund();
            }
        }

        class IndexViewHolder extends RecyclerView.ViewHolder {
            private BadgerTextView t;

            public IndexViewHolder(View itemView) {
                super(itemView);
                t = (BadgerTextView) itemView;
            }
        }
    }
}
