package viomi.com.mojingface.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import viomi.com.mojingface.R;

/**
 * Created by Mocc on 2018/1/9
 */

public class LoadingView extends RelativeLayout {

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setBackground(getResources().getDrawable(R.drawable.loading_bg));

        ImageView iv = new ImageView(context);

        LayoutParams ivParams = new LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.loading_width), getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.loading_width), getResources().getDisplayMetrics()));
        ivParams.addRule(CENTER_IN_PARENT, TRUE);

        Glide.with(context).load(R.drawable.loading).into(iv);
        this.addView(iv, ivParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        this.setVisibility(GONE);
        return true;
    }
}
