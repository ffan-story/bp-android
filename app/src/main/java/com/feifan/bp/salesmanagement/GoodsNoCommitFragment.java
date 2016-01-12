package com.feifan.bp.salesmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.network.BaseModel;
import com.feifan.bp.salesmanagement.GoodsListModel.GoodsDetailModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Frank on 15/12/21.
 */
public class GoodsNoCommitFragment extends Fragment implements GoodsListSwipeAdapter.onCheckChangeListener, GoodsListSwipeAdapter.onItemDeleteListener {

    private CheckBox cbAllCheck;
    private RecyclerView mProductList;
    private GoodsListSwipeAdapter mSwipeAdapter;
    private List<GoodsDetailModel> datas;
    private HashMap<Integer, Boolean> checkStatus = new HashMap<>();//商品选中状态
    private RelativeLayout mRlEnrollBottom;
    private Button mBtnCommit;

    private String mStoreId, mMerchantId, mUid, mPromotionId;
    private UpdateStatusListener updateStatusListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        mPromotionId = ((RegisterDetailActivity) getActivity()).promotionId;
        mStoreId = UserProfile.getInstance().getAuthRangeId();
        mMerchantId = UserProfile.getInstance().getMerchantId();
        mUid = String.valueOf(UserProfile.getInstance().getUid());
        initViews(view);
        getGoodsList();
        return view;

//        ItemTouchHelper.Callback mCallBack = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT){
//
//            /**
//             * @param recyclerView
//             * @param viewHolder 拖动的ViewHolder
//             * @param target 目标位置的ViewHolder
//             * @return
//             */
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
//                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
//                if (fromPosition < toPosition) {
//                    //分别把中间所有的item的位置重新交换
//                    for (int i = fromPosition; i < toPosition; i++) {
//                        Collections.swap(datas, i, i + 1);
//                    }
//                } else {
//                    for (int i = fromPosition; i > toPosition; i--) {
//                        Collections.swap(datas, i, i - 1);
//                    }
//                }
//                mAdapter.notifyItemMoved(fromPosition, toPosition);
//                //返回true表示执行拖动
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                datas.remove(position);
//                mAdapter.notifyItemRemoved(position);
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                    //左右滑动时改变Item的透明度
//                    final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
//                    viewHolder.itemView.setAlpha(alpha);
//                    viewHolder.itemView.setTranslationX(dX);
//                }
//            }
//
//            @Override
//            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                super.onSelectedChanged(viewHolder, actionState);
//                //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
//                Log.v("fangke", "onSelectedChanged");
//            }
//
//            @Override
//            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                super.clearView(recyclerView, viewHolder);
//                //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
//                Log.v("fangke", "clearView");
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallBack);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initViews(View view) {
        cbAllCheck = (CheckBox) view.findViewById(R.id.allcheck);
        cbAllCheck.setChecked(false);
        cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        mProductList = (RecyclerView) view.findViewById(R.id.fragment_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mProductList.setItemAnimator(new DefaultItemAnimator());
        mProductList.setLayoutManager(linearLayoutManager);
        mProductList.setHasFixedSize(true);

        mRlEnrollBottom = (RelativeLayout) view.findViewById(R.id.rl_enroll_bottom);
        mBtnCommit = (Button) view.findViewById(R.id.btn_commit);
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

        Response.Listener<GoodsListModel> listener = new Response.Listener<GoodsListModel>() {
            @Override
            public void onResponse(GoodsListModel model) {

                if (model.goodsList != null) {
                    datas = model.goodsList;
                    mSwipeAdapter = new GoodsListSwipeAdapter(getActivity(), datas, checkStatus,mPromotionId);
                    mProductList.setAdapter(mSwipeAdapter);
                    mSwipeAdapter.setCheckChangeListener(GoodsNoCommitFragment.this);
                    mSwipeAdapter.setItemDeleteListener(GoodsNoCommitFragment.this);
                    if (model.goodsList.size() != 0) {
                        mRlEnrollBottom.setVisibility(View.VISIBLE);
                    } else {
                        mRlEnrollBottom.setVisibility(View.GONE);
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
            if (checkStatus.size() == mSwipeAdapter.getItemCount()) {
                cbAllCheck.setOnCheckedChangeListener(null);
                cbAllCheck.setChecked(true);
                cbAllCheck.setOnCheckedChangeListener(checkAllListener);
            }
        } else {
            checkStatus.remove(position);
            cbAllCheck.setOnCheckedChangeListener(null);
            cbAllCheck.setChecked(false);
            cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        }
    }

    /**
     * GoodsListSwipeAdapter的回调函数,删除item传递位置;
     *
     * @param position
     */
    @Override
    public void onDelete(int position) {
        //TODO 调用删除商品API
        changeCheckStatus(position);
        mSwipeAdapter.notifyItemRemoved(position);
        mSwipeAdapter.notifyItemRangeChanged(position, mSwipeAdapter.getItemCount());
    }

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (int i = 0; i < mSwipeAdapter.getItemCount(); i++) {
                    checkStatus.put(i, isChecked);
                }
            } else {
                for (int i = 0; i < mSwipeAdapter.getItemCount(); i++) {
                    checkStatus.remove(i);
                }
            }
            mSwipeAdapter.notifyDataSetChanged();
        }
    };

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
        //更新全选按钮状态
        if (mSwipeAdapter.getItemCount() == 0) {

        } else if (checkStatus.size() == mSwipeAdapter.getItemCount()) {
            cbAllCheck.setOnCheckedChangeListener(null);
            cbAllCheck.setChecked(true);
            cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        }
    }

    public interface UpdateStatusListener {
        void updateStatus();
    }

    public void setUpdateListener(UpdateStatusListener listener) {
        this.updateStatusListener = listener;
    }
}
