package viomi.com.mojingface.mediaplayer.musicstrategy;

import android.text.TextUtils;

import com.rich.czlylibary.bean.MusicInfo;
import com.rich.czlylibary.bean.SearchSongNewResult;
import com.rich.czlylibary.bean.SongNew;
import com.rich.czlylibary.sdk.HttpClientManager;
import com.rich.czlylibary.sdk.ResultCallback;
import viomi.com.mojingface.mediaplayer.Track;
import viomi.com.mojingface.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>descript：根据歌手查询歌曲<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/12<p>
 * <p>update time：2018/11/12<p>
 * <p>version：1<p>
 */
public class MusicListBySingerStrategy implements Strategy<List<MusicInfo>> {

    private static final String TAG = MusicListBySingerStrategy.class.getName();

    private int getMusicInfoCount;

    @Override
    public void getPlayList(MiguMusicApiCallback callback, String... searchKey) {
        if (searchKey == null || searchKey.length < 1) {
            return;
        }

        LogUtils.i(TAG, "searchKey:" + searchKey[0]);

        HttpClientManager.searchMusicByKey(searchKey[0], 1, 50, 1, 1, 1, new ResultCallback<SearchSongNewResult>() {
            @Override
            public void onStart() {
                LogUtils.i(TAG, "onStart");

                if (callback != null) {
                    callback.apiOnStart();
                }
            }

            @Override
            public void onSuccess(SearchSongNewResult searchSongNewResult) {
                LogUtils.i(TAG, "total:" + searchSongNewResult.getSearchSong().getData().getTotal());

                MusicInfoStack musicInfoStack = new MusicInfoStack(searchSongNewResult.getSearchSong().getData().getResult().length);

                List<SongNew> songNewList = new ArrayList<>();
                for (SongNew songNew : searchSongNewResult.getSearchSong().getData().getResult()) {
                    LogUtils.i(TAG, "musicId:" + songNew.getId());
                    LogUtils.i(TAG, "musicName:" + songNew.getName());
                    LogUtils.i(TAG, "musicSingerName:" + songNew.getSingerName());
                    LogUtils.i(TAG, "musicPlayUrl:" + songNew.getSuggestPlayUrl());
                    LogUtils.i(TAG, "musicLrcUrl:" + songNew.getLyricUrl());

                    songNewList.add(songNew);
                }

                Producer producer = new Producer(musicInfoStack);
                Producer_Thread producer_thread = new Producer_Thread(producer, songNewList);
                producer_thread.start();

                Consumer consumer = new Consumer(musicInfoStack, new Consumer.OnGetMusicInfosCallback<MusicInfo>() {
                    @Override
                    public void musicInfosCallback(List<MusicInfo> musicInfos) {
                        List<MusicInfo> realMusicInfos = new ArrayList();
                        for (MusicInfo musicInfo : musicInfos) {
                            LogUtils.i(TAG, "sheetInfosCallback musicId:" + musicInfo.getMusicId());
                            LogUtils.i(TAG, "sheetInfosCallback musicName:" + musicInfo.getMusicName());
                            LogUtils.i(TAG, "sheetInfosCallback musicSinger:" + musicInfo.getSingerName());
                            LogUtils.i(TAG, "sheetInfosCallback musicPicUrl:" + musicInfo.getPicUrl());
                            LogUtils.i(TAG, "sheetInfosCallback musicListenUrl:" + musicInfo.getListenUrl());
                            LogUtils.i(TAG, "sheetInfosCallback musicLrc:" + musicInfo.getLrcUrl());

                            if (!TextUtils.isEmpty(musicInfo.getListenUrl())) {
                                realMusicInfos.add(musicInfo);
                            }
                        }

                        for (MusicInfo musicInfo : realMusicInfos) {
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicId:" + musicInfo.getMusicId());
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicName:" + musicInfo.getMusicName());
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicSinger:" + musicInfo.getSingerName());
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicPicUrl:" + musicInfo.getPicUrl());
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicListenUrl:" + musicInfo.getListenUrl());
                            LogUtils.i(TAG, "sheetInfosCallback  real  musicLrc:" + musicInfo.getLrcUrl());
                        }

                        List<Track> trackList = parseMusicList(realMusicInfos);

                        if (callback != null) {
                            callback.apiOnSuccess(trackList);
                        }
                    }
                });
                Consumer_Thread consumer_thread = new Consumer_Thread(consumer);
                consumer_thread.start();
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.i(TAG, "onFailed s:" + s + "  s1:" + s1);

                getMusicInfoCount++;
                if (getMusicInfoCount <= 5) {
                    getPlayList(callback, searchKey[0]);
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
    public List<Track> parseMusicList(List<MusicInfo> musicInfoList) {
        if (musicInfoList == null) {
            return new ArrayList<>();
        }

        List<Track> returnList = new ArrayList<>();

        for (MusicInfo musicInfo : musicInfoList) {
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
