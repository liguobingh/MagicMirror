package viomi.com.mojingface.mediaplayer;

import android.net.Uri;

import com.google.android.exoplayer2.Player;

/**
 * <p>descript：音乐控制接口<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/24<p>
 * <p>update time：2018/10/24<p>
 * <p>version：1<p>
 */
public interface MediaPlayerControlerListener {

    /**
     * 初始化播放器
     */
    void initPlayer();

    /**
     * 获取播放器
     */
    Player getPlayer();

    /**
     * 预备播放资源
     * @param sourceUri
     */
    void prepareSource(Uri sourceUri);

    /**
     * 设置播放进度
     */
    void setProgress();

    /**
     * 是否正在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 播放新音乐
     */
    void playNewMusic();

    /**
     * 暂停音乐
     */
    void pauseMusic();

    /**
     * 恢复播放
     */
    void resumeMusic();

    /**
     * 停止音乐
     */
    void stopMusic();

    /**
     * 上一首
     */
    void preMusic();

    /**
     * 下一首
     */
    void nextMusic();

    /**
     * 根据索引播放指定音乐
     * @param index
     */
    void playMusicByIndex(int index);

    /**
     * 触发暂停动作
     */
    void setActionPause(boolean flag);

    /**
     * 重置播放界面
     */
    void resetView();

    /**
     * 释放播放器资源
     */
    void releasePlayerReasoure();

    /**
     * 播放音乐事件回调
     */
    interface OnPlayNewMusicCallback {
        void playnewMusicCallback();
    }

    /**
     * 音乐进度回调
     */
    interface OnProgressMusicCallback {
        void progressMusicCallback();
    }

    /**
     * 暂停音乐事件回调
     */
    interface OnPauseMusicCallback {
        void pauseMusicCallback(int playStatus);
    }

    /**
     * 重播音乐事件回调
     */
    interface OnResumeMusicCallback {
        void resumeMusicCallback();
    }

    /**
     * 停止音乐事件回调
     */
    interface OnStopMusicCallback {
        void stopMusicCallback();
    }

    /**
     * 重置播放界面
     */
    interface OnResetViewCallback {
        void resetViewCallback();
    }

    /**
     * 播放出错回调
     */
    interface OnPlayErrorCallback {
        void playErorCallback();
    }

}
