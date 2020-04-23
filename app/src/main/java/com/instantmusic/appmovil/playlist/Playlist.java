package com.instantmusic.appmovil.playlist;

import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Playlist {
    public String playlistName;
    public ArrayList<Song> songs = new ArrayList<>();
    public String user;
    public int id;
    // Constructor to convert JSON object into a Java class instance
    public Playlist(JSONObject object, boolean esInicio){
        try {
            serverInterface server = new remoteServer();
            this.songs = new ArrayList<>();
            this.playlistName = object.getString("name");
            this.user = object.getString("user");
            this.id = object.getInt("id");
            if ( esInicio ) {
                server.getPlaylistData(this.id, new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if ( responseCode == 200 ) {
                            try {
                                for ( int i = 0; i < data.getJSONArray("songs").length(); i++ ) {
                                    Song cancion = new Song(data.getJSONArray("songs").getJSONObject(i));
                                    Playlist.this.songs.add(cancion);
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
    public static ArrayList<Playlist> fromJson(JSONArray jsonObjects, boolean esInicio) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                playlists.add(new Playlist(jsonObjects.getJSONObject(i), esInicio));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlists;
    }
}