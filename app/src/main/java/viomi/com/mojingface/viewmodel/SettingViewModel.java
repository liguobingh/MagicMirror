package viomi.com.mojingface.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.repo.UserInfoRepository;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.viewmodel
 * @ClassName: SettingViewModel
 * @Description: 设置页viewmodel
 * @Author: randysu
 * @CreateDate: 2019/4/2 10:45 AM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 10:45 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SettingViewModel extends BaseAndroidViewModel {

    private UserInfoRepository userInfoRepository;
    private MutableLiveData<UserEntity> userEntityLiveData = new MutableLiveData<>();

    public SettingViewModel(@NonNull Application application) {
        super(application);

        userInfoRepository = UserInfoRepository.getInstance();
        userInfoRepository.onStart();
    }

    public LiveData<UserEntity> getUserInfo() {
        userInfoRepository.setUserInfoListener(new UserInfoRepository.UserLoginInfoListener() {
            @Override
            public void onLoginSuccess(UserEntity userEntity) {
                userEntityLiveData.postValue(userEntity);
            }

            @Override
            public void onLogout() {
                userEntityLiveData.postValue(null);
            }
        });

        return userEntityLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        userInfoRepository.onDestroy();
    }
}
