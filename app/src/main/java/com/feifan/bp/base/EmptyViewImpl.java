package com.feifan.bp.base;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.R;

public class EmptyViewImpl implements IEmptyView {
    private final Activity mActivity;
    protected LayoutInflater mLayoutInflater;

    /**
     * Common Empty View
     */
    protected TextView mTvEmptyText;
    protected ImageView mIvEmptyImage;
    protected Button mBtnRetry;
    protected ViewGroup mEmptyView;

    protected int mEmptyViewContainerLayoutId;
    protected EmptyViewPlaceHolderType mEmptyViewPlaceHolderType;
    protected EmptyViewAlignment mEmptyViewAlignment;

    protected int mEmptyViewLayoutResourceId;

    public EmptyViewImpl(Activity activity, int emptyViewContainerLayoutId,
                         int resourceId, EmptyViewPlaceHolderType holderType,
                         EmptyViewAlignment alignment) {
        super();
        mActivity = activity;
        mEmptyViewContainerLayoutId = emptyViewContainerLayoutId;
        mEmptyViewPlaceHolderType = holderType;
        mEmptyViewAlignment = alignment;
        mLayoutInflater = mActivity.getWindow().getLayoutInflater();
        mEmptyViewLayoutResourceId = resourceId;
    }

    protected ViewGroup inflateEmptyView() {
        ViewGroup view = (ViewGroup) mLayoutInflater.inflate(
                mEmptyViewLayoutResourceId, null);
        if (EmptyViewPlaceHolderType.PlaceHolderTypeNoNeedInsert == mEmptyViewPlaceHolderType) {
            throw new IllegalArgumentException(
                    "PlaceHolderTypeNoNeedInsert type no need to inflate view");
        }
        return view;
    }

    public void setupEmptyView() {
        if (EmptyViewPlaceHolderType.PlaceHolderTypeNoNeedInsert == mEmptyViewPlaceHolderType) {
            mEmptyView = inflateEmptyView();
            mTvEmptyText = (TextView) mEmptyView
                    .findViewById(R.id.tv_empty_text);
            mIvEmptyImage = (ImageView) mEmptyView
                    .findViewById(R.id.iv_empty_image);
            mBtnRetry = (Button) mEmptyView.findViewById(R.id.btn_retry);
            mBtnRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            mEmptyView = new FrameLayout(mActivity);
            mEmptyView.setBackgroundColor(mActivity.getResources().getColor(
                    R.color.app_background_color));
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            switch (mEmptyViewAlignment) {
                case AlignmentTop:
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                    break;
                case AlignmentBottom:
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                            | Gravity.BOTTOM;
                    break;
                case AlignmentCenter:
                default:
                    layoutParams.gravity = Gravity.CENTER;
                    break;
            }
            ViewGroup view = inflateEmptyView();
            mTvEmptyText = (TextView) view.findViewById(R.id.tv_empty_text);
            mIvEmptyImage = (ImageView) view.findViewById(R.id.iv_empty_image);
            mBtnRetry = (Button) view.findViewById(R.id.btn_retry);
            mBtnRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).retryRequestNetwork();
                    } else if (mActivity instanceof PlatformBaseActivity) {
                        ((PlatformBaseActivity) mActivity).retryRequestNetwork();
                    }
                }
            });
            mEmptyView.addView(view, layoutParams);
        }
    }

    public ViewGroup getEmptyView() {
        return mEmptyView;
    }

    public void showEmptyView() {
        if (mEmptyView == null) {
            return;
        }
        boolean emptyViewParentResolved = false;
        if (mEmptyView.getParent() == null) {
            ViewGroup decorView = (ViewGroup) mActivity.getWindow()
                    .getDecorView();
            switch (mEmptyViewPlaceHolderType) {
                case PlaceHolderTypeInsertToView:
                    if (mEmptyViewContainerLayoutId > 0) {
                        //edit
                        ViewGroup viewGroup = (ViewGroup) decorView
                                .findViewById(mEmptyViewContainerLayoutId);
                        if (viewGroup != null) {
                            if (viewGroup instanceof FrameLayout
                                    || viewGroup instanceof RelativeLayout) {
                                emptyViewParentResolved = true;
                                viewGroup
                                        .addView(
                                                mEmptyView,
                                                new LayoutParams(
                                                        LayoutParams.MATCH_PARENT,
                                                        LayoutParams.MATCH_PARENT));
                            } else {
                                throw new IllegalArgumentException(
                                        "EmptyView place holder must be FrameLayout\\RelativeLayout");
                            }
                        }
                    }
                    break;
                case PlaceHolderTypeInsertToDecor:
                    decorView.addView(mEmptyView, new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                    emptyViewParentResolved = true;
                    break;
                case PlaceHolderTypeNoNeedInsert:
                default:
                    // do nothing
                    emptyViewParentResolved = true;
                    break;
            }
        } else {
            emptyViewParentResolved = true;
        }

        if (emptyViewParentResolved) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void hideEmptyView() {
        if (mEmptyView == null) {
            return;
        }
        mEmptyView.setVisibility(View.GONE);
        if (EmptyViewPlaceHolderType.PlaceHolderTypeNoNeedInsert != mEmptyViewPlaceHolderType) {
            if (mEmptyView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) mEmptyView.getParent();
                viewGroup.removeView(mEmptyView);
            }
        }
    }
}
