package viomi.com.mojingface.speech;

import android.content.Context;
import android.content.Intent;

import com.viomi.speech.listener.ViomiSpeechListener;
import com.viomi.speech.manager.ViomiSpeechManager;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.speech.skill.ViomiControlManager;
import viomi.com.mojingface.util.LogUtils;


/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.speech
 * @ClassName: SpeechApiImpl
 * @Description: 语音响应回调
 * @Author: randysu
 * @CreateDate: 2019/3/1 7:26 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/1 7:26 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SpeechApiImpl implements ViomiSpeechListener {

    private static final String TAG = SpeechApiImpl.class.getName();

    private static final String QUICK_WORD_NEXT_STEP = "next.step";
    private static final String QUICK_WORD_LAST_STEP = "last.step";
    private static final String QUICK_WORD_INCREASE_VOL = "increase.volume";//大声一点
    private static final String QUICK_WORD_DECREASE_VOL = "decrease.volume";//小声一点
    private static final String QUICK_WORD_BACK_DESKTOP = "back.desktop";//回到桌面
    private static final String QUICK_WORD_OPEN_SETTING = "open.setting";//打开设置
    private static final String QUICK_WORD_INCREASE_BRIGHT = "increase.bright";
    private static final String QUICK_WORD_DECREASE_BRIGHT = "decrease.bright";

    @Override
    public void wakeUp(Context context) {
        LogUtils.i(TAG, "VioAIListener-wakeUp");
        context.sendBroadcast(new Intent(BroadcastAction.SPEECHSTART));
    }

    @Override
    public void endDialog(Context context) {
        LogUtils.i(TAG, "VioAIListener-endDialog");
        context.sendBroadcast(new Intent(BroadcastAction.DIALOGSTOP));
    }

    @Override
    public void onNluResult(Context context, String data) {
        LogUtils.e(TAG, "VioAIListener-onNluResult ----- data:" + data);

        HandleSkill.getInstance().handleNlu(data);
    }

    @Override
    public void onNlpResult(Context context, String s, String data) {
        LogUtils.e(TAG, "VioAIListener-onNlpResult -qiuckorder ----- data:" + data);

        switch (s) {
            case QUICK_WORD_NEXT_STEP: {
                context.sendBroadcast(new Intent(BroadcastAction.QIUCKORDER_NEXT));
            }
            break;

            case QUICK_WORD_LAST_STEP: {
                context.sendBroadcast(new Intent(BroadcastAction.QIUCKORDER_PRE));
            }
            break;
            case QUICK_WORD_INCREASE_VOL: {//大声一点
                ViomiSpeechManager.getInstance().speechContent("正在为您调大音量");
                ViomiControlManager.soundUp(5, MagicMirrorApplication.getAppContext());
            }
            break;

            case QUICK_WORD_DECREASE_VOL: {//小声一点
                ViomiControlManager.soundDown(5, MagicMirrorApplication.getAppContext());
                ViomiSpeechManager.getInstance().speechContent("正在为您调小音量");
            }
            break;

            case QUICK_WORD_BACK_DESKTOP: {//回到桌面
                ViomiSpeechManager.getInstance().speechContent("正在为您返回桌面");

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("viomi.com.mojingface.action.DesktopActivity");
                MagicMirrorApplication.getAppContext().startActivity(intent);

//                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
//                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出

//                BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                Intent settingIntent = new Intent(currentActivity, DesktopActivity.class);
//                currentActivity.startActivity(settingIntent);
            }
            break;

            case QUICK_WORD_OPEN_SETTING: {//打开设置
                ViomiSpeechManager.getInstance().speechContent("正在为您打开设置");

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("viomi.com.mojingface.action.SettingActivity");
                MagicMirrorApplication.getAppContext().startActivity(intent);

//                BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                Intent settingIntent = new Intent(currentActivity, SettingActivity.class);
//                currentActivity.startActivity(settingIntent);

            }
            break;
//            case QUICK_WORD_INCREASE_BRIGHT: {//亮一点
//                context.sendBroadcast(new Intent(BroadcastAction.SCREEN_BRIGHT_UP));
//            }
//            break;
//
//            case QUICK_WORD_DECREASE_BRIGHT: {//暗一点
//                context.sendBroadcast(new Intent(BroadcastAction.SCREEN_BRIGHT_DOWN));
//            }
//            break;
            case "context.widget.custom":
            case "context.widget.media":
            case "context.widget.content":
            case "context.widget.list":
            case "context.widget.text":
                HandleSkill.getInstance().handleNlp(data);
                break;
            default:
                break;
        }
    }
}
