package viomi.com.mojingface.util;

import android.util.Log;

import viomi.com.mojingface.BuildConfig;


/**
 * Created by Mocc on 2018/1/3
 */

public class LogUtils {

    private final static String TAG = "LogUtils:";

    enum LogType {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        ASSERT
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.VERBOSE);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.DEBUG);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.INFO);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.WARN);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.ERROR);
        }
    }

    public static void a(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            showLongLog(tag, msg, LogType.ASSERT);
        }
    }

    private static void showLongLog(String tag, String msg, LogType type) {
        msg = msg.trim();
        int index = 0;
        int maxLength = 4000;

        String sub;
        while (index < msg.length()) {
            if (msg.length() <= index + maxLength) {
                sub = msg.substring(index);
            } else {
                sub = msg.substring(index, index + maxLength);
            }

            index += maxLength;

            if (type == LogType.VERBOSE) {
                Log.v(tag, sub.trim());
            } else if (type == LogType.DEBUG) {
                Log.i(tag, sub.trim());
            } else if (type == LogType.INFO) {
                Log.i(tag, sub.trim());
            } else if (type == LogType.WARN) {
                Log.w(tag, sub.trim());
            } else if (type == LogType.ERROR) {
                Log.e(tag, sub.trim());
            } else if (type == LogType.ASSERT) {
                Log.wtf(tag, sub.trim());
            } else {
                Log.v(tag, sub.trim());
            }
        }
    }

}
