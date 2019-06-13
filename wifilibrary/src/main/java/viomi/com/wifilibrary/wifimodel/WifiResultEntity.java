package viomi.com.wifilibrary.wifimodel;

import android.net.wifi.ScanResult;

/**
 * <p>descript：带对比排序字段的wifi扫描结果<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/31<p>
 * <p>update time：2018/10/31<p>
 * <p>version：1<p>
 */
public class WifiResultEntity {

    private ScanResult scanResult;
    private int powerLevel;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }
}
