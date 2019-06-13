package viomi.com.mojingface.mediaplayer.musicstrategy;


import viomi.com.mojingface.mediaplayer.Track;

import java.util.List;

/**
 * <p>descript：抽象策略类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public interface Strategy<T> {

    /**
     * 对完返回歌曲列表
     * @return
     */
    void getPlayList(MiguMusicApiCallback callback, String... searchKey);

    List<Track> parseMusicList(T t);

}
