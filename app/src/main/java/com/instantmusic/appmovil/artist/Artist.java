package com.instantmusic.appmovil.artist;

import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.song.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Artist {
    public int id;
    public String name;
    public ArrayList<Album> albums = new ArrayList<>();

    // Constructor to convert JSON object into a Java class instance
    public Artist(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            JSONArray albumes = object.getJSONArray("albums");
            for ( int i = 0; i < albumes.length(); i++ ) {
                Album album = new Album(albumes.getJSONObject(i));
                this.albums.add(album);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Artist> fromJson(JSONArray jsonObjects) {
        ArrayList<Artist> artists = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                artists.add(new Artist(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return artists;
    }
}
