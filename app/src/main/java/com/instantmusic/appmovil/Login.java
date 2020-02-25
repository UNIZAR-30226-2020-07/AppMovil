package com.instantmusic.appmovil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.instantmusic.appmovil.localServer;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Login extends AppCompatActivity {
    private ListView resList;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER =Menu.FIRST+1;
    private static final int LOGIN = Menu.FIRST + 2;
    private EditText cancion;
    private serverInterface server=new localServer();
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
                cancion.findViewById(R.id.searchbar);
                server.searchCancion(cancion.getText().toString(),Cursor );
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SEARCH:
                cancion=findViewById(R.id.searchbar);
                server.searchCancion(cancion.getText().toString());
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }
}
