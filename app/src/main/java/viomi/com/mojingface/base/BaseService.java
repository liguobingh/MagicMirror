package viomi.com.mojingface.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import viomi.com.mojingface.util.LogUtils;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.base
 * @ClassName: BaseService
 * @Description: 服务基类
 * @Author: randysu
 * @CreateDate: 2019/4/2 2:31 PM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 2:31 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseService extends Service {

    protected  String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i(TAG, "onBind");

        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i(TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogUtils.i(TAG, "onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
