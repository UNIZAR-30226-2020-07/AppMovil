package com.instantmusic.appmovil;

public class localServer implements serverInterface {
    private serverInterface implementor;
    public void searchCancion(String song) {
        implementor.searchCancion(song);
    }

    public void registuser(String mail, String pass) {
        implementor.registuser(mail,pass);
    }

    public void recover(String mail) {
        implementor.recover(mail);
    }public void login(String mail,String pass) {

    }

}
