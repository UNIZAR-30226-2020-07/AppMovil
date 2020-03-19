package com.instantmusic.appmovil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class addPlaylist extends AppCompatActivity {
    private serverInterface server;
    private String user;
    private String name;
    private String song;
    private String playList;
    private ListView myPlaylist;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_addplaylist);
        server = new localServer(this);
//        registerForContextMenu(resList);
        myPlaylist = findViewById(R.id.myPlayLists);
        //name = aux.getString(3);
        Bundle extras = getIntent().getExtras();
        song= extras.getString("song");
        Cursor shitCursor = server.allPlaylists("Admin");
        startManagingCursor(shitCursor);
        String[] from = new String[]{UsersDbAdapter.KEY_NAMEP};
        int[] to = new int[]{R.id.text1};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.myplaylists2_row, shitCursor, from, to);
        myPlaylist.setAdapter(search);
        Button back=findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        myPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               SimpleCursorAdapter search=(SimpleCursorAdapter)parent.getAdapter();
               playList=search.getCursor().getString(1);
               long out=server.addSongToPlaylist(song,playList);
               System.out.println(song);
               System.out.println(playList);
               System.out.println(out);
               backScreen();
            }
        });

    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
}
