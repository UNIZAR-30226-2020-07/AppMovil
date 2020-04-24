package com.instantmusic.appmovil.podcast;

import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Podcast {
    public String playlistName;
    public ArrayList<Song> songs = new ArrayList<>();
    public String user;
    public int id;
    // Constructor to convert JSON object into a Java class instance
    public Podcast(JSONObject object, boolean esInicio){
        try {
            serverInterface server = new remoteServer();
            this.songs = new ArrayList<>();
            this.playlistName = object.getString("title");
            this.user = object.getJSONObject("artist").getString("name");
            this.id = object.getInt("id");
            if ( esInicio ) {
                server.getPlaylistData(this.id, new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if ( responseCode == 200 ) {
                            try {
                                for ( int i = 0; i < data.getJSONArray("songs").length(); i++ ) {
                                    Song cancion = new Song(data.getJSONArray("songs").getJSONObject(i));
                                    Podcast.this.songs.add(cancion);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    @Override
                    public void onErrorResponse(Throwable throwable) {
                    }
                });
            }
            else {
                JSONArray canciones = object.getJSONArray("songs");
                for ( int i = 0; i < canciones.length(); i++ ) {
                    Song cancion = new Song(canciones.getJSONObject(i));
                    this.songs.add(cancion);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Podcast> fromJson(JSONArray jsonObjects, boolean esInicio) {
        ArrayList<Podcast> playlists = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                playlists.add(new Podcast(jsonObjects.getJSONObject(i), esInicio));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlists;
    }
}