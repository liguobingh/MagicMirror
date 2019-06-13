package viomi.com.mojingface.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.wifilibrary.wifimodel.Wifi;
import viomi.com.wifilibrary.wifimodel.WifiAdapter;
import viomi.com.wifilibrary.wifimodel.WifiConnectActivity;
import viomi.com.wifilibrary.wifimodel.WifiInfoTools;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.ui.activity
 * @ClassName: AppWifiScanActivity
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/4/2 1:49 PM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 1:49 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AppWifiScanActivity extends AppCompatActivity {

    private ImageView back_icon;
    private ListView wifiListview;
    private WifiAdapter adapter;
    private WifiManager wifiManager;
    private List<ScanResult> scanlist;
    private Switch switch_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(viomi.com.wifilibrary.R.layout.activity_wifi_scan);

        back_icon = findViewById(viomi.com.wifilibrary.R.id.back_icon);
        switch_btn = findViewById(viomi.com.wifilibrary.R.id.switch_btn);

        wifiListview = findViewById(viomi.com.wifilibrary.R.id.wifiListview);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        switch_btn.setChecked(wifiManager.isWifiEnabled());

        scanlist = new ArrayList<>();
        adapter = new WifiAdapter(this, scanlist, wifiManager);
        wifiListview.setAdapter(adapter);

        initListener();
        init();
    }

    protected void initListener() {
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wifiManager.setWifiEnabled(isChecked);
                if (!isChecked) {
                    scanlist.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        wifiListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ScanResult result = scanlist.get(position);
                launchWifiConnecter(AppWifiScanActivity.this, result);
            }
        });
    }

    protected void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
//        filter2.addAction(WifiManager.RSSI_CHANGED_ACTION); //信号强度变化
        filter2.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); //网络状态变化
        filter2.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //wifi状态，是否连上，密码
        filter2.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION); //是不是正在获得IP地址
        filter2.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver2, filter2);
        wifiManager.startScan();

        showwifilist();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
        unregisterReceiver(mReceiver2);
    }

    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("AppWifiScanActivity-re2", intent.getAction() + "");
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults!=null) {
                LogUtils.e("AppWifiScanActivity-re2", "scanResults.size()"+scanResults.size());

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("AppWifiScanActivity-re", intent.getAction() + "");

            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                showwifilist();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wifiManager.startScan();
                    }
                }, 2000);
            }
        }
    };

    private void showwifilist() {
        List<ScanResult> sResults = wifiManager.getScanResults();
        if (sResults==null) {
            return;
        }
        Map<String, ScanResult> sMap = new HashMap<>();
        for (int i = 0; i < sResults.size(); i++) {
            ScanResult result = sResults.get(i);
            if (result.SSID != null && "" != result.SSID) {
                if (sMap.containsKey(result.SSID)) {
                    String security = Wifi.ConfigSec.getScanResultSecurity(result);
                    WifiConfiguration config = Wifi.getWifiConfiguration(wifiManager, result, security);
                    WifiInfo info = wifiManager.getConnectionInfo();
                    boolean isCurrentNetwork_WifiInfo = info != null && android.text.TextUtils.equals(info.getSSID(), "\"" + result.SSID + "\"") && android.text.TextUtils.equals(info.getBSSID(), result.BSSID);
                    if (!(config != null && isCurrentNetwork_WifiInfo)) {
                        continue;
                    }
                }

                sMap.put(result.SSID, result);
            }
        }

        sMap.remove(null);
        sMap.remove("");

        Collection<ScanResult> values = sMap.values();
        List<ScanResult> scanResults = new ArrayList<>();
        scanResults.addAll(values);

        scanlist.clear();
        scanlist.addAll(WifiInfoTools.sortWifiResults(MagicMirrorApplication.getAppContext(), scanResults));
        adapter.notifyDataSetChanged();
    }


    private void launchWifiConnecter(final Activity activity, final ScanResult hotspot) {
        final Intent intent = new Intent(this, WifiConnectActivity.class);

        intent.putExtra("EXTRA_HOTSPOT", hotspot);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

}
