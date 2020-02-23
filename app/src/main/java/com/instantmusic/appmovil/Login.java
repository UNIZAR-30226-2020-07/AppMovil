package com.instantmusic.appmovil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private ListView mList;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER =Menu.FIRST+1;
    private static final int LOGIN = Menu.FIRST + 2;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);

        /*mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();*/
        mList =findViewById(R.id.searchResults);
        registerForContextMenu(mList);
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

    }
}
