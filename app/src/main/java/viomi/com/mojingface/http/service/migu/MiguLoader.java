package viomi.com.mojingface.http.service.migu;

import viomi.com.mojingface.http.ObjectLoader;
import viomi.com.mojingface.http.RetrofitServiceManager;

import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http.service.migu
 * @ClassName: MiguLoader
 * @Description: 咪咕Loader
 * @Author: randysu
 * @CreateDate: 2019/3/4 9:49 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 9:49 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MiguLoader extends ObjectLoader {

    private static final String TAG = MiguLoader.class.getName();

    private MiguService miguService;

    public MiguLoader() {
        miguService = RetrofitServiceManager.getInstance().create(MiguService.class);
    }

    public Observable<MiguReportResponse> reportDevice(String timestamp,
                                                       String noise,
                                                       String sign,
                                                       MiguReportRequest reportRequest) {
        return observe(miguService.reportDevice(timestamp, noise, sign, reportRequest));
    }

}
