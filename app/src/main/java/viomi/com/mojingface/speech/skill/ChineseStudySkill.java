package viomi.com.mojingface.speech.skill;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.util.JsonUitls;


/**
 * Created by Mocc on 2018/7/26
 */
public class ChineseStudySkill {
    private static final String TAG = "ChineseStudySkill";

    public static void handleSkill(String data, Context context) {

        JsonObject rawJosn = JsonUitls.getJsonObject(data);
        String intentName = JsonUitls.getString(rawJosn, "intentName");

        switch (intentName) {
            case "播放国学": {
                JsonArray content = JsonUitls.getJsonArray(rawJosn, "content");
                if (content.size() > 0) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("music_directive", data);
                    intent.putExtra("playtype", "CHINESE_STUDY_ID");
                    intent.setAction("viomi.com.mojingface.action.MiguMusicMediaPlayActivity");
                    MagicMirrorApplication.getAppContext().startActivity(intent);

//                    BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//                    currentActivity.startActivity(intent);
                }
            }
            break;

            case "查询上文": {

            }
            break;

            case "查询下文": {

            }
            break;

            case "查询出处": {
            }
            break;

            case "查询作者": {
            }
            break;

            case "查询简介": {
            }
            break;

            case "查询释义": {
            }
            break;

            default:
                break;
        }
    }
}
