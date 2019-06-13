package viomi.com.mojingface.config;

import viomi.com.mojingface.BuildConfig;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.config
 * @ClassName: MirrorConstans
 * @Description: 配置常量
 * @Author: randysu
 * @CreateDate: 2019/3/19 12:13 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 12:13 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MirrorConstans {

    public final static String NET_ERROR_CODE = "400";
    public final static String NET_RESPONSE_ERROR_CODE = "401";
    public final static String NOMORE_SPACE_ERROR_CODE = "402";

    // SharePreference Key
    public final static String SHAREPREFERENCE_FILE_NAME = "mojingface_sp";

    public final static String USERFILENAME_KEY = "userinfo";
    public final static String FIRSTLAUNCH_KEY = "firstlaunch";
    public final static String SCANTYPE_KEY = "scantype";
    public final static String USERFILENAME = "userfilename";
    public final static String LATITUDE_KEY = "latitude";
    public final static String LONGITUDE_KEY = "longitude";
    public final static String CITY_KEY = "city";
    public final static String WEATHERJSON_KEY = "weatherjson_key";
    public final static String NEWAPKVERSIONNAME_KEY = "newapkversionname_key";
    public final static String NEWAPKVERSIONSIZE_KEY = "newapkversionsize_key";
    public final static String DOWNLOADINGAPK_KEY = "downloadingapk_key";
    public final static String DEBUGMODE_KEY = "debugmode_key";

    // 正式服更新参数 rk芯片
    public final static String UPDATEENVIRONMENT = "viomi_rk";

    public final static String DefaultReply = "抱歉，该功能暂未实现！";

    // URL
    //版本更新检测
    public final static String UPDATE = "http://iot-app.viomi.com.cn/getdata";

    private static final String VMALL_BASEURL_RELEASE = "https://vmall-auth.viomi.com.cn/services";// 扫码登陆商城正式环境
    private static final String VMALL_BASEURL_DEBUG = "https://vwater-auth.viomi.com.cn/services";// 扫码登陆商城测试环境

    private static final String BASEURL = VMALL_BASEURL_RELEASE;

    public static final String QRCODE_URL = BASEURL + "/vmall/login/QRCode.json";// 生成登录二维码
    public static final String CHECK_LOGIN = BASEURL + "/vmall/login/QRCode.json";// 轮询获取二维码登录状态

    public static final String VIOMI_BACKEND_BASEURL = "http://ms.viomi.com.cn";
    public static final String MIGU_REPORT_URL = "/device/im/migu/deviceIdReport";

    public static final String IDEACODE_BACKEND_BASEURL = BuildConfig.IDEACODE_HOST;
    public static final String IDEACODE_DEVICE_REPORT_URL = "/DeviceApi/registDevice";

    // 咪咕平台相关
    public static boolean HASREPORTTOMUSICPLANTFORM = false;
    public static final String SERVER_APP_KEY = "viomi-face";
    public static final String SERVER_ACCESSKEYID = "11GjGGh7Bug8iaoO";
    public static final String SERVER_ACCESSKEYSECRET = "11e3632c1ac611e9ab14d663bd873d93";

}
