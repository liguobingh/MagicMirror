package viomi.com.mojingface.service;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.viomi.speech.manager.ViomiSpeechConfig;
import com.viomi.speech.manager.ViomiSpeechManager;

import java.util.Calendar;

import viomi.com.mojingface.SpeechServiceConnection;
import viomi.com.mojingface.base.BaseService;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.model.MiIndentify;
import viomi.com.mojingface.model.Miconfig;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.speech.SpeechApiImpl;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SystemUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomi_face
 * @Package: viomi.com.mojingface.service
 * @ClassName: SpeechInitService
 * @Description: 启动语音服务
 * @Author: randysu
 * @CreateDate: 2019/3/25 10:03 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/25 10:03 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SpeechInitService extends BaseService {

    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.i(TAG, "initSpeech");
        initSpeech();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG, "onStartCommand");

        startForeground(1, new Notification());
        bindService(new Intent(this, SpeechInitGuardService.class),
                mServiceConnection, Context.BIND_IMPORTANT);

        getLock();

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

        Log.i(TAG, "SpeechInitService  销毁");

        releaseLock();
        unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //链接上
            Log.d(TAG,"SpeechInitService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"SpeechInitService:断开链接");
            //断开链接
            startService(new Intent(SpeechInitService.this, SpeechInitGuardService.class));
            //重新绑定
            bindService(new Intent(SpeechInitService.this, SpeechInitGuardService.class),
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

    private void initSpeech() {
        MiIndentify miIndentify = SystemUtil.getMiIdentify();

        ViomiSpeechConfig.Builder builder = ViomiSpeechConfig.newBuilder();
        builder.setBranch("prod")
                .setDebug(false)
                .setDeviceDebug(false)
                .setLog(true)
                .setDid(miIndentify.did)
                .setMac(miIndentify.mac)
                .setModel("com.viomi.magicmirror")
                .setType(ViomiSpeechConfig.Type.DEVICE_MAGIC_MIRROR);
        ViomiSpeechConfig config = builder.build();
        SpeechApiImpl speechApi = new SpeechApiImpl();
        ViomiSpeechManager.getInstance().init(this, config, speechApi);

        UserEntity user = (UserEntity) FileUtil.getObject(this, MirrorConstans.USERFILENAME);
        if (user != null) {
            ViomiSpeechManager.getInstance().saveUserInfo(user.getMiId(), user.getViomiUserId(),
                    String.valueOf(Miconfig.getMI_OAUTH_APP_ID()),
                    user.getViomiToken());
        }

    }

    private synchronized void getLock() {
        if(mWakeLock==null){
            PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, SpeechInitService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((System.currentTimeMillis()));
            int hour =c.get(Calendar.HOUR_OF_DAY);
            if(hour>=23||hour<=6){
                mWakeLock.acquire(5000);
            }else{
                mWakeLock.acquire(300000);
            }
        }
    }

    private synchronized void releaseLock() {
        if(mWakeLock!=null){
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
                Log.v(TAG,"release lock");
            }

            mWakeLock=null;
        }
    }

}
