package com.instantmusic.appmovil.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class addSongToPlaylist extends AppCompatActivity {
    private serverInterface server;
    private int idSong;
    private Playlist playlistSelected;
    private ArrayList<Playlist> arrayOfPlaylist = new ArrayList<>();
    private PlaylistAdapter adapterPlaylist;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_addplaylist);
        server = new remoteServer();
        ListView myPlaylist;
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            idSong = extras.getInt("id");
        }
        myPlaylist = findViewById(R.id.myPlayLists);
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylist,1);
        myPlaylist.setAdapter(adapterPlaylist);

        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if ( responseCode == 200 ) {
                        JSONArray playlistsUser = data.getJSONArray("playlists");
                        ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser,true);
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
        Button back=findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        myPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Playlist> playlists = (ArrayAdapter<Playlist>) parent.getAdapter();
                playlistSelected = playlists.getItem(position);
                if ( playlistSelected != null ) {
                    final ArrayList<Integer> songs = new ArrayList<>();
                    for ( int i = 0; i < playlistSelected.songs.size(); i++ ) {
                        songs.add(playlistSelected.songs.get(i).id);
                    }
                    songs.add(idSong);
                    server.getSongData(idSong, new JSONConnection.Listener() {
                        @Override
                        public void onValidResponse(int responseCode, JSONObject data) {
                            if ( responseCode == 200 ) {
                                Song newSong = new Song(data);
                                playlistSelected.songs.add(newSong);
                                server.addOrRemoveSong(playlistSelected.id, songs, new JSONConnection.Listener() {
                                    @Override
                                    public void onValidResponse(int responseCode, JSONObject data) {
                                        if ( responseCode == 200 ) {
                                            backScreen();
                                        }
                                    }
                                    @Override
                                    public void onErrorResponse(Throwable throwable) {
                                        setTitle("Unknown user");
                                    }
                                });
                            }
                        }
                        @Override
                        public void onErrorResponse(Throwable throwable) {
                        }
                    });
                }
            }
        });

    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
}
