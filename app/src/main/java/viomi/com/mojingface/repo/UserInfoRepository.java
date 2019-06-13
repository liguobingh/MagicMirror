package viomi.com.mojingface.repo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.repo
 * @ClassName: UserInfoRepository
 * @Description: 用户信息Repository
 * @Author: randysu
 * @CreateDate: 2019/4/2 10:49 AM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 10:49 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class UserInfoRepository {

    private static final String TAG = UserInfoRepository.class.getName();

    private static UserInfoRepository mInstance;

    private Subscription subscription;
    private UserLoginInfoListener listener;

    public static UserInfoRepository getInstance() {
        if (mInstance == null) {
            synchronized (UserInfoRepository.class) {
                if (mInstance == null) {
                    mInstance = new UserInfoRepository();
                }
            }
        }
        return mInstance;
    }

    private UserInfoRepository() {

    }

    public void setUserInfoListener(UserLoginInfoListener listener) {
        this.listener = listener;
    }

    public void onStart() {
        IntentFilter userFliter = new IntentFilter();
        userFliter.addAction(BroadcastAction.LOGIN_SUCCESS);
        userFliter.addAction(BroadcastAction.LOGINOUT);
        MagicMirrorApplication.getAppContext().registerReceiver(userReciver, userFliter);

        subscription = Observable.interval(0, 10, TimeUnit.MINUTES).subscribe(aLong -> {
            UserEntity user = (UserEntity) FileUtil.getObject( MagicMirrorApplication.getAppContext(), MirrorConstans.USERFILENAME);

            if (listener != null) {
                if (user != null) {
                    listener.onLoginSuccess(user);
                } else {
                    listener.onLogout();
                }
            }
        });
    }

    public void onDestroy() {
        MagicMirrorApplication.getAppContext().unregisterReceiver(userReciver);

        subscription.unsubscribe();
    }

    BroadcastReceiver userReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastAction.LOGIN_SUCCESS)) {
                UserEntity user = (UserEntity) FileUtil.getObject(MagicMirrorApplication.getAppContext(), MirrorConstans.USERFILENAME);

                if (listener != null && user != null) {
                    listener.onLoginSuccess(user);
                }
            } else if (intent.getAction().equals(BroadcastAction.LOGINOUT)) {
                if (listener != null) {
                    listener.onLogout();
                }
            }
        }
    };

    public interface UserLoginInfoListener {
        void onLoginSuccess(UserEntity userEntity);
        void onLogout();
    }

}
