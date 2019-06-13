package viomi.com.mojingface.config;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import viomi.com.mojingface.util.LogUtils;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.config
 * @ClassName: LbsManager
 * @Description: LBS定位管理
 * @Author: randysu
 * @CreateDate: 2019/3/2 3:07 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 3:07 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LbsManager {

    private static final String TAG = LbsManager.class.getName();

    private static LbsManager instance;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private OnLocationChangedCallback callback;

    private LbsManager() {

    }

    public static synchronized LbsManager getInstance() {
        if (instance == null) {
            synchronized (LbsManager.class) {
                if (instance == null) {
                    instance = new LbsManager();
                }
            }
        }
        return instance;
    }

    public void initLocation(Context context) {
        //初始化client
        locationClient = new AMapLocationClient(context);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    public void setCallback(OnLocationChangedCallback callback) {
        this.callback = callback;
    }

    /**
     * 默认的定位参数
     *
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(4 * 60 * 60 * 1000);//可选，设置定位间隔。4小时定位一次
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
//        mOption.setGeoLanguage(GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogUtils.i(TAG, "AMapLocation:" + aMapLocation.toString());
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (callback != null) {
                        callback.onLocationChangedSuccess(aMapLocation);
                    }
                } else {
                    if (callback != null) {
                        callback.onLocationChangedFail();
                    }
                }
            } else {
                if (callback != null) {
                    callback.onLocationChangedFail();
                }
            }
        }
    };


    /**
     * 开始获取定位数据
     */
    public void startLocation() {
        if (null != locationClient) {
            locationClient.startLocation();
        }
    }

    /**
     * 关闭定位
     */
    public void stopLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
        }
    }

    /**
     * 注销实例
     */
    public void destoryLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    public interface OnLocationChangedCallback {
        void onLocationChangedSuccess(AMapLocation aMapLocation);

        void onLocationChangedFail();
    }


}
