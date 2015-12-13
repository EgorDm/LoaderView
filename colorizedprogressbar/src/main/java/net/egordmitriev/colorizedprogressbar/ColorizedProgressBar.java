package net.egordmitriev.colorizedprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by EgorDm on 12/13/2015.
 */
public class ColorizedProgressBar extends ProgressBar {

    /**
     * Default layout for loading state
     */
    protected int progressbar_color = -1;

    public ColorizedProgressBar(Context context) {
        super(context, null, 0);
    }

    public ColorizedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(attrs);
    }

    public ColorizedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Initialize
     *
     * @param attrs
     */
    protected void init(AttributeSet attrs) {
        //Init attrs
        initAttrs(attrs);
        //Init view
        initView();
    }


    /**
     * Init custom attrs.
     *
     * @param attrs
     */
    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ColorizedProgressBar);
        try {
            progressbar_color = a.getColor(R.styleable.ColorizedProgressBar_progressbar_color, -1);
        } finally {
            a.recycle();
        }
    }

    /**
     * Init View
     */
    protected void initView() {
        if(progressbar_color != -1) {
            getIndeterminateDrawable().setColorFilter(progressbar_color, PorterDuff.Mode.SRC_IN);
        }
    }

    public void changeColor(int color)  {
        getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public void changeColor(Context context, int colorRes)  {
        getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, colorRes), PorterDuff.Mode.SRC_IN);
    }
}
