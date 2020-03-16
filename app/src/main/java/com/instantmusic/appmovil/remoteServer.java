package com.instantmusic.appmovil;

import android.database.Cursor;

import org.json.JSONObject;

import io.swagger.client.ApiException;
import io.swagger.client.JSON;
import io.swagger.client.api.RestAuthApi;
import io.swagger.client.model.Key;
import io.swagger.client.model.Login;
import io.swagger.client.model.Register;

public class remoteServer implements serverInterface {
    private static final int ACTIVITY_CREATE = 0;
    private RestAuthApi serverRequest;

    remoteServer() {
    }

    public Cursor searchShit(String shit) {
        JSONObject serverRes = new JSONObject();
        return null;
    }

    public int registUser(String mail, String pass1, String pass2, String user) {
        String serverRes;
        Key key = new Key();
        Register regist = new Register();
        regist.setPassword1(pass1);
        regist.setPassword2(pass2);
        regist.setUsername(user);
        regist.setEmail(mail);
        try {
            key = serverRequest.restAuthRegistrationCreate(regist);
            if (key.hashCode() == 1) {
                serverRes = key.toString();
                System.out.println(serverRes);
            }
            System.out.println(key.hashCode());
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return serverRes;
    }

    public int checkUser(String mail) {
        return 0;
    }

    public int login(String mail, String pass) {
        String serverRes;
        Key key = new Key();
        io.swagger.client.model.Login log = new Login();
        log.setPassword(pass);
        log.setEmail(mail);
        try {
            key = serverRequest.restAuthLoginCreate(log);
            if (key.hashCode() == 1) {
                serverRes = key.toString();
                System.out.println(serverRes);
                System.out.println(key.hashCode());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return 1;//error inesperado
        }

        return 0;
    }

    public Cursor infoUser(String email) {
        return null;
    }

    public int deleteUser(String email) {
        JSONObject serverRes = new JSONObject();
        if (regist.deleteUser(email)) {
            return 0;
        }
        return 1;
    }

    public int songInfo(String name, String artist, String categoria) {
        return 0;
    }

    public Cursor buscarCancion(String song) {
        JSONObject serverRes = new JSONObject();
        return regist.buscarCancion(song);
    }

    public Cursor buscarArtista(String artist) {
        JSONObject serverRes = new JSONObject();
        return regist.buscarArtista(artist);
    }
    public Cursor buscarCategoria(String artist) {
        JSONObject serverRes = new JSONObject();
        return regist.buscarCategoria(artist);
    }

    public Cursor buscarAlbum(String artist) {
        JSONObject serverRes = new JSONObject();
        return regist.buscarAlbum(artist);
    }


    public Cursor allPlaylists(String user) {
        return regist.searchPlaylists(user);

    }

    public long addSong(String name, String artist, String category) {
        return regist.addSong(name, artist, category);
    }


    public long addPlaylist(String playlist, String author) {
        regist.addPlaylist(playlist, author);
        return 0;
    }

    public int recover(String mail) {
        return 0;
    }

    public int addSongToPlaylist(String playlist, String song, String author) {
        regist.addSongToPlaylist(playlist, song, author);
        return 0;
    }
}