package com.instantmusic.appmovil.server;


import android.database.Cursor;

import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;

import java.util.ArrayList;

public interface serverInterface  {
    void searchShit(String shit, JSONConnection.Listener listener );
    void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener);
    void getUserData(JSONConnection.Listener listener);
    void login(String username_email, String password, JSONConnection.Listener listener);
    int recover(String mail);
    int checkUser(String email);
    Cursor infoUser(String email);
    int addSongToPlaylist(String playlist, String song);
    long addSong(String name,String artist,String categoria);
    long addPlaylist(String playlist, ArrayList<Playlist> playlists,JSONConnection.Listener listener);
    Cursor searchPlaylist(String playlist);
    Cursor buscarCancion(String song);
    Cursor buscarArtista(String artist);
    Cursor allPlaylists(String user);
    void close();

}
