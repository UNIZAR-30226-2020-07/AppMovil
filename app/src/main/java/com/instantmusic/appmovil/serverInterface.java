package com.instantmusic.appmovil;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface serverInterface  {
    Cursor searchShit(String shit);
    long registUser(String mail,String pass,String user);
    int recover(String mail);
    int checkUser(String email);
    int login(String mail,String pass);
    int addSongToPlaylist(String playlist, String song,String author);
    long addSong(String name,String artist,String categoria);
    long addPlaylist(String playlist,String author);
    Cursor buscarCancion(String song);
    Cursor buscarArtista(String artist);
   // Cursor allPlaylists(String user);
    void close();

}
