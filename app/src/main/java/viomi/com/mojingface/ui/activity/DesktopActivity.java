package viomi.com.mojingface.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.config.DeviceReportManager;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.config.NetManager;
import viomi.com.mojingface.config.TimerActionManager;
import viomi.com.mojingface.service.SpeechInitGuardService;
import viomi.com.mojingface.service.SpeechInitService;
import viomi.com.mojingface.service.SpeechServiceJobWakeUpService;
import viomi.com.mojingface.ui.fragment.DeviceBallFragment;
import viomi.com.mojingface.ui.fragment.WeatherInfoFragment;
import viomi.com.mojingface.util.SharedPreferencesUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.activity
 * @ClassName: DesktopActivity
 * @Description: 首页界面
 * @Author: randysu
 * @CreateDate: 2019/3/19 3:14 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 3:14 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DesktopActivity extends BaseActivity {

    private TimerActionManager timerActionManager;
    private NetManager netManager;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_desktop);

        SharedPreferencesUtil.putData(MirrorConstans.FIRSTLAUNCH_KEY, false);

        if (savedInstanceState == null) {
            DeviceBallFragment deviceBallFragment = new DeviceBallFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.device_balls_layout, deviceBallFragment, "DeviceBallFragment")
                    .commit();

            WeatherInfoFragment weatherInfoFragment = new WeatherInfoFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weatherinfo_layout, weatherInfoFragment, "WeatherInfoFragment")
                    .commit();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void init() {
        startService(new Intent(this, SpeechInitService.class));
        startService(new Intent(this, SpeechInitGuardService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "SpeechServiceJobWakeUpService");
            startService(new Intent(this, SpeechServiceJobWakeUpService.class));
        }

        netManager = new NetManager();
        timerActionManager = new TimerActionManager(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(intentReceive, intentFilter);

        DeviceReportManager.getInstance().registMusicPlantform();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(intentReceive);
    }

    private BroadcastReceiver intentReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_TIME_TICK:
                    if (timerActionManager != null) {
                        timerActionManager.actionCheckNetWork(netManager, context);
                    }
                    break;
            }
        }
    };
}
