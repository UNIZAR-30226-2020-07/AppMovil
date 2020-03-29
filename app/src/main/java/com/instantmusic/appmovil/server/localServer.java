package com.instantmusic.appmovil.server;

import android.content.Context;
import android.database.Cursor;

import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;

import java.util.ArrayList;

public class localServer implements serverInterface {
    private static final int ACTIVITY_CREATE = 0;
    private UsersDbAdapter localDb;

    public localServer(Context ctx) {
        this.localDb = new UsersDbAdapter(ctx);
        localDb.open();
    }

    public void searchShit(String shit, JSONConnection.Listener listener ) {
    }

    public void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener) {
        //return localDb.createUser(mail, pass,user);
    }

    @Override
    public void getUserData(JSONConnection.Listener listener) {

    }

    public int checkUser(String mail) {
        Cursor user = localDb.checkUser((mail));
        if (user == null) {
            return 1;
        }
        return 0;
    }

    @Override
    public Cursor infoUser(String email) {
        return localDb.infoUser(email);
    }

    public int deleteUser(String email){
        if(localDb.deleteUser(email)){
            return 0;
        }
        return 1;
    }
    public int songInfo(int id) {

        return 0;

    }

    public Cursor buscarCancion(String song) {
        return localDb.buscarCancion(song);
    }

    public Cursor buscarArtista(String artist) {
        return localDb.buscarArtista(artist);
    }

    @Override
    public Cursor allPlaylists(String user) {
        return localDb.searchPlaylists(user);

    }

    public long addSong(String name,String artist,String category) {
        return localDb.addSong(name,artist,category);
    }

    @Override
    public long getSong(String title, JSONConnection.Listener listener) {
        return 0;
    }

    @Override
    public long addPlaylist(String playlist, ArrayList<Playlist> playlists, JSONConnection.Listener listener) {
        return 0;
    }

    @Override
    public Cursor searchPlaylist(String playlist) {
        return localDb.searchPlaylist(playlist);
    }

    public int recover(String mail) {
        return 0;
    }

    public void login(String username_email, String password, JSONConnection.Listener listener) {
        /*
        Cursor user = localDb.checkUser(mail);
        if (user != null) {
            if (user.getString(2).equals(pass)) {
                return 0;
            }
        }
        return 1;
         */
    }

    @Override
    public int addSongToPlaylist(String song, String playlist) {
        localDb.addSongToPlaylist(playlist,song);
        return 0;
    }
    public void close(){
        localDb.close();
    }
}