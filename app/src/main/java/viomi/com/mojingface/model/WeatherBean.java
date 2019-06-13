package viomi.com.mojingface.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mocc on 2018/1/29
 */

public class WeatherBean implements Parcelable {

    private String date;
    private String distrct;
    private String pm25;
    private String temperature;
    //"weather" : "多云转阵雨" ,
    private String weather_dayTime;
    //"wind" : "无持续风向,3级"
    private String wind;

    public WeatherBean() {

    }

    public WeatherBean(String date, String distrct, String pm25, String temperature, String weather_dayTime) {
        this.date = date;
        this.distrct = distrct;
        this.pm25 = pm25;
        this.temperature = temperature;
        this.weather_dayTime = weather_dayTime;
    }

    public WeatherBean(String date, String distrct, String pm25, String temperature, String weather_dayTime, String wind) {
        this.date = date;
        this.distrct = distrct;
        this.pm25 = pm25;
        this.temperature = temperature;
        this.weather_dayTime = weather_dayTime;
        this.wind = wind;

    }

    protected WeatherBean(Parcel in) {
        date = in.readString();
        distrct = in.readString();
        pm25 = in.readString();
        temperature = in.readString();
        weather_dayTime = in.readString();
        wind = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(distrct);
        dest.writeString(pm25);
        dest.writeString(temperature);
        dest.writeString(weather_dayTime);
        dest.writeString(wind);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherBean> CREATOR = new Creator<WeatherBean>() {
        @Override
        public WeatherBean createFromParcel(Parcel in) {
            return new WeatherBean(in);
        }

        @Override
        public WeatherBean[] newArray(int size) {
            return new WeatherBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrct() {
        return distrct;
    }

    public void setDistrct(String distrct) {
        this.distrct = distrct;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather_dayTime() {
        return weather_dayTime;
    }

    public void setWeather_dayTime(String weather_dayTime) {
        this.weather_dayTime = weather_dayTime;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}