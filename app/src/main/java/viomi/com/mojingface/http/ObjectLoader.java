package viomi.com.mojingface.http;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http
 * @ClassName: ObjectLoader
 * @Description: 将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:27 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:27 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ObjectLoader {

    /**
     *
     * @param observable
     * @param <T>
     * @return
     */
    protected  <T> Observable<T> observe(Observable<T> observable){
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
