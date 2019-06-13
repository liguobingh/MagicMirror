package viomi.com.mojingface.mediaplayer.musicstrategy;

/**
 * <p>descript：音乐播放环境角色类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public class MusicContext {

    private Strategy strategy;

    public MusicContext(Strategy strategy) {
        this.strategy = strategy;
    }

    public void getPlayList(MiguMusicApiCallback callback, String... searchKey) {
        strategy.getPlayList(callback, searchKey);
    }

}
