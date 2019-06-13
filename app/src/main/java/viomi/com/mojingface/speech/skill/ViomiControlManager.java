package viomi.com.mojingface.speech.skill;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.google.gson.JsonObject;
import com.viomi.speech.manager.ViomiSpeechManager;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;

public class ViomiControlManager {

    private static final String TAG = ViomiControlManager.class.getName();

    public static void handleSkill(JsonObject jsonData, Context context) {

        String intentName = JsonUitls.getString(jsonData, "intentName");
        LogUtils.i(TAG, "intentName:" + intentName);

        switch (intentName) {

            //音量控制
            case "soundUp": {
                soundUp(5, context);
                ViomiSpeechManager.getInstance().speechContent("正在为您调大音量");
            }
            break;

            case "soundDown": {
                soundDown(5, context);
                ViomiSpeechManager.getInstance().speechContent("正在为您调小音量");
            }
            break;

            case "soundSet": {
                int value_set_int = JsonUitls.getInt(jsonData, "value");
                if (value_set_int >= 0) {
                    setSound(value_set_int, context);
                    ViomiSpeechManager.getInstance().speechContent("正在为您设置音量");
                }

            }
            break;

            case "soundMax": {
                setSound(100, context);
                ViomiSpeechManager.getInstance().speechContent("正在为您设置最大音量");
            }
            break;

            case "soundMin": {
                setSound(0, context);
                ViomiSpeechManager.getInstance().speechContent("正在为您设置最小音量");
            }
            break;

            //跳转控制
            case "openset": {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("viomi.com.mojingface.action.SettingActivity");
                MagicMirrorApplication.getAppContext().startActivity(intent);

//                BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                Intent settingIntent = new Intent(context, SettingActivity.class);
//                currentActivity.startActivity(settingIntent);
//                ViomiSpeechManager.getInstance().speechContent("正在为您打开设置");
            }
            break;

            case "BackToTheDesktop": {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("viomi.com.mojingface.action.DesktopActivity");
                MagicMirrorApplication.getAppContext().startActivity(intent);

//                BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                Intent destTopIntent = new Intent(context, DesktopActivity.class);
//                currentActivity.startActivity(destTopIntent);
//                ViomiSpeechManager.getInstance().speechContent("正在为您返回桌面");
            }
            break;

            //播放控制
            case "next": {
                context.sendBroadcast(new Intent(BroadcastAction.PLAYCONTROL_NEXT));
            }
            break;

            case "prev": {
                context.sendBroadcast(new Intent(BroadcastAction.PLAYCONTROL_PREV));
            }
            break;

            case "play": {
                context.sendBroadcast(new Intent(BroadcastAction.PLAYCONTROL_PLAY));
            }
            break;

            case "pause": {
                context.sendBroadcast(new Intent(BroadcastAction.PLAYCONTROL_PAUSE));
            }
            break;

            case "exit": {
                context.sendBroadcast(new Intent(BroadcastAction.PLAYCONTROL_EXIT));
            }
            break;

            default:
                ViomiSpeechManager.getInstance().speechContent(MirrorConstans.DefaultReply);
                break;
        }
    }


    public static void soundDown(int soundValue, Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = (int) Math.ceil(soundValue / 100.0 * streamMaxVolume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setMusicSound(currentVolume - volume, context);
    }

    public static void soundUp(int soundValue, Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = (int) Math.ceil(soundValue / 100.0 * streamMaxVolume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setMusicSound(currentVolume + volume, context);
    }

    private static void setSound(int soundValue, Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = (int) Math.ceil(soundValue / 100.0 * streamMaxVolume);
        setMusicSound(volume, context);
    }

    private static void setMusicSound(int soundValue, Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, soundValue, 0);
    }

}
