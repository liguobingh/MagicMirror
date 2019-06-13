package viomi.com.mojingface.viewmodel;

import android.app.Application;

import viomi.com.mojingface.util.ObservableUtil;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.viewmodel
 * @ClassName: WelcomeViewModel
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/19 3:00 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 3:00 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class WelcomeViewModel extends BaseAndroidViewModel {



    public WelcomeViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<Long> delayObserver(long delayTime, TimeUnit unit) {
        return ObservableUtil.createObserver().delay(delayTime, unit);
    }
}
