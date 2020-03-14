package com.instantmusic.appmovil;

import android.content.Context;
import android.database.Cursor;

public class remoteServer implements serverInterface {
    private static final int ACTIVITY_CREATE = 0;
    private UsersDbAdapter localDb;

    remoteServer(Context ctx) {
        this.localDb = new UsersDbAdapter(ctx);
        localDb.open();
    }

    public Cursor searchShit(String shit) {
        return localDb.searchShit(shit);
    }

    public long registUser(String mail, String pass,String user) {
        return localDb.createUser(mail, pass,user);
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
    public int songInfo(String name,String artist,String categoria) {
        Cursor user = localDb.songInfo((name));
        if (user == null) {
            return 1;
        }
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
    public long addPlaylist(String playlist,String author) {
        localDb.addPlaylist(playlist,author);
        return 0;
    }

    public int recover(String mail) {
        return 0;
    }

    public int login(String mail, String pass) {
        Cursor user = localDb.checkUser(mail);
        if (user != null) {
            if (user.getString(2).equals(pass)) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int addSongToPlaylist(String playlist, String song,String author) {
        localDb.addSongToPlaylist(playlist,song,author);
        return 0;
    }
    public void close(){
        localDb.close();
    }
}