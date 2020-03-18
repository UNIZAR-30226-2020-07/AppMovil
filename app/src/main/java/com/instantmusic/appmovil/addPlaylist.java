package com.instantmusic.appmovil;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class addPlaylist extends AppCompatActivity {
    private serverInterface server;
    private String user;
    private String name;
    private HorizontalListView myPlaylist;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_addplaylist);
        server = new localServer(this);
//        registerForContextMenu(resList);
        myPlaylist = findViewById(R.id.myPlayLists);
        //name = aux.getString(3);
        Cursor shitCursor = server.allPlaylists("Admin");
        startManagingCursor(shitCursor);
        String[] from = new String[]{UsersDbAdapter.KEY_NAMEP};
        int[] to = new int[]{R.id.text1};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.myplaylists_row, shitCursor, from, to);
        myPlaylist.setAdapter(search);
    }
}
