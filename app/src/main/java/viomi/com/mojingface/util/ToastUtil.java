package viomi.com.mojingface.util;

import android.widget.Toast;

import viomi.com.mojingface.base.MagicMirrorApplication;

/**
 * Created by viomi on 2016/10/24.
 * toast封装
 */

public class ToastUtil {

    private static long lastTime;
    private static String lastResult;

    private static long lastTimeLow;
    private static String lastResultLow;

    public static void show(String result) {
        if (result == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime <= 5* 1000 && result.equals(lastResult)) {
            return;
        }
        lastTime = currentTime;
        lastResult = result;
        Toast.makeText(MagicMirrorApplication.getAppContext(), result, Toast.LENGTH_SHORT).show();
    }

    public static void showLow(String result) {
        if (result == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeLow <= 60*60* 1000 && result.equals(lastResultLow)) {
            return;
        }
        lastTimeLow = currentTime;
        lastResultLow = result;
        Toast.makeText(MagicMirrorApplication.getAppContext(), result, Toast.LENGTH_SHORT).show();
    }


}
