package net.egordmitriev.loaderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by EgorDm on 11/15/2015.
 */
public class LoaderView extends FrameLayout {

    public static final int STATE_LOADING = 0;
    public static final int STATE_IDLE = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EXTRA = 3;

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
    protected int component_layout_extra_resourceID = -1;
    protected View mExtraView;

    /**
     * Current state
     */
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
            mCurrentState = a.getInt(R.styleable.LoaderView_state, STATE_LOADING);
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
            this.addView(mLoadingView);
        }
        if(component_layout_idle_resourceID != -1) {
            mIdleView = inflater.inflate(component_layout_idle_resourceID, this, false);
            this.addView(mIdleView);
        }
        if(component_layout_error_resourceID != -1) {
            mErrorView = inflater.inflate(component_layout_error_resourceID, this, false);
            this.addView(mErrorView);
        }
        if(component_layout_extra_resourceID != -1) {
            mExtraView = inflater.inflate(component_layout_extra_resourceID, this, false);
            this.addView(mExtraView);
        }
        setState(mCurrentState, true);

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
    }

    public void setState(int state) {
        setState(state, false);
    }
    public void setState(int state, boolean force) {
        if(state == mCurrentState && !force) return;
        hideViews();
        switch (state) {
            case STATE_LOADING:
                changeVisView(mLoadingView, true);
                break;
            case STATE_IDLE:
                changeVisView(mIdleView, true);
                break;
            case STATE_ERROR:
                changeVisView(mErrorView, true);
                break;
            case STATE_EXTRA:
                changeVisView(mExtraView, true);
                break;
        }

        mCurrentState = state;
    }

    protected void changeVisView(View view, boolean visible) {
        if(view != null) {
            view.setVisibility((visible) ? VISIBLE : GONE);
        }
    }

    protected void hideViews() {
        changeVisView(mLoadingView, false);
        changeVisView(mIdleView, false);
        changeVisView(mErrorView, false);
        changeVisView(mExtraView, false);
    }

    public void setListener(LoaderViewCallback listener) {
        this.listener = listener;
    }

    public interface LoaderViewCallback {
        void onRetryClick();
    }
}
