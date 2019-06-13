package viomi.com.mojingface.config;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.config
 * @ClassName: AppExecutors
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/1 2:23 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/1 2:23 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppExecutors {

    /**
     * 核心线程池的数量，同时能够执行的线程数量
     */
    private int corePoolSize;

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    public AppExecutors() {
        /**
         * 给corePoolSize赋值：当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行（有研究论证的）
         */
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;

        this.mDiskIO = Executors.newSingleThreadExecutor();
        this.mNetworkIO = Executors.newFixedThreadPool(corePoolSize);
        this.mMainThread = new MainThreadExecutor();
    }

    public AppExecutors(Executor mDiskIO, Executor mNetworkIO, Executor mMainThread) {
        this.mDiskIO = mDiskIO;
        this.mNetworkIO = mNetworkIO;
        this.mMainThread = mMainThread;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
