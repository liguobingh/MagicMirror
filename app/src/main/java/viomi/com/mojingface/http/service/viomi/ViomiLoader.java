package viomi.com.mojingface.http.service.viomi;

import com.google.gson.JsonObject;
import viomi.com.mojingface.http.BaseResponse;
import viomi.com.mojingface.http.ObjectLoader;
import viomi.com.mojingface.http.RetrofitServiceManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.http.service.viomi
 * @ClassName: ViomiLoader
 * @Description: 云米Loader
 * @Author: randysu
 * @CreateDate: 2019/3/20 3:24 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/20 3:24 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ViomiLoader extends ObjectLoader {

    private static final String TAG = ViomiLoader.class.getName();

    private ViomiService viomiService;

    public ViomiLoader() {
        viomiService = RetrofitServiceManager.getInstance().create(ViomiService.class);
    }

    public Observable<BaseResponse<String>> getQRImg(String type, String clientID) {
        return observe(viomiService.getQRImg(type, clientID));
    }

    public Observable<JsonObject> checkLogin(String clientID) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("clientID", clientID);
        return observe(viomiService.checkLogin(paramsMap));
    }

}
