package kanotLiveUploader.utils;

import org.json.JSONObject;

public class JsonUtils {

    public static String formatJson(String jsonString){
        JSONObject json = new JSONObject(jsonString); // Convert text to object
        return json.toString(4); // Print it with specified indentation
    }
}
