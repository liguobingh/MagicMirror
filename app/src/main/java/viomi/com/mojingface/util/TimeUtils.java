package viomi.com.mojingface.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/28.
 * 时间相关的工具类
 */

public class TimeUtils {

    public static final String DATETIMESECOND = "yyyyMMddHHmmss";
    public static final String DATETIMESECOND_SPLIT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将秒数转换成 00:30 的格式
     */
    public static String formatTime(int time) {
        int sec = time % 60;
        int min = time / 60;
        return (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec);
    }

    /**
     * 秒转时分秒
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "0秒";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分钟" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99小时59分钟59秒";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分钟" + unitFormat(second) + "秒";
            }
        }
        return timeStr;
    }

    /**
     * 秒转时分
     *
     * @param time
     * @return
     */
    public static String secToOvenTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
//        int second = 0;
        if (time <= 0) {
            return "0分";
        } else {
            minute = time / 60;
            if (minute < 60) {
//                second = time % 60;
                timeStr = unitFormat(minute) + "分";
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99小时59分";
                }
                minute = minute % 60;
//                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分";
            }
        }
        return timeStr;
    }


    public static String minToTime(int min) {
        int hour = 0;
        String timeStr = null;
        if (min <= 0) {
            return "0分钟";
        } else {
            if (min < 60) {
                timeStr = min + "分钟";
            } else {
                hour = min / 60;
                int minute = min % 60;
                if (minute != 0) {
                    timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分钟";
                } else {
                    timeStr = unitFormat(hour) + "小时";
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getFormatTime(long time, String pormatPattern) {
        SimpleDateFormat df = new SimpleDateFormat(pormatPattern);
        return df.format(time);
    }

    public static String getFriendlyDayTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 6) {
            return "凌晨";
        }
        if (a > 6 && a <= 12) {
            return "上午";
        }
        if (a > 12 && a <= 13) {
            return "中午";
        }
        if (a > 13 && a <= 18) {
            return "下午";
        }
        if (a > 18 && a <= 24) {
            return "晚上";
        }

        return "";
    }

}
