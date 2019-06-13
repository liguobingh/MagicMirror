package viomi.com.mojingface.mediaplayer.musicstrategy;

import java.util.List;

/**
 * <p>descript：咪咕音乐接口访问callback<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public interface MiguMusicApiCallback<T> {

    void apiOnStart();

    void apiOnSuccess(List<T> list);

    void apiOnFailed(String s, String s1);

    void apiOnFinish();

}
