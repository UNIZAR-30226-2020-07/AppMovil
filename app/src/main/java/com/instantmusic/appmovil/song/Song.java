package com.instantmusic.appmovil.song;

import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;

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
            serverInterface server = new remoteServer();
            if ( esInicio ) {
                JSONObject album = object.getJSONObject("album");
                this.artist = album.getJSONObject("artist").getString("name");
            }
            else {
                server.getAlbumData(object.getInt("album"), new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if ( responseCode == 200 ) {
                            Album albumSelected = new Album(data);
                            Song.this.artist = albumSelected.artistName;
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable throwable) {

                    }
                });
            }
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
        ArrayList<Song> songs = new ArrayList<>();
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
