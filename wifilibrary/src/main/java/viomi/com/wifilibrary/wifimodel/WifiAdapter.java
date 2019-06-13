package viomi.com.wifilibrary.wifimodel;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import viomi.com.wifilibrary.R;

/**
 * Created by Mocc on 2017/5/4
 */

public class WifiAdapter extends BaseAdapter {
    private static final String TAG = WifiAdapter.class.getName();
    private Context context;
    private List<ScanResult> list;
    private LayoutInflater inflater;
    private WifiManager wifiManager;
    private int[] wifiIcons;

    public WifiAdapter(Context context, List<ScanResult> list, WifiManager wifiManager) {
        this.context = context;
        this.list = list;
        this.wifiManager = wifiManager;
        inflater = LayoutInflater.from(context);
        wifiIcons = new int[]{R.drawable.wifi_icon1, R.drawable.wifi_icon2, R.drawable.wifi_icon3};
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? 0 : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wifi_item_layout, null);
            holder = new ViewHolder();
            holder.connect_status = (TextView) convertView.findViewById(R.id.connect_status);
            holder.wifi_name = (TextView) convertView.findViewById(R.id.wifi_name);
            holder.wifi_icon = (ImageView) convertView.findViewById(R.id.wifi_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScanResult scanResult = list.get(position);
        holder.wifi_name.setText(scanResult.SSID + "");
        holder.wifi_icon.setImageResource(wifiIcons[WifiManager.calculateSignalLevel(scanResult.level, wifiIcons.length)]);

        final String security = Wifi.ConfigSec.getScanResultSecurity(scanResult);
        final WifiConfiguration config = Wifi.getWifiConfiguration(wifiManager, scanResult, security);

        if (config == null) {
            //未连接
            holder.connect_status.setText("未连接");
            holder.wifi_name.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));
            holder.connect_status.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));

        } else {
            final boolean isCurrentNetwork_ConfigurationStatus = config.status == WifiConfiguration.Status.CURRENT;
            final WifiInfo info = wifiManager.getConnectionInfo();
            final boolean isCurrentNetwork_WifiInfo = info != null
                    && android.text.TextUtils.equals(info.getSSID(), "\"" + scanResult.SSID + "\"")
                    && android.text.TextUtils.equals(info.getBSSID(), scanResult.BSSID);

            if (isCurrentNetwork_WifiInfo) {
                if (isCurrentNetwork_ConfigurationStatus) {
                    //已连接连接
                    holder.connect_status.setText("已连接");
                    holder.wifi_name.setTextColor(context.getResources().getColor(R.color.wifi_conntected_status));
                    holder.connect_status.setTextColor(context.getResources().getColor(R.color.wifi_conntected_status));
                } else {
                    //正在连接
                    holder.connect_status.setText("正在连接...");
                    holder.wifi_name.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));
                    holder.connect_status.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));
                }

            } else {
                //未连接
                holder.connect_status.setText("未连接");
                holder.wifi_name.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));
                holder.connect_status.setTextColor(context.getResources().getColor(R.color.wifi_unconntected_status));
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView wifi_name, connect_status;
        ImageView wifi_icon;
    }

}
