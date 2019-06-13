package viomi.com.mojingface.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import viomi.com.mojingface.mediaplayer.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mocc on 2018/3/4
 */

public class ParsePlayList {

    public static List<Track> parseMusic(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }

        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");
        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String subTitle = JsonUitls.getString(item, "subTitle");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            String label = JsonUitls.getString(item, "label");
            list.add(new Track(title, subTitle, label, linkUrl, imageUrl));
        }
        return list;
    }

    public static List<Track> parseContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");
        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String artist = JsonUitls.getString(item, "artist");
            String album = JsonUitls.getString(item, "album");
            String url = JsonUitls.getString(item, "linkUrl");
            String img = JsonUitls.getString(item, "imageUrl");
            String lrcUrl = JsonUitls.getString(item, "lrcUrl");
            list.add(new Track(title, artist, album, url, img));
        }
        return list;
    }

    public static List<Track> parseToyNews(String music_directive) {
        List<Track> list = new ArrayList<>();
        if (music_directive == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(music_directive);
        JsonArray content = JsonUitls.getJsonArray(json, "content");
        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            JsonObject itemExtra = JsonUitls.getJsonObject(item, "extra");
            String title = JsonUitls.getString(itemExtra, "title");
            String catalog_name = JsonUitls.getString(itemExtra, "catalog_name");
            String source = JsonUitls.getString(itemExtra, "source");
            String audio = JsonUitls.getString(itemExtra, "audio");
            String image = JsonUitls.getString(itemExtra, "image");
            list.add(new Track(title, catalog_name, source, audio, image));
        }
        return list;
    }

    public static List<Track> parseLTNews(String music_directive) {

        List<Track> list = new ArrayList<>();
        if (music_directive == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(music_directive);
        JsonArray content = JsonUitls.getJsonArray(json, "content");
        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            JsonObject itemExtra = JsonUitls.getJsonObject(item, "extra");
            String title = JsonUitls.getString(itemExtra, "title");
            String catalog_name = JsonUitls.getString(itemExtra, "catalog_name");
            String source = JsonUitls.getString(itemExtra, "source");
            String audio = JsonUitls.getString(itemExtra, "audio");
            String image = JsonUitls.getString(itemExtra, "image");
            list.add(new Track(title, catalog_name, source, audio, image));
        }
        return list;
    }

    public static List<Track> parseToyStory(String music_directive) {
        List<Track> list = new ArrayList<>();
        if (music_directive == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(music_directive);
        JsonArray content = JsonUitls.getJsonArray(json, "content");
        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, null, null, linkUrl, imageUrl));
        }
        return list;
    }

    public static List<Track> parseInsideMusicContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");

        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String subTitle = JsonUitls.getString(item, "subTitle");
            String label = JsonUitls.getString(item, "label");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, subTitle, label, linkUrl, imageUrl));
        }
        return list;
    }

    public static List<Track> parseFolkArtContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");

        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, null, null, linkUrl, imageUrl));
        }
        return list;
    }

    public static List<Track> parseFunContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");

        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, null, null, linkUrl, imageUrl));
        }
        return list;
    }

    public static List<Track> parseChildSongContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");

        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, null, null, linkUrl, imageUrl));
        }
        return list;
    }


    public static List<Track> parseStoryContent(String jsonstr) {
        List<Track> list = new ArrayList<>();
        if (jsonstr == null) {
            return list;
        }
        JsonObject json = JsonUitls.getJsonObject(jsonstr);
        JsonArray content = JsonUitls.getJsonArray(json, "content");

        for (int i = 0; i < content.size(); i++) {
            JsonObject item = JsonUitls.getJsonObject(content, i);
            String title = JsonUitls.getString(item, "title");
            String linkUrl = JsonUitls.getString(item, "linkUrl");
            String imageUrl = JsonUitls.getString(item, "imageUrl");
            list.add(new Track(title, null, null, linkUrl, imageUrl));
        }
        return list;
    }


}
