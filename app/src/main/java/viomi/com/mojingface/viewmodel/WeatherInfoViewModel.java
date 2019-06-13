package viomi.com.mojingface.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.model.WeatherBean;
import viomi.com.mojingface.repo.WeatherRepository;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SharedPreferencesUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.viewmodel
 * @ClassName: WeatherInfoViewModel
 * @Description: 天气viewmodel
 * @Author: randysu
 * @CreateDate: 2019/3/21 5:47 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/21 5:47 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class WeatherInfoViewModel extends BaseAndroidViewModel {

    private WeatherRepository weatherRepository;
    private MutableLiveData<WeatherBean> weatherLiveData = new MutableLiveData<>();

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);

        weatherRepository = WeatherRepository.getInstance();
        weatherRepository.setOnWeatherCallbackListener(new WeatherRepository.WeatherCallbackListener() {
            @Override
            public void onGetWeatherSuccess() {
                LogUtils.i(TAG, "获取天气成功");

                String weatherJson = (String) SharedPreferencesUtil.getData(MirrorConstans.WEATHERJSON_KEY, "");
                if (!TextUtils.isEmpty(weatherJson)) {
                    WeatherBean bean = JSON.parseObject(weatherJson, new TypeReference<WeatherBean>() {
                    });
                    weatherLiveData.postValue(bean);
                }
            }

            @Override
            public void onGetWeatherFail() {
                LogUtils.i(TAG, "获取天气失败");
            }

            @Override
            public void onGetLocationFail() {
                LogUtils.i(TAG, "获取地址失败");
            }
        });
        weatherRepository.startLbs();

    }

    public LiveData<WeatherBean> getWeatherInfo() {
        String weatherJson = (String) SharedPreferencesUtil.getData(MirrorConstans.WEATHERJSON_KEY, "");
        if (!TextUtils.isEmpty(weatherJson)) {
            WeatherBean bean = JSON.parseObject(weatherJson, new TypeReference<WeatherBean>() {
            });
            weatherLiveData.postValue(bean);
        }
        return weatherLiveData;
    }


}
