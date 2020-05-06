package com.instantmusic.appmovil.album;

import com.instantmusic.appmovil.artist.Artist;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Album {
    public int id;
    public String name;
    public String artistName;
    public int artistId;
    public boolean esPodcast;
    public ArrayList<Song> songs = new ArrayList<>();

    // Constructor to convert JSON object into a Java class instance
    public Album(JSONObject object, boolean podcast){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.artistName = object.getJSONObject("artist").getString("name");
            this.artistId=object.getJSONObject("artist").getInt("id");
            this.esPodcast=object.getBoolean("podcast");

            if ( podcast ) {
                serverInterface server = new remoteServer();
                server.getAlbumData(this.id, new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if ( responseCode == 200 ) {
                            Album album = new Album(data,false);
                            Album.this.songs = album.songs;
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
                    Song cancion = new Song(canciones.getJSONObject(i), this.artistName);
                    this.songs.add(cancion);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Album(JSONObject object, Artist artista){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.artistName = artista.name;
            this.esPodcast=object.getBoolean("podcast");
            serverInterface server = new remoteServer();
            server.getAlbumData(this.id, new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) {
                    if ( responseCode == 200 ) {
                        Album album = new Album(data,false);
                        Album.this.songs = album.songs;
                    }
                }

                @Override
                public void onErrorResponse(Throwable throwable) {
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Album(JSONObject object, String artist ) {

        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.artistName = artist;
            this.esPodcast=object.getBoolean("podcast");
            serverInterface server = new remoteServer();
            server.getAlbumData(this.id, new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) {
                    if ( responseCode == 200 ) {
                        Album album = new Album(data,false);
                        Album.this.songs = album.songs;
                    }
                }

                @Override
                public void onErrorResponse(Throwable throwable) {
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    public static ArrayList<Album> fromJson(JSONArray jsonObjects, boolean tieneObjeto, Artist artista, boolean podcast) {
        ArrayList<Album> albums = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                if ( tieneObjeto ) {
                    albums.add(new Album(jsonObjects.getJSONObject(i), artista));
                }
                else {
                    albums.add(new Album(jsonObjects.getJSONObject(i), podcast));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return albums;
    }
}
