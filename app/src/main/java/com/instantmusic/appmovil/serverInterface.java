package com.instantmusic.appmovil;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface serverInterface  {
    void openDb();
    Cursor searchShit(String shit);
    long registUser(String mail,String pass);
    int recover(String mail);
    Cursor checkUser(String email);
    int login(String mail,String pass);
    int addSong(String mail,String pass,String song,String playlist);

}
