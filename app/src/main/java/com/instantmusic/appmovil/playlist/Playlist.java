package com.instantmusic.appmovil.playlist;

import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Playlist {
    public String playlistName;
    public ArrayList<Song> songs;
    public String user;

    // Constructor to convert JSON object into a Java class instance
    public Playlist(JSONObject object){
        try {
            this.playlistName = object.getString("name");
            JSONArray playlistSongs = object.getJSONArray("songs");
            this.songs = Song.fromJson(playlistSongs);
            this.user = object.getString("user");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Playlist> fromJson(JSONArray jsonObjects) {
        ArrayList<Playlist> songs = new ArrayList<Playlist>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                songs.add(new Playlist(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }
}