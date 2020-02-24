package com.instantmusic.appmovil;



public interface serverInterface {
    int searchCancion(String song);
    int registuser(String mail,String pass);
    int recover(String mail);
    int login(String mail,String pass);

}
