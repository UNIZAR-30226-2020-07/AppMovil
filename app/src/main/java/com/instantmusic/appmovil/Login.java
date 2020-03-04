package com.instantmusic.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private static final int SEARCH = Menu.FIRST;
    private AutoCompleteTextView shit;
    private serverInterface server;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server=new localServer(this);
//        registerForContextMenu(resList);

        Button confirmButton = findViewById(R.id.menuButton1);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
    }

    private void Search() {
        Intent i = new Intent(this, Search.class);
        startActivityForResult(i, 1);

    }
    @Override
    public void onBackPressed() {}
}
