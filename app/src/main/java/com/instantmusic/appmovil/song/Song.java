package com.instantmusic.appmovil.song;

import android.annotation.SuppressLint;

import com.instantmusic.appmovil.album.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Song {
    public String songName;
    public String artist;
    public String category;
    public int duration;
    public String url;
    public int id;
    public int rate;
    public double rate_average;
    public JSONObject album;
    public String fecha;

    // Constructor to convert JSON object into a Java class instance
    @SuppressLint("SimpleDateFormat")
    public Song(JSONObject object) {
        try {
            album = object.getJSONObject("album");
            this.artist = album.getJSONObject("artist").getString("name");
            this.songName = object.getString("title");
            this.category = object.getString("genre");
            this.duration = object.getInt("duration");
            this.url = object.getString("stream_url");
            this.id = object.getInt("id");
            this.rate = object.optInt("user_valoration", 0);
            this.rate_average = object.optDouble("avg_valoration", 0);
            this.fecha = object.getString("created_at");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Song(JSONObject object, Album album) {
        try {
            this.artist = album.artistName;
            this.songName = object.getString("title");
            this.category = object.getString("genre");
            this.duration = object.getInt("duration");
            this.url = object.getString("stream_url");
            this.id = object.getInt("id");
            this.rate = object.optInt("user_valoration", 0);
            this.rate_average = object.optDouble("avg_valoration", 0);
            this.fecha = object.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Song(JSONObject object, String artistName) {
        try {
            this.artist = artistName;
            this.songName = object.getString("title");
            this.category = object.getString("genre");
            this.duration = object.getInt("duration");
            this.url = object.getString("stream_url");
            this.id = object.getInt("id");
            this.rate = object.optInt("user_valoration", 0);
            this.rate_average = object.optDouble("avg_valoration", 0);
            this.fecha = object.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Song> fromJson(JSONArray jsonObjects, boolean tieneObjeto, Album album) {
        ArrayList<Song> songs = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                if (tieneObjeto) {
                    songs.add(new Song(jsonObjects.getJSONObject(i)));
                } else {
                    songs.add(new Song(jsonObjects.getJSONObject(i), album));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }

    public String toJson() throws JSONException {
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
