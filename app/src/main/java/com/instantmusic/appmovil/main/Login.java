package com.instantmusic.appmovil.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.instantmusic.appmovil.adapter.HorizontalListView;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.*;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.UsersDbAdapter;
import com.instantmusic.appmovil.server.localServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.PlaylistAdapter;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private ArrayList<Playlist> arrayOfPlaylist = new ArrayList<Playlist>();
    private PlaylistAdapter adapterPlaylist;
    private AutoCompleteTextView shit;
    private serverInterface server;
    private HorizontalListView myPlaylist;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server = new localServer(this);
//        registerForContextMenu(resList);
        myPlaylist = findViewById(R.id.myPlayLists);
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylist);
        //name = aux.getString(3);
       /* Cursor shitCursor = server.allPlaylists("Admin");
        startManagingCursor(shitCursor);
        String[] from = new String[]{UsersDbAdapter.KEY_NAMEP};
        int[] to = new int[]{R.id.text1};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.myplaylists_row, shitCursor, from, to);
        myPlaylist.setAdapter(search);
        */
        Button Button2 = findViewById(R.id.menuButton2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        Button Button3 = findViewById(R.id.menuButton3);
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Podcasts();
            }
        });
        Button Button4 = findViewById(R.id.menuButton4);
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends();
            }
        });
        Button Button5 = findViewById(R.id.menuButton5);
        Button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings();
            }
        });
        myPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SimpleCursorAdapter search=(SimpleCursorAdapter)parent.getAdapter();
                String playList=search.getCursor().getString(1);
                String creador=search.getCursor().getString(2);
                openPlaylist(playList,creador);
            }
        });
    }

    private void openPlaylist(String playlist,String creador){
        Intent i = new Intent(this, PlaylistActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("playlist",playlist);
        i.putExtra("creador",creador);
        startActivityForResult(i, 1);
    }
    private void Search() {
        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(i, 1);

    }

    private void Podcasts() {
        Intent i = new Intent(this, Podcasts.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(i, 1);

    }

    private void Friends() {
        Intent i = new Intent(this, Friends.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Settings() {
        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    @Override
    public void onBackPressed() {
    }
}
