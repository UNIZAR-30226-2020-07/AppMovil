package com.instantmusic.appmovil.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.playlist.PlaylistSongs;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class AlbumActivity extends AppCompatActivity {
    private ListView resList;
    private int idAlbum;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSong;
    private LinearLayout searchMenu;
    private Album albumSelected;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_album);
        resList = findViewById(R.id.playlist);
        serverInterface server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
        resList = findViewById(R.id.playlist);
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            idAlbum = extras.getInt("idAlbum");
        }
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        searchMenu=findViewById(R.id.searchMenu);
        resList.setAdapter(adapterSong);
        server.getAlbumData(idAlbum, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    albumSelected = new Album(data);
                    adapterSong.addAll(albumSelected.songs);
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        TextView name=findViewById(R.id.albumName);
        TextView creator=findViewById(R.id.albumCreator);
        name.setText(albumSelected.name);
        creator.setText(albumSelected.artistName);
        Button Button1 = findViewById(R.id.backButton);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        playB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ( !resList.getAdapter().isEmpty() ) {
                    ArrayAdapter<Song> search = (ArrayAdapter<Song>) resList.getAdapter();
                    Song cancion = search.getItem(0);
                    if ( cancion != null ) {
                        Song(cancion.songName, cancion.artist,cancion.duration,cancion.url, cancion.id,0,true);
                    }
                }
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = search.getItem(position);
                if ( cancion != null ) {
                    Song(cancion.songName, cancion.artist,cancion.duration,cancion.url, cancion.id,position,false);
                }
            }
        });

        Button Button6 = findViewById(R.id.optionSong);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                } else {
                    searchMenu.setVisibility(View.VISIBLE);
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

    private void Song(String songName, String autorName, int durationSong, String stream_url, int id, int position, boolean botonPlay) {
        Intent i = new Intent(this, PlaylistSongs.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        i.putExtra(this.getPackageName() + ".duration", durationSong);
        i.putExtra(this.getPackageName() + ".url", stream_url);
        i.putExtra(this.getPackageName() + ".positionId", position);
        i.putExtra(this.getPackageName() + ".botonPlay", botonPlay);
        i.putExtra(this.getPackageName() + ".id", id);
        ArrayList<Integer> idSongs = new ArrayList<>();
        for ( int j = 0; j < adapterSong.getCount(); j++ ) {
            idSongs.add(Objects.requireNonNull(adapterSong.getItem(j)).id);
        }
        i.putExtra(this.getPackageName() + ".songs", idSongs);
        this.startActivity(i);
    }

}