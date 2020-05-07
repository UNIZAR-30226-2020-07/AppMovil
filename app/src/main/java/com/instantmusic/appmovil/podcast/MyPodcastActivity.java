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

import com.instantmusic.appmovil.IntentTransfer;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class MyPodcastActivity extends AppCompatActivity {
    private ListView resList;
    private String podcastName;
    private String creador;
    private int idPodcast;
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
        setContentView(R.layout.activity_instant_music_app_mypodcasts);
        resList = findViewById(R.id.cancionesAlbum);
        server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
        Bundle extras = getIntent().getExtras();
        final TextView name=findViewById(R.id.albumName);
        final TextView creator=findViewById(R.id.albumCreator);
        if ( extras != null ) {
            idPodcast = extras.getInt(this.getPackageName() + ".id");
            server.getAlbumData(idPodcast, new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                    if ( responseCode == 200 ) {
                        Album playlistSelected = new Album(data, false);
                        artist=data.getInt("id");
                        creador = data.getJSONObject("artist").getString("name");
                        podcastName = data.getString("name");
                        name.setText(podcastName);
                        creator.setText(creador);
                        adapterSong.addAll(playlistSelected.songs);
                        sortBy("titulo");
                    }
                }
                @Override
                public void onErrorResponse(Throwable throwable) {
                }
            });
        }
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        searchMenu=findViewById(R.id.searchMenu);
        changeMenu2=findViewById(R.id.changeName2);
        changeMenu=findViewById(R.id.change);
        resList.setAdapter(adapterSong);
        orderName=findViewById(R.id.orderName);
        orderCategory=findViewById(R.id.orderCategory);
        orderArtist=findViewById(R.id.orderArtist);
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
                    Song(0, true);
                }
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = search.getItem(position);
                if ( cancion != null ) {
                    Song(position, false);
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
                seeArtist();
                searchMenu.setVisibility(View.INVISIBLE);
            }
        });

        Button deletePodcast = findViewById(R.id.deletePodcast);
        deletePodcast.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMenu.setVisibility(View.INVISIBLE);
                deletePodcast();
            }
        });

    }

    private void seeArtist() {
        Intent i = new Intent(this, ArtistActivity.class);
        i.putExtra("idArtist",artist);
        startActivityForResult(i, 1);
    }

    private void deletePodcast() {
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        ArrayList<Album> albums = Album.fromJson(data.getJSONArray("albums"), false, null, true);
                        ArrayList<Integer> newAlbums = new ArrayList<>();
                        for ( int i = 0; i < albums.size(); i++ ) {
                            if ( !(albums.get(i).id == idPodcast) ) {
                                newAlbums.add(albums.get(i).id);
                            }
                        }
                        server.addOrRemovePodcast(newAlbums, new JSONConnection.Listener() {
                            @Override
                            public void onValidResponse(int responseCode, JSONObject data) {
                                if ( responseCode == 200 ) {
                                    backScreen();
                                }
                            }
                            @Override
                            public void onErrorResponse(Throwable throwable) {
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });

    }

    private void sortBy(String comp) {
        switch (comp) {
            case "date":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.fecha.compareTo(o2.fecha);
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
        server.getAlbumData(idPodcast, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Album playlistSelected = new Album(data, false);
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

    private void Song(int position, boolean botonPlay) {
        IntentTransfer.setData("songs", adapterSong.getSongs());
        IntentTransfer.setData("positionId", position);
        IntentTransfer.setData("botonPlay", botonPlay);

        this.startActivity(new Intent(this, SongActivity.class));
    }

}
