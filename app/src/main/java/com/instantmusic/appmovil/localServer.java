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
        this.localDb=new UsersDbAdapter(ctx);
    }

    public Cursor searchShit(String shit) {
        return localDb.searchShit(shit);
    }

    public long registUser(String mail, String pass) {
        return localDb.createUser(mail, pass);
    }

    public Cursor checkUser(String mail) {
        Cursor aux = localDb.checkUser(mail);
        return aux;
    }

    public int recover(String mail) {
        return 0;
    }

    public int login(String mail, String pass) {
        return 0;
    }

    public int addSong(String mail, String pass, String song, String playlist) {
        if (localDb.updateUser(mail, pass, song, playlist)) {
            return 0;
        } else {
            return 1;
        }
    }
}