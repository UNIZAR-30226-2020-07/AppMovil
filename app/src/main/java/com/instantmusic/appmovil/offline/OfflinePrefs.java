package com.instantmusic.appmovil.offline;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

public class OfflinePrefs {

    private SharedPreferences prefs;
    private static final String IDS = "ids";
    private static final String SONG = "song";

    OfflinePrefs(Context cntx) {
        this.prefs = cntx.getSharedPreferences("offline", Context.MODE_PRIVATE);
    }


    ArrayList<Song> getSongs() {
        Set<String> set = prefs.getStringSet(IDS, Collections.<String>emptySet());

        ArrayList<Song> ids = new ArrayList<>(set.size());
        for (String s : set) {
            ids.add(getSong(Integer.parseInt(s)));
        }
        return ids;
    }

    Song getSong(int id) {
        try {
            return new Song(new JSONObject(prefs.getString(SONG + id, "")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    void saveSong(Song song) {
        int id = song.id;

        Set<String> ids = prefs.getStringSet(IDS, new HashSet<String>());
        ids.add(Integer.toString(id));

        try {
            prefs.edit()
                    .putString(SONG + id, Fixes.songToJson(song))
                    .putStringSet(IDS, ids)
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void deleteSong(int id) {
        Set<String> ids = prefs.getStringSet(IDS, new HashSet<String>());
        ids.remove(Integer.toString(id));

        prefs.edit()
                .remove(SONG + id)
                .putStringSet(IDS, ids)
                .apply();
    }

}
