package viomi.com.mojingface.speech.skill;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.util.JsonUitls;


public class ToyMusicSkill {

    private static final String TAG = "ToyMusicSkill";

    public static void handleSkill(String data, Context context) {

        JsonObject rawJosn = JsonUitls.getJsonObject(data);
        String intentName = JsonUitls.getString(rawJosn, "intentName");

        switch (intentName) {
            case "音乐播放": {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("music_directive", data);
                intent.putExtra("playtype", "MUSIC_ID");
                intent.setAction("viomi.com.mojingface.action.MiguMusicMediaPlayActivity");
                MagicMirrorApplication.getAppContext().startActivity(intent);

//                BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                currentActivity.startActivity(intent);
            }
            break;

            case "本地音乐": {

            }
            break;

            case "查询歌手名": {

            }
            break;

            case "查询歌曲名": {

            }
            break;
        }
    }
}
