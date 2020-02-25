package com.instantmusic.appmovil;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface serverInterface  {
    Cursor searchCancion(String song);
    int registuser(String mail,String pass);
    int recover(String mail);
    int login(String mail,String pass);

}
