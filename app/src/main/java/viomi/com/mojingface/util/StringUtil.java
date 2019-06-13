package viomi.com.mojingface.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * <p>descript：字符串工具<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/6<p>
 * <p>update time：2018/11/6<p>
 * <p>version：1<p>
 */
public class StringUtil {

    private static final String TAG = StringUtil.class.getName();

    /**
     * 安全转换int
     * @param object
     * @return
     */
    public static int saveToInt(Object object) {
        int result = 0;
        try {
            result = Integer.parseInt(object.toString().trim());
        } catch (NumberFormatException e) {
            LogUtils.i(TAG, e.getMessage());
            result = 0;
        }

        return result;
    }

    /**
     * 转换音乐的格式时间
     * @param timeMs
     * @return
     */
    public static String musicFormatTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds =  timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
