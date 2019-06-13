package viomi.com.mojingface.service.apkdownload;


import viomi.com.mojingface.download.AbsFileDownloadManager;
import viomi.com.mojingface.download.DownloadFeedbackCallback;

/**
 * <p>descript：APK下载管理器<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/31<p>
 * <p>update time：2018/10/31<p>
 * <p>version：1<p>
 */
public class ApkDownloadManager extends AbsFileDownloadManager {

    private static ApkDownloadManager instance;

    public static ApkDownloadManager getInstance() {
        if (instance == null) {
            synchronized (ApkDownloadManager.class) {
                if (instance == null) {
                    instance = new ApkDownloadManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void setCallback(DownloadFeedbackCallback callback) {
        superCallback = callback;
    }
}
