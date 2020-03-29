package com.instantmusic.appmovil.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.instantmusic.appmovil.adapter.HorizontalListView;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.*;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.UsersDbAdapter;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.localServer;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.PlaylistAdapter;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements JSONConnection.Listener {
    private ArrayList<Playlist> arrayOfPlaylist = new ArrayList<Playlist>();
    private PlaylistAdapter adapterPlaylist;
    private AutoCompleteTextView shit;
    private serverInterface server;
    private HorizontalListView myPlaylist;

    @SuppressLint("WrongViewCast")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server = new remoteServer();
//        registerForContextMenu(resList);
        myPlaylist = findViewById(R.id.myPlayLists);
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylist);
        //name = aux.getString(3);
        server.getUserData(this);
       /* Cursor shitCursor = server.allPlaylists("Admin");
        startManagingCursor(shitCursor);
        String[] from = new String[]{UsersDbAdapter.KEY_NAMEP};
        int[] to = new int[]{R.id.text1};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.myplaylists_row, shitCursor, from, to);
        myPlaylist.setAdapter(search);
        */
        Button createP = findViewById(R.id.createPlaylist);
        createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreate();
            }
        });
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

                SimpleCursorAdapter search = (SimpleCursorAdapter) parent.getAdapter();
                String playList = search.getCursor().getString(1);
                String creador = search.getCursor().getString(2);
                openPlaylist(playList, creador);
            }
        });
        Button Button6 = findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlaylist();
            }
        });
    }

    private void createPlaylist() {
        LinearLayout panel = findViewById(R.id.panelPlaylist);
        EditText insert = findViewById(R.id.insertTitle);
        String title = insert.getText().toString();
        panel.setVisibility(View.GONE);
        findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(false);
        server.addPlaylist(title, arrayOfPlaylist,this);
    }

        private void openCreate () {
            LinearLayout panel = findViewById(R.id.panelPlaylist);
            panel.setVisibility(View.VISIBLE);
            findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(true);
        }

        private void openPlaylist (String playlist, String creador){
            Intent i = new Intent(this, PlaylistActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.putExtra("playlist", playlist);
            i.putExtra("creador", creador);
            startActivityForResult(i, 1);
        }
        private void Search () {
            Intent i = new Intent(this, Search.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            startActivityForResult(i, 1);

        }

        private void Podcasts () {
            Intent i = new Intent(this, Podcasts.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(i, 1);

        }

        private void Friends () {
            Intent i = new Intent(this, Friends.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(i, 1);
        }

        private void Settings () {
            Intent i = new Intent(this, Settings.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(i, 1);
        }

        @Override
        public void onBackPressed () {
        }

        @Override
        public void onValidResponse ( int responseCode, JSONObject data){
            try {
                JSONArray results = data.getJSONArray("results");
                ArrayList<Playlist> newPlaylists = Playlist.fromJson(results);
                adapterPlaylist.addAll(newPlaylists);
            } catch (JSONException e) {
                onErrorResponse(e);
            }
        }

        @Override
        public void onErrorResponse (Throwable throwable){
        }
    }
