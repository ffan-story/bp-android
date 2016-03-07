package com.feifan.bp.salesmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.R;
import com.feifan.bp.Statistics;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.browser.SimpleBrowserFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.statlib.FmsAgent;



/**
 * Created by Frank on 15/11/23.
 */
public class IndexSalesManageFragment extends BaseFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private String url = "";

    public static IndexSalesManageFragment newInstance(){
        IndexSalesManageFragment fragment = new IndexSalesManageFragment();
        return  fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onTitleChanged(getString(R.string.sales_management));
        if (isAdded()){
            url =getActivity().getIntent().getStringExtra(SimpleBrowserFragment.EXTRA_KEY_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_management, container, false);
        ((RelativeLayout) view.findViewById(R.id.rl_coupon_management)).setOnClickListener(this);
        ((RelativeLayout) view.findViewById(R.id.rl_activity_management)).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexSalesManageFragment.class.getName());
        switch (v.getId()){
            case R.id.rl_coupon_management:
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_FINA_GENCOUPON);
                LogUtil.i("congjing","url=="+url);
                BrowserActivity.startActivity(getContext(),url);
//                BrowserActivity.startActivity(getContext(), UrlFactory.urlForHtml(UserProfile.getInstance().getAuthList().get(5).url));
                break;
            case R.id.rl_activity_management:
                FmsAgent.onEvent(getActivity().getApplicationContext(), Statistics.FB_FINA_FLASHBUY);
                // TODO 跳转到活动管理界面
                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                        .addFragment(EventListFragment.class.getName(), getString(R.string.event_register))
                        .addArgument(EventListFragment.class.getName(), EventListFragment.REGISTER,false)
                        .addFragment(EventListFragment.class.getName(), getString(R.string.event_registered))
                        .addArgument(EventListFragment.class.getName(), EventListFragment.REGISTER, true)
                        .build();

                Intent intent = PlatformTabActivity.buildIntent(getContext(), getString(R.string.event_management), fragmentArgs);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.sales_management);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Bundle b = new Bundle();
                    b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
                            IndexSalesManageFragment.class.getName());
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
