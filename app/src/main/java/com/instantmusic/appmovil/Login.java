package com.instantmusic.appmovil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import com.instantmusic.appmovil.localServer;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.net.UnknownServiceException;
import java.util.List;

public class Login extends AppCompatActivity {
    private ListView resList;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER = Menu.FIRST+1;
    private static final int LOGIN = Menu.FIRST + 2;
    private AutoCompleteTextView shit;
    private serverInterface server;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        resList =findViewById(R.id.searchResults);
        server=new localServer(this);
//        registerForContextMenu(resList);
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSearch();
            }
        });
    }

    private void confirmSearch() {
        shit = findViewById(R.id.searchbar);
        Cursor shitCursor = server.searchShit(shit.getText().toString());
        startManagingCursor(shitCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.text2};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
        resList.setAdapter(search);
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
