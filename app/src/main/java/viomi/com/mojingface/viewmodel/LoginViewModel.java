package viomi.com.mojingface.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.miot.api.MiotManager;
import com.miot.common.config.AppConfiguration;
import com.miot.common.exception.MiotException;
import com.miot.common.people.People;
import com.umeng.analytics.MobclickAgent;
import com.viomi.speech.manager.ViomiSpeechManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MagicMirrorConfig;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.config.XiaomiAccountGetPeopleInfoTask;
import viomi.com.mojingface.http.service.viomi.ViomiLoader;
import viomi.com.mojingface.model.Miconfig;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.ResponeCode;
import viomi.com.mojingface.util.SharedPreferencesUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.viewmodel
 * @ClassName: LoginViewModel
 * @Description: 登录ViewModel
 * @Author: randysu
 * @CreateDate: 2019/3/20 11:30 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/20 11:30 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LoginViewModel extends BaseAndroidViewModel {

    private ViomiLoader viomiLoader;

    private MutableLiveData<String> qrImgLiveData = new MutableLiveData<>();
    private MutableLiveData<UserEntity> userEntityMutableLiveData = new MutableLiveData<>();

    private long checkLoginStartTime = 0L;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        viomiLoader = new ViomiLoader();
    }

    public LiveData<String> getQRImg(String clientID) {
        MagicMirrorConfig.getInstance().getExecutors().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                viomiLoader.getQRImg("1", clientID).subscribe(stringResponseResult -> {
                    LogUtils.i(TAG, "result:" + stringResponseResult.toString());
                    int code = stringResponseResult.getMobBaseRes().getCode();
                    if (code == 100) {
                        LogUtils.i(TAG, stringResponseResult.getMobBaseRes().getResult());
                        qrImgLiveData.postValue(stringResponseResult.getMobBaseRes().getResult());
                    } else {
                        qrImgLiveData.postValue("");
                    }
                }, throwable -> {
                    LogUtils.i(TAG, "获取二维码失败");
                    qrImgLiveData.postValue("");
                });
            }
        });

        return qrImgLiveData;
    }

    public LiveData<UserEntity> checkLogin(String clientID) {
        checkLoginStartTime = System.currentTimeMillis();
        clearSubscription();
        Subscription subscription = Observable.interval(0, 3, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(aLong -> {
            MagicMirrorConfig.getInstance().getExecutors().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    viomiLoader.checkLogin(clientID).subscribe(result -> {
                        LogUtils.i(TAG, "result:" + result);

                        JsonObject mobBaseRes = result.getAsJsonObject("mobBaseRes");

                        int code = JsonUitls.getInt(mobBaseRes, "code");
                        String desc = JsonUitls.getString(mobBaseRes, "desc");

                        if (ResponeCode.isSuccess(code, desc, false)) {
                            clearSubscription();

                            String viomiToken = JsonUitls.getString(mobBaseRes, "token");
                            JsonObject loginData = JsonUitls.getJsonObject(mobBaseRes, "loginData");
                            String viomiUserId = JsonUitls.getString(loginData, "userId");
                            String viomiUserCode = JsonUitls.getString(loginData, "userCode");
                            String weChatHeadImg = JsonUitls.getString(loginData, "headImg");

                            String appendAttr = JsonUitls.getString(mobBaseRes, "appendAttr");
                            JsonObject appendAttrJson = JsonUitls.getJsonObject(appendAttr);

                            String viomiNikeName = JsonUitls.getString(appendAttrJson, "nikeName");
                            String viomiHeadImg = JsonUitls.getString(appendAttrJson, "headImg");
                            String viomiAccount = JsonUitls.getString(appendAttrJson, "account");

                            JsonObject miJson = JsonUitls.getJsonObject(appendAttrJson, "xiaomi");

                            String miId = JsonUitls.getString(miJson, "miId");
                            String miAccessToken = JsonUitls.getString(miJson, "accessToken");
                            String miUserId = JsonUitls.getString(miJson, "userId");
                            String scanType = JsonUitls.getString(miJson, "type");
                            long miExpiresIn_L = JsonUitls.getLong(miJson, "mExpiresIn");

                            String miMacKey = JsonUitls.getString(miJson, "macKey");
                            String miMacAlgorithm = JsonUitls.getString(miJson, "macAlgorithm");

                            // 向VioAI设置UID  MID
                            ViomiSpeechManager.getInstance().saveUserInfo(miId, viomiUserId, String.valueOf(Miconfig.getMI_OAUTH_APP_ID()), viomiToken);

                            // 友盟登录帐号统计
                            MobclickAgent.onProfileSignIn("VIOMI", viomiUserId);
                            MobclickAgent.onProfileSignIn("XIAOMI", miId);

                            SharedPreferencesUtil.putData(MirrorConstans.SCANTYPE_KEY, scanType);

                            AppConfiguration appConfig = new AppConfiguration();
                            appConfig.setAppId(Miconfig.getMI_OAUTH_APP_ID());
                            appConfig.setAppKey(Miconfig.getMI_OAUTH_APP_KEY());
                            MiotManager.getInstance().setAppConfig(appConfig);// 保存配置

                            String miExpiresIn = miExpiresIn_L + "";

                            UserEntity user = new UserEntity(viomiToken, viomiUserId, viomiUserCode, weChatHeadImg, viomiNikeName, viomiHeadImg, viomiAccount, miId, miAccessToken, miUserId, scanType, miExpiresIn, miMacKey, miMacAlgorithm);
                            LogUtils.i(TAG, user.toString());
                            FileUtil.saveObject(MagicMirrorApplication.getAppContext(), MirrorConstans.USERFILENAME, user);

                            // 小米账号相关信息
                            new XiaomiAccountGetPeopleInfoTask(miAccessToken, String.valueOf(miExpiresIn_L), miMacKey, miMacAlgorithm,
                                    new XiaomiAccountGetPeopleInfoTask.Handler() {
                                        @Override
                                        public void onSucceed(People people) {
                                            Log.d(TAG, "XiaomiAccountGetPeopleInfoTask OK");
                                            try {
                                                MiotManager.getPeopleManager().savePeople(people);
                                                LogUtils.e(TAG, "save mi people success!");
                                                userEntityMutableLiveData.postValue(user);
                                            } catch (MiotException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailed() {
                                            Log.d(TAG, "XiaomiAccountGetPeopleInfoTask Failed");
                                            userEntityMutableLiveData.postValue(null);
                                        }
                                    }).execute();
                        }

                        if (915 == code || System.currentTimeMillis() - checkLoginStartTime > 5 * 60 * 1000) {
                            userEntityMutableLiveData.postValue(null);
                            clearSubscription();
                        }

                    }, throwable -> {
                        LogUtils.i(TAG, "检查登录状态失败  " + throwable.getMessage());
                        clearSubscription();
                    });
                }
            });
        });
        addSubscription(subscription);

        return userEntityMutableLiveData;
    }

}
