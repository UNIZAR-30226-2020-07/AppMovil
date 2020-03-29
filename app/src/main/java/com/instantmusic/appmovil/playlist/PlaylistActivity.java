package com.instantmusic.appmovil.playlist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.UsersDbAdapter;
import com.instantmusic.appmovil.server.localServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.SongActivity;

public class PlaylistActivity extends AppCompatActivity {
    private ListView resList;
    private Button playB;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER = Menu.FIRST + 1;
    private static final int LOGIN = Menu.FIRST + 2;
    private serverInterface server;
    private String playList;
    private String creador;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_playlists);
        resList = findViewById(R.id.playlist);
        server = new localServer(this);
        playB = findViewById(R.id.playButton);
        resList = findViewById(R.id.playlist);
        Bundle extras = getIntent().getExtras();
        playList= extras.getString("playlist");
        creador= extras.getString("creador");
        TextView name=findViewById(R.id.playlistName);
        TextView creator=findViewById(R.id.playlistCreator);
        name.setText(playList);
        creator.setText(creador);
        search();
        Button Button1 = findViewById(R.id.backButton);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        server = new localServer(this);
//        registerForContextMenu(resList);
        playB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resList.setSelection(0);
                registerForContextMenu(resList);
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCursorAdapter search=(SimpleCursorAdapter)parent.getAdapter();
                String cancion=search.getCursor().getString(1);
                String artista=search.getCursor().getString(2);
               Song(cancion,artista);
            }
        });
    }

    private void search() {
        Cursor shitCursor = server.searchPlaylist(playList);
        startManagingCursor(shitCursor);
        String[] from = new String[]{UsersDbAdapter.KEY_NAME,UsersDbAdapter.KEY_ARTIST,UsersDbAdapter.KEY_CATEGORY};
        int[] to = new int[]{R.id.text1,R.id.text2,R.id.text3};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.playlist_row, shitCursor, from, to);
        resList.setAdapter(search);

    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
    private void Song(String songName, String autorName) {
        Intent i = new Intent(this, SongActivity.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        this.startActivity(i);
    }

}
