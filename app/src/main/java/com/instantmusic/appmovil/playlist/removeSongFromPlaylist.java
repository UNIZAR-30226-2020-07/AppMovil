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
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class removeSongFromPlaylist extends AppCompatActivity {
    private serverInterface server;
    private int idPlaylist;
    private int idSong;
    private Playlist playlistSelected;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSong;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_removesong);
        server = new remoteServer();
        ListView mySongs;
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            idPlaylist = extras.getInt("id");
        }
        mySongs = findViewById(R.id.myPlayLists);
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        mySongs.setAdapter(adapterSong);

        server.getPlaylistData(idPlaylist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if ( responseCode == 200 ) {
                        playlistSelected = new Playlist(data, false);
                        ArrayList<Song> newSongs = Song.fromJson(data.getJSONArray("songs"), true, null);
                        adapterSong.addAll(newSongs);
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
        mySongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> song = (ArrayAdapter<Song>) parent.getAdapter();
                idSong = song.getItem(position).id;
                if ( playlistSelected != null ) {
                    final ArrayList<Integer> songs = new ArrayList<>();
                    for ( int i = 0; i < playlistSelected.songs.size(); i++ ) {
                        if ( !(playlistSelected.songs.get(i).id == idSong) ) {
                            songs.add(playlistSelected.songs.get(i).id);
                        }
                    }
                    server.addOrRemoveSong(playlistSelected.id, songs, new JSONConnection.Listener() {
                        @Override
                        public void onValidResponse(int responseCode, JSONObject data) {
                            if (responseCode == 200 ) {
                                backScreen();
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
