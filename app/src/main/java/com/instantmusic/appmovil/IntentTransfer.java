package com.instantmusic.appmovil;

import java.util.HashMap;

/**
 * A static key-value container.
 * To use when transferring data between activities
 */
public class IntentTransfer {
    private static HashMap<String, Object> data = new HashMap<>();

    /**
     * Saves data, call before startActivity
     *
     * @param key   key to identify this data
     * @param value any data
     * @param <T>   class of the data
     */
    public static <T> void setData(String key, T value) {
        IntentTransfer.data.put(key, value);
    }

    /**
     * Restores data, call in onCreate
     *
     * @param key same key used in setData
     * @param <T> class of the data (to avoid external cast)
     */
    public static <T> T getData(String key) {
        return (T) data.remove(key);
    }

    /**
     * Checks data, call before getData if needed
     *
     * @param key same key used in setData
     * @return true iff there is saved data
     */
    public static boolean hasData(String key) {
        return data.containsKey(key);
    }
}
