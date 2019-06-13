package viomi.com.mojingface.util;

/**
 * Created by Mocc on 2018/1/9
 */

public class ResponeCode {

    /*
    * 方便以后统一管理
    * */
    public static boolean isSuccess(int code, String desc, Boolean isShowToast) {

        if (100 == code) {
            return true;
        } else {
            if (isShowToast) {
                ToastUtil.show(desc);
            }
            return false;
        }
    }

    public static void onErrorHint(Object obj) {

        String result = obj.toString();
        if (result == null) {
            result = "";
        }
        int type = 0;

        //网络连接超时
        if (result.contains("SocketTimeoutException")) {
            type = 1;
        }

        //没有网络
        if (result.contains("Network is unreachable")) {
            type = 2;
        }

        //没有网络
        if (result.contains("UnknownHostException")) {
            type = 2;
        }

        //服务器端
        if (result.contains("errorCode")) {
            type = 3;
        }

        switch (type) {
            case 0:
                ToastUtil.show("网络连接失败，请检查网络！");
                break;
            case 1:
                ToastUtil.show("网络连接超时，请重试！");
                break;
            case 2:
                ToastUtil.show("网络连接失败，请检查网络！");
                break;
            case 3:
                ToastUtil.show("加载数据失败，请重试！");
                break;
        }
    }
}
