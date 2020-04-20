package com.instantmusic.appmovil.album;

import com.instantmusic.appmovil.song.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Album {
    public int id;
    public String name;
    public String artistName;
    public ArrayList<Song> songs = new ArrayList<>();

    // Constructor to convert JSON object into a Java class instance
    public Album(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.artistName = object.getJSONObject("artist").getString("name");
            JSONArray canciones = object.getJSONArray("songs");
            for ( int i = 0; i < canciones.length(); i++ ) {
                Song cancion = new Song(canciones.getJSONObject(i));
                this.songs.add(cancion);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Album> fromJson(JSONArray jsonObjects) {
        ArrayList<Album> albums = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                albums.add(new Album(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return albums;
    }
}
