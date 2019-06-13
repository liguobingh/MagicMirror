package viomi.com.mojingface.widget;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceCache {
    static Typeface typeface;

    public static Typeface getTypeface(Context context) {
        if (typeface == null)
            Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/MI_LanTing_Regular.ttf");
        return typeface;
    }
}
