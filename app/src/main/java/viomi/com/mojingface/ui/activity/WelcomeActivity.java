package viomi.com.mojingface.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SharedPreferencesUtil;
import viomi.com.mojingface.viewmodel.WelcomeViewModel;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.activity
 * @ClassName: WelcomeActivity
 * @Description: 启动页
 * @Author: randysu
 * @CreateDate: 2019/3/19 2:30 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 2:30 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class WelcomeActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private WelcomeViewModel welcomeViewModel;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void init() {
        welcomeViewModel = ViewModelProviders.of(this).get(WelcomeViewModel.class);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            welcomeViewModel.delayObserver(2000, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                LogUtils.i(TAG, "currentTime:" + aLong);
                jumpActivity();
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    welcomeViewModel.delayObserver(2000, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                        jumpActivity();
                    });
                } else {
                    Intent intent = new Intent(this, DesktopActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            }
        }
    }

    private void jumpActivity() {
        boolean firstLaunch = (boolean) SharedPreferencesUtil.getData(MirrorConstans.FIRSTLAUNCH_KEY, true);
        if (firstLaunch) {
            Intent intent = new Intent(this, FirstConnectActivity.class);
            intent.putExtra("isFisrt", true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, DesktopActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
