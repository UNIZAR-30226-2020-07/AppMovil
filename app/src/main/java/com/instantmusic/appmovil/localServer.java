package com.instantmusic.appmovil;

import android.content.Intent;
import com.instantmusic.appmovil.Login;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class localServer implements serverInterface {
    private static final int ACTIVITY_CREATE =0;
    localServer(){}
    public int searchCancion(String song) {
            return 0;
    }

    public int registuser(String mail, String pass) {
        return 0;
    }

    public int recover(String mail) {
        return 0;
    }public int login(String mail,String pass) {
        return 0;
    }

}
