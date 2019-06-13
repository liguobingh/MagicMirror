package viomi.com.mojingface.http.service.ideacode;

import viomi.com.mojingface.http.ObjectLoader;
import viomi.com.mojingface.http.RetrofitServiceManager;

import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: com.viomi.viomifaceinwall.http.service.ideacode
 * @ClassName: IdeacodeLoader
 * @Description: Ideacode Loader
 * @Author: randysu
 * @CreateDate: 2019/3/4 2:28 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 2:28 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class IdeacodeLoader extends ObjectLoader {

    private static final String TAG = IdeacodeLoader.class.getName();

    private IdeacodeService ideacodeService;

    public IdeacodeLoader() {
        ideacodeService = RetrofitServiceManager.getInstance().create(IdeacodeService.class);
    }

    public Observable<String> reportDevice(DeviceRegisteRequest registeRequest) {
        return observe(ideacodeService.reportDevice(registeRequest));
    }

}
