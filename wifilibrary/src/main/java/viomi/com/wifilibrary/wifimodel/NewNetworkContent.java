package viomi.com.wifilibrary.wifimodel;

import android.net.wifi.ScanResult;
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


public class NewNetworkContent extends BaseContent {
	
	private boolean mIsOpenNetwork = false;
	
	public NewNetworkContent(final Floating floating, final WifiManager wifiManager, ScanResult scanResult) {
		super(floating, wifiManager, scanResult);
		
		mView.findViewById(R.id.Status).setVisibility(View.GONE);
		mView.findViewById(R.id.Speed).setVisibility(View.GONE);
		mView.findViewById(R.id.IPAddress).setVisibility(View.GONE);
		if(Wifi.ConfigSec.isOpenNetwork(mScanResultSecurity)) {
			mIsOpenNetwork = true;
			mView.findViewById(R.id.Password).setVisibility(View.GONE);
		} else {
			((TextView)mView.findViewById(R.id.Password_TextView)).setText(R.string.please_type_passphrase);
		}
	}
	
	private OnClickListener mConnectOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			boolean connResult;
			if(mIsOpenNetwork) {
				connResult = Wifi.connectToNewNetwork(mFloating, mWifiManager, mScanResult, null, mNumOpenNetworksKept);
			} else {
				String password = ((EditText) mView.findViewById(R.id.Password_EditText)).getText().toString();
				if (password==null||password.length()==0) {
                    Toast.makeText(mFloating, R.string.hint_input, Toast.LENGTH_LONG).show();
					return;
				}
				connResult = Wifi.connectToNewNetwork(mFloating, mWifiManager, mScanResult
						, password
						, mNumOpenNetworksKept);
			}
			
			if(!connResult) {
				Toast.makeText(mFloating, R.string.toastFailed, Toast.LENGTH_LONG).show();
			}
			
			mFloating.finish();
		}
	};
	
	private OnClickListener mOnClickListeners[] = {mConnectOnClick, mCancelOnClick};

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
		switch(index) {
		case 0:
			return mFloating.getText(R.string.connect);
		case 1:
			return getCancelString();
		default:
			return null;
		}
	}

	@Override
	public CharSequence getTitle() {
		return mFloating.getString(R.string.wifi_connect_to, mScanResult.SSID);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
	}

}
