package viomi.com.mojingface.http;

import android.util.Log;

import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http
 * @ClassName: HttpCommonInterceptor
 * @Description: 自定义拦截器
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:07 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:07 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class HttpCommonInterceptor implements Interceptor {

    public static final String TAG = HttpCommonInterceptor.class.getName();

    public static final String HTTP_POST_TYPE_TAG = "domain";

    private Map<String,String> mHeaderParamsMap = new HashMap<>();

    public HttpCommonInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("HttpCommonInterceptor","add common params");
        Request oldRequest = chain.request();

        // 动态更改URL，因为
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        List<String> domainHeaderValues = request.headers(HTTP_POST_TYPE_TAG);
        if (domainHeaderValues == null || domainHeaderValues.size() <= 0) {
            return chain.proceed(request);
        }

        LogUtils.i(TAG, "dimain:" + domainHeaderValues.get(0));
        String domain = domainHeaderValues.get(0);

        HttpUrl newBaseUrl;
        HttpUrl oldHttpUrl = request.url();
        LogUtils.i(TAG, "oldHttpUrl:" + oldHttpUrl);
        if ("viomi".equals(domain)) {
            newBaseUrl = HttpUrl.parse(MirrorConstans.VIOMI_BACKEND_BASEURL);
        } else if ("ideacode".equals(domain)) {
            newBaseUrl = HttpUrl.parse(MirrorConstans.IDEACODE_BACKEND_BASEURL);
        } else {
            newBaseUrl = oldHttpUrl;
        }
        if (newBaseUrl == null)
            throw new IllegalArgumentException("New Request Url is null.");

        HttpUrl newFullUrl = oldHttpUrl
                .newBuilder()
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .scheme(newBaseUrl.scheme())
                .build();
        LogUtils.d(TAG, newFullUrl.url().toString());

        // 新的请求
        Request.Builder requestBuilder =  oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());
        //添加公共参数,添加到header中
        if(mHeaderParamsMap.size() > 0){
            for(Map.Entry<String,String> params:mHeaderParamsMap.entrySet()){
                requestBuilder.header(params.getKey(),params.getValue());
            }
        }

        return chain.proceed(builder.url(newFullUrl).build());
    }

    public static class Builder{
        HttpCommonInterceptor mHttpCommonInterceptor;

        public Builder(){
            mHttpCommonInterceptor = new HttpCommonInterceptor();
        }

        public Builder addHeaderParams(String key, String value){
            mHttpCommonInterceptor.mHeaderParamsMap.put(key,value);
            return this;
        }

        public Builder  addHeaderParams(String key, int value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, float value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, long value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, double value){
            return addHeaderParams(key, String.valueOf(value));
        }


        public HttpCommonInterceptor build(){
            return mHttpCommonInterceptor;
        }
    }
}
