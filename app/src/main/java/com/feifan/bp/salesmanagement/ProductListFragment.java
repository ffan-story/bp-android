package com.feifan.bp.salesmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.feifan.bp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Frank on 15/12/21.
 */
public class ProductListFragment extends Fragment implements GoodsListAdapter.onCheckChangeListener, GoodsListAdapter.onItemDeleteListener {

    public static final String ENROLL_STATUS = "enrollStatus";
    public static final int STATUS_NO_COMMIT = 1; //未提交状态
    public static final int STATUS_AUDIT = 2;//审核中
    public static final int STATUS_AUDIT_PASS = 3;//审核通过
    public static final int STATUS_AUDIT_DENY = 4;//审核拒绝

    private CheckBox cbAllCheck;
    private RecyclerView mProductList;
    private GoodsListAdapter mAdapter;
    private List<String> datas;
    private HashMap<Integer, Boolean> checkStatus = new HashMap<>();//商品选中状态
    private int enrollStatus;//报名状态
    private RelativeLayout mRlEnrollBottom;

    private String mItemData = "苹果 梨子 香蕉 葡萄 桃子 橘子";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        enrollStatus = getArguments().getInt(ENROLL_STATUS);
        initViews(view);
        initDatas();
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

    private void initDatas() {

        String[] listItems = mItemData.split(" ");
        datas = new ArrayList<>();
        Collections.addAll(datas, listItems);

        if (enrollStatus == STATUS_NO_COMMIT) {
            mAdapter = new GoodsListAdapter(getActivity(), datas, enrollStatus, checkStatus);
            mRlEnrollBottom.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new GoodsListAdapter(getActivity(), datas, enrollStatus);
            mRlEnrollBottom.setVisibility(View.GONE);
        }
        mProductList.setAdapter(mAdapter);
        mAdapter.setCheckChangeListener(this);
        mAdapter.setItemDeleteListener(this);
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
    }

    /**
     * GoodsListAdapter的回调函数,当点击item的CheckBox传递点击位置和状态
     *
     * @param position
     * @param isChecked
     */
    @Override
    public void getCheckData(int position, boolean isChecked) {
        if (isChecked) {
            checkStatus.put(position, isChecked);
            if (checkStatus.size() == mAdapter.getItemCount()) {
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
     * GoodsListAdapter的回调函数,删除item传递位置;
     *
     * @param position
     */
    @Override
    public void onDelete(int position) {
        //TODO 调用删除商品API
        changeCheckStatus(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
    }

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    checkStatus.put(i, isChecked);
                }
            } else {
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    checkStatus.remove(i);
                }
            }
            mAdapter.notifyDataSetChanged();
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
        if (mAdapter.getItemCount() == 0) {

        } else if (checkStatus.size() == mAdapter.getItemCount()) {
            cbAllCheck.setOnCheckedChangeListener(null);
            cbAllCheck.setChecked(true);
            cbAllCheck.setOnCheckedChangeListener(checkAllListener);
        }
    }
}
