package com.instantmusic.appmovil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Playlists extends AppCompatActivity {
    private ListView resList;
    private Button playB;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER = Menu.FIRST + 1;
    private static final int LOGIN = Menu.FIRST + 2;
    private serverInterface server;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_playlists);
        resList = findViewById(R.id.playlist);
        server = new localServer(this);
        playB = findViewById(R.id.playButton);

        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Song("Nombre de la cancion","Nombre del artista");
            }
        });
    }

    private void Song(String songName, String autorName) {
        Intent i = new Intent(this, Song.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        this.startActivity(i);
    }

}
