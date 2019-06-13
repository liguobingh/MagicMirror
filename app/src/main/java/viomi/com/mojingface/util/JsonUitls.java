package viomi.com.mojingface.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by viomi on 2016/10/25.
 * json解析封装
 */

public class JsonUitls {

    public static String getString(JsonObject json, String name) {
        String result;
        if (json != null) {
            try {
                JsonElement element = json.get(name);
                if (null != element) {
                    result = element.getAsString();
                } else {
                    result = "";
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                result = "";
            }
        } else {
            result = "";
        }
        return result;
    }

    public static JsonObject getJsonObject(String jsonStr) {
        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        if (jsonObject == null || jsonStr == null) {
            jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        }
        return jsonObject;
    }

    public static JsonObject getJsonObject(JsonObject json, String name) {
        JsonObject jsonObject;
        if (json != null) {
            try {
                JsonElement element = json.get(name);
                if (null != element) {
                    jsonObject = element.getAsJsonObject();
                } else {
                    jsonObject = new JsonObject();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                jsonObject = new JsonObject();
            }
        } else {
            jsonObject = new JsonObject();
        }
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }

    public static JsonObject getJsonObject(JsonArray array, int index) {
        JsonObject jsonObject = null;
        try {
            if (null != array) {
                JsonElement element = array.get(index);
                if (null != element) {
                    jsonObject = element.getAsJsonObject();
                } else {
                    jsonObject = new JsonObject();
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }


    public static JsonArray getJsonArray(JsonObject json, String name) {
        JsonArray jsonArray = null;
        if (json != null) {
            try {
                if (null != json) {
                    JsonElement element = json.get(name);
                    if (null != element) {
                        jsonArray = element.getAsJsonArray();
                    } else {
                        jsonArray = new JsonArray();
                    }
                } else {
                    jsonArray = new JsonArray();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                jsonArray = new JsonArray();
            }
        }
        return jsonArray;
    }

    public static int getInt(JsonObject json, String name) {
        int result = 0;
        if (json == null) {
            return result;
        }
        try {
            JsonElement element = json.get(name);
            if (null != element) {
                result = json.get(name).getAsInt();
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getDouble(JsonObject json, String name) {
        double result = 0d;
        if (json == null) {
            return result;
        }
        try {
            JsonElement element = json.get(name);
            if (null != element) {
                result = element.getAsDouble();
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long getLong(JsonObject json, String name) {
        long result = 0L;
        if (json == null) {
            return result;
        }
        try {
            JsonElement element = json.get(name);
            if (null != element) {
                result = json.get(name).getAsLong();
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

}
