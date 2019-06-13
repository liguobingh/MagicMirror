package viomi.com.mojingface.config;

import android.os.Handler;
import android.os.Message;

import viomi.com.mojingface.util.LogUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;



/**
 * <p>descript：网络管理类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/12/6<p>
 * <p>update time：2018/12/6<p>
 * <p>version：1<p>
 */
public class NetManager {

    private static final String TAG = NetManager.class.getName();

    private CallbackHandler handler;
    private Comparable<Boolean> comparable;

    static class CallbackHandler extends Handler {

        private WeakReference<NetManager> netManagerWeakReference;

        public CallbackHandler(NetManager netManager) {
            netManagerWeakReference = new WeakReference<>(netManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (netManagerWeakReference.get() != null) {
                NetManager netManager = netManagerWeakReference.get();
                Comparable<Boolean> comparable = netManager.comparable;
                if (comparable != null) {
                    LogUtils.i(TAG, "result:" + (msg.arg1 == 0));
                    comparable.compareTo(msg.arg1 == 0);
                }
            }
        }
    }

    public NetManager() {
        handler = new CallbackHandler(this);
    }

    /**
     * 检查互联网地址是否可以访问
     *
     * @param address  要检查的域名或IP地址
     * @param callback 检查结果回调（是否可以ping通地址）
     */
    public void isNetWorkAvailable(final String address, final Comparable<Boolean> callback) {
        this.comparable = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                Message msg = new Message();
                try {
                    Process pingProcess = runtime.exec("ping -c 3 -w 500 " + address);
                    InputStreamReader isr = new InputStreamReader(pingProcess.getInputStream());
                    BufferedReader buf = new BufferedReader(isr);
                    if (buf.readLine() == null) {
                        msg.arg1 = -1;
                    } else {
                        msg.arg1 = 0;
                    }
                    buf.close();
                    isr.close();
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    runtime.gc();
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * 检查互联网地址是否可以访问-使用get请求
     *
     * @param urlStr   要检查的url
     * @param callback 检查结果回调（是否可以get请求成功）{@see java.lang.Comparable<T>}
     */
    public void isNetWorkAvailableOfGet(final String urlStr, final Comparable<Boolean> callback) {
        this.comparable = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Connection conn = new Connection(urlStr);
                    Thread thread = new Thread(conn);
                    thread.start();
                    thread.join(3 * 1000); // 设置等待DNS解析线程响应时间为3秒
                    int resCode = conn.get(); // 获取get请求responseCode
                    msg.arg1 = resCode == 200 ? 0 : -1;
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * HttpURLConnection请求线程
     */
    private static class Connection implements Runnable {
        private String urlStr;
        private int responseCode;

        public Connection(String urlStr) {
            this.urlStr = urlStr;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                set(conn.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(int responseCode) {
            this.responseCode = responseCode;
        }

        public synchronized int get() {
            return responseCode;
        }
    }

    /**
     * 检查互联网地址是否可以访问-使用DNS解析
     *
     * @param hostname   要检查的域名或IP
     * @param callback 检查结果回调（是否可以解析成功）{@see java.lang.Comparable<T>}
     */
    public  void isNetWorkAvailableOfDNS(final String hostname, final Comparable<Boolean> callback) {
        this.comparable = comparable;
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = new Message();
                try {
                    DNSParse parse = new DNSParse(hostname);
                    Thread thread = new Thread(parse);
                    thread.start();
                    thread.join(3 * 1000); // 设置等待DNS解析线程响应时间为3秒
                    InetAddress resCode = parse.get(); // 获取解析到的IP地址
                    msg.arg1 = resCode == null ? -1 : 0;
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * DNS解析线程
     */
    private static class DNSParse implements Runnable {
        private String hostname;
        private InetAddress address;

        public DNSParse(String hostname) {
            this.hostname = hostname;
        }

        @Override
        public void run() {
            try {
                set(InetAddress.getByName(hostname));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(InetAddress address) {
            this.address = address;
        }

        public synchronized InetAddress get() {
            return address;
        }
    }

}
