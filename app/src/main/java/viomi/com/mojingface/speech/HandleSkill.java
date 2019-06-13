package viomi.com.mojingface.speech;

import android.content.Context;

import com.google.gson.JsonObject;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.speech.skill.ChildSongSkill;
import viomi.com.mojingface.speech.skill.ChineseStudySkill;
import viomi.com.mojingface.speech.skill.FolkArtSkill;
import viomi.com.mojingface.speech.skill.FunSkill;
import viomi.com.mojingface.speech.skill.LTNewsSkill;
import viomi.com.mojingface.speech.skill.MusicSkill;
import viomi.com.mojingface.speech.skill.NewWeatherSkill;
import viomi.com.mojingface.speech.skill.StorySkill;
import viomi.com.mojingface.speech.skill.ToyFunSkill;
import viomi.com.mojingface.speech.skill.ToyMusicSkill;
import viomi.com.mojingface.speech.skill.ToyNewsSkill;
import viomi.com.mojingface.speech.skill.ToyStorySkill;
import viomi.com.mojingface.speech.skill.ViomiControlManager;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;

public class HandleSkill {

    private static final String TAG = HandleSkill.class.getName();

    /*****************          小V内置技能  start           ********************/
    private static final String TOY_NEWS_ID = "2017122600000017";
    private static final String TOY_STORY_ID = "2017120200000007";
    private static final String TOY_FUN_ID = "2017120200000010";
    private static final String TOY_TRANSLATE_ID = "2017120200000009";
    private static final String LT_NEWS_ID = "2018081600000049";
    private static final String TOY_MUSIC_ID = "2018050900000076";
    private static final String NEW_WEAHER_ID = "2018081400000044";
    private static final String CHINESE_STUDY_ID = "2018060500000011";
    private static final String FOLK_ART_ID = "2018041600000006";
    private static final String FUN_ID = "100001760";
    private static final String CHILD_SONG_ID = "2018082200000085";
    private static final String STORY_ID = "2018040200000012";
    /*****************          小V内置技能  end           ********************/


    /*****************          控制设备  start           ********************/
    private static final String MEDIA_MUSIC_CONTROL = "音乐播放";
    private static final String MEDIA_VIDEO_CONTROL = "视频播放";
    private static final String MUSICCONTROL = "播放控制";
    private static final String VOLUMECONTROL = "音量控制";
    private static final String BRIGHTNESSCONTROL = "云米小V控制";  // 现只能调整亮度
    private static final String TVCONTROL = "电视控制";
    private static final String AIRCONDITIONCONTROL = "空调控制";
    private static final String COOK = "菜谱";
    private static final String CARCONTROL = "汽车控制";
    /*****************          控制设备  end           ********************/

    private Context context;
    private static volatile HandleSkill instance;

    private HandleSkill(Context context) {
        this.context = context;
    }

    public static HandleSkill getInstance() {
        if (instance == null) {
            synchronized (HandleSkill.class) {
                if (instance == null) {
                    instance = new HandleSkill(MagicMirrorApplication.getAppContext());
                }
            }
        }
        return instance;
    }

    public void handleNlu(String data) {
        JsonObject jsonData = JsonUitls.getJsonObject(data);

        String task = JsonUitls.getString(jsonData, "task");
        LogUtils.e(TAG, "handleNlu-task:" + task);

        switch (task) {
            case MEDIA_MUSIC_CONTROL:{
                MusicSkill.getInstance().handleSkill(jsonData, context);
            }
            break;

            case MEDIA_VIDEO_CONTROL:{

            }
            break;

            case MUSICCONTROL:
            case VOLUMECONTROL: {
                ViomiControlManager.handleSkill(jsonData, context);
            }
            break;

            case COOK:{
//                CookSkill.handleSkill(jsonData, context);
            }
            break;
            default:
                break;
        }
    }

    public void handleNlp(String data) {
        JsonObject jsonData = JsonUitls.getJsonObject(data);
        String skillId = JsonUitls.getString(jsonData, "skillId");
        LogUtils.e(TAG, "handleNlp-skillId:" + skillId);
        LogUtils.e(TAG, "inner skill handleNlp-data:" + data);

        switch (skillId) {

            case LT_NEWS_ID: {
                LTNewsSkill.handleSkill(data, context);
            }
            break;

            case TOY_NEWS_ID: {
                ToyNewsSkill.handleSkill(data, context);
            }
            break;

            case TOY_STORY_ID: {
                ToyStorySkill.handleSkill(data, context);
            }
            break;

            case TOY_FUN_ID: {
                ToyFunSkill.handleSkill(data, context);
            }
            break;

            case TOY_TRANSLATE_ID: {
            }
            break;

            case TOY_MUSIC_ID: {
                ToyMusicSkill.handleSkill(data, context);
            }
            break;

            case NEW_WEAHER_ID: {
                NewWeatherSkill.handleSkill(data);
            }
            break;

            case CHINESE_STUDY_ID: {
                ChineseStudySkill.handleSkill(data, context);
            }
            break;

            case FOLK_ART_ID: {
                FolkArtSkill.handleSkill(data, context);
            }
            break;

            case FUN_ID: {
                FunSkill.handleSkill(data, context);
            }
            break;

            case CHILD_SONG_ID: {
                ChildSongSkill.handleSkill(data, context);
            }
            break;

            case STORY_ID: {
                StorySkill.handleSkill(data, context);
            }
            break;

            default:
                break;
        }
    }
}
