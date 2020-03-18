package com.instantmusic.appmovil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Utils {
    public static String listifyErrors(JSONObject data) {
        StringBuilder message = new StringBuilder("");

        Iterator<String> keys = data.keys();
        if (keys.hasNext()) {
            String key = keys.next();
            try {
                JSONArray messages = data.getJSONArray(key);
                for (int i = 0; i < messages.length(); i++) {
                    message.append(messages.getString(i));
                }
            } catch (JSONException ignore) {
            }
        }
        return message.toString();
    }
}
