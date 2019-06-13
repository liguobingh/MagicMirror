package viomi.com.wifilibrary.wifimodel;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>descript：wifi信息工具<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/31<p>
 * <p>update time：2018/10/31<p>
 * <p>version：1<p>
 */
public class WifiInfoTools {

    private static final int SCAN_LEVEL = 5;

    private static WifiManager wifiManager;

    public static List<ScanResult> sortWifiResults(Context context, List<ScanResult> scanlist) {
        if (scanlist == null) {
            return new ArrayList<>();
        }

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        List<ScanResult> returnResults = new ArrayList<>();
        List<WifiResultEntity> wifiCompareList = new ArrayList<>();

        for (ScanResult scanResult : scanlist) {

            int userLevel = WifiManager.calculateSignalLevel(scanResult.level, SCAN_LEVEL);

            boolean isCurrentNetwork_WifiInfo = info != null
                    && android.text.TextUtils.equals(info.getSSID(), "\"" + scanResult.SSID + "\"")
                    && android.text.TextUtils.equals(info.getBSSID(), scanResult.BSSID);
            if (isCurrentNetwork_WifiInfo) {
                returnResults.add(0, scanResult);
            } else {
                WifiResultEntity wifiResultEntity = new WifiResultEntity();
                wifiResultEntity.setScanResult(scanResult);
                wifiResultEntity.setPowerLevel(userLevel);
                wifiCompareList.add(wifiResultEntity);
            }

        }

        Collections.sort(wifiCompareList, new Comparator<WifiResultEntity>() {
            @Override
            public int compare(WifiResultEntity o1, WifiResultEntity o2) {
                if (o1.getPowerLevel() > o2.getPowerLevel()) {
                    return -1;
                } else if (o1.getPowerLevel() == o2.getPowerLevel()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        for (WifiResultEntity wifiResultEntity : wifiCompareList) {
            returnResults.add(wifiResultEntity.getScanResult());
        }

        return returnResults;
    }

}
