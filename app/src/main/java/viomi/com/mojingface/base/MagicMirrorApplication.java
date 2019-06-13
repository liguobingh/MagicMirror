package viomi.com.mojingface.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.miot.api.MiotManager;
import com.miot.common.exception.MiotException;
import com.miot.common.people.People;
import com.viomi.apm.library.api.ApmTask;
import com.viomi.apm.library.api.Client;
import com.viomi.apm.library.core.Config;
import com.viomi.apm.library.utils.PackageUtil;
import com.viomi.devicelib.manager.DeviceCentre;
import com.viomi.devicelib.manager.MiDeviceManager;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import java.util.ArrayList;
import java.util.List;

import viomi.com.mojingface.BuildConfig;
import viomi.com.mojingface.R;
import viomi.com.mojingface.config.MagicMirrorConfig;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.config.XiaomiAccountGetPeopleInfoTask;
import viomi.com.mojingface.model.Miconfig;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.base
 * @ClassName: MagicMirrorApplication
 * @Description: 全局Application
 * @Author: randysu
 * @CreateDate: 2019/3/19 11:46 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 11:46 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MagicMirrorApplication extends Application {

    private static final String TAG = MagicMirrorApplication.class.getName();
    private static MagicMirrorApplication app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        initAcra();

        initApm();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (isMainProcess()) {

            app = this;

            MagicMirrorConfig.getInstance().init();

            initMijia();

            Fresco.initialize(this);
        }
    }

    public static MagicMirrorApplication getAppContext() {
        return app;
    }

    private void initAcra() {
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri(BuildConfig.ACRA_ADDRESS)
                .setHttpMethod(HttpSender.Method.POST)
                .setSocketTimeout(30 * 1000)
                .setConnectionTimeout(10 * 1000)
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class)
                .setResText(R.string.crash_text)
                .setLength(Toast.LENGTH_SHORT)
                .setEnabled(true);
        ACRA.init(this, builder, false);
    }

    private boolean isMainProcess() {
        String mainProcessName = getPackageName();
        String processName = getCurrentProcessName();
        return TextUtils.equals(processName, mainProcessName);
    }

    private String getCurrentProcessName() {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    private void initMijia() {
        DeviceCentre.getInstance().addMiSdkListener(new MiDeviceManager.MiSDKListener() {
            @Override
            public void initSuccess() {
                UserEntity user = (UserEntity) FileUtil.getObject(MagicMirrorApplication.this, MirrorConstans.USERFILENAME);
                if (user != null) {
                    //  小米账号相关信息
                    new XiaomiAccountGetPeopleInfoTask(user.getMiAccessToken(), String.valueOf(user.getMiExpiresIn()), user.getMiMacKey(), user.getMiMacAlgorithm(),
                            new XiaomiAccountGetPeopleInfoTask.Handler() {
                                @Override
                                public void onSucceed(People people) {
                                    Log.d(TAG, "XiaomiAccountGetPeopleInfoTask OK");
                                    try {
                                        MiotManager.getPeopleManager().savePeople(people);
                                    } catch (MiotException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailed() {
                                    Log.d(TAG, "XiaomiAccountGetPeopleInfoTask Failed");
                                }
                            }).execute();
                }
            }
        });
        DeviceCentre.getInstance().init(this, Miconfig.getMI_OAUTH_APP_ID(),
                Miconfig.getMI_OAUTH_APP_KEY(),
                false,
                false);
    }

    private void initApm() {
        Config.ConfigBuilder builder = new Config.ConfigBuilder()
                .setAppContext(this)
                .setAppName(PackageUtil.getVersionName(this))
                .setAppVersion(String.valueOf(PackageUtil.getVersionCode(this)));

        // 指定使用hook形式进行Activity生命周期拦截
        builder.setEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_INSTRUMENTATION);

        // 配置需要监控的系统组件
        List<String> workTaskNames = new ArrayList();
        if (isMainProcess()) {
            workTaskNames.add(ApmTask.TASK_ACTIVITY);
            workTaskNames.add(ApmTask.TASK_NET);
            workTaskNames.add(ApmTask.TASK_MEM);
            // workTaskNames.add(ApmTask.TASK_FPS);  // 按需开启或关闭特定监控功能
            workTaskNames.add(ApmTask.TASK_APP_START);
            // workTaskNames.add(ApmTask.TASK_BLOCK);  // 按需开启或关闭特定监控功能
        }
        // 进程监控要独立设置
        workTaskNames.add(ApmTask.TASK_PROCESS_INFO);
        builder.setWorkTasks(workTaskNames);

        Client.attach(builder.build());
        Client.startWork();
    }

}
