package viomi.com.mojingface.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.List;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.model.MiIndentify;


/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.util
 * @ClassName: SystemUtil
 * @Description: 系统工具类
 * @Author: randysu
 * @CreateDate: 2019/3/2 4:14 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 4:14 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SystemUtil {

    /**
     * 获取应用版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode;

        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 0;
        }

        return versionCode;
    }

    /**
     * 获取应用版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName;

        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "";
        }

        return versionName;
    }

    /***
     * 获取小米标识
     * @return
     */
    public static MiIndentify getMiIdentify() {
        MiIndentify miIndentify = new MiIndentify();
        miIndentify.mac = MiIndentify.DefaultMac;
        miIndentify.did = MiIndentify.DefaultDeviceId;
        miIndentify.token = MiIndentify.DefaultMiotToken;
        String serial = Build.SERIAL;

        if (serial == null) {
            LogUtils.e("getMiIdentify", "null");
            return miIndentify;
        } else {
            LogUtils.d("getMiIdentify", serial + ",length=" + serial.length());
        }

        if (serial != null) {
            String[] serials = serial.split("\\|");
            if (serials != null && serials.length == 3) {
                try {
                    miIndentify.did =serials[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    miIndentify.token=serials[2];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            miIndentify.mac = getPhoneMac(MagicMirrorApplication.getAppContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return miIndentify;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取rom显示的名称
     * @return
     */
    public static String getRomVersionDetail() {
        return Build.DISPLAY;
    }

    /***
     * 获取手机mac
     * @return
     */
    public static String getPhoneMac(Context ctx) {
        String mac = "";
        try {
            WifiManager wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception e) {
            LogUtils.e("getLocalMacAddress", "fail,msg=" + e.getMessage());
        }
        return mac;
    }

    /**
     * 判断服务是否正在运行
     * @param serviceName
     * @return
     */
    public static boolean isServiceAlive(Context context, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
