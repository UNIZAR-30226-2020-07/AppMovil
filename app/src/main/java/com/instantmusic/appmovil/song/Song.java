package com.instantmusic.appmovil.song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Song {
    public String songName;
    public String artist;
    public String category;
    public int duration;
    public String url;
    public int id;
    public int rate;

    // Constructor to convert JSON object into a Java class instance
    public Song(JSONObject object, boolean esInicio){
        try {
            if ( esInicio ) {
                JSONObject album = object.getJSONObject("album");
                this.artist = album.getJSONObject("artist").getString("name");
            }
            /*else {
               this.artist = object.getString("name");
            }

             */
            this.songName = object.getString("title");
            this.category = object.getString("genre");
            this.duration = object.getInt("duration");
            this.url = object.getString("stream_url");
            this.id = object.getInt("id");
            this.rate = object.optInt("user_valoration",0);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Song> fromJson(JSONArray jsonObjects, boolean esInicio) {
        ArrayList<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                songs.add(new Song(jsonObjects.getJSONObject(i), esInicio));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }
}
