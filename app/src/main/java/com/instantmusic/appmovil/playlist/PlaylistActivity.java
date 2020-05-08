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

import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class PlaylistActivity extends AppCompatActivity {
    private ListView resList;
    private String playList;
    private String creador;
    private int idPlaylist;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_playlists);
        resList = findViewById(R.id.playlist);
        server = new remoteServer();
        Button playB = findViewById(R.id.playButton);
        resList = findViewById(R.id.playlist);
        Bundle extras = getIntent().getExtras();
        Button Button6 = findViewById(R.id.optionSong);
        Button Button7 = findViewById(R.id.changeName);
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
        if ( extras != null ) {
            playList= extras.getString("playlist");
            creador= extras.getString("creador");
            idPlaylist = extras.getInt("idPlaylist");
            String friends=extras.getString("friends");
            if(friends!=null){
                Button6.setVisibility(View.INVISIBLE);
                Button6.setClickable(false);
                Button7.setVisibility(View.INVISIBLE);
                Button7.setClickable(false);

            }
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

        Button delete = findViewById(R.id.addPlaylist);
        Button removeSong = findViewById(R.id.removeSong);
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
        removeSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                }
                else {
                    searchMenu.setVisibility(View.VISIBLE);
                }
                removeSong();
            }
        });
        orderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                searchType = 1;
                sortBy("titulo");
            }
        });orderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                searchType = 2;
                sortBy("categoria");
            }
        });orderArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderName.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderCategory.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                orderArtist.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                searchType = 3;
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
                resList.smoothScrollToPosition(0);
            break;
            case "titulo":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.songName.compareTo(o2.songName);
                    }
                });
                resList.smoothScrollToPosition(0);
            break;
            case "categoria":
                adapterSong.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.category.compareTo(o2.category);
                    }
                });
                resList.smoothScrollToPosition(0);
            break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapterSong.clear();
        server.getPlaylistData(idPlaylist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Playlist playlistSelected = new Playlist(data,false);
                    adapterSong.addAll(playlistSelected.songs);
                    if ( searchType == 1 ) {
                        sortBy("titulo");
                    }
                    else if ( searchType == 2 ) {
                        sortBy("categoria");
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

    private void removeSong() {
        Intent i=new Intent(this, removeSongFromPlaylist.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("id", idPlaylist);
        startActivityForResult(i, 1);
    }

}
