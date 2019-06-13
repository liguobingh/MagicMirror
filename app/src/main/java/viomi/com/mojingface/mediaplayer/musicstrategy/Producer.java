package viomi.com.mojingface.mediaplayer.musicstrategy;

/**
 * <p>descript：数据生产者  线程间通信模式为  多生产一消费 <p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/12<p>
 * <p>update time：2018/11/12<p>
 * <p>version：1<p>
 */
public class Producer {

    private MusicInfoStack musicInfoStack;

    public Producer(MusicInfoStack musicInfoStack) {
        super();
        this.musicInfoStack = musicInfoStack;
    }

    public <T> void pushMusic(T t) {
        musicInfoStack.push(t);
    }

    public void deleteCounterInMusicInfoStack() {
        if (musicInfoStack != null) {
            musicInfoStack.deleteCounter();
        }
    }

}
