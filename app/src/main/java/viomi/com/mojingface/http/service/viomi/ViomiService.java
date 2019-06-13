package viomi.com.mojingface.http.service.viomi;

import com.google.gson.JsonObject;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.http.BaseResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: com.viomi.viomifaceinwall.http.service.viomi
 * @ClassName: ViomiService
 * @Description: 云米服务
 * @Author: randysu
 * @CreateDate: 2019/3/4 2:12 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 2:12 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface ViomiService {

    @GET(MirrorConstans.QRCODE_URL)
    Observable<BaseResponse<String>> getQRImg(@Query("type") String type, @Query("clientID") String clientID);

    @POST(MirrorConstans.CHECK_LOGIN)
    Observable<JsonObject> checkLogin(@Body Map<String, String> paramsMap);

}
