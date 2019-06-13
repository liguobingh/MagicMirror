package viomi.com.mojingface.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: viomi.com.viomiaiface.viewmodel
 * @ClassName: BaseAndroidViewModel
 * @Description: ViewModel 基类
 * @Author: randysu
 * @CreateDate: 2019/3/8 7:26 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/8 7:26 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseAndroidViewModel extends AndroidViewModel {

    protected final String TAG = this.getClass().getName();

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);

    }

    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }

    public void clearSubscription() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearSubscription();
    }

}
