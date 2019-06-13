package viomi.com.mojingface.config;


import android.util.Log;

import com.rich.czlylibary.sdk.MiguCzlySDK;
import com.viomi.speech.util.SignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rx.functions.Action1;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.http.Fault;
import viomi.com.mojingface.http.service.ideacode.DeviceRegisteRequest;
import viomi.com.mojingface.http.service.ideacode.IdeacodeLoader;
import viomi.com.mojingface.http.service.migu.MiguLoader;
import viomi.com.mojingface.http.service.migu.MiguReportRequest;
import viomi.com.mojingface.http.service.migu.MiguReportResponse;
import viomi.com.mojingface.model.MiIndentify;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SystemUtil;
/**
 * <p>descript：设备上报服务<p>
 * <p>author：randysu<p>
 * <p>create time：2018/12/18<p>
 * <p>update time：2018/12/18<p>
 * <p>version：1<p>
 */
public class DeviceReportManager {

    private static final String TAG = DeviceReportManager.class.getName();

    private IdeacodeLoader ideacodeLoader;
    private MiguLoader miguLoader;

    private static DeviceReportManager mInstance;

    public static DeviceReportManager getInstance() {
        if (mInstance == null) {
            synchronized (DeviceReportManager.class) {
                if (mInstance == null) {
                    mInstance = new DeviceReportManager();
                }
            }
        }
        return mInstance;
    }

    private DeviceReportManager() {
        ideacodeLoader = new IdeacodeLoader();
        miguLoader = new MiguLoader();
    }

    /**
     * 上报设备信息
     *
     */
    public void registDeviceInfo(double longitude, double latitude, String address) {

        MiIndentify miIndentify = SystemUtil.getMiIdentify();
        DeviceRegisteRequest registeRequest = new DeviceRegisteRequest();
        registeRequest.setLongitude(longitude);
        registeRequest.setLatitude(latitude);
        registeRequest.setCity_name(address);
        registeRequest.setDevice_no(android.os.Build.SERIAL);
        registeRequest.setDevice_did(miIndentify.did);
        registeRequest.setApk_version_code(SystemUtil.getVersionCode(MagicMirrorApplication.getAppContext()) + "");
        registeRequest.setApk_version_name(SystemUtil.getVersionName(MagicMirrorApplication.getAppContext()));
        registeRequest.setRom_version_name(SystemUtil.getRomVersionDetail());
        registeRequest.setAndroid_version(SystemUtil.getSystemVersion());

        MagicMirrorConfig.getInstance().getExecutors().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                ideacodeLoader.reportDevice(registeRequest).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.i(TAG, "设备上报成功");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.i(TAG, "设备上报失败");
                    }
                });
            }
        });
    }

    public void registMusicPlantform() {
        Map<String, String> params = new HashMap<>();

        long now = System.currentTimeMillis();
        LogUtils.i(TAG, "timestamp:{" + String.valueOf(now) + "}");

        String noise = UUID.randomUUID().toString().replace("-", "");
        LogUtils.i(TAG, "noise:{" + noise + "}");

        params.put("VIOMI-App-Key", MirrorConstans.SERVER_APP_KEY);
        params.put("VIOMI-Access-Key-Id", MirrorConstans.SERVER_ACCESSKEYID);
        params.put("VIOMI-Timestamp", String.valueOf(now));
        params.put("VIOMI-Noise", noise);

        String sign = SignUtil.sign("post", params, MirrorConstans.SERVER_ACCESSKEYSECRET);
        LogUtils.i(TAG, "sign:{" + sign + "}");

        try {
            String deviceId = SystemUtil.getPhoneMac(MagicMirrorApplication.getAppContext()).replace(":", "");
            LogUtils.i(TAG, "deviceId:" + deviceId);

            MagicMirrorConfig.getInstance().getExecutors().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (null != miguLoader) {
                        MiguReportRequest reportRequest = new MiguReportRequest();
                        reportRequest.setChCode("dbdfec221e93a715");
                        reportRequest.setSmartDeviceId(deviceId);
                        reportRequest.setToneQuality("1");
                        reportRequest.setOpenTime("365");

                        miguLoader.reportDevice(String.valueOf(now), noise, sign, reportRequest).subscribe(new Action1<MiguReportResponse>() {
                            @Override
                            public void call(MiguReportResponse miguReportResponse) {
                                Log.e(TAG,"MiguReportResponse:" + miguReportResponse.getResCode());

                                if ("000000".equals(miguReportResponse.getResCode()) || "SDM000010".equals(miguReportResponse.getResCode())) {
                                    MiguCzlySDK migu = MiguCzlySDK.getInstance().init(MagicMirrorApplication.getAppContext());
                                    migu.setSmartDeviceId(deviceId);//设置设备号（必填） 这是测试的设备号
                                    migu.setUid("");//设置用户ID（非必填）
                                    migu.setKey("dbdfec221e93a715");//设置渠道号（必填）测试取到：246e74c078e79adf  正式取到：dbdfec221e93a715
                                    migu.setPhoneNum("");//设置手机号（非必填）

                                    LogUtils.i(TAG, "咪咕音乐初始化成功");

                                    MirrorConstans.HASREPORTTOMUSICPLANTFORM = true;
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG,"error message:"+throwable.getMessage());
                                if(throwable instanceof Fault){
                                    Fault fault = (Fault) throwable;

                                    MirrorConstans.HASREPORTTOMUSICPLANTFORM = false;
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
