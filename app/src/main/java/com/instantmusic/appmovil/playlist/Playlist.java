package com.instantmusic.appmovil.playlist;

import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Playlist {
    public String playlistName;
    public ArrayList<Integer> songs;
    public String user;
    public int id;
    // Constructor to convert JSON object into a Java class instance
    public Playlist(JSONObject object){
        try {
            this.songs = new ArrayList<>();
            this.playlistName = object.getString("name");
            this.user = object.getString("user");
            this.id = object.getInt("id");
            JSONArray canciones = object.getJSONArray("songs");
            for ( int i = 0; i < canciones.length(); i++ ) {
                try {
                    this.songs.add(canciones.getInt(i));
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Playlist> fromJson(JSONArray jsonObjects) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                playlists.add(new Playlist(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlists;
    }
}