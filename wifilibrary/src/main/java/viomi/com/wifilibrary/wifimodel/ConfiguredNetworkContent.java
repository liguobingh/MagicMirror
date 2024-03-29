package viomi.com.wifilibrary.wifimodel;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import viomi.com.wifilibrary.R;


public class ConfiguredNetworkContent extends BaseContent {

	public ConfiguredNetworkContent(Floating floating, WifiManager wifiManager,
			ScanResult scanResult) {
		super(floating, wifiManager, scanResult);
		
		mView.findViewById(R.id.Status).setVisibility(View.GONE);
		mView.findViewById(R.id.Speed).setVisibility(View.GONE);
		mView.findViewById(R.id.IPAddress).setVisibility(View.GONE);
		mView.findViewById(R.id.Password).setVisibility(View.GONE);
	}

	@Override
	public int getButtonCount() {
		return 3;
	}

	@Override
	public OnClickListener getButtonOnClickListener(int index) {
		switch(index) {
		case 0:
			return mConnectOnClick;
		case 1:
			if(mIsOpenNetwork) {
				return mForgetOnClick;
			} else {
				return mOpOnClick;
			}
		case 2:
			return mCancelOnClick;
		default:
			return null;
		}
	}

	@Override
	public CharSequence getButtonText(int index) {
		switch(index) {
		case 0:
			return mFloating.getString(R.string.connect);
		case 1:
			if(mIsOpenNetwork) {
				return mFloating.getString(R.string.forget_network);
			} else {
				return mFloating.getString(R.string.buttonOp);
			}
		case 2:
			return getCancelString();
		default:
			return null;
		}
	}

	@Override
	public CharSequence getTitle() {
		return mFloating.getString(R.string.wifi_connect_to, mScanResult.SSID);
	}
	
	private OnClickListener mConnectOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, mScanResult, mScanResultSecurity);
			boolean connResult = false;
			if(config != null) {
				connResult = Wifi.connectToConfiguredNetwork(mFloating, mWifiManager, config, false);
			}
			if(!connResult) {
				Toast.makeText(mFloating, R.string.toastFailed, Toast.LENGTH_LONG).show();
			}
			
			mFloating.finish();
		}
	};
	
	private OnClickListener mOpOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mFloating.registerForContextMenu(v);
			mFloating.openContextMenu(v);
			mFloating.unregisterForContextMenu(v);
		}
	};
	
	private OnClickListener mForgetOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			forget();
		}
	};

	private void forget() {
		final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, mScanResult, mScanResultSecurity);
		boolean result = false;
		if(config != null) {
			result = mWifiManager.removeNetwork(config.networkId)
				&& mWifiManager.saveConfiguration();
		}
		if(!result) {
			Toast.makeText(mFloating, R.string.toastFailed, Toast.LENGTH_LONG).show();
		}
		
		mFloating.finish();
	}
	
	private static final int MENU_FORGET = 0;
	private static final int MENU_CHANGE_PASSWORD = 1;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_FORGET:
			forget();
			break;
		case MENU_CHANGE_PASSWORD:
			changePassword();
			break;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, MENU_FORGET, Menu.NONE, R.string.forget_network);
		menu.add(Menu.NONE, MENU_CHANGE_PASSWORD, Menu.NONE, R.string.wifi_change_password);
	}

}
