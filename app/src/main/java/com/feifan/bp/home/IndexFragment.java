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

import com.android.volley.Response;
import com.feifan.bp.BuildConfig;
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
import com.feifan.bp.base.envir.EnvironmentManager;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;
import com.feifan.bp.base.ui.BaseFragment;
import com.feifan.bp.base.ui.OnTabLifetimeListener;
import com.feifan.bp.biz.check.CheckManageFragment;
import com.feifan.bp.biz.commoditymanager.BrandFragment;
import com.feifan.bp.biz.commoditymanager.InstantsBuyFragment;
import com.feifan.bp.biz.marketinganalysis.MarketingHomeFragment;
import com.feifan.bp.biz.receiptsrecord.ReceiptsFragment;
import com.feifan.bp.biz.salesmanagement.IndexSalesManageFragment;
import com.feifan.bp.biz.storeanalysis.visitorsAnalysisFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.home.function.Function;
import com.feifan.bp.home.function.Function.LaunchFunction;
import com.feifan.bp.home.function.FunctionStore;
import com.feifan.bp.home.userinfo.UserInfoFragment;
import com.feifan.bp.home.writeoff.CodeQueryResultFragment;
import com.feifan.bp.login.AuthListModel.AuthItem;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.BadgerTextView;
import com.feifan.statlib.FmsAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener, OnTabLifetimeListener {

    private static final String TAG = "IndexFragment";

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private EditText mCodeEditText;

    public static final String USER_TYPE = "1";
    private String storeId = "";
    private String merchantId = "";

    private BadgerTextView mRefundMenu;

    private FunctionStore mFunctions;

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
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        // 获取未读提示状态
        if (UserProfile.getInstance().isStoreUser()) {
            storeId = UserProfile.getInstance().getAuthRangeId();
        } else {
            merchantId = UserProfile.getInstance().getAuthRangeId();
        }
        HomeCtrl.getUnReadtatus(new Response.Listener<ReadMessageModel>() {
            @Override
            public void onResponse(ReadMessageModel readMessageModel) {
                int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                PlatformState.getInstance().updateUnreadStatus(refundId, readMessageModel.refundCount > 0);
                refreshRefund();
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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

        Collections.sort(list, new Comparator<AuthItem>() {
            @Override
            public int compare(AuthItem item1, AuthItem item2) {
                if(item1 != null && !TextUtils.isEmpty(item1.sort) && item2 != null && !TextUtils.isEmpty(item2.sort)) {
                    try {
                        if (Integer.parseInt(item1.sort) > Integer.parseInt(item2.sort)) {
                            return 1;
                        } else if (Integer.parseInt(item1.sort) < Integer.parseInt(item2.sort)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }catch (Exception e){
                        return 0;
                    }
                }
                return 0;
            }
        });

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

        // 初始化功能对象库
        // FIXME 可优化在每次登录后初始化一次
        mFunctions = new FunctionStore();
        if(isAdded()) {

            // 营销分析
            Function maFunc = new Function.LaunchFunction(EnvironmentManager.getAuthFactory().getMarketingAnalysisId())
                    .activity(PlatformTopbarActivity.class)
                    .param(Constants.EXTRA_KEY_TITLE, getString(R.string.sale_anal))
                    .param(Constants.EXTRA_KEY_TO, MarketingHomeFragment.class.getName());
            mFunctions.addFunction(maFunc);

            // 营销管理
            Function mmFunc = new Function.LaunchFunction(EnvironmentManager.getAuthFactory().getMarketingManageId())
                    .activity(PlatformTopbarActivity.class)
                    .param(Constants.EXTRA_KEY_TO, IndexSalesManageFragment.class.getName());
            mFunctions.addFunction(mmFunc);

            // 对账管理
            Function cmFunc = new Function.LaunchFunction(EnvironmentManager.getAuthFactory().getReportId())
                    .activity(PlatformTopbarActivity.class)
                    .param(Constants.EXTRA_KEY_TO, CheckManageFragment.class.getName());
            mFunctions.addFunction(cmFunc);

            // 店铺分析
            Function saFunc = new Function.LaunchFunction(EnvironmentManager.getAuthFactory().getStoreAnalysisId())
                    .activity(PlatformTabActivity.class)
                    .param(Constants.EXTRA_KEY_TITLE, getString(R.string.store_analysis))
                    .param(Constants.EXTRA_KEY_FRAGMENTS, new PlatformTabActivity.ArgsBuilder()
                            .addFragment(SimpleBrowserFragment.class.getName(), "概览")
                            .addArgument(SimpleBrowserFragment.class.getName(), SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                            .addFragment(visitorsAnalysisFragment.class.getName(), "访客分析")
                            .addArgument(visitorsAnalysisFragment.class.getName(), visitorsAnalysisFragment.EXTRA_KEY_URL, UrlFactory.visitorsAnalysisForHtml())
                            .build());
            mFunctions.addFunction(saFunc);

            // 商品管理
            Function gmFunc = new Function.LaunchFunction(EnvironmentManager.getAuthFactory().getCommodityManagerId())
                    .activity(PlatformTabActivity.class)
                    .param(Constants.EXTRA_KEY_TITLE, getString(R.string.index_commodity_text))
                    .param(Constants.EXTRA_KEY_FRAGMENTS, new PlatformTabActivity.ArgsBuilder()
                            .addFragment(InstantsBuyFragment.class.getName(), getString(R.string.commodity_instants_buy))
                            .addArgument(InstantsBuyFragment.class.getName(), InstantsBuyFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                            .addFragment(BrandFragment.class.getName(), getString(R.string.commodity_brand))
                            .addArgument(BrandFragment.class.getName(), BrandFragment.EXTRA_KEY_URL, UrlFactory.visitorsAnalysisForHtml())
                            .build());
            mFunctions.addFunction(gmFunc);

            // 收款流水
            Function rjFunc = new LaunchFunction(EnvironmentManager.getAuthFactory().getReceiptsId())
                    .activity(PlatformTopbarActivity.class)
                    .param(Constants.EXTRA_KEY_TITLE, getString(R.string.receipts_title))
                    .param(Constants.EXTRA_KEY_TO, ReceiptsFragment.class.getName());
            mFunctions.addFunction(rjFunc);
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(!isAdded()) {
            return;
        }
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                //统计埋点  扫码验证
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SCANCODE);
                if (!UserProfile.getInstance().isStoreUser()) {
                    Utils.showShortToastSafely(R.string.error_message_permission_limited);
                    return;
                }
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;
            case R.id.login_info_icon:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, UserInfoFragment.class.getName());
                break;
            case R.id.index_history:
                //统计埋点  验证历史
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_VERIFY);

                if (Utils.isNetworkAvailable()) {
                    if (!UserProfile.getInstance().getHistoryUrl().equals(Constants.NO_STRING)) {
                        String url = UrlFactory.checkHistoryForHtml(UserProfile.getInstance().getHistoryUrl()) + "&status=";
                        args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserTabActivity.class.getName());
                        args.putString(BrowserTabActivity.EXTRA_KEY_URL, url);
                        args.putStringArray(BrowserTabActivity.EXTRA_KEY_STATUS, getActivity().getResources().getStringArray(R.array.data_type));
                        args.putStringArray(BrowserTabActivity.EXTRA_KEY_TITLES, getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));
                    } else {
                        Utils.showShortToast(getActivity(), R.string.error_message_permission_limited);
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
                    Utils.showShortToastSafely(R.string.error_message_permission_limited);
                    mCodeEditText.setText("");
                    return;
                }

                String code = mCodeEditText.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(code)) {
                    Utils.showShortToastSafely(R.string.chargeoff_code_empty);
                    return;
                }
                int mIntCodeLength = code.trim().length();
                if (mIntCodeLength<Constants.CODE_LENGTH_TEN) {
                    args.putString(ErrorFragment.EXTRA_KEY_ERROR_MESSAGE, getActivity().getApplicationContext().getString(R.string.error_message_text_sms_code_length_min));
                    PlatformTopbarActivity.startActivity(getActivity(), ErrorFragment.class.getName(),
                            getActivity().getApplicationContext().getString(R.string.query_result), args);
                    return;
                }else{
                    if (!Utils.isNetworkAvailable()) {
                        Utils.showShortToastSafely(R.string.error_message_text_offline);
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

                        String url = UrlFactory.urlForHtml(item.url);
                        // 响应功能调用
                        try{
                            final String id = String.valueOf(item.id);
                            if(url != null) {
                                Bundle params = new Bundle();
                                params.putString(Constants.EXTRA_KEY_URL, url);
                                mFunctions.invokeFunc(id, params);
                            }else {
                                mFunctions.invokeFunc(id);
                            }

                            sendStatEventById(id);
                            LogUtil.i(TAG, "Invoke " + item.id + " function successfully");
                            return;
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 处理其他功能调用
                        // FIXME 统一使用FunctionStore处理
                        if (Utils.isNetworkAvailable()) {
                            // 添加统计埋点
                            addStatistices(item.id);
                            if (EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id) != -1 && EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id) != -1) {
                                String titleName = "";
                                if (!url.contains("/staff?") && !item.name.equals("员工管理")) {
                                    titleName = item.name;
                                }
                                BrowserTabActivity.startActivity(getContext(), url + "&status=",
                                        getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id)),
                                        getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id)),
                                        titleName);
                            } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getStoreAnalysisId())) {//TODO 跳转到店铺分析界面
                                /**
                                 * 统计埋点    店铺分析
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STOREANA);

                                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                                        .addFragment(SimpleBrowserFragment.class.getName(), "概览")
                                        .addArgument(SimpleBrowserFragment.class.getName(), SimpleBrowserFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                                        .addFragment(visitorsAnalysisFragment.class.getName(), "访客分析")
                                        .addArgument(visitorsAnalysisFragment.class.getName(), visitorsAnalysisFragment.EXTRA_KEY_URL, UrlFactory.visitorsAnalysisForHtml())
                                        .build();
                                Intent intent = PlatformTabActivity.buildIntent(getContext(), "店铺分析", fragmentArgs);
                                startActivity(intent);
                            } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getCommodityManagerId())) {//TODO 跳转到商品管理页面
                                /**
                                 * 统计埋点 商品管理
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_GOODSMANA);

                                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                                        .addFragment(InstantsBuyFragment.class.getName(), getString(R.string.commodity_instants_buy))
                                        .addArgument(InstantsBuyFragment.class.getName(), InstantsBuyFragment.EXTRA_KEY_URL, UrlFactory.storeOverviewForHtml())
                                        .addFragment(BrandFragment.class.getName(), getString(R.string.commodity_brand))
                                        .addArgument(BrandFragment.class.getName(), BrandFragment.EXTRA_KEY_URL, UrlFactory.visitorsAnalysisForHtml())
                                        .build();
                                Intent intent = PlatformTabActivity.buildIntent(getContext(), getString(R.string.index_commodity_text), fragmentArgs);
                                startActivity(intent);
                            } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getMarketingAnalysisId())) {//TODO 跳转到营销分析
                                /**
                                 * 统计埋点  营销分析
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_PROMTION_ANA);

                                Bundle args = new Bundle();
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, MarketingHomeFragment.class.getName());

                                mListener.onFragmentInteraction(args);
                            } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getMarketingManageId())) {//TODO 跳转到营销管理
                                /**
                                 * 统计埋点  营销管理
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SALEMANA);

                                Bundle args = new Bundle();
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                                args.putString(SimpleBrowserFragment.EXTRA_KEY_URL, url);
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, IndexSalesManageFragment.class.getName());
                                mListener.onFragmentInteraction(args);
                            } else if (item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getReportId())) {//TODO 跳转到对账管理
                                /**
                                 *  统计埋点 对账管理
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_FINA);

                                Bundle args = new Bundle();
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CheckManageFragment.class.getName());
                                mListener.onFragmentInteraction(args);
                            }else if(item.id == Integer.valueOf(EnvironmentManager.getAuthFactory().getReceiptsId())){ // TODO 跳转到收款流水
                                /**
                                 * 统计埋点 收款流水
                                 */
                                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_CHECKOUT_LST);
                                PlatformTopbarActivity.startActivityFromOther(PlatformState.getApplicationContext(), ReceiptsFragment.class.getName(), "收款流水");
                            }else {
                                BrowserActivity.startActivity(getContext(), url);
                            }
                        } else {
                            Utils.showShortToastSafely(R.string.error_message_text_offline);
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

        // 根据function的ID发送统计信息
        private void sendStatEventById(String id) {
            if(id.equals(EnvironmentManager.getAuthFactory().getMarketingAnalysisId())) { // 营销分析
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_PROMTION_ANA);
            } else if(id.equals(Integer.valueOf(EnvironmentManager.getAuthFactory().getMarketingManageId()))) { //营销管理
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_SALEMANA);
            } else if(id.equals(EnvironmentManager.getAuthFactory().getReportId())) { // 对账管理
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_FINA);
            } else if(id.equals(EnvironmentManager.getAuthFactory().getStoreAnalysisId())){ //店铺分析
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STOREANA);
            } else if(id.equals(EnvironmentManager.getAuthFactory().getCommodityManagerId())) { // 商品管理
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_GOODSMANA);
            } else if(id.equals(EnvironmentManager.getAuthFactory().getReceiptsId())) { // 收款流水
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_CHECKOUT_LST);
            }
        }

        class IndexViewHolder extends RecyclerView.ViewHolder {
            private BadgerTextView t;

            public IndexViewHolder(View itemView) {
                super(itemView);
                t = (BadgerTextView) itemView;
            }
        }

        /**
         * 添加埋点
         * @param id
         */
        private void addStatistices(int id){
            if (BuildConfig.CURRENT_ENVIRONMENT.equals(Constants.Environment.SIT)){
                //统计埋点  sit
                switch (id) {
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
                }
            }else{
                //统计埋点  Product
                switch (id) {
                    case 997:
                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_ORDERMANA);
                        break;// 订单管理
                    case 1002:
                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STAT);
                        break;// 统计报表
                    case 1003:
                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_STAFFMANA);
                        break;// 员工管理
                    case 1004:
                        FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_HOME_RETURN);
                        break;// 退款售后
                }
            }
        }
    }
}
