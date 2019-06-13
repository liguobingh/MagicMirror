package viomi.com.mojingface.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * <p>descript：程序包工具类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/9/28<p>
 * <p>update time：2018/9/28<p>
 * <p>version：1<p>
 */
public class PackageUtil {

    private static final String TAG = PackageUtil.class.getName();

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

    /**
     * 安装apk文件
     * @param context
     * @param apkFilePath
     */
    public static void installApk(Context context, String apkFilePath, String extra) {
        if (context == null) {
            LogUtils.d(TAG, "context is null");
            return;
        }

        if (TextUtils.isEmpty(apkFilePath)) {
            LogUtils.d(TAG, "apkFilePath is null");
            return;
        }

        File file = new File(apkFilePath);
        if (!file.exists()) {
            LogUtils.e(TAG, "apkFile not found");
            return;
        }

//        String romVersion = Build.DISPLAY;
//        if (romVersion.length() > 10) {
//            LogUtils.i(TAG, "正常进行  普通  安装");
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri uri = Uri.parse("file://" + file.toString());
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } else {
//            LogUtils.i(TAG, "正常进行  SilenceInstall  安装");
//
//            Intent mIntent = new Intent("SilenceInstall");
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.putExtra("originPath", apkFilePath);
//            mIntent.putExtra("installerPackageName", "viomi.com.viomiaiface");
//            context.sendBroadcast(mIntent);
//        }

        Log.i(TAG, "开始执行安装: " + apkFilePath);
        File apkFile = new File(apkFilePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(
//                    context
//                    , "你的包名.fileprovider"
//                    , apkFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w(TAG, "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
