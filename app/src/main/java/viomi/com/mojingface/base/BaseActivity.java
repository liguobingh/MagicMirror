package viomi.com.mojingface.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import viomi.com.mojingface.R;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.SnackbarUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import viomi.com.wifilibrary.wifimodel.WifiScanActivity;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.base
 * @ClassName: BaseActivity
 * @Description: Activity基类
 * @Author: randysu
 * @CreateDate: 2019/3/1 7:28 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/1 7:28 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getName();
    public BaseActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "onCreate");

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AppManager.getInstance().addActivity(this);
        activity = this;

        // 绑定网络变化监听广播
        IntentFilter netWorkConnectedFilter = new IntentFilter(BroadcastAction.NETWORK_REAL_CONNECTED);
        registerReceiver(netWorkConnectedReceive, netWorkConnectedFilter);

        IntentFilter netWorkDisconnectFilter = new IntentFilter(BroadcastAction.NETWORK_REAL_DISCONNECT);
        registerReceiver(netWorkDisconnectReceive, netWorkDisconnectFilter);

        initView(savedInstanceState);
        initListener();
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");

        unregisterReceiver(netWorkConnectedReceive);
        unregisterReceiver(netWorkDisconnectReceive);
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void init();

    public View getContentView(){
        return this.findViewById(android.R.id.content);
    }

    private BroadcastReceiver netWorkConnectedReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SnackbarUtil.dismiss();
        }
    };

    private BroadcastReceiver netWorkDisconnectReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity currentActivity = AppManager.getInstance().currentActivity();
            if (currentActivity != null) {
                boolean isShowing = SnackbarUtil.isShowing();
                if (!isShowing) {
                    View contentView = currentActivity.getContentView();
                    if (contentView != null) {
                        SnackbarUtil.make(contentView, "网络连接错误，请检查设置", 0, Snackbar.LENGTH_INDEFINITE,
                                ContextCompat.getColor(context, R.color.snarkbar_warm_bg_color), "设置",
                                ContextCompat.getColor(context, R.color.btn_blue), new SnackbarUtil.ActionCallback() {
                                    @Override
                                    public void onAction(View view) {
                                        Intent wifiSettingIntent = new Intent(activity, WifiScanActivity.class);
                                        activity.startActivity(wifiSettingIntent);
                                    }
                                });
                    }
                }
            }

        }
    };

}
