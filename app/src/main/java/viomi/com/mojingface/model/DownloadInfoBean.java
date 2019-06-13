package viomi.com.mojingface.model;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.model
 * @ClassName: DownloadInfoBean
 * @Description: 下载文件bean
 * @Author: randysu
 * @CreateDate: 2019/4/2 7:17 PM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 7:17 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DownloadInfoBean {

    public static final int DOWNLOADSTATUS_ANALYSISING = 101;
    public static final int DOWNLOADSTATUS_PROGRESSING = 102;
    public static final int DOWNLOADSTATUS_DOWNLOADCOMPLETE = 103;

    private int downloadStatus;
    private long progress;
    private long currentBytes;
    private long contentLength;
    private int speed;

    public DownloadInfoBean() {

    }

    public DownloadInfoBean(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
