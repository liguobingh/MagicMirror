package viomi.com.wifilibrary.wifimodel;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import viomi.com.wifilibrary.R;


public class ChangePasswordContent extends BaseContent {

    private ChangingAwareEditText mPasswordEditText;

    public ChangePasswordContent(Floating floating, WifiManager wifiManager,
                                 ScanResult scanResult) {
        super(floating, wifiManager, scanResult);

        mView.findViewById(R.id.Status).setVisibility(View.GONE);
        mView.findViewById(R.id.Speed).setVisibility(View.GONE);
        mView.findViewById(R.id.IPAddress).setVisibility(View.GONE);

        mPasswordEditText = ((ChangingAwareEditText) mView.findViewById(R.id.Password_EditText));

        ((TextView) mView.findViewById(R.id.Password_TextView)).setText(R.string.please_type_passphrase);

        ((EditText) mView.findViewById(R.id.Password_EditText)).setHint(R.string.wifi_password_unchanged);
    }

    @Override
    public int getButtonCount() {
        return 2;
    }

    @Override
    public OnClickListener getButtonOnClickListener(int index) {
        return mOnClickListeners[index];
    }

    @Override
    public CharSequence getButtonText(int index) {
        switch (index) {
            case 0:
                return mFloating.getString(R.string.wifi_save_config);
            case 1:
                return getCancelString();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getTitle() {
        return mScanResult.SSID;
    }

    private OnClickListener mSaveOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mPasswordEditText.getChanged()) {
                final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, mScanResult, mScanResultSecurity);
                boolean saveResult = false;
                if (config != null) {
                    saveResult = Wifi.changePasswordAndConnect(mFloating, mWifiManager, config
                            , mPasswordEditText.getText().toString()
                            , mNumOpenNetworksKept);
                }

                if (!saveResult) {
                    Toast.makeText(mFloating, R.string.toastFailed, Toast.LENGTH_LONG).show();
                }
            }

            mFloating.finish();
        }
    };

    OnClickListener mOnClickListeners[] = {mSaveOnClick, mCancelOnClick};

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
    }

}
