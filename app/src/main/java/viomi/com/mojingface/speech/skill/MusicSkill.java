package viomi.com.mojingface.speech.skill;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.viomi.speech.manager.ViomiSpeechManager;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;

/**
 * <p>descript：音乐控制技能<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/7<p>
 * <p>update time：2018/11/7<p>
 * <p>version：1<p>
 */
public class MusicSkill {

    private static final String TAG = MusicSkill.class.getName();

    public static final int SEARCHTYPE_NONE = 100;
    public static final int SEARCHTYPE_BYSONG = 101;
    public static final int SEARCHTYPE_BYALBUM = 102;
    public static final int SEARCHTYPE_BYARTIST = 103;
    public static final int SEARCHTYPE_BYSHEETID = 104;

    public static final String SEARCHTYPE = "searchType";
    public static final String SEARCHKEY = "searchKey";

    public static MusicSkill instance = null;

    public static MusicSkill getInstance() {
        if (instance == null) {
            synchronized (MusicSkill.class) {
                if (instance == null) {
                    instance = new MusicSkill();
                }
            }
        }
        return instance;
    }

    private MusicSkill() {

    }

    public void handleSkill(JsonObject jsonData, Context context) {
        if (!MirrorConstans.HASREPORTTOMUSICPLANTFORM) {
            ViomiSpeechManager.getInstance().speechContent("音乐功能正在初始化，请稍候");
            return;
        }

        String intentName = JsonUitls.getString(jsonData, "intentName");
        LogUtils.i(TAG, "intentName:" + intentName);

        Intent intent = new Intent();
        String searchKey = "";

        switch (intentName) {
            case "打开音乐":
                intent.putExtra("playtype", "MUSIC_ID");
                intent.putExtra(SEARCHTYPE, SEARCHTYPE_NONE);
                break;
            case "按歌名播放":
                searchKey = JsonUitls.getString(jsonData, "song");
                intent.putExtra("playtype", "MUSIC_ID");
                intent.putExtra(SEARCHTYPE, SEARCHTYPE_BYSONG);
                intent.putExtra(SEARCHKEY, searchKey);
                break;
            case "按专辑播放":
                String artist = JsonUitls.getString(jsonData, "artist");
                String album = JsonUitls.getString(jsonData, "album");
                searchKey = artist + album;
                intent.putExtra("playtype", "MUSIC_ID");
                intent.putExtra(SEARCHTYPE, SEARCHTYPE_BYALBUM);
                intent.putExtra(SEARCHKEY, searchKey);
                break;
            case "按歌手播放":
                searchKey = JsonUitls.getString(jsonData, "artist");
                intent.putExtra("playtype", "MUSIC_ID");
                intent.putExtra(SEARCHTYPE, SEARCHTYPE_BYARTIST);
                intent.putExtra(SEARCHKEY, searchKey);
                break;
                default:
                    break;
        }

        ViomiSpeechManager.getInstance().speechContent("即将为您播放音乐");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("viomi.com.mojingface.action.MiguMusicMediaPlayActivity");
        MagicMirrorApplication.getAppContext().startActivity(intent);

//        BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//        currentActivity.startActivity(intent);
    }

}
