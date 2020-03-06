package com.instantmusic.appmovil;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface serverInterface  {
    Cursor searchShit(String shit);
    long registUser(String mail,String pass,String user);
    int recover(String mail);
    int checkUser(String email);
    int login(String mail,String pass);
    int addSongToUser(String mail,String pass,String song);
    long addSong(String name,String artist,String categoria);
    String buscarCancion(long id);
    String buscarAutor(long id);
    void close();

}
