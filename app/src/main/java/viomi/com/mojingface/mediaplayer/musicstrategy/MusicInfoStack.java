package viomi.com.mojingface.mediaplayer.musicstrategy;

import viomi.com.mojingface.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>descript：多生产一消费   音乐实体<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/9<p>
 * <p>update time：2018/11/9<p>
 * <p>version：1<p>
 */
public class MusicInfoStack {

    private static final String TAG = MusicInfoStack.class.getName();

    private int musicListCount = 0;
    private ArrayList list = new ArrayList();

    public MusicInfoStack(int musicListCount) {
        this.musicListCount = musicListCount;
    }

    /**
     * 向列表增加数据
     * @param t
     */
    synchronized public <T> void push(T t) {
        list.add(t);
        this.notify();
        LogUtils.i(TAG, "push= " + list.size() + "  musicListCount:" + musicListCount);
    }

    synchronized public List getList() {
        try {
            while (list.size() < musicListCount) {
                this.wait();
            }
            LogUtils.i(TAG, "list size = " + list.size());

            return list;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * 如果查不到歌曲信息，需要将计数器减1
     */
    synchronized public void deleteCounter() {
        musicListCount--;
    }

}
