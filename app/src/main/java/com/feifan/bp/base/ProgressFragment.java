package com.feifan.bp.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import com.feifan.bp.R;
import com.feifan.bp.util.LogUtil;

/**
 *
 * 带有加载进度的Fragment
 * <pre>
 *     该Fragment具有内容（Content）、空（Empty）和加载（Progress）三种状态，
 *     根据数据加载状况可以在三种状态下进行切换。通过{@link #onCreateContentView(ViewStubCompat)}
 *     设置内容视图；通过重载{@link #initEmptyView()}方法，可定制空视图
 * </pre>
 *
 *
 */
public abstract class ProgressFragment extends PlatformFragment {

    private View mProgressContainer;
    private View mContentContainer;
    private View mContentView;
    private View mEmptyView;
    private boolean mContentShown;
    private boolean mIsContentEmpty;

    public ProgressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        ViewStubCompat stub = (ViewStubCompat)v.findViewById(android.R.id.content);
        mContentView = onCreateContentView(stub);
        return v;
    }

    /**
     * 在这里初始化内容视图
     * <pre>
     * {@code
     * stub.setLayoutResource(R.layout.fragment_settings);
     * View v = stub.inflate();
     * return v;
     * }
     * </pre>
     * @param stub
     * @return
     */
    protected abstract View onCreateContentView(ViewStubCompat stub);

    /**
     * 创建视图后初始化视图实例
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureContent();
        initEmptyView();
    }

    /**
     * 初始化空视图
     * <pre>
     *     重载此方法可以定制空视图
     * </pre>
     */
    protected void initEmptyView() {
        ensureContent();
    }

    /**
     * 活动创建完毕后，加载数据
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentShown(false);
        requestData();
    }

    /**
     * 请求数据
     * <pre>
     *     子类实现此方法，用于请求数据和填充视图
     * </pre>
     * @return
     */
    protected abstract void requestData();

    @Override
    public void onDestroyView() {
        mContentShown = false;
        mIsContentEmpty = false;
        mProgressContainer = mContentContainer = mContentView = mEmptyView = null;
        super.onDestroyView();
    }



    /**
     * Return content view or null if the content view has not been initialized.
     *
     * @return content view or null
     * @see #setContentView(View)
     * @see #setContentView(int)
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * Set the content content from a layout resource.
     *
     * @param layoutResId Resource ID to be inflated.
     * @see #setContentView(View)
     * @see #getContentView()
     */
    public void setContentView(int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View contentView = layoutInflater.inflate(layoutResId, null);
        setContentView(contentView);
    }

    /**
     * Set the content view to an explicit view. If the content view was installed earlier,
     * the content will be replaced with a new view.
     *
     * @param view The desired content to display. Value can't be null.
     * @see #setContentView(int)
     * @see #getContentView()
     */
    public void setContentView(View view) {
        if (!isAdded()){
            return;
        }
        ensureContent();
        if (view == null) {
            throw new IllegalArgumentException("Content view can't be null");
        }
        if (mContentContainer instanceof ViewGroup) {
            ViewGroup contentContainer = (ViewGroup) mContentContainer;
            if (mContentView == null) {
                contentContainer.addView(view);
            } else {
                int index = contentContainer.indexOfChild(mContentView);
                // replace content view
                contentContainer.removeView(mContentView);
                contentContainer.addView(view, index);
            }
            mContentView = view;
        } else {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
    }

    /**
     * The default content for a ProgressFragment has a TextView that can be shown when
     * the content is empty {@link #setContentEmpty(boolean)}.
     * If you would like to have it shown, call this method to supply the text it should use.
     *
     * @param resId Identification of string from a resources
     * @see #setEmptyText(CharSequence)
     */
    public void setEmptyText(int resId) {
        setEmptyText(getString(resId));
    }

    /**
     * The default content for a ProgressFragment has a TextView that can be shown when
     * the content is empty {@link #setContentEmpty(boolean)}.
     * If you would like to have it shown, call this method to supply the text it should use.
     *
     * @param text Text for empty view
     * @see #setEmptyText(int)
     */
    public void setEmptyText(CharSequence text) {
        ensureContent();
    }

    /**
     * 控制内容视图的显示和隐藏.隐藏时会显示加载进度视图
     *
     * @param shown 设置为true，显示内容视图，否则显示加载进度
     * @see #setContentShownNoAnimation(boolean)
     */
    public void setContentShown(boolean shown) {
        setContentShown(shown, true);
    }

    /**
     * 与{@link #setContentShown(boolean)}相同, 状态切换时无动画
     *
     * @param shown 设置为true，显示内容视图，否则显示加载进度
     * @see #setContentShown(boolean)
     */
    public void setContentShownNoAnimation(boolean shown) {
        setContentShown(shown, false);
    }

    /**
     * 控制内容视图的显示和隐藏，隐藏时显示加载进度视图
     *
     * @param shown   设置为true，显示内容视图，否则显示加载进度
     * @param animate 设置为true，状态切换时，有动画效果
     */
    private void setContentShown(boolean shown, boolean animate) {
        if (!isAdded()){
            return;
        }
        ensureContent();
        if (mContentShown == shown) {
            return;
        }
        mContentShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mContentContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Returns true if content is empty. The default content is not empty.
     *
     * @return true if content is null or empty
     * @see #setContentEmpty(boolean)
     */
    public boolean isContentEmpty() {
        return mIsContentEmpty;
    }

    /**
     * 控制空视图与内容视图的显示和隐藏，调用此方法时，内容视图不能为空
     * {@link #setContentView(View)}.
     *
     * @param isEmpty true显示空视图，否则显示内容视图
     * @see #isContentEmpty()
     */
    public void setContentEmpty(boolean isEmpty) {
        if (!isAdded()){
            return;
        }
        ensureContent();
        if (mContentView == null) {
            throw new IllegalStateException("Content view must be initialized before");
        }
        if (isEmpty) {
            mEmptyView.setVisibility(View.VISIBLE);
            mContentView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);
        }
        mIsContentEmpty = isEmpty;
    }

    /**
     * 初始化视图.
     */
    private void ensureContent() {
        if (mContentContainer != null && mProgressContainer != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        mProgressContainer = root.findViewById(R.id.progress_container);
        if (mProgressContainer == null) {
            throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.progress_container'");
        }
        mContentContainer = root.findViewById(R.id.content_container);
        if (mContentContainer == null) {
            throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.content_container'");
        }
        mEmptyView = root.findViewById(android.R.id.extractArea);
        ((View)root.findViewById(android.R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentShown(false);
                requestData();
            }
        });
        mEmptyView.setVisibility(View.GONE);

        mContentShown = true;
        // We are starting without a content, so assume we won't
        // have our data right away and start with the progress indicator.
        if (mContentView == null) {
            setContentShown(false, false);
        }
    }

    // FIXME refactory me later
    private Dialog mWaitingDlg;

    protected void startWaiting() {
        if(isAdded()) {
            if(mWaitingDlg == null) {
                mWaitingDlg = new Dialog(getActivity(), R.style.LoadingDialog);
                mWaitingDlg.setContentView(R.layout.progress_bar_layout);
                mWaitingDlg.setCancelable(false);
            }
            mWaitingDlg.show();
        }
    }

    protected void stopWaiting() {
        if(isAdded()) {
            if(mWaitingDlg != null && mWaitingDlg.isShowing()) {
                mWaitingDlg.dismiss();
            }
        }
    }
}
