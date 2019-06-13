package viomi.com.mojingface.http.service.migu;

import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.http.HttpCommonInterceptor;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http.service
 * @ClassName: MiguService
 * @Description: migu服务
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:36 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:36 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface MiguService {

    @Headers({HttpCommonInterceptor.HTTP_POST_TYPE_TAG + ":viomi", "VIOMI-App-Key:" + MirrorConstans.SERVER_APP_KEY, "VIOMI-Access-Key-Id:" + MirrorConstans.SERVER_ACCESSKEYID})
    @POST(MirrorConstans.MIGU_REPORT_URL)
    Observable<MiguReportResponse> reportDevice(@Header("VIOMI-Timestamp") String timestamp,
                                                @Header("VIOMI-Noise") String noise,
                                                @Header("Authorization") String sign,
                                                @Body MiguReportRequest reportRequest);

}
