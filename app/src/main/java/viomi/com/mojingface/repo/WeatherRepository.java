package viomi.com.mojingface.repo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.viomi.speech.manager.ViomiSpeechManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.config.DeviceReportManager;
import viomi.com.mojingface.config.LbsManager;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.config.NetManager;
import viomi.com.mojingface.model.WeatherBean;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SharedPreferencesUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.repo
 * @ClassName: WeatherRepository
 * @Description: 天气Repository
 * @Author: randysu
 * @CreateDate: 2019/3/22 10:54 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/22 10:54 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getName();

    private static WeatherRepository mInstance;

    private Subscription subscription;
    private NetManager netManager;
    private LbsManager lbsManager;
    private int locateFailCount = 0;

    private WeatherCallbackListener listener;

    public static WeatherRepository getInstance() {
        if (mInstance == null) {
            synchronized (WeatherRepository.class) {
                if (mInstance == null) {
                    mInstance = new WeatherRepository();
                }
            }
        }
        return mInstance;
    }

    private WeatherRepository() {
        netManager = new NetManager();

        IntentFilter weatherFilter = new IntentFilter(BroadcastAction.VIOCESERVICE_WEATHER);
        MagicMirrorApplication.getAppContext().registerReceiver(weatherReceive, weatherFilter);

        lbsManager = LbsManager.getInstance();
        lbsManager.initLocation(MagicMirrorApplication.getAppContext());
        lbsManager.setCallback(new LbsManager.OnLocationChangedCallback() {
            @Override
            public void onLocationChangedSuccess(AMapLocation aMapLocation) {
                LogUtils.e(TAG, aMapLocation.toString());

                locateFailCount = 0;

                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                String city = aMapLocation.getCity();

                if (latitude > 0 && longitude > 0 && !TextUtils.isEmpty(city)) {

                    LogUtils.e(TAG, "开始上报经纬度   latitude:" + latitude + "  longitude:" + longitude + "  city:" + city);
                    reportDeviceLocationInfo(aMapLocation.getLongitude(), aMapLocation.getLatitude(), aMapLocation.getAddress());

                    SharedPreferencesUtil.putData(MirrorConstans.LATITUDE_KEY, latitude);
                    SharedPreferencesUtil.putData(MirrorConstans.LONGITUDE_KEY, longitude);
                    SharedPreferencesUtil.putData(MirrorConstans.CITY_KEY, city);

                    LogUtils.e(TAG, "city=" + city);

                    ViomiSpeechManager.getInstance().saveLocation(String.valueOf(latitude), String.valueOf(longitude), city);

                    subscription = Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(aLong -> {
                        sendWeatherSpeech();
                    });

                }
            }

            @Override
            public void onLocationChangedFail() {
                locateFailCount++;
                LogUtils.i(TAG, "locateFailCount:" + locateFailCount);
                if (locateFailCount <= 100) {
                    startLbs();
                } else {
                    LogUtils.e(TAG, "定位失败");

                    if (listener != null) {
                        listener.onGetLocationFail();
                    }
                }
            }
        });
    }

    public void startLbs() {
        netManager.isNetWorkAvailable("www.baidu.com", new Comparable<Boolean>() {
            @Override
            public int compareTo(Boolean available) {
                if (available) {
                    MagicMirrorApplication.getAppContext().sendBroadcast(new Intent(BroadcastAction.NETWORK_REAL_CONNECTED));
                    LogUtils.i(TAG, "开始获得地址信息");
                    lbsManager.startLocation();
                } else {
                    LogUtils.i(TAG, "重新获得地址信息");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 每隔3秒重新检查定位信息
                                Thread.sleep(3 * 1000);
                                startLbs();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                return 0;
            }
        });
    }

    private void reportDeviceLocationInfo(double lon, double lat, String address) {
        DeviceReportManager.getInstance().registDeviceInfo(lon, lat, address);
    }

    private BroadcastReceiver weatherReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getParcelable("VOICE_WEATHER") != null) {
                WeatherBean bean = bundle.getParcelable("VOICE_WEATHER");
                LogUtils.e(TAG, "showResult  " + bean.getTemperature());

                Gson gson = new Gson();
                String weatherJson = gson.toJson(bean);
                SharedPreferencesUtil.putData(MirrorConstans.WEATHERJSON_KEY, weatherJson);

                if (listener != null) {
                    listener.onGetWeatherSuccess();
                }
            } else {
                LogUtils.e(TAG, "获取不到天气");
                //获取失败后再检查
                sendWeatherSpeech();

                if (listener != null) {
                    listener.onGetWeatherFail();
                }
            }
        }
    };

    private void sendWeatherSpeech() {
        // 需要一个触发器每隔1秒检查  地址、语音是否都成功
        LogUtils.i(TAG, "ISVOICESERVICECOMPLETED:" + ViomiSpeechManager.getInstance().isAuth());
        String city = (String)SharedPreferencesUtil.getData(MirrorConstans.CITY_KEY, "");
        if (!TextUtils.isEmpty(city) && ViomiSpeechManager.getInstance().isAuth()) {
            LogUtils.i(TAG, "开始获取天气信息");
            ViomiSpeechManager.getInstance().sendText("程序获取后台天气数据" + city);
            subscription.unsubscribe();
        } else {
            LogUtils.i(TAG, "轮训触发条件检查");
        }
    }

    public void setOnWeatherCallbackListener(WeatherCallbackListener listener) {
        this.listener = listener;
    }

    public interface WeatherCallbackListener {
        void onGetWeatherSuccess();
        void onGetWeatherFail();
        void onGetLocationFail();
    }

}
