package viomi.com.mojingface.speech.skill;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.util.JsonUitls;


public class StorySkill {

    private static final String TAG = "StorySkill";

    public static void handleSkill(String data, Context context) {

        JsonObject rawJosn = JsonUitls.getJsonObject(data);
        String intentName = JsonUitls.getString(rawJosn, "intentName");

        switch (intentName) {
            case "播放故事": {
                JsonArray content = JsonUitls.getJsonArray(rawJosn, "content");
                if (content.size() > 0) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("music_directive", data);
                    intent.putExtra("playtype", "STORY_ID");
                    intent.setAction("viomi.com.mojingface.action.MiguMusicMediaPlayActivity");
                    MagicMirrorApplication.getAppContext().startActivity(intent);

//                    BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                    currentActivity.startActivity(intent);
                }
            }
            break;

            case "播放控制": {
            }
            break;

            case "查询故事信息": {
            }
            break;

            case "新手引导": {
            }
            break;

            default:
                break;
        }
    }
}
