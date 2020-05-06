package com.instantmusic.appmovil.friends;

import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Friend {
    public int id;
    public String username;
    public String mail;
    public ArrayList<Playlist> playlists;
    public JSONArray friends;
    public JSONArray albums;
    // Constructor to convert JSON object into a Java class instance
    public Friend(JSONObject object) {
        try {
            JSONArray playlistsUser = object.getJSONArray("playlists");
            this.mail=object.getString("email");
            this.username=object.getString("username");
            this.playlists = Playlist.fromJson(playlistsUser, true);
            this.id = object.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Friend> fromJson(JSONArray jsonObjects) {
        ArrayList<Friend> songs = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                songs.add(new Friend(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }

    public String toJson() throws JSONException {
        return new JSONObject()
                .put("username", username)
                .put("mail", mail)
                .put("playlists", playlists)
                .put("friends", friends)
                .put("id", id)
                .put("albums", albums)
                .toString();
    }
}
