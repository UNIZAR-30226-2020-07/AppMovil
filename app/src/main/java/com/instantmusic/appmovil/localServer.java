package com.instantmusic.appmovil;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instantmusic.appmovil.Login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class localServer implements serverInterface {
    private static final int ACTIVITY_CREATE = 0;
    private UsersDbAdapter localDb;

    localServer(Context ctx) {
        this.localDb = new UsersDbAdapter(ctx);
        localDb.open();
    }

    public void searchShit(String shit,JSONConnection.Listener listener ) {
    }

    public void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener) {
        //return localDb.createUser(mail, pass,user);
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
    public long addPlaylist(String playlist,String author) {
        localDb.addPlaylist(playlist,author);
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