package viomi.com.mojingface.speech.skill;

import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.model.WeatherBean;
import viomi.com.mojingface.util.JsonUitls;
import viomi.com.mojingface.util.LogUtils;


/**
 * Created by Mocc on 2018/7/18
 */
public class NewWeatherSkill {

    private static final String TAG = "NewWeatherSkill";

    public static void handleSkill(String data) {

        JsonObject rawJosn = JsonUitls.getJsonObject(data);
        String intentName = JsonUitls.getString(rawJosn, "intentName");
        LogUtils.e(TAG, intentName);

        switch (intentName) {

            case "查询风力":
            case "查询气象":
            case "查询温度":
            case "查询风向":
            case "查询日出日落":
            case "查询天气": {
            }
            break;

            case "天气数据后台获取":
                String city = JsonUitls.getString(rawJosn, "cityName");
                JsonArray forecastChoose = JsonUitls.getJsonArray(rawJosn, "forecastChoose");

                if (forecastChoose.size() > 0) {
                    JsonObject item = JsonUitls.getJsonObject(forecastChoose, 0);
                    WeatherBean bean = new WeatherBean();
                    bean.setDistrct(city);
                    bean.setDate(JsonUitls.getString(item, "predictDate"));
                    bean.setTemperature(JsonUitls.getString(item, "tempDay"));
                    bean.setWeather_dayTime(JsonUitls.getString(item, "conditionDay"));
                    bean.setWind(JsonUitls.getString(item, "windDirDay"));

                    Intent intent = new Intent(BroadcastAction.VIOCESERVICE_WEATHER);
                    intent.putExtra("VOICE_WEATHER", bean);
                    MagicMirrorApplication.getAppContext().sendBroadcast(intent);
                }
                break;

            case "查询湿度":
            case "查询空气质量":
            case "查询紫外线":
            case "查询活动":
            case "查询装备":
            case "穿衣推荐":
            case "查询日期":
            case "查询城市":
            case "查询场景":
            case "查询护肤品":
            case "查询能见度":
            case "查询指数":
            case "查询降水量":
            case "查询降雪量":
            case "新手引导":
            case "非城市天气处理":
                break;
                default:
        }
    }
}
