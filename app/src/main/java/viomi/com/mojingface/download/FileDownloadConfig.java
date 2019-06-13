package viomi.com.mojingface.download;

import java.io.File;

/**
 * <p>descript：文件下载必备方法<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/31<p>
 * <p>update time：2018/10/31<p>
 * <p>version：1<p>
 */
public interface FileDownloadConfig {

    /**
     * 删除已经下载的文件
     * @return boolean
     */
    boolean deleteFile(String savePath, File deleteFile, int downloadType, String fileKey);

    /**
     * 下载文件
     * @param url          网络地址
     * @param savePath     保存路径（没有具体文件名）
     * @param saveFile     保存文件
     * @param downloadType 1:  apk下载   2:  rom下载
     * @param fileKey      用于校验是否是相同文件（主要针对固件下载，因为固件的文件名都叫做update.zip）
     */
    void startToDownloadFile(String url, String savePath, File saveFile, int downloadType, String fileKey);

    /**
     * 取消下载任务
     */
    void cancelDownloadTask();

}
