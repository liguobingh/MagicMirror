package viomi.com.mojingface.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: com.viomi.viomifaceinwall.widget
 * @ClassName: MiLanTingTextView
 * @Description: 统一使用米兰亭字体
 * @Author: randysu
 * @CreateDate: 2019/3/5 11:16 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/5 11:16 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MiLanTingTextView extends AppCompatTextView {

    public MiLanTingTextView(Context context) {
        this(context, null);
    }

    public MiLanTingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiLanTingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypefaceCache.getTypeface(getContext()));
    }
}
