package viomi.com.mojingface.config;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SharedPreferencesUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.config
 * @ClassName: MagicMirrorConfig
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/19 11:50 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 11:50 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MagicMirrorConfig {

    private static final String TAG = MagicMirrorConfig.class.getName();

    private AppExecutors executors;

    private static MagicMirrorConfig instance;

    public static MagicMirrorConfig getInstance() {
        if (instance == null) {
            synchronized (MagicMirrorConfig.class) {
                if (instance == null) {
                    instance = new MagicMirrorConfig();
                }
            }
        }
        return instance;
    }

    private MagicMirrorConfig() {

    }

    public void init() {
        LogUtils.i(TAG, "全局功能初始化");
        executors = new AppExecutors();
        SharedPreferencesUtil.getInstance(MagicMirrorApplication.getAppContext(), MirrorConstans.SHAREPREFERENCE_FILE_NAME);
    }

    public AppExecutors getExecutors() {
        return executors;
    }

}
