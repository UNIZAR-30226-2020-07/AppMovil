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
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;
import org.json.JSONObject;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    private ListView resList;
    private Button playB;
    private serverInterface server;
    private String playList;
    private String creador;
    private ArrayList<Integer> songs;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSong;
    private LinearLayout searchMenu;
    private LinearLayout changeMenu;
    private Button delete;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_playlists);
        resList = findViewById(R.id.playlist);
        server = new remoteServer();
        playB = findViewById(R.id.playButton);
        resList = findViewById(R.id.playlist);
        Bundle extras = getIntent().getExtras();
        playList= extras.getString("playlist");
        creador= extras.getString("creador");
        adapterSong = new SongsAdapter(this, arrayOfSongs,0);
        searchMenu=findViewById(R.id.searchMenu);
        changeMenu=findViewById(R.id.changeMenu);
        resList.setAdapter(adapterSong);
        songs = extras.getIntegerArrayList("canciones");
        if ( songs != null ) {
            for (int i = 0; i < songs.size(); i++) {
                server.getSongData(songs.get(i), new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if ( responseCode == 200 ) {
                            Song newSong = new Song(data);
                            adapterSong.add(newSong);
                        }
                    }
                    @Override
                    public void onErrorResponse(Throwable throwable) {
                    }
                });
            }
        }
        TextView name=findViewById(R.id.playlistName);
        EditText change=findViewById(R.id.change);
        change.setText(playList);
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
                resList.setSelection(0);
                registerForContextMenu(resList);
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = (Song) search.getItem(position);
                if ( cancion != null ) {
                    String name = cancion.songName;
                    String artista = cancion.artist;
                    int duracion = cancion.duration;
                    String url = cancion.url;
                    Song(name, artista,duracion,url,position);
                }
            }
        });

        delete=findViewById(R.id.addPlaylist);
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
                    findViewById(R.id.playlistName).setVisibility(View.VISIBLE);
                } else {
                    changeMenu.setVisibility(View.VISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.INVISIBLE);
                }
                EditText change=findViewById(R.id.change);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(change, InputMethodManager.SHOW_IMPLICIT);
                change.requestFocus();
                change.setSelection(change.getText().length());
            }
        });
        Button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeMenu.getVisibility() == View.VISIBLE) {
                    changeMenu.setVisibility(View.INVISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.VISIBLE);
                } else {
                    changeMenu.setVisibility(View.VISIBLE);
                    findViewById(R.id.playlistName).setVisibility(View.INVISIBLE);
                }
                EditText change=findViewById(R.id.change);
                playList=change.getText().toString();
                TextView playlistname= findViewById(R.id.playlistName);
                playlistname.setText(playList);
                //////////////////////////////////////////////////////////////////////////AQUI SE AÑADE LA OPERACION PARA CAMBIAR EL NOMBRE A LA PLAYLIST EN EL BACKEND
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                } else {
                    searchMenu.setVisibility(View.VISIBLE);
                }
                deletePlaylist();
            }
        });
    }

    private void deletePlaylist() {//Aqui puedes hacer la funciona de borrar la playlist en la que se encuentra el usuario

    }

    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
    private void Song(String songName, String autorName, int durationSong, String stream_url, int position) {
        Intent i = new Intent(this, PlaylistSongs.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        i.putExtra(this.getPackageName() + ".duration", durationSong);
        i.putExtra(this.getPackageName() + ".url", stream_url);
        i.putExtra(this.getPackageName() + ".songs", songs);
        i.putExtra(this.getPackageName() + ".positionId", position);
        this.startActivity(i);
    }

}
