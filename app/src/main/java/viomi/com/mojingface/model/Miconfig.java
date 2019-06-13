package viomi.com.mojingface.model;


import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.SharedPreferencesUtil;

public class Miconfig {

    private static final long OAUTH_ANDROID_APP_ID = 2882303761517454408L;
    private static final String OAUTH_ANDROID_APP_KEY = "5891745422408";

    private static final long OAUTH_IOS_APP_ID = 2882303761517484785L;// 云米 ios
    private static final String OAUTH_IOS_APP_KEY = "5701748476785";

    public static long getMI_OAUTH_APP_ID() {
        return "ios".equals(SharedPreferencesUtil.getData(MirrorConstans.SCANTYPE_KEY, "ios")) ? OAUTH_IOS_APP_ID : OAUTH_ANDROID_APP_ID;
    }

    public static String getMI_OAUTH_APP_KEY() {
        return "ios".equals(SharedPreferencesUtil.getData(MirrorConstans.SCANTYPE_KEY, "ios")) ? OAUTH_IOS_APP_KEY : OAUTH_ANDROID_APP_KEY;
    }
}
