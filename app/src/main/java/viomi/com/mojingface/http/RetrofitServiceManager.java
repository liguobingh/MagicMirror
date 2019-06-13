package viomi.com.mojingface.http;

import viomi.com.mojingface.config.MirrorConstans;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http
 * @ClassName: RetrofitServiceManager
 * @Description: Retrofit + okhttp + rxjava
 * @Author: randysu
 * @CreateDate: 2019/3/2 4:57 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 4:57 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class RetrofitServiceManager {

    private static final String TAG = RetrofitServiceManager.class.getName();

    private static final int DEFAULT_TIME_OUT = 20; //20秒超时
    private static final int DEFAULT_READ_WRITE_TIME_OUT = 20;

    private Retrofit mRetrofit;

    private RetrofitServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                .build();
        builder.addInterceptor(commonInterceptor);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MirrorConstans.VIOMI_BACKEND_BASEURL)
                .build();
    }

    private static RetrofitServiceManager mInstance;

    public static RetrofitServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitServiceManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

}
