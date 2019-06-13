package viomi.com.mojingface.mediaplayer.musicstrategy;

import java.util.List;

/**
 * <p>descript：数据消费者  线程间通信模式为  多生产一消费<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/12<p>
 * <p>update time：2018/11/12<p>
 * <p>version：1<p>
 */
public class Consumer {

    private MusicInfoStack musicInfoStack;
    private OnGetMusicInfosCallback callback;

    public Consumer(MusicInfoStack musicInfoStack, OnGetMusicInfosCallback callback) {
        super();
        this.musicInfoStack = musicInfoStack;
        this.callback = callback;
    }

    public void getList() {
        List musicInfos = musicInfoStack.getList();
        if (callback != null) {
            callback.musicInfosCallback(musicInfos);
        }
    }

    interface OnGetMusicInfosCallback<T> {
        void musicInfosCallback(List<T> musicInfos);
    }

}
