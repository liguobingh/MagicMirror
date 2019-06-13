package viomi.com.mojingface.widget;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by viomi on 2017/2/24.
 */

public class TitleClockView extends MiLanTingTextView {

    private Paint paint;

    public TitleClockView(Context context) {
        this(context, null);
    }

    public TitleClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mClockHandler.sendEmptyMessage(0);
    }

    private Handler mClockHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateClock();
            mClockHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private void updateClock() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(date);
        this.setText(time);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mClockHandler.removeCallbacksAndMessages(null);
    }
}
