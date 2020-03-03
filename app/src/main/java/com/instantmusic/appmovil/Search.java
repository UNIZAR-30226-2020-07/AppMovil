package com.instantmusic.appmovil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {
    private ListView resList;
    private String stringSearch;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER = Menu.FIRST + 1;
    private static final int LOGIN = Menu.FIRST + 2;
    private AutoCompleteTextView shit;
    private serverInterface server;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_search);
        resList = findViewById(R.id.searchRes);
        Bundle datos = this.getIntent().getExtras();
        stringSearch = datos.getString("search");
        server = new localServer(this);
//        registerForContextMenu(resList);
        shit = findViewById(R.id.searchbar2);
        shit.setText(stringSearch);
        Cursor shitCursor = server.searchShit(shit.getText().toString());
        startManagingCursor(shitCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
        resList.setAdapter(search);
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                confirmSearch();
                resList.setSelection(0);

                resList.setVisibility(View.VISIBLE);

                registerForContextMenu(resList);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    private void confirmSearch() {
        shit = findViewById(R.id.searchbar);
        Cursor shitCursor = server.searchShit(shit.getText().toString());
        startManagingCursor(shitCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter search =
                new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
        resList.setAdapter(search);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SEARCH:
                shit = findViewById(R.id.searchbar);
                server.searchShit(shit.getText().toString());
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
