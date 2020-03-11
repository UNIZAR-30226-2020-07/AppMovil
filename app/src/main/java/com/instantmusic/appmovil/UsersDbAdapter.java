package com.instantmusic.appmovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple Users database access helper class. Defines the basic CRUD operations
 * for the Userpad example, and gives the ability to list all Users as well as
 * retrieve or modify a specific User.
 * <p>
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class UsersDbAdapter {

    public static final String KEY_MAIL = "mail";
    public static final String KEY_PLAYLIST = "playlist";
    public static final String KEY_PASS = "pass";
    public static final String KEY_USER = "user";
    public static final String KEY_NAME = "name";
    public static final String KEY_NAMEP = "nameP";
    public static final String KEY_ARTIST = "_artist";
    public static final String KEY_CATEGORY = "_categoria";
    public static final String KEY_ID = "_id";

    public static final String KEY_AUTHOR = "autor";

    private static final String TAG = "UsersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_USERS =
            "create table users (_id integer primary key autoincrement," + "mail text not null, pass text not null,user not null,playlist text not null);";
    private static final String DATABASE_CREATE_SONGS =
            "create table songs (_id integer primary key autoincrement," + "name text not null, _artist text not null, _categoria text not null);";
    private static final String DATABASE_CREATE_PLAYLISTS =
            "create table playlists (_id integer primary key autoincrement," + "nameP text not null, autor text not null, name text not null);";
    private static final String DATABASE_CREATE_LIST_PLAYLISTS =
            "create table listplaylists (_id integer primary key autoincrement," + "nameP text not null, autor text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_USERS = "users";
    private static final String DATABASE_TABLE_SONGS = "songs";
    private static final String DATABASE_TABLE_PLAYLISTS = "playlists";
    private static final String DATABASE_TABLE_LIST_PLAYLISTS = "listplaylists";
    private static final int DATABASE_VERSION = 4;

    private final Context mCtx;

    public Cursor infoUser(String email) {
        String[] columns = new String[]{KEY_ID, KEY_MAIL, KEY_PASS, KEY_USER, KEY_PLAYLIST};
        Cursor mDbCursor =
                mDb.query(true, DATABASE_TABLE_USERS, columns, KEY_MAIL + "=?", new String[]{email},
                        null, null, null, null);
        if (mDbCursor != null) {
            mDbCursor.moveToFirst();
            if (mDbCursor.getCount() == 0) mDbCursor = null;
        }
        return mDbCursor;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_USERS);
            db.execSQL(DATABASE_CREATE_SONGS);
            db.execSQL(DATABASE_CREATE_PLAYLISTS);
            db.execSQL(DATABASE_CREATE_LIST_PLAYLISTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users;");
            db.execSQL("DROP TABLE IF EXISTS songs;");
            db.execSQL("DROP TABLE IF EXISTS playlists;");
            db.execSQL("DROP TABLE IF EXISTS listplaylists;");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public UsersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the Users database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public UsersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the Users database.
     */
    public void close() {
        mDbHelper.close();
    }


    /*
     * Create a new User using the mail and playlists provided. If the User is
     * successfully created return the new mail for that User, otherwise return
     * a -1 to indicate failure.
     *
     * @param mail    the mail of the User
     * @param playlists     the playlists of the User
     * @param pass the pass id (null for no pass)
     * @return mail or -1 if failed
     */
    public long createUser(String mail, String pass, String user) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MAIL, mail);
        initialValues.put(KEY_PLAYLIST, "none");
        initialValues.put(KEY_PASS, pass);
        initialValues.put(KEY_USER, user);

        return mDb.insert(DATABASE_TABLE_USERS, null, initialValues);
    }

    public Cursor checkUser(String email) throws SQLException {
        if (email == null) {
            return null;
        } else {
            String[] columns = new String[]{KEY_ID, KEY_MAIL, KEY_PASS, KEY_USER, KEY_PLAYLIST};
            Cursor mDbCursor =
                    mDb.query(true, DATABASE_TABLE_USERS, columns, KEY_MAIL + "=?", new String[]{email},
                            null, null, null, null);
            if (mDbCursor != null) {
                mDbCursor.moveToFirst();
                if (mDbCursor.getCount() == 0) mDbCursor = null;
            }
            return mDbCursor;
        }
    }

    public long addSong(String name, String artist, String category) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ARTIST, artist);
        initialValues.put(KEY_CATEGORY, category);

        return mDb.insert(DATABASE_TABLE_SONGS, null, initialValues);
    }

    public long addPlaylist(String playlist, String author) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PLAYLIST, playlist);

        return mDb.update(DATABASE_TABLE_USERS, initialValues, KEY_MAIL + "=?" ,new String[]{author});
    }

    public long addSongToPlaylist(String playlist, String song,String author) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAMEP, playlist);
        args.put(KEY_NAME, song);
        args.put(KEY_AUTHOR, author);
        return mDb.insert(DATABASE_TABLE_PLAYLISTS,null,args);

    }

    public Cursor songInfo(String name) throws SQLException {
        if (name == null) {
            return null;
        } else {
            String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_ARTIST, KEY_CATEGORY};
            Cursor mDbCursor =
                    mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_NAME + "=?", new String[]{name},
                            null, null, null, null);
            if (mDbCursor != null) {
                mDbCursor.moveToFirst();
                if (mDbCursor.getCount() == 0) mDbCursor = null;
            }
            return mDbCursor;
        }
    }

    public Cursor buscarCancion(String song) {
        String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_ARTIST, KEY_CATEGORY};
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_NAME + "=?", new String[]{song},
                null, null, null, null);
        String resultado = "Esta fallando";
        if (mCursor != null) {
            mCursor.moveToFirst();
            if (mCursor.getCount() == 0) mCursor = null;
        }
        return mCursor;
    }

    public Cursor buscarArtista(String artist) {
        String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_ARTIST, KEY_CATEGORY};
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_ARTIST + "=?", new String[]{artist},
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            if (mCursor.getCount() == 0) mCursor = null;
        }
        return mCursor;
    }

    public Cursor searchPlaylists(String user) {
        String[] columns = new String[]{KEY_ID, KEY_MAIL, KEY_PASS, KEY_USER, KEY_PLAYLIST};
        Cursor aux = mDb.query(DATABASE_TABLE_USERS, columns, KEY_MAIL + "=?", new String[]{user}, null, null, null);
        if (aux != null) {
            aux.moveToFirst();
            if (aux.getCount() == 0) aux = null;
        }
        return aux;
    }

    /*
     * Delete the User with the given mail
     *
     * @param mail id of User to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteUser(String email) {
        return mDb.delete(DATABASE_TABLE_USERS, KEY_MAIL + "=?", new String[]{email}) > 0;
    }

    /*
     * Return a Cursor over the list of all Users in the database.
     * Can be sorted by a data column (KEY_MAIL, KEY_PASS, ...)
     * Can be filtered by pass name
     *
     * @param orderBy  if null not ordered. if not, order by that column name
     * @param pass if null show all, if not, show only Users with that pass name (not id)
     * @return a cursor with the Users
     */
   /*public Cursor searchAllUsers(String orderBy, String pass) {
        try {
//        if (pass != null) {
//            pass = KEY_PASS + "=" + searchpass(pass);
//        }
            //return mDb.rawQuery("SELECT " + DATABASE_TABLE_USERS + "." + KEY_ARTIST + " AS " + KEY_ARTIST + "," + KEY_MAIL + "," + KEY_PLAYLISTS + "," + KEY_NAME + " FROM " + DATABASE_TABLE_USERS + " LEFT JOIN " + DATABASE_TABLE_SONGS + " ON " + DATABASE_TABLE_USERS + '.' + KEY_PASS + '=' + DATABASE_TABLE_SONGS+"."+KEY_ARTIST,null);
            return mDb.rawQuery(
                    "SELECT " + DATABASE_TABLE_USERS + "." + KEY_ARTIST + " AS " + KEY_ARTIST + ", " +
                            KEY_MAIL + ", " +
                            KEY_PLAYLISTS + ", " +
                            DATABASE_TABLE_SONGS + "." + KEY_NAME + " AS " + KEY_NAME +
                            " FROM " + DATABASE_TABLE_USERS + "" +
                            " LEFT JOIN " + DATABASE_TABLE_SONGS + " ON " + DATABASE_TABLE_USERS + '.' + KEY_PASS + '=' + DATABASE_TABLE_SONGS + "." + KEY_ARTIST +
                            (pass != null ? " WHERE " + KEY_NAME + "='" + pass + '\'' : "") +
                            (orderBy != null ? " ORDER BY " + orderBy : "")
                    , null);
//        return mDb.query(DATABASE_TABLE_USERS, new String[]{KEY_ARTIST, KEY_MAIL,
//                KEY_PLAYLISTS, KEY_PASS}, pass, null, null, null, orderBy);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*
     * Return a Cursor positioned at the User that matches the given mail
     *
     * @param mail id of User to retrieve
     * @return Cursor positioned to matching User, if found
     * @throws SQLException if User could not be found/retrieved
     */
    public Cursor searchShit(String shit) throws SQLException {
        String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_ARTIST, KEY_CATEGORY};
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_NAME + "=?", new String[]{shit}, null,
                        null, null, null, null);
        if (mCursor == null) {
            mCursor =
                    mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_ARTIST + "=?", new String[]{shit},
                            null, null, null, null);
        }
        if (mCursor == null) {
            mCursor =
                    mDb.query(true, DATABASE_TABLE_SONGS, columns, KEY_CATEGORY + "=?", new String[]{shit},
                            null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
            if (mCursor.getCount() == 0) mCursor = null;
        }
        return mCursor;
    }

    /*
     * Update the User using the details provided. The User to be updated is
     * specified using the mail, and it is altered to use the mail and playlists
     * values passed in
     *
     * @param mail id of User to update
     * @param mail value to set User mail to
     * @param playlists  value to set User playlists to
     * @return true if the User was successfully updated, false otherwise
     */
    public boolean updateUser(String mail, String pass, String song) {
        ContentValues args = new ContentValues();
        String playlist;
        if (mail != null) {
            args.put(KEY_MAIL, mail);

        }
        if (pass != null) {
            args.put(KEY_PASS, pass);

        }
        if (song != null) {
            Cursor c;
            c = checkUser(mail);
            playlist = c.getString(2);
            playlist = playlist + "/" + song;
        }
        return mDb.update(DATABASE_TABLE_USERS, args, KEY_ARTIST + "=" + mail, null) > 0;
    }

    /**
     * Return a Cursor over the list of all Users in the database
     *
     * @return Cursor over all Users
     */
    public Cursor searchAllSongs() {
        return mDb.query(DATABASE_TABLE_SONGS, new String[]{KEY_ARTIST, KEY_NAME}, null, null, null, null, null);
    }

    /*
     * Update the User using the details provided. The User to be updated is
     * specified using the mail, and it is altered to use the mail and playlists
     * values passed in
     *
     * @param mail id of User to update
     * @param name value to set pass name to
     * @return true if the User was successfully updated, false otherwise
     */
    public boolean updatePass(String mail, String pass, String nPass) {
        if (mail == null || mail.isEmpty()) return false;

        ContentValues args = new ContentValues();
        args.put(KEY_PASS, nPass);
        return mDb.update(DATABASE_TABLE_SONGS, args, KEY_MAIL + "=" + mail, null) > 0;
    }
}