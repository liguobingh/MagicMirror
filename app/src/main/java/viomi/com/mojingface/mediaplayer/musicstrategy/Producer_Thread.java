package viomi.com.mojingface.mediaplayer.musicstrategy;

import com.rich.czlylibary.bean.MiguAppContentItem;
import com.rich.czlylibary.bean.MusicInfo;
import com.rich.czlylibary.bean.MusicinfoResult;
import com.rich.czlylibary.bean.SongNew;
import com.rich.czlylibary.sdk.HttpClientManager;
import com.rich.czlylibary.sdk.ResultCallback;
import viomi.com.mojingface.util.LogUtils;

import java.util.List;


/**
 * <p>descript：生产者<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/10<p>
 * <p>update time：2018/11/10<p>
 * <p>version：1<p>
 */
public class Producer_Thread extends Thread {

    private static final String TAG = Producer_Thread.class.getName();

    private Producer producer;
    private List musicInfoList;

    public Producer_Thread(Producer producer, List musicInfoList) {
        super();
        this.producer = producer;
        this.musicInfoList = musicInfoList;
    }

    @Override
    public void run() {
        super.run();

        for (Object object : musicInfoList) {
            String musicId = "";
            if (object instanceof MusicInfo) {
                musicId = ((MusicInfo)object).getMusicId();
            } else if (object instanceof SongNew) {
                SongNew songNew = (SongNew)object;
                if (songNew.getFullSongs().length > 0) {
                    LogUtils.i(TAG, "musicCopyrightId:" + songNew.getFullSongs()[0].getCopyrightId());
                    musicId = songNew.getFullSongs()[0].getCopyrightId();
                } else {
                    continue;
                }
            } else if (object instanceof MiguAppContentItem) {
                MiguAppContentItem item = (MiguAppContentItem)object;
                LogUtils.i(TAG, "contentId:" + item.getContentId());
                musicId = item.getContentId();
            }
            LogUtils.i(TAG, "musicId:" + musicId);
            HttpClientManager.findMusicInfoByid(musicId, new ResultCallback<MusicinfoResult>() {
                @Override
                public void onStart() {
                    LogUtils.i(TAG, "onStart");
                }

                @Override
                public void onSuccess(MusicinfoResult musicinfoResult) {
                    MusicInfo musicInfo = musicinfoResult.getMusicInfo();
                    LogUtils.i(TAG, "getMusicId:" + musicInfo.getMusicId());
                    LogUtils.i(TAG, "getMusicName:" + musicInfo.getMusicName());
                    LogUtils.i(TAG, "getSingerName:" + musicInfo.getSingerName());
                    LogUtils.i(TAG, "getPicUrl:" + musicInfo.getPicUrl());
                    LogUtils.i(TAG, "getListenUrl:" + musicInfo.getListenUrl());
                    LogUtils.i(TAG, "getAlbums:" + musicInfo.getLrcUrl());

                    producer.pushMusic(musicinfoResult.getMusicInfo());
                }

                @Override
                public void onFailed(String s, String s1) {
                    LogUtils.i(TAG, "onFailed s:" + s + "  s1:" + s1);
                    producer.deleteCounterInMusicInfoStack();
                }

                @Override
                public void onFinish() {
                    LogUtils.i(TAG, "onFinish");
                }
            });
        }

    }
}
