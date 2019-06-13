package viomi.com.mojingface.http.service.ideacode;

import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.http.HttpCommonInterceptor;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: com.viomi.viomifaceinwall.http.service.ideacode
 * @ClassName: IdeacodeService
 * @Description: Ideacode的服务
 * @Author: randysu
 * @CreateDate: 2019/3/4 2:16 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 2:16 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IdeacodeService {

    @Headers(HttpCommonInterceptor.HTTP_POST_TYPE_TAG + ":ideacode")
    @POST(MirrorConstans.IDEACODE_DEVICE_REPORT_URL)
    Observable<String> reportDevice(@Body DeviceRegisteRequest registeRequest);

}
