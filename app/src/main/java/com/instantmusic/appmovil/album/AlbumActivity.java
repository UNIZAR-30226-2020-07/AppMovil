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
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONException;
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
        resList = findViewById(R.id.cancionesAlbum);
        serverInterface server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
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
                    TextView name=findViewById(R.id.albumName);
                    TextView creator=findViewById(R.id.albumCreator);
                    name.setText(albumSelected.name);
                    creator.setText(albumSelected.artistName);
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
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
        Button artist=findViewById(R.id.seeArtist2);
        artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleOptionsMenu();
                try {
                    seeArtist();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void seeArtist() throws JSONException {
        Intent i = new Intent(this, ArtistActivity.class);
        i.putExtra("idArtist",albumSelected.artistId);
        startActivityForResult(i, 1);
    }
    private void toggleOptionsMenu() {
        searchMenu.setVisibility(
                searchMenu.getVisibility() == View.VISIBLE
                        ? View.INVISIBLE
                        : View.VISIBLE
        );
    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    private void Song(String songName, String autorName, int durationSong, String stream_url, int id, int position, boolean botonPlay) {
        Intent i = new Intent(this, SongActivity.class);
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
