package viomi.com.mojingface.download;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SharedPreferencesUtil;
import viomi.com.mojingface.util.StringUtil;

/**
 * <p>descript：文件下载抽象类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/31<p>
 * <p>update time：2018/10/31<p>
 * <p>version：1<p>
 */
public abstract class AbsFileDownloadManager implements FileDownloadConfig {

    private static final String TAG = AbsFileDownloadManager.class.getName();

    private static final long time_out = 20 * 1000;

    private DownloadManager downloadManager;
    private int downloadTaskId = 100;
    protected DownloadFeedbackCallback superCallback;

    public abstract void setCallback(DownloadFeedbackCallback callback);

    public AbsFileDownloadManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(time_out, TimeUnit.MILLISECONDS)
                // 读取超时
                .readTimeout(time_out, TimeUnit.MILLISECONDS)
                // 写入超时
                .writeTimeout(time_out, TimeUnit.MILLISECONDS)
                .build();
        downloadManager =
                new DownloadManager.Builder().context(MagicMirrorApplication.getAppContext())
                        .downloader(OkHttpDownloader.create(client))
                        .threadPoolSize(3)
                        .logger(new Logger() {
                            @Override public void log(String message) {
                                LogUtils.d("TAG", message);
                            }
                        })
                        .build();
    }

    @Override
    public boolean deleteFile(String savePath, File deleteFile, int downloadType, String fileKey) {
        // 已下载完整的旧文件需要删除
        if (deleteFile == null) {
            return false;
        }

        if (deleteFile.isFile()) {
            return deleteFile.delete();
        }

        String downloadingKey = "";
        if (1 == downloadType) {
            downloadingKey = (String)SharedPreferencesUtil.getData(MirrorConstans.DOWNLOADINGAPK_KEY, "");

            // apk下载要删掉之前下载的旧版apk
            File savePathFile = new File(savePath);
            if (savePathFile.isDirectory()) {
                for (File file : savePathFile.listFiles()) {
                    if (file.isFile() && file.getName().endsWith("apk")) {
                        file.delete();
                    }
                }
            }
        }

        if (2 == downloadType) {
            downloadingKey = (String)SharedPreferencesUtil.getData(MirrorConstans.DOWNLOADINGAPK_KEY, "");
        }
        if (!downloadingKey.equals(fileKey)) {
            File savePathFile = new File(savePath);
            if (savePathFile.isDirectory()) {
                for (File file : savePathFile.listFiles()) {
                    if (file.isFile() && file.getName().endsWith("tmp")) {
                        file.delete();
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void startToDownloadFile(String url, String savePath, File saveFile, int downloadType, String fileKey) {

        deleteFile(savePath, saveFile, downloadType, fileKey);

        if (1 == downloadType) {
            SharedPreferencesUtil.putData(MirrorConstans.DOWNLOADINGAPK_KEY, fileKey);
        }

        if (2 == downloadType) {
            SharedPreferencesUtil.putData(MirrorConstans.DOWNLOADINGAPK_KEY, fileKey);
        }

        DownloadRequest request = new DownloadRequest.Builder()
                .url(url)
                .destinationDirectory(savePath)
                .destinationFilePath(saveFile.getAbsolutePath())
                .downloadCallback(new AbsFileDownloadCallback())
                .retryTime(500)
                .retryInterval(30, TimeUnit.SECONDS)
                .progressInterval(1, TimeUnit.SECONDS)
                .priority(Priority.HIGH)
                .allowedNetworkTypes(DownloadRequest.NETWORK_MOBILE)
                .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
                .build();
        downloadTaskId = downloadManager.add(request);


    }

    @Override
    public void cancelDownloadTask() {
        if (downloadManager != null) {
            if (downloadManager.isDownloading(downloadTaskId)) {
                downloadManager.cancel(downloadTaskId);
            }
        }
    }

    public class AbsFileDownloadCallback extends com.coolerfall.download.DownloadCallback {

        @Override
        public void onStart(int downloadId, long totalBytes) {
            LogUtils.d(TAG, "start download: " + downloadId);
            LogUtils.d(TAG, "totalBytes: " + totalBytes);

            // 判断是否还有足够的剩余空间
//            long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
            long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
//            long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
            LogUtils.i("XXX", "可用大小:"+usableSpace/1024/1024+"M 文件大小:"+totalBytes/1024/1024+"M");

            if (usableSpace <= totalBytes) {
                if (superCallback != null) {
                    superCallback.onFailure(downloadId, StringUtil.saveToInt(MirrorConstans.NOMORE_SPACE_ERROR_CODE), "系统无足够剩余空间");
                    if (downloadManager.isDownloading(downloadTaskId)) {
                        downloadManager.cancel(downloadTaskId);
                    }
                }
            } else {
                if (superCallback != null) {
                    superCallback.onStart(downloadId, totalBytes);
                }
            }
        }

        @Override
        public void onRetry(int downloadId) {
            LogUtils.d(TAG, "retry downloadId: " + downloadId);

            if (superCallback != null) {
                superCallback.onRetry(downloadId);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
            LogUtils.d(TAG, "onProgress downloadId: " + downloadId
                    + "  bytesWritten: " + bytesWritten
                    + "  totalBytes: " + totalBytes);

            if (superCallback != null) {
                superCallback.onProgress(downloadId, bytesWritten, totalBytes);
            }
        }

        @Override
        public void onSuccess(int downloadId, String filePath) {
            LogUtils.d(TAG, "success: " + downloadId
                    + " filePath: " + filePath
                    + " size: " + new File(filePath).length());

            if (superCallback != null) {
                superCallback.onSuccess(downloadId, filePath);
            }
        }

        @Override
        public void onFailure(int downloadId, int statusCode, String errMsg) {
            LogUtils.d(TAG, "fail: " + downloadId + " " + statusCode + " " + errMsg);

            if (superCallback != null) {
                superCallback.onFailure(downloadId, statusCode, errMsg);
            }
        }
    }
}