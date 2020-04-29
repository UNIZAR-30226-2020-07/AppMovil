package com.instantmusic.appmovil.podcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class PodcastActivity extends AppCompatActivity {
    private ListView resList;
    private String playList;
    private String creador;
    private int idPlaylist;
    private ArrayList<Integer> songs;
    private serverInterface server;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSong;
    private LinearLayout searchMenu;
    private Button changeMenu2;
    private Button orderName;
    private Button orderCategory;
    private Button orderArtist;
    int searchType = 1;
    private EditText changeMenu;
    private int page=1;
    private int artist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_podcasts);
        resList = findViewById(R.id.cancionesAlbum);
        server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            playList= extras.getString(this.getPackageName() + ".name");
            creador= extras.getString(this.getPackageName() + ".user");
            songs = extras.getIntegerArrayList(this.getPackageName() + ".songs");
            idPlaylist = extras.getInt(this.getPackageName() + ".id");
        }
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        searchMenu=findViewById(R.id.searchMenu);
        changeMenu2=findViewById(R.id.changeName2);
        changeMenu=findViewById(R.id.change);
        resList.setAdapter(adapterSong);
        orderName=findViewById(R.id.orderName);
        orderCategory=findViewById(R.id.orderCategory);
        orderArtist=findViewById(R.id.orderArtist);
        server.getAlbumData(idPlaylist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if ( responseCode == 200 ) {
                    Album playlistSelected = new Album(data);
                    artist=data.getInt("id");
                    adapterSong.addAll(playlistSelected.songs);
                    sortBy("titulo");
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        TextView name=findViewById(R.id.albumName);
        TextView creator=findViewById(R.id.albumCreator);
        name.setText(playList);
        creator.setText(creador);
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
                    Song(cancion.songName, cancion.artist,cancion.duration,cancion.url, cancion.id,0,true);
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
        orderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));

                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                searchType = 1;
                sortBy("titulo");
            }
        });
        orderArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));

                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                searchType = 3;
                sortBy("date");
            }
        });
        Button seeArtist=findViewById(R.id.seeArtist2);
        seeArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    seeArtist();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                searchMenu.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void seeArtist() throws JSONException {
        Intent i = new Intent(this, ArtistActivity.class);
        i.putExtra("idArtist",artist);
        startActivityForResult(i, 1);
    }
    private void sortBy(String comp) {
        switch (comp) {
            case "date":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return Integer.toString(o1.id).compareTo(Integer.toString(o2.id));
                    }
                });
            break;
            case "titulo":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.songName.compareTo(o2.songName);
                    }
                });
            break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapterSong.clear();
        server.getAlbumData(idPlaylist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Album playlistSelected = new Album(data);
                    adapterSong.addAll(playlistSelected.songs);
                    if ( searchType == 1 ) {
                        sortBy("titulo");
                    }
                    else if ( searchType == 3 ) {
                        sortBy("artista");
                    }
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
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
