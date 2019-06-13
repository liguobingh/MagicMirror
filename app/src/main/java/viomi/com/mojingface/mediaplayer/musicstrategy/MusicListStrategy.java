package viomi.com.mojingface.mediaplayer.musicstrategy;

import com.rich.czlylibary.bean.MusicInfo;
import com.rich.czlylibary.bean.QuerySheetMusicInfo;
import com.rich.czlylibary.sdk.HttpClientManager;
import com.rich.czlylibary.sdk.ResultCallback;
import viomi.com.mojingface.mediaplayer.Track;
import viomi.com.mojingface.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>descript：获取热门歌单列表<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public class MusicListStrategy implements Strategy<QuerySheetMusicInfo> {

    private static final String TAG = MusicListStrategy.class.getName();

    private int getMusicInfoCount;

    @Override
    public void getPlayList(MiguMusicApiCallback callback, String... searchKey) {
        HttpClientManager.findSongByMusicSheetId("1967", "0", "50", new ResultCallback<QuerySheetMusicInfo>() {
            @Override
            public void onStart() {
                LogUtils.i(TAG, "onStart");
                if (callback != null) {
                    callback.apiOnStart();
                }
            }

            @Override
            public void onSuccess(QuerySheetMusicInfo querySheetMusicInfo) {
                LogUtils.i(TAG, "count:" + querySheetMusicInfo.getCount());

                List<Track> playList = parseMusicList(querySheetMusicInfo);

                if (callback != null) {
                    callback.apiOnSuccess(playList);
                }
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.i(TAG, "onFailed s1：" + s1);

                getMusicInfoCount++;
                if (getMusicInfoCount <= 5) {
                    getPlayList(callback, "");
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
    public List<Track> parseMusicList(QuerySheetMusicInfo querySheetMusicInfo) {
        if (querySheetMusicInfo.getMusicInfos() == null) {
            return new ArrayList<>();
        }

        List<Track> returnList = new ArrayList<>();

        for (MusicInfo musicInfo : querySheetMusicInfo.getMusicInfos()) {
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
