package viomi.com.mojingface.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import viomi.com.mojingface.model.DownloadInfoBean;
import viomi.com.mojingface.service.apkdownload.ApkUpdateConfig;
import viomi.com.mojingface.service.apkdownload.ApkUpdateManager;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.viewmodel
 * @ClassName: ApkUpdateViewModel
 * @Description: 应用升级ViewModel
 * @Author: randysu
 * @CreateDate: 2019/4/2 3:54 PM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 3:54 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApkUpdateViewModel extends BaseAndroidViewModel {

    private ApkUpdateManager apkUpdateManager;
    private MutableLiveData<String> updateResultLiveData = new MutableLiveData<>();

    private DownloadInfoBean downloadInfoBean = new DownloadInfoBean();
    private MutableLiveData<DownloadInfoBean> downloadInfoLiveData = new MutableLiveData<>();

    public ApkUpdateViewModel(@NonNull Application application) {
        super(application);

        apkUpdateManager = new ApkUpdateManager();
        apkUpdateManager.init(new ApkUpdateConfig.OnDownloadApkProgressListener() {
            @Override
            public void onFileAnalysis() {
                downloadInfoBean.setDownloadStatus(DownloadInfoBean.DOWNLOADSTATUS_ANALYSISING);

                downloadInfoLiveData.postValue(downloadInfoBean);
            }

            @Override
            public void onProgressing(long progress, long currentBytes, long contentLength, int speed) {
                downloadInfoBean.setDownloadStatus(DownloadInfoBean.DOWNLOADSTATUS_PROGRESSING);
                downloadInfoBean.setProgress(progress);
                downloadInfoBean.setCurrentBytes(currentBytes);
                downloadInfoBean.setContentLength(contentLength);
                downloadInfoBean.setSpeed(speed);

                downloadInfoLiveData.postValue(downloadInfoBean);
            }

            @Override
            public void onDownloadComplete() {
                downloadInfoBean.setDownloadStatus(DownloadInfoBean.DOWNLOADSTATUS_DOWNLOADCOMPLETE);

                downloadInfoLiveData.postValue(downloadInfoBean);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        apkUpdateManager.destroy();
    }

    public LiveData<DownloadInfoBean> getDownloadInfo() {
        return downloadInfoLiveData;
    }

    public LiveData<String> checkUpdate() {
        apkUpdateManager.checkUpdate(new ApkUpdateConfig.CheckUpdateCallback<String>() {
            @Override
            public void onResultFail() {
                updateResultLiveData.postValue(null);
            }

            @Override
            public void onResultSuccess(String result) {
                updateResultLiveData.postValue(result);
            }
        });
        return updateResultLiveData;
    }

    public boolean hasNewestVersion(String apkVersionInfo) {
        return apkUpdateManager.hasNewestVersion(apkVersionInfo);
    }

    public void showNewVersionDialog() {
        apkUpdateManager.showNewVersionDialog();
    }

    public void cancelDownloadTask() {
        apkUpdateManager.cancelDownloadTask();
    }

    public boolean isApkExits() {
        return apkUpdateManager.isApkExits();
    }

    public void startToInstallApk(String newVersionName) {
        apkUpdateManager.startToInstallApk(newVersionName);
    }

}
