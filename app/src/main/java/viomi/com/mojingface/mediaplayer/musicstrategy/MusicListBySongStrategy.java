package viomi.com.mojingface.mediaplayer.musicstrategy;

import com.rich.czlylibary.bean.MusicInfo;
import com.rich.czlylibary.bean.SearchMusic;
import com.rich.czlylibary.sdk.HttpClientManager;
import com.rich.czlylibary.sdk.ResultCallback;
import viomi.com.mojingface.mediaplayer.Track;
import viomi.com.mojingface.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>descript：根据歌名查询歌曲<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public class MusicListBySongStrategy implements Strategy<SearchMusic> {

    private static final String TAG = MusicListBySongStrategy.class.getName();

    private int getMusicInfoCount;

    @Override
    public void getPlayList(MiguMusicApiCallback callback, String... searchKey) {
        if (searchKey == null || searchKey.length < 1) {
            return;
        }

        LogUtils.i(TAG, "searchKey:" + searchKey[0]);

        HttpClientManager.searchMusicByKeyByApi2(searchKey[0], "2", 1, 20, new ResultCallback<SearchMusic>() {
            @Override
            public void onStart() {
                LogUtils.i(TAG, "onStart");
            }

            @Override
            public void onSuccess(SearchMusic searchMusic) {
                LogUtils.i(TAG, "count:" + searchMusic.getCount());

                List<Track> playList = parseMusicList(searchMusic);

                if (callback != null) {
                    callback.apiOnSuccess(playList);
                }
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.i(TAG, "onFailed s:" + s + "   s1:" + s1);

                getMusicInfoCount++;
                if (getMusicInfoCount <= 5) {
                    getPlayList(callback, searchKey);
                } else {
                    getMusicInfoCount = 0;
                    if (callback != null) {
                        callback.apiOnFailed(s, s1);
                    }
                }
            }

            @Override
            public void onFinish() {
                LogUtils.i(TAG, "onFinish");

                if (callback != null) {
                    callback.apiOnFinish();
                }
            }
        });
    }

    @Override
    public List<Track> parseMusicList(SearchMusic searchMusic) {
        if (searchMusic == null) {
            return new ArrayList<>();
        }

        List<Track> returnList = new ArrayList<>();

        for (MusicInfo musicInfo : searchMusic.getMusicInfos()) {
            LogUtils.i(TAG, "musicId:" + musicInfo.getMusicId());
            LogUtils.i(TAG, "musicName:" + musicInfo.getMusicName());
            LogUtils.i(TAG, "musicPicUrl:" + musicInfo.getPicUrl());
            LogUtils.i(TAG, "musicListenUrl:" + musicInfo.getListenUrl());
            LogUtils.i(TAG, "musicLrc:" + musicInfo.getLrcUrl());

            Track track = new Track();
            track.setTitle(musicInfo.getMusicName());
            track.setSubTitle(musicInfo.getSingerName());
            track.setLinkUrl(musicInfo.getListenUrl());
            track.setImageUrl(musicInfo.getPicUrl());
            track.setLrcUrl(musicInfo.getLrcUrl());

            returnList.add(track);
        }

        return returnList;
    }
}
