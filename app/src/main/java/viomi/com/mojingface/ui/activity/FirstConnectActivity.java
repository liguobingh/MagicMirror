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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.util.ObservableUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import viomi.com.wifilibrary.wifimodel.Wifi;
import viomi.com.wifilibrary.wifimodel.WifiAdapter;
import viomi.com.wifilibrary.wifimodel.WifiConnectActivity;
import viomi.com.wifilibrary.wifimodel.WifiInfoTools;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.activity
 * @ClassName: FirstConnectActivity
 * @Description: 首次选择wifi界面
 * @Author: randysu
 * @CreateDate: 2019/3/19 4:02 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 4:02 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FirstConnectActivity extends BaseActivity {

    private ListView wifiListview;
    private Button skip_btn;

    private WifiAdapter adapter;
    private WifiManager wifiManager;
    private List<ScanResult> scanlist;
    private boolean isFisrt;
    private boolean isJumped;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_first_connect);

        wifiListview = findViewById(R.id.wifiListview);
        skip_btn = findViewById(R.id.skip_btn);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        scanlist = new ArrayList<>();
        adapter = new WifiAdapter(this, scanlist, wifiManager);
        wifiListview.setAdapter(adapter);

        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mScanResultReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); //网络状态变化
        filter2.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //wifi状态，是否连上，密码
        filter2.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION); //是不是正在获得IP地址
        filter2.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, filter2);
    }

    @Override
    protected void initListener() {
        wifiListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult result = scanlist.get(position);
                launchWifiConnecter(FirstConnectActivity.this, result);
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstConnectActivity.this, DesktopActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void init() {
        Intent intent = getIntent();
        isFisrt = intent.getBooleanExtra("isFisrt", false);

        wifiManager.startScan();
        showwifilist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mScanResultReceiver);
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults != null) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    private BroadcastReceiver mScanResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                showwifilist();
                ObservableUtil.createObserver().delay(2000, TimeUnit.MILLISECONDS).subscribe(aLong -> wifiManager.startScan() );
            }
        }
    };

    private void showwifilist() {
        List<ScanResult> sResults = wifiManager.getScanResults();
        if (sResults == null) return;
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
        scanlist.addAll(WifiInfoTools.sortWifiResults(this, scanResults));
        adapter.notifyDataSetChanged();

        for (int i = 0; i < scanResults.size(); i++) {
            ScanResult scanResult1 = scanResults.get(i);
            String security = Wifi.ConfigSec.getScanResultSecurity(scanResult1);
            WifiConfiguration config = Wifi.getWifiConfiguration(wifiManager, scanResult1, security);

            if (config == null) {
                continue;
            }

            boolean isCurrentNetwork_ConfigurationStatus = config.status == WifiConfiguration.Status.CURRENT;
            WifiInfo info = wifiManager.getConnectionInfo();
            boolean isCurrentNetwork_WifiInfo = info != null
                    && android.text.TextUtils.equals(info.getSSID(), "\"" + scanResult1.SSID + "\"")
                    && android.text.TextUtils.equals(info.getBSSID(), scanResult1.BSSID);

            if (isCurrentNetwork_WifiInfo && isCurrentNetwork_ConfigurationStatus) {
                if (isFisrt && !isJumped) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("isFirst", true);
                    startActivity(intent);
                    finish();

                    isJumped = true;
                } else {
                    Intent intent = new Intent(this, DesktopActivity.class);
                    startActivity(intent);
                    finish();

                    isJumped = true;
                }
                break;
            }
        }

    }

    private void launchWifiConnecter(final Activity activity, final ScanResult hotspot) {
        Intent intent = new Intent(FirstConnectActivity.this, WifiConnectActivity.class);
        intent.putExtra("EXTRA_HOTSPOT", hotspot);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }
}
