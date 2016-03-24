package com.feifan.bp.salesmanagement;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.ui.LazyLoadFragment;
import com.feifan.bp.base.network.BaseModel;
import com.feifan.bp.salesmanagement.GoodsListModel.GoodsDetailModel;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.swipemenu.recyler.SwipeMenuRecyclerView;
import com.feifan.material.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Frank on 15/12/21.
 */
public class GoodsNoCommitFragment extends LazyLoadFragment implements GoodsListSwipeAdapter.onCheckChangeListener, GoodsListSwipeAdapter.onItemDeleteListener {

    private CheckBox cbAllCheck;
    private SwipeMenuRecyclerView mProductList;
    private GoodsListSwipeAdapter mSwipeAdapter;
    private List<GoodsDetailModel> datas;
    private HashMap<Integer, Boolean> checkStatus = new HashMap<>();//商品选中状态
    private RelativeLayout mRlEnrollBottom;
    private Button mBtnCommit;

    private String mStoreId, mMerchantId, mUid, mPromotionId;
    private UpdateStatusListener updateStatusListener;
    private int itemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        cbAllCheck = (CheckBox) view.findViewById(R.id.allcheck);
        mProductList = (SwipeMenuRecyclerView) view.findViewById(R.id.fragment_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mProductList.setItemAnimator(new DefaultItemAnimator());
        mProductList.setLayoutManager(linearLayoutManager);
        mProductList.setHasFixedSize(true);
        mProductList.setOpenInterpolator(new BounceInterpolator());
        mProductList.setCloseInterpolator(new BounceInterpolator());

        mRlEnrollBottom = (RelativeLayout) view.findViewById(R.id.rl_enroll_bottom);
        mBtnCommit = (Button) view.findViewById(R.id.btn_commit);
        commitBtnStatus(false);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStatus != null && checkStatus.size() != 0) {
                    commitGoods();
                }
            }
        });
    }

    private void commitGoods() {

        String goodsCodeStr = "";
        Iterator it = checkStatus.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int key = Integer.valueOf(entry.getKey().toString());
            String goodsCode = datas.get(key).getGoodsCode();

            if (goodsCodeStr.equals("")) {
                goodsCodeStr = goodsCode;
            } else {
                goodsCodeStr = goodsCodeStr + "," + goodsCode;
            }
        }

        Response.Listener<BaseModel> listener = new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel model) {
                Toast.makeText(getActivity(), "提交成功!", Toast.LENGTH_SHORT).show();
                checkStatus.clear();
                updateStatusListener.updateStatus();//更新角标状态
            }
        };
        PromotionCtrl.goodsAudit(mStoreId, mMerchantId, mUid, mPromotionId, goodsCodeStr, listener);
    }

    private void getGoodsList() {
        datas = new ArrayList<>();
        mPromotionId = ((RegisterDetailActivity) getActivity()).promotionId;
        mStoreId = UserProfile.getInstance().getAuthRangeId();
        mMerchantId = UserProfile.getInstance().getMerchantId();
        mUid = String.valueOf(UserProfile.getInstance().getUid());

        Response.Listener<GoodsListModel> listener = new Response.Listener<GoodsListModel>() {
            @Override
            public void onResponse(GoodsListModel model) {

                if (model.goodsList != null) {
                    LogUtil.i("checkStatus", "getGoodList------------->" + checkStatus.toString());
                    datas = model.goodsList;
                    mSwipeAdapter = new GoodsListSwipeAdapter(getActivity(), datas, checkStatus, mPromotionId);
                    mProductList.setAdapter(mSwipeAdapter);
                    itemCount = mSwipeAdapter.getItemCount();
                    mSwipeAdapter.setCheckChangeListener(GoodsNoCommitFragment.this);
                    mSwipeAdapter.setItemDeleteListener(GoodsNoCommitFragment.this);
                    if (model.goodsList.size() == 0) {
                        cbAllCheck.setClickable(false);
                    } else {
                        cbAllCheck.setClickable(true);
                    }
                    if (itemCount != 0 && itemCount == checkStatus.size()) {
                        updateCheckAllListener(true);
                    } else {
                        updateCheckAllListener(false);
                    }
                    if (checkStatus.size() == 0) {
                        commitBtnStatus(false);
                    } else {
                        commitBtnStatus(true);
                    }
                }
            }
        };
        PromotionCtrl.getGoodsList(mStoreId, mMerchantId, mPromotionId, "0", listener);
    }

    /**
     * GoodsListSwipeAdapter的回调函数,当点击item的CheckBox传递点击位置和状态
     *
     * @param position
     * @param isChecked
     */
    @Override
    public void getCheckData(int position, boolean isChecked) {
        if (isChecked) {
            checkStatus.put(position, isChecked);
            if (checkStatus.size() == itemCount) {
                updateCheckAllListener(true);
            }
            commitBtnStatus(true);
        } else {
            checkStatus.remove(position);
            updateCheckAllListener(false);
            if (checkStatus.size() == 0) {
                commitBtnStatus(false);
            }
        }
    }

    /**
     * GoodsListSwipeAdapter的回调函数,删除item传递位置;
     *
     * @param position
     */
    @Override
    public void onDelete(final int position) {

        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog.setCanceledOnTouchOutside(false)
                .setTitle(getString(R.string.common_title))
                .setMessage(getString(R.string.goods_delete_confirm))
                .setNegativeButton(getString(R.string.common_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.common_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                        Response.Listener<BaseModel> listener = new Response.Listener<BaseModel>() {
                            @Override
                            public void onResponse(BaseModel model) {
                                Toast.makeText(getActivity(), "删除成功!", Toast.LENGTH_SHORT).show();
                                changeCheckStatus(position);//更新选中状态
                                updateStatusListener.updateStatus();//更新角标状态
                            }
                        };
                        String goodsCode = datas.get(position).getGoodsCode();
                        PromotionCtrl.goodsDelete(mStoreId, mMerchantId, mUid, mPromotionId, goodsCode, listener);
                    }
                }).show();
    }

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (int i = 0; i < itemCount; i++) {
                    checkStatus.put(i, isChecked);
                }
                commitBtnStatus(true);
            } else {
                for (int i = 0; i < itemCount; i++) {
                    checkStatus.remove(i);
                }
                commitBtnStatus(false);
            }
            mSwipeAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 提交按钮是否置灰不可点击
     *
     * @param canCommit true->正常 false->不可点
     */
    private void commitBtnStatus(boolean canCommit) {
        if (canCommit) {
            mBtnCommit.setClickable(true);
            mBtnCommit.setBackgroundResource(R.drawable.bg_button_orange);
        } else {
            mBtnCommit.setClickable(false);
            mBtnCommit.setBackgroundResource(R.drawable.bg_button_grey);
        }
    }

    /**
     * 更新全选按钮监听
     *
     * @param isCheckAll true->选中 false->未选中
     */
    private void updateCheckAllListener(boolean isCheckAll) {
        if (isCheckAll) {
            cbAllCheck.setOnCheckedChangeListener(null);
            cbAllCheck.setChecked(true);
            cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        } else {
            cbAllCheck.setOnCheckedChangeListener(null);
            cbAllCheck.setChecked(false);
            cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        }
    }

    /**
     * 更新checkStatus状态
     *
     * @param position
     */
    private void changeCheckStatus(int position) {
        ArrayList<Integer> changePosList = new ArrayList<>();//位置变动的元素集合
        Set<Integer> keyset = checkStatus.keySet();
        Iterator<Integer> it = keyset.iterator();
        while (it.hasNext()) {
            Integer checkPos = it.next();
            if (checkPos > position) {
                changePosList.add(checkPos);//比删除位置大的元素添加入集合
            }
        }
        checkStatus.remove(position);
        //如果删除的元素不是最后一个,则更新顺序
        if (changePosList.size() != 0) {
            int changePosLast = 0;
            for (int changePos : changePosList) {
                checkStatus.put(changePos - 1, checkStatus.get(changePos));
                if (changePos > changePosLast) {
                    changePosLast = changePos;
                }
            }
            //删除checkStatus最后一个元素
            checkStatus.remove(changePosLast);
        }
        LogUtil.i("checkStatus", "changeCheckStatus------------->" + checkStatus.toString());
        if (itemCount != 0) {
            itemCount--;
        }
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            return;
        }
        getGoodsList();
    }

    public interface UpdateStatusListener {
        void updateStatus();
    }

    public void setUpdateListener(UpdateStatusListener listener) {
        this.updateStatusListener = listener;
    }
}
