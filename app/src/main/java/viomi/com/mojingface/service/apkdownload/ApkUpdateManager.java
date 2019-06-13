package viomi.com.mojingface.service.apkdownload;

import android.app.Dialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.AppManager;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.download.DownloadFeedbackCallback;
import viomi.com.mojingface.http.HttpApi;
import viomi.com.mojingface.ui.activity.ApkUpdateActivity;
import viomi.com.mojingface.ui.activity.SettingActivity;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.PackageUtil;
import viomi.com.mojingface.util.SharedPreferencesUtil;
import viomi.com.mojingface.util.ToastUtil;

/**
 * <p>descript：软件升级管理类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/15<p>
 * <p>update time：2018/10/15<p>
 * <p>version：1<p>
 */
public class ApkUpdateManager implements ApkUpdateConfig {

    private static final String TAG = ApkUpdateManager.class.getName();

    private static final int CHECK_UPDATE = 3000;
    private static final int CHECK_UPDATE_CALLBACK_SUCCESS = 4000;
    private static final int CHECK_UPDATE_CALLBACK_FAIL = 5000;
    private static final int APK_FILEANALYSIS = 6000;
    private static final int APK_PROGRESSING = 7000;
    private static final int APK_DOWNLOADCOMPLETE = 8000;

    private Dialog updateDialog;
    private ApkHandler apkHandler;
    private String version;
    private String downlink;
    private CheckUpdateCallback<String> checkUpdateCallback;
    private long startTimestamp = 0;
    private long apkTotalBytes = 0;
    private String savePath;
    private File saveFile;
    private String apkVersion;
    private OnDownloadApkProgressListener listener;

    public ApkUpdateManager() {
        apkHandler = new ApkHandler(this);
        apkHandler.sendEmptyMessage(CHECK_UPDATE);
    }

    @Override
    public void init(OnDownloadApkProgressListener listener) {

        this.listener = listener;

        startTimestamp = System.currentTimeMillis();

        ApkDownloadManager.getInstance().setCallback(new DownloadFeedbackCallback() {

            private long startSize = 0;

            @Override
            public void onStart(int downloadId, long totalBytes) {
                startTimestamp = System.currentTimeMillis();
            }

            @Override
            public void onRetry(int downloadId) {

            }

            @Override
            public void onProgress(int downloadId, long bytesWritten, long totalBytes) {

                apkTotalBytes = totalBytes;
                LogUtils.i("XXX", "apkTotalBytes:" + apkTotalBytes);

                long progress = (long) (bytesWritten * 100f / totalBytes);
                progress = progress == 100 ? 0 : progress;
                long currentTimestamp = System.currentTimeMillis();
                LogUtils.d(TAG, "progress: " + progress);

                int speed;
                int deltaTime = (int) (currentTimestamp - startTimestamp + 1);
                speed = (int)((bytesWritten - startSize) * 1000 / deltaTime) / 1024;
                startSize = bytesWritten;

                LogUtils.d(TAG, "speed: " + speed + "kb/s");

                if (progress < 0) {
                    apkHandler.sendEmptyMessage(APK_FILEANALYSIS);
                } else if (progress >= 0 && progress <= 100) {
                    Message message = apkHandler.obtainMessage();
                    message.what = APK_PROGRESSING;

                    HashMap obj = new HashMap<>();
                    obj.put("progress", progress);
                    obj.put("bytesWritten", bytesWritten);
                    obj.put("totalBytes", totalBytes);
                    obj.put("speed", speed);
                    message.obj = obj;

                    apkHandler.sendMessage(message);
                }
            }

            @Override
            public void onSuccess(int downloadId, String filePath) {

                File apkFile = new File(filePath);
                if (apkFile.isFile()) {
                    LogUtils.i("XXX", "apkTotalBytes:" + apkTotalBytes + "  file length:" + apkFile.length());
                    if (apkTotalBytes == apkFile.length()) {
                        SharedPreferencesUtil.putData(MirrorConstans.NEWAPKVERSIONSIZE_KEY, apkFile.length());
                        SharedPreferencesUtil.removeByKey(MirrorConstans.DOWNLOADINGAPK_KEY);
                        apkHandler.sendEmptyMessage(APK_DOWNLOADCOMPLETE);
                    } else {
                        ApkDownloadManager.getInstance().startToDownloadFile(downlink, savePath, saveFile, 1, apkVersion);
                    }
                }
            }

            @Override
            public void onFailure(int downloadId, int statusCode, String errMsg) {
                ToastUtil.showLow(errMsg);
            }
        });
    }

    @Override
    public void checkUpdate(CheckUpdateCallback callback) {

        this.checkUpdateCallback = callback;

        Map<String, String> map = new HashMap<>();
        if ((boolean)SharedPreferencesUtil.getData(MirrorConstans.DEBUGMODE_KEY, false)) {
            map.put("type", "version");
            map.put("package", "viomi.com.mojingface.debug");
            map.put("channel", MirrorConstans.UPDATEENVIRONMENT);
            map.put("p", "1");
            map.put("l", "1");
        } else {
            map.put("type", "version");
            map.put("package", "viomi.com.mojingface");
            map.put("channel", MirrorConstans.UPDATEENVIRONMENT);
            map.put("p", "1");
            map.put("l", "1");
        }

        HttpApi.getRequestParam(MirrorConstans.UPDATE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apkHandler.sendEmptyMessage(CHECK_UPDATE_CALLBACK_FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        Message successMessage = apkHandler.obtainMessage();
                        successMessage.what = CHECK_UPDATE_CALLBACK_SUCCESS;
                        successMessage.obj = result;
                        apkHandler.sendMessage(successMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    apkHandler.sendEmptyMessage(CHECK_UPDATE_CALLBACK_FAIL);
                }

            }
        });

    }

    @Override
    public boolean hasNewestVersion(String apkVersionInfo) {
        LogUtils.i(TAG, "UpdateInfo = " + apkVersionInfo);

        try {
            JsonObject json = JsonUitls.getJsonObject(apkVersionInfo);
            JsonArray data = JsonUitls.getJsonArray(json, "data");
            if (data != null && data.size() > 0) {
                JsonObject item = JsonUitls.getJsonObject(data, 0);
                version = JsonUitls.getString(item, "code");
                downlink = JsonUitls.getString(item, "url");
                String newVersion = "viomiface" + version + ".apk";

                SharedPreferencesUtil.putData(MirrorConstans.NEWAPKVERSIONNAME_KEY, newVersion);

                LogUtils.i(TAG, "newVersion:" + newVersion);

                // 解析描述信息
                String detail = JsonUitls.getString(item, "detail");
                if (!TextUtils.isEmpty(detail)) {
                    JsonObject detailJsonObject = JsonUitls.getJsonObject(detail);
                    long totalSize = JsonUitls.getLong(detailJsonObject, "totalSize");
                    SharedPreferencesUtil.putData(MirrorConstans.NEWAPKVERSIONSIZE_KEY, totalSize);
                    String descript = JsonUitls.getString(detailJsonObject, "descript");
                }

                int currentVersionCode = PackageUtil.getVersionCode(MagicMirrorApplication.getAppContext());
                int versionCodeInt = Integer.parseInt(version);
                if (currentVersionCode < versionCodeInt) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void startDownload() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";

            saveFile = new File(savePath, (String)SharedPreferencesUtil.getData(MirrorConstans.NEWAPKVERSIONNAME_KEY, ""));

            apkVersion = "apk" + version;
            ApkDownloadManager.getInstance().startToDownloadFile(downlink, savePath, saveFile, 1, apkVersion);
        } else {
            ToastUtil.show("SDCard 未挂载");
        }
    }

    @Override
    public void showNewVersionDialog() {
        LogUtils.i(TAG, "showNewVersionDialog");

        if (updateDialog != null && updateDialog.isShowing()) {
            return;
        }
        BaseActivity currentActivity = AppManager.getInstance().currentActivity();
        updateDialog = new Dialog(currentActivity, R.style.selectorDialog);
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.update_dialog_layout, null);
        updateDialog.setContentView(view);

        Window dialogWindow = updateDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        TextView update_title = (TextView) view.findViewById(R.id.update_title);
        ListView tv_list = (ListView) view.findViewById(R.id.tv_list);
        TextView no_thanks = (TextView) view.findViewById(R.id.no_thanks);
        TextView ok_update = (TextView) view.findViewById(R.id.ok_update);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < version.length(); i++) {
            sb.append(version.charAt(i));
            if (i != version.length() - 1) {
                sb.append(".");
            }
        }
        update_title.setText("我是小V，我的技能又升级了");

        no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        ok_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isApkExits()) {
                    startToInstallApk((String)SharedPreferencesUtil.getData(MirrorConstans.NEWAPKVERSIONNAME_KEY, ""));
                } else {
                    startDownload();
                }
                updateDialog.dismiss();
            }
        });

        updateDialog.show();
    }

    @Override
    public void startToInstallApk(String newVersionName) {
        LogUtils.i(TAG, "开始安装程序 newVersion:" + newVersionName);

        // 必须退回到DesktopActivity才能正常安装更新包
        BaseActivity updateCheckActivity = AppManager.getInstance().getActivityByName(ApkUpdateActivity.class);
        if (updateCheckActivity != null) {
            updateCheckActivity.finish();
        }

        BaseActivity settingActivity = AppManager.getInstance().getActivityByName(SettingActivity.class);
        if (settingActivity != null) {
            settingActivity.finish();
        }

        String downloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + newVersionName;

        PackageUtil.installApk(MagicMirrorApplication.getAppContext(), downloadPath, newVersionName);
    }

    @Override
    public void destroy() {
        ApkDownloadManager.getInstance().setCallback(null);
    }

    @Override
    public boolean isApkExits() {
        String downloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + (String)SharedPreferencesUtil.getData(MirrorConstans.NEWAPKVERSIONNAME_KEY, "");

        File newVersionApk = new File(downloadPath);

        if (newVersionApk.isFile() && newVersionApk.length() == (Long)SharedPreferencesUtil.getData(MirrorConstans.NEWAPKVERSIONSIZE_KEY, 0L)) {
            LogUtils.i(TAG, "isApkExits:" + true);
            return true;
        } else {
            LogUtils.i(TAG, "isApkExits:" + false);
            return false;
        }

    }

    @Override
    public void cancelDownloadTask() {

        ApkDownloadManager.getInstance().cancelDownloadTask();

        apkHandler.removeCallbacksAndMessages(null);
    }

    static class ApkHandler extends Handler {

        private WeakReference<ApkUpdateManager> apkUpdateManagerWeakReference;
        private int checkUpdateCount = 0;

        public ApkHandler(ApkUpdateManager apkUpdateManager) {
            super(Looper.getMainLooper());
            apkUpdateManagerWeakReference = new WeakReference<>(apkUpdateManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ApkUpdateManager manager = apkUpdateManagerWeakReference.get();
            if (manager != null) {

                switch (msg.what) {
                    case CHECK_UPDATE:
                        if (manager.checkUpdateCallback != null && checkUpdateCount > 0) {
                            manager.checkUpdate(manager.checkUpdateCallback);
                        }

                        checkUpdateCount++;

                        long delayTime = 0;
                        if (checkUpdateCount <= 5) {
                            delayTime = 20 * 60 * 1000;
                        } else {
                            delayTime = 2 * 60 * 60 * 1000; // 每隔2小时检测一次更新信息
                        }

                        sendEmptyMessageDelayed(CHECK_UPDATE, delayTime);
                        break;
                    case CHECK_UPDATE_CALLBACK_SUCCESS:
                        String result = (String)msg.obj;
                        manager.checkUpdateCallback.onResultSuccess(result);
                        break;
                    case CHECK_UPDATE_CALLBACK_FAIL:
                        manager.checkUpdateCallback.onResultFail();
                        break;
                    case APK_FILEANALYSIS:
                        if (manager.listener != null) {
                            manager.listener.onFileAnalysis();
                        }
                        break;
                    case APK_PROGRESSING:
                        HashMap progressObj = (HashMap) msg.obj;
                        long progress = (long)progressObj.get("progress");
                        long bytesWritten = (long)progressObj.get("bytesWritten");
                        long totalBytes = (long)progressObj.get("totalBytes");
                        int speed = (int)progressObj.get("speed");

                        if (manager.listener != null) {
                            manager.listener.onProgressing(progress, bytesWritten, totalBytes, speed);
                        }
                        break;
                    case APK_DOWNLOADCOMPLETE:
                        if (manager.listener != null) {
                            manager.listener.onDownloadComplete();
                        }
                        break;
                }
            }
        }
    }

}
