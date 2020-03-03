package com.instantmusic.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.instantmusic.appmovil.localServer;
import android.database.Cursor;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.net.UnknownServiceException;
import java.util.List;

public class Login extends AppCompatActivity {
    private ListView resList;
    private String stringSearch;
    private View resElement;
    private static final int SEARCH = Menu.FIRST;
    private AutoCompleteTextView shit;
    private serverInterface server;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        resList =findViewById(R.id.searchRes);
        server=new localServer(this);
//        registerForContextMenu(resList);
        EditText searchBar=findViewById(R.id.searchbar);
        stringSearch=searchBar.getText().toString();
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSearch();
            }
        });
    }

    private void confirmSearch() {
        Intent i = new Intent(this, Search.class);
        Bundle bundle = new Bundle();
        bundle.putString("search",stringSearch);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtras(bundle);
        startActivityForResult(i, 0);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SEARCH:
                shit=findViewById(R.id.searchbar);
                server.searchShit(shit.getText().toString());
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }
}
