package viomi.com.mojingface.service;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import viomi.com.mojingface.SpeechServiceConnection;
import viomi.com.mojingface.base.BaseService;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomi_face
 * @Package: viomi.com.mojingface.service
 * @ClassName: SpeechInitGuardService
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/25 10:09 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/25 10:09 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SpeechInitGuardService extends BaseService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,new Notification());
        bindService(new Intent(this, SpeechInitService.class),
                mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SpeechServiceConnection.Stub() {};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "SpeechInitGuardService  销毁");
        unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接上
            Log.d(TAG,"SpeechInitGuardService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"SpeechInitGuardService:断开链接");
            //断开链接
            startService(new Intent(SpeechInitGuardService.this, SpeechInitService.class));
            //重新绑定
            bindService(new Intent(SpeechInitGuardService.this, SpeechInitService.class),
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

}
