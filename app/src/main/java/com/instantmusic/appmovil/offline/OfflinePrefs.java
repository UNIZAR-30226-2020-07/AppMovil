package com.instantmusic.appmovil.offline;

import android.content.Context;
import android.content.SharedPreferences;

import com.instantmusic.appmovil.song.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the offline preferences
 * Data about saved songs
 */
class OfflinePrefs {

    // constants
    private static final String PREFS_NAME = "offline";
    private static final String IDS = "ids";
    private static final String SONG = "song";

    // variable
    private SharedPreferences prefs;

    /**
     * Base constructor
     *
     * @param cntx base context
     */
    OfflinePrefs(Context cntx) {
        this.prefs = cntx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @return the list of saved songs, empty list if none saved
     */
    ArrayList<Song> getSongs() {
        Set<String> set = prefs.getStringSet(IDS, Collections.<String>emptySet());

        // convert from string to int
        ArrayList<Song> ids = new ArrayList<>(set.size());
        for (String s : set) {
            try {
                ids.add(new Song(new JSONObject(prefs.getString(SONG + Integer.parseInt(s), ""))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ids;
    }

    /**
     * Saves the song data
     * @param song the song to save (only data)
     */
    void saveSong(Song song) {
        int id = song.id;

        Set<String> ids = prefs.getStringSet(IDS, new HashSet<String>());
        ids.add(Integer.toString(id));

        try {
            prefs.edit()
                    .putString(SONG + id, song.toJson())
                    .putStringSet(IDS, ids)
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the song data
     *
     * @param id id of the song to remove
     */
    void removeSong(int id) {
        Set<String> ids = prefs.getStringSet(IDS, new HashSet<String>());
        ids.remove(Integer.toString(id));

        prefs.edit()
                .remove(SONG + id)
                .putStringSet(IDS, ids)
                .apply();
    }

}
