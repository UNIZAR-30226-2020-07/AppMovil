package com.instantmusic.appmovil.playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class PlaylistActivity extends AppCompatActivity {
    private ListView resList;
    private String playList;
    private String creador;
    private int idPlaylist;
    private ArrayList<Integer> songs;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSong;
    private LinearLayout searchMenu;
    private Button changeMenu2;
    private Button orderName;
    private Button orderCategory;
    private Button orderArtist;
    private EditText changeMenu;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_playlists);
        resList = findViewById(R.id.playlist);
        final serverInterface server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
        resList = findViewById(R.id.playlist);
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            playList= extras.getString("playlist");
            creador= extras.getString("creador");
            songs = extras.getIntegerArrayList("canciones");
            idPlaylist = extras.getInt("idPlaylist");
        }
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        searchMenu=findViewById(R.id.searchMenu);
        changeMenu2=findViewById(R.id.changeName2);
        changeMenu=findViewById(R.id.change);
        resList.setAdapter(adapterSong);
        orderName=findViewById(R.id.orderName);
        orderCategory=findViewById(R.id.orderCategory);
        orderArtist=findViewById(R.id.orderArtist);
        server.getPlaylistData(idPlaylist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Playlist playlistSelected = new Playlist(data,false);
                    adapterSong.addAll(playlistSelected.songs);
                    sortBy("titulo");
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        TextView name=findViewById(R.id.playlistName);
        changeMenu.setText(playList);
        TextView creator=findViewById(R.id.playlistCreator);
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
                    String name = cancion.songName;
                    String artista = cancion.artist;
                    int duracion = cancion.duration;
                    String url = cancion.url;
                    Song(name, artista,duracion,url,0,true);
                }
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = search.getItem(position);
                if ( cancion != null ) {
                    String name = cancion.songName;
                    String artista = cancion.artist;
                    int duracion = cancion.duration;
                    String url = cancion.url;
                    Song(name, artista,duracion,url,position,false);
                }
            }
        });

        Button delete = findViewById(R.id.addPlaylist);
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
        Button Button7 = findViewById(R.id.changeName);
        Button Button8 =findViewById(R.id.changeName2);
        Button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeMenu.getVisibility() == View.VISIBLE) {
                    changeMenu.setVisibility(View.INVISIBLE);
                    changeMenu2.setVisibility(View.INVISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.VISIBLE);
                } else {
                    changeMenu.setVisibility(View.VISIBLE);
                    changeMenu2.setVisibility(View.VISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.INVISIBLE);
                }
                EditText change=findViewById(R.id.change);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if ( imm != null ) {
                    imm.showSoftInput(change, InputMethodManager.SHOW_IMPLICIT);
                }
                change.requestFocus();
                change.setSelection(change.getText().length());
            }
        });
        Button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeMenu.getVisibility() == View.VISIBLE) {
                    changeMenu.setVisibility(View.INVISIBLE);
                    changeMenu2.setVisibility(View.INVISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.VISIBLE);
                }
                else {
                    changeMenu.setVisibility(View.VISIBLE);
                    changeMenu2.setVisibility(View.VISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.INVISIBLE);
                }
                EditText change=findViewById(R.id.change);
                playList=change.getText().toString();
                TextView playlistname= findViewById(R.id.playlistName);
                playlistname.setText(change.getText().toString());

                server.changeNamePlaylist(change.getText().toString(), idPlaylist, new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                    }
                    @Override
                    public void onErrorResponse(Throwable throwable) {
                    }
                });
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                }
                else {
                    searchMenu.setVisibility(View.VISIBLE);
                }

                server.deletePlaylist(idPlaylist, new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        backScreen();
                    }
                    @Override
                    public void onErrorResponse(Throwable throwable) {
                    }
                });
            }
        });
        orderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                sortBy("titulo");
            }
        });orderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                sortBy("categoria");
            }
        });orderArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                sortBy("artista");
            }
        });
    }

    private void sortBy(String comp) {
        switch (comp) {
            case "artista":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.artist.compareTo(o2.artist);
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
            case "categoria":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.category.compareTo(o2.category);
                    }
                });
            break;
        }
    }

    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
    private void Song(String songName, String autorName, int durationSong, String stream_url, int position, boolean botonPlay) {
        Intent i = new Intent(this, PlaylistSongs.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        i.putExtra(this.getPackageName() + ".duration", durationSong);
        i.putExtra(this.getPackageName() + ".url", stream_url);
        i.putExtra(this.getPackageName() + ".positionId", position);
        i.putExtra(this.getPackageName() + ".botonPlay", botonPlay);
        ArrayList<Integer> idSongs = new ArrayList<>();
        for ( int j = 0; j < adapterSong.getCount(); j++ ) {
            idSongs.add(Objects.requireNonNull(adapterSong.getItem(j)).id);
        }
        i.putExtra(this.getPackageName() + ".songs", idSongs);
        this.startActivity(i);
    }

}
