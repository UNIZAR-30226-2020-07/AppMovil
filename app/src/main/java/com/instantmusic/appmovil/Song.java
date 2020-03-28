package com.instantmusic.appmovil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Song {
    public String songName;
    public String artist;
    public String category;

    // Constructor to convert JSON object into a Java class instance
    public Song(JSONObject object){
        try {
            JSONObject album = object.getJSONObject("album");
            this.songName = object.getString("title");
            this.artist = album.getJSONObject("artist").getString("name");
            this.category = object.getString("genre");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Song> fromJson(JSONArray jsonObjects) {
        ArrayList<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                songs.add(new Song(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }
}
