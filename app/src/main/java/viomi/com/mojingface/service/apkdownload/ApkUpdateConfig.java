package viomi.com.mojingface.service.apkdownload;

/**
 * <p>descript：软件升级接口<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/15<p>
 * <p>update time：2018/10/15<p>
 * <p>version：1<p>
 */
public interface ApkUpdateConfig {

    void init(OnDownloadApkProgressListener listener);

    void checkUpdate(CheckUpdateCallback callback);

    boolean hasNewestVersion(String apkVersionInfo);

    void startDownload();

    void showNewVersionDialog();

    void startToInstallApk(String newVersionName);

    void destroy();

    boolean isApkExits();

    void cancelDownloadTask();

    public interface CheckUpdateCallback<T> {
        void onResultFail();
        void onResultSuccess(T t);
    }

    public interface OnDownloadApkProgressListener {
        void onFileAnalysis();
        void onProgressing(long progress, long currentBytes, long contentLength, int speed);
        void onDownloadComplete();
    }

}
