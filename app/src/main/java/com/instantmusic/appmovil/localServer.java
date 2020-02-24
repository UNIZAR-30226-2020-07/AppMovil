package com.instantmusic.appmovil;

import android.content.Intent;
import com.instantmusic.appmovil.Login;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class localServer implements serverInterface {
    private static final int ACTIVITY_CREATE =0 ;
    private serverInterface implementor;
    
    public void searchCancion(String song) {
        implementor.searchCancion(song);
    }

    public void registuser(String mail, String pass) {
        implementor.registuser(mail,pass);
    }

    public void recover(String mail) {
        implementor.recover(mail);
    }public int login(String mail,String pass) {
        return 0;
    }

}
