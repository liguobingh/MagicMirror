package viomi.com.mojingface.mediaplayer.musicstrategy;

/**
 * <p>descript：<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/10<p>
 * <p>update time：2018/11/10<p>
 * <p>version：1<p>
 */
public class Consumer_Thread extends Thread {

    private Consumer consumer;

    public Consumer_Thread(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        super.run();
        consumer.getList();
    }
}
