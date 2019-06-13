package viomi.com.mojingface.config;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.viomi.speech.manager.ViomiSpeechManager;

import java.util.Calendar;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.model.Miconfig;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.LogUtils;

/**
 * <p>descript：需要定时操作的事件管理类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/26<p>
 * <p>update time：2018/10/26<p>
 * <p>version：1<p>
 */
public class TimerActionManager {

    private static final String TAG = TimerActionManager.class.getName();

    private Context context;
    private boolean isInstalling = false;

    public TimerActionManager(Context context) {
        this.context = context;
    }

    /**
     * 心跳检测
     */
    public void actionHeartBeat(int beatsCount) {
        Intent intent = new Intent("viomi.com.viomiaiface.action.heartbeat");
        intent.putExtra("beats", beatsCount);
        MagicMirrorApplication.getAppContext().sendBroadcast(intent);
    }

    /**
     * 静默安装
     *
     * 每天凌晨3点检查是否已经下载apk，如果是，测进行静默安装   3:00 -- 3:05 区间进行安装
     */
    public void actionSlienceInstall() {
        Calendar calendar = Calendar.getInstance();
        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        if (!isInstalling) {
            if (currentHours == 3 && currentMinutes > 0 && currentMinutes < 5) {

                Intent sendInstallBroadcast = new Intent();
                sendInstallBroadcast.setAction(BroadcastAction.SLIENCE_INSTALL);
                context.sendBroadcast(sendInstallBroadcast);

                isInstalling = true;
            }
        }
    }

    /**
     * 检查是否真的联通网络
     */
    public void actionCheckNetWork(NetManager netManager, Context context) {
        LogUtils.i("XXX", "进入检查网络连通性");
        if (netManager != null) {
            netManager.isNetWorkAvailable("www.baidu.com", new Comparable<Boolean>() {
                @Override
                public int compareTo(@NonNull Boolean available) {
                    if (available) {
                        if (!MirrorConstans.HASREPORTTOMUSICPLANTFORM) {
                            DeviceReportManager.getInstance().registMusicPlantform();
                        }
                        context.sendBroadcast(new Intent(BroadcastAction.NETWORK_REAL_CONNECTED));
                    } else {
                        context.sendBroadcast(new Intent(BroadcastAction.NETWORK_REAL_DISCONNECT));
                    }
                    return 0;
                }
            });
        }
    }

    /**
     * 检查用户信息初始化成功是否
     */
    public void actionCheckUserLogin(Context context) {
        LogUtils.i("XXX", "轮询检查用户信息初始化是否成功");
        UserEntity user = (UserEntity) FileUtil.getObject(context, MirrorConstans.USERFILENAME);

        if (user != null) {
            ViomiSpeechManager.getInstance().saveUserInfo(user.getMiId(), user.getViomiUserId(),
                    String.valueOf(Miconfig.getMI_OAUTH_APP_ID()),
                    user.getViomiToken());
        }
    }

}
