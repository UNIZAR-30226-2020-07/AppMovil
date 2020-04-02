package com.instantmusic.appmovil.server;


import android.database.Cursor;

import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;

import java.util.ArrayList;

public interface serverInterface  {
    void searchShit(String shit, JSONConnection.Listener listener );
    void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener);
    void getUserData(JSONConnection.Listener listener);
    void getPlaylistData(int idPlaylist, JSONConnection.Listener listener);
    void getSongData(int idSong, JSONConnection.Listener listener);
    void login(String username_email, String password, JSONConnection.Listener listener);
    int recover(String mail);
    int checkUser(String email);
    Cursor infoUser(String email);
    void addSongToPlaylist(String namePlaylist, int idPlaylist, ArrayList<Integer> songs, JSONConnection.Listener listener);
    long addSong(String name,String artist,String categoria);
    long getSong(String title,JSONConnection.Listener listener);
    void addPlaylist(String playlist, JSONConnection.Listener listener);
    Cursor searchPlaylist(String playlist);
    Cursor buscarCancion(String song);
    Cursor buscarArtista(String artist);
    Cursor allPlaylists(String user);
    void close();

}
