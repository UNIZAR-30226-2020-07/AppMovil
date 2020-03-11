package com.instantmusic.appmovil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Login extends AppCompatActivity {
    private static final int SEARCH = Menu.FIRST;
    private AutoCompleteTextView shit;
    private serverInterface server;
    private String user;
    private ListView myPlaylist;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server = new localServer(this);
//        registerForContextMenu(resList);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("email");
        server.addPlaylist("Jojos",user);
        server.addSongToPlaylist("Fighting Gold","Jojos",user);
        myPlaylist = findViewById(R.id.myPlaylists);
        Button Button2 = findViewById(R.id.menuButton2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        Button Button3 = findViewById(R.id.menuButton3);
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Podcasts();
            }
        });
        Button Button4 = findViewById(R.id.menuButton4);
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends();
            }
        });
        Button Button5 = findViewById(R.id.menuButton5);
        Button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings();
            }
        });
//        Cursor shitCursor=server.allPlaylists(user);
        //startManagingCursor(shitCursor);
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        //int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
       // SimpleCursorAdapter search =
         //       new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
       // myPlaylist.setAdapter(search);
    }



    private void Search() {
        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Podcasts() {
        Intent i = new Intent(this, Podcasts.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Friends() {
        Intent i = new Intent(this, Friends.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Settings() {
        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    @Override
    public void onBackPressed() {
    }
}
