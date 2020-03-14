package com.instantmusic.appmovil;

import android.content.Context;
import android.database.Cursor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.swagger.client.JSON;
import io.swagger.client.api.RestAuthApi;
import io.swagger.client.auth.HttpBasicAuth;
import io.swagger.client.model.Register;

public class remoteServer implements serverInterface {
    private static final int ACTIVITY_CREATE = 0;
    private RestAuthApi serverRequest;
    private String BASE_URL= "https://ps-20-server-django-app.herokuapp.com/api/v1";

    remoteServer() { }
    public Cursor searchShit(String shit) {
        JSONObject serverRes = new JSONObject();
        return regist.searchShit(shit);
    }

    public long registUser(String mail, String pass1,String pass2, String user) {
        JSONObject serverRes = new JSONObject();
        Register regist=new Register();
        regist.setPassword1(pass1);
        regist.setPassword2(pass2);
        regist.setUsername(user);
        regist.setEmail(mail);
        return 0;
    }

    public int checkUser(String mail) {
        return 0;
    }

    @Override
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

    @Override
    public Cursor allPlaylists(String user) {
        return regist.searchPlaylists(user);

    }

    public long addSong(String name, String artist, String category) {
        return regist.addSong(name, artist, category);
    }

    @Override
    public long addPlaylist(String playlist, String author) {
        regist.addPlaylist(playlist, author);
        return 0;
    }

    public int recover(String mail) {
        return 0;
    }

    public int login(String mail, String pass) {
        Cursor user = regist.checkUser(mail);
        if (user != null) {
            if (user.getString(2).equals(pass)) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int addSongToPlaylist(String playlist, String song, String author) {
        regist.addSongToPlaylist(playlist, song, author);
        return 0;
    }

    public void close() {
        regist.close();
    }
}