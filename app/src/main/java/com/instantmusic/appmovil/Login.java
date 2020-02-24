package com.instantmusic.appmovil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Login extends AppCompatActivity {
    private ListView resList;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER =Menu.FIRST+1;
    private static final int LOGIN = Menu.FIRST + 2;
    private EditText cancion;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);

        /*mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();*/
        resList =findViewById(R.id.searchResults);

        registerForContextMenu(resList);
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchCancion();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SEARCH:
                searchCancion();
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchCancion() {
        cancion=findViewById(R.id.searchbar);
        String cancionSearch=cancion.toString();
        System.out.print(cancionSearch);
    }
}
