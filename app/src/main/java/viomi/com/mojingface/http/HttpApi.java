package viomi.com.mojingface.http;

import android.os.Handler;
import android.os.Message;

import viomi.com.mojingface.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mocc on 2018/1/3
 */

public class HttpApi {

    private static OkHttpClient client;
    private static String TAG = "HttpApi";

    private HttpApi() {
    }

    private static OkHttpClient getClientInstance() {

        if (client == null) {
            synchronized (HttpApi.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }


    //get请求封装1
    public static Call getRequest(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();

        Call call = getClientInstance().newCall(request);
        //发起异步请求
        call.enqueue(callback);

        return call;
    }

    //get请求封装2
    public static Call getRequestParam(String url, Map<String, String> paramsMap, Callback callback) {
        return getRequest(dealUrl(url, paramsMap), callback);
    }


    //get请求封装3
    public static Call getRequestHandler(String url, Map<String, String> paramsMap, final Handler mhandler, final int successCode, final int failCode) {

        if (mhandler == null) {
            LogUtils.e(TAG, "handler传值为null");
            return null;
        }

        Call call = getRequestParam(url, paramsMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseFail(e, mhandler, failCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseSuccess(response, mhandler, successCode);
            }
        });
        return call;
    }

    //post JSON数据请求封装1
    public static Call postRequest(String url, Map<String, String> paramsMap, Callback callback) {
        JSONObject paramsJson = new JSONObject();
        if (paramsMap != null) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                try {
                    paramsJson.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return postRequest(url, paramsJson, callback);
    }

    public static Call postRequest(String url, JSONObject paramsJson, Callback callback) {
        return postRequest(url, null, paramsJson, callback);
    }

    public static Call postRequest(String url, Headers headers, JSONObject paramsJson, Callback callback) {
        MediaType jsonMediaType = MediaType.parse("application/json;charset=utf-8");

        String paramsString = "";
        if (paramsJson != null) {
            paramsString = paramsJson.toString();
        }

        RequestBody requestBody = RequestBody.create(jsonMediaType, paramsString);

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(requestBody);
        if (null != headers) {
            builder.headers(headers);
        }

        Request request = builder.build();
        Call call = getClientInstance().newCall(request);
        //发起异步请求
        call.enqueue(callback);
        return call;
    }

    //post JSON数据请求封装2
    public static Call postRequestHandler(String url, Map<String, String> paramsMap, final Handler mhandler, final int successCode, final int failCode) {

        if (url == null || url.length() == 0 || mhandler == null) {
            return null;
        }

        JSONObject paramsJson = new JSONObject();

        if (paramsMap != null) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                try {
                    paramsJson.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Call call=postRequest(url, paramsJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseFail(e, mhandler, failCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseSuccess(response, mhandler, successCode);
            }
        });
        return call;
    }


    //put JSON数据请求封装1
    public static Call putRequest(String url, JSONObject paramsJson, Callback callback) {
        MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");

        String paramsString = "";
        if (paramsJson != null) {
            paramsString = paramsJson.toString();
        }

        RequestBody requestBody = RequestBody.create(jsonMediaType, paramsString);

        Request request = new Request.Builder().url(url).put(requestBody).build();
        LogUtils.e("request:=",request.toString()+"");

        Call call = getClientInstance().newCall(request);
        call.enqueue(callback);
        return call;
    }



    private static void responseFail(IOException e, Handler mhandler, int failCode) {
        Message msg = mhandler.obtainMessage();
        msg.what = failCode;
        msg.obj = e;
        mhandler.sendMessage(msg);
    }

    private static void responseSuccess(Response response, Handler mhandler, int successCode) throws IOException {
        if (response.isSuccessful()) {
            String result = response.body().string();
            Message msg = mhandler.obtainMessage();
            msg.what = successCode;
            msg.obj = result;
            mhandler.sendMessage(msg);
        }
    }


    //get通用并凑参数方法
    public static String dealUrl(String url, Map<String, String> paramsMap) {

        if (url == null || url.length() == 0) {
            LogUtils.e(TAG, "url传值出错");
            return null;
        }

        if (paramsMap.keySet().size() > 0) {

            String paramsUrl = "";
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                paramsUrl = paramsUrl + "&" + entry.getKey() + "=" + entry.getValue();
            }
            if (paramsUrl.length() > 0) {
                paramsUrl = paramsUrl.substring(1, paramsUrl.length());
            }
            url = url + "?" + paramsUrl;
            LogUtils.e(TAG, "RequstUrl=" + url);
        }
        return url;
    }


    //put请求
    public static String doPut(String url) {

        BufferedReader in = null;
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
