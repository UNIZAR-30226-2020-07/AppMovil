package com.instantmusic.appmovil.offline;

import com.instantmusic.appmovil.song.Song;

import org.json.JSONException;
import org.json.JSONObject;

public class Fixes {

    public static String songToJson(Song song) throws JSONException {
        return dataToJson(song.artist, song.songName, song.category, song.duration, song.url, song.id, song.rate, song.rate_average);
    }

    public static String dataToJson(String artist, String songName, String category, int duration, String url, int id, int rate, double rate_average) throws JSONException {
        return new JSONObject()
                .put("album", new JSONObject().put(
                        "artist", new JSONObject().put(
                                "name", artist
                        )
                        )
                )
                .put("title", songName)
                .put("genre", category)
                .put("duration", duration)
                .put("stream_url", url)
                .put("id", id)
                .put("user_valoration", rate)
                .put("avg_valoration", rate_average)
                .toString();
    }
}
