package com.instantmusic.appmovil;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Song extends AppCompatActivity {
    private ListView resList;
    private String stringSearch;
    private View resElement;
    private static final int SEARCH = Menu.FIRST;
    private AutoCompleteTextView shit;
    private serverInterface server;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_song);
        server=new localServer(this);
    }
}
