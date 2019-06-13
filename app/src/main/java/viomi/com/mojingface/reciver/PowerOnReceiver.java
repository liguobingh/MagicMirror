package viomi.com.mojingface.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import viomi.com.mojingface.util.LogUtils;

/**
 * <p>descript：开机广播<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/15<p>
 * <p>update time：2018/11/15<p>
 * <p>version：1<p>
 */
public class PowerOnReceiver extends BroadcastReceiver {

    private static final String TAG = PowerOnReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) { //开机启动完成后，要做的事情
            LogUtils.i(TAG, "power reboot");

//            Intent mIntent = new Intent(context, SpeechInitService.class);
//            context.startService(mIntent);
        }
    }

}
