package com.instantmusic.appmovil;

import android.content.Intent;
import android.database.Cursor;

import com.instantmusic.appmovil.Login;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class localServer implements serverInterface {
    private static final int ACTIVITY_CREATE =0;
    private UsersDbAdapter localDb;
    public Cursor searchShit(String shit) {
            return localDb.searchShit(shit);
    }

    public int registUser(String mail, String pass) {
        localDb.createUser(mail,pass);
        return 0;
    }
    public Cursor checkUser(String mail) {
        return localDb.checkUser(mail);
    }
    public int recover(String mail) {
        return 0;
    }public int login(String mail,String pass) {
        return 0;
    }
    public int addSong(String mail,String pass,String song,String playlist){
        if(localDb.updateUser(mail,pass,song,playlist)){
        return 0;
        }else{
            return 1;
        }
        }

    }



