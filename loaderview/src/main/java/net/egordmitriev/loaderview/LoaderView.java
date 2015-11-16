package net.egordmitriev.loaderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by EgorDm on 11/15/2015.
 */
public class LoaderView extends FrameLayout {

    public static final int STATE_LOADING = 0;
    public static final int STATE_IDLE = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EXTRA = 3;

    protected static final int ANIMATION_DURATION = 200;

    /**
     * Default layout for loading state
     */
    protected int component_layout_loading_resourceID = R.layout.component_loaderview_loading;
    protected View mLoadingView;

    /**
     * Default layout for idle state
     */
    protected int component_layout_idle_resourceID = -1;
    protected View mIdleView;
    /**
     * Default layout for error state
     */
    protected int component_layout_error_resourceID = R.layout.component_loaderview_error;
    protected View mErrorView;
    /**
     * Default layout for idle state
     */
    protected String mErrorMessage;

    /**
     * Default error message
     */
    protected int component_layout_extra_resourceID = -1;
    protected View mExtraView;

    /**
     * Current state
     */
    protected int mInitialState = -1;
    protected int mCurrentState = -1;
    protected LoaderViewCallback listener;

    public LoaderView(Context context) {
        super(context, null, 0);
    }

    public LoaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Initialize
     *
     * @param attrs
     * @param defStyle
     */
    protected void init(AttributeSet attrs, int defStyle) {
        //Init attrs
        initAttrs(attrs, defStyle);
        //Init view
        initView();
    }

    /**
     * Init custom attrs.
     *
     * @param attrs
     * @param defStyle
     */
    protected void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.LoaderView, defStyle, defStyle);

        try {
            component_layout_loading_resourceID = a.getResourceId(R.styleable.LoaderView_loading_resourceID, component_layout_loading_resourceID);
            component_layout_idle_resourceID = a.getResourceId(R.styleable.LoaderView_idle_resourceID, -1);
            component_layout_error_resourceID = a.getResourceId(R.styleable.LoaderView_error_resourceID, component_layout_error_resourceID);
            component_layout_extra_resourceID = a.getResourceId(R.styleable.LoaderView_extra_resourceID, -1);
            component_layout_extra_resourceID = a.getResourceId(R.styleable.LoaderView_extra_resourceID, -1);
            if(mInitialState == -1) {
                mInitialState = a.getInt(R.styleable.LoaderView_state, STATE_LOADING);
            }
            mErrorMessage = a.getString(R.styleable.LoaderView_error_message);
        } finally {
            a.recycle();
        }
    }

    /**
     * Init View
     */
    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(component_layout_loading_resourceID != -1) {
            mLoadingView = inflater.inflate(component_layout_loading_resourceID, this, false);
            mLoadingView.setVisibility(GONE);
            this.addView(mLoadingView);
        }
        if(component_layout_idle_resourceID != -1) {
            mIdleView = inflater.inflate(component_layout_idle_resourceID, this, false);
            mIdleView.setVisibility(GONE);
            this.addView(mIdleView);
        }
        if(component_layout_error_resourceID != -1) {
            mErrorView = inflater.inflate(component_layout_error_resourceID, this, false);
            mErrorView.setVisibility(GONE);
            if(mErrorMessage != null) {
                TextView errorText = (TextView) mErrorView.findViewById(R.id.loaderview_errormsg);
                if(errorText != null) {
                    errorText.setText(mErrorMessage);
                }
            }

            this.addView(mErrorView);
        }
        if(component_layout_extra_resourceID != -1) {
            mExtraView = inflater.inflate(component_layout_extra_resourceID, this, false);
            mExtraView.setVisibility(GONE);
            this.addView(mExtraView);
        }

        if(!isInEditMode()) {
            Button retryButton = (Button)mErrorView.findViewById(R.id.loaderview_retry);
            retryButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRetryClick();
                    }
                }
            });
        }

        setState(mInitialState, true);
    }

    public void setState(int state) {
        setState(state, false);
    }
    public void setState(int state, boolean force) {
        if(state == mCurrentState && !force) return;
        changeVisibleView(getStateView(state), getStateView(mCurrentState), true);
        mCurrentState = state;
    }

    protected View getStateView(int state) {
        switch (state) {
            case STATE_LOADING:
                return mLoadingView;
            case STATE_IDLE:
                return mIdleView;
            case STATE_ERROR:
                return mErrorView;
            case STATE_EXTRA:
                return mExtraView;
        }
        return null;
    }

    protected void toggleVisibility(View view, boolean visible) {
        if(view != null) {
            view.setVisibility((visible) ? VISIBLE : GONE);
        }
    }

    protected void changeVisibleView(final View newView, final View oldView, boolean animate) {
        if(animate && !isInEditMode()) {
            crossfade(newView, oldView);
        } else {
            toggleVisibility(oldView, false);
            toggleVisibility(newView, true);
        }
    }

    private void crossfade(final View newView, final View oldView) {
        newView.setAlpha(0f);
        newView.setVisibility(View.VISIBLE);

        newView.animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null);

        if(oldView != null) {
            oldView.animate()
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            oldView.setVisibility(View.GONE);
                        }
                    });
        }
    }

    public void setListener(LoaderViewCallback listener) {
        this.listener = listener;
    }

    public interface LoaderViewCallback {
        void onRetryClick();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instance_state", super.onSaveInstanceState());
        bundle.putInt("current_state", mCurrentState);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mInitialState = bundle.getInt("current_state");

            state = bundle.getParcelable("instance_state");
        }
        super.onRestoreInstanceState(state);
    }
}
