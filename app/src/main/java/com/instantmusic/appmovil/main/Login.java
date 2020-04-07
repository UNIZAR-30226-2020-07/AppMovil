package com.instantmusic.appmovil.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.instantmusic.appmovil.adapter.HorizontalListView;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.*;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.playlist.PlaylistAdapter;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements JSONConnection.Listener {
    private ArrayList<Playlist> arrayOfPlaylist = new ArrayList<>();
    private PlaylistAdapter adapterPlaylist;
    private serverInterface server;
    private HorizontalListView myPlaylist;
    private String username;

    @SuppressLint("WrongViewCast")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server = new remoteServer();
        myPlaylist = findViewById(R.id.myPlayLists);
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylist,0);
        myPlaylist.setAdapter(adapterPlaylist);
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if ( responseCode == 200 ) {
                        username = data.getString("username");
                        JSONArray playlistsUser = data.getJSONArray("playlists");
                        ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser);
                        adapterPlaylist.addAll(newPlaylists);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });
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
                ArrayAdapter<Playlist> search = (ArrayAdapter<Playlist>) parent.getAdapter();
                Playlist playlist = (Playlist) search.getItem(position);
                String creador = username;
                openPlaylist(playlist.playlistName, creador,playlist.songs, playlist.id);
            }
        });
        Button Button6 = findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPlaylist();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        adapterPlaylist.clear();
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if ( responseCode == 200 ) {
                        username = data.getString("username");
                        JSONArray playlistsUser = data.getJSONArray("playlists");
                        ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser);
                        adapterPlaylist.addAll(newPlaylists);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });
    }

    private void createPlaylist() throws JSONException {
        LinearLayout panel = findViewById(R.id.panelPlaylist);
        EditText insert = findViewById(R.id.insertTitle);
        String title = insert.getText().toString();
        panel.setVisibility(View.GONE);
        findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(false);
        server.addPlaylist(title, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if (responseCode == 201 ) {
                    Playlist newPlaylist = new Playlist(data);
                    adapterPlaylist.add(newPlaylist);
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });
    }

    private void openCreate () {
        LinearLayout panel = findViewById(R.id.panelPlaylist);
        if(panel.getVisibility()==View.VISIBLE){
            panel.setVisibility(View.INVISIBLE);
        }else {
            panel.setVisibility(View.VISIBLE);
        }
        findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(true);
    }

    private void openPlaylist (String playlist, String creador, ArrayList<Integer> songs, int idPlaylist){
        Intent i = new Intent(this, PlaylistActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("playlist", playlist);
        i.putExtra("creador", creador);
        i.putExtra("canciones",songs);
        i.putExtra("idPlaylist",idPlaylist);
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
    public void onBackPressed () {}

    @Override
    public void onValidResponse ( int responseCode, JSONObject data){
        try {
            JSONArray playlistsUser = data.getJSONArray("playlists");
            ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser);
            adapterPlaylist.addAll(newPlaylists);
        } catch (JSONException e) {
            onErrorResponse(e);
        }
    }

    @Override
    public void onErrorResponse (Throwable throwable){
    }
}
