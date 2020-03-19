package com.instantmusic.appmovil;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface serverInterface  {
    Cursor searchShit(String shit);
    void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener);
    void login(String username_email, String password, JSONConnection.Listener listener);
    int recover(String mail);
    int checkUser(String email);
    Cursor infoUser(String email);
    int addSongToPlaylist(String playlist, String song);
    long addSong(String name,String artist,String categoria);
    long addPlaylist(String playlist,String author);
    Cursor buscarCancion(String song);
    Cursor buscarArtista(String artist);
    Cursor allPlaylists(String user);
    void close();

}
