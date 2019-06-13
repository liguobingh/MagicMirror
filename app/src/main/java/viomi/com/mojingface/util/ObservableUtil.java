package viomi.com.mojingface.util;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.util
 * @ClassName: ObservableUtil
 * @Description: Observable工具类
 * @Author: randysu
 * @CreateDate: 2019/3/20 10:32 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/20 10:32 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ObservableUtil {

    private static final String TAG = ObservableUtil.class.getName();

    public static Observable<Long> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long currentTime = getCurrentTime();
                LogUtils.i(TAG, "currentTime:" + currentTime);
                subscriber.onNext(currentTime);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

}
