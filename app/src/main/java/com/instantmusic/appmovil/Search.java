package com.instantmusic.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements JSONConnection.Listener{
    private ListView resList;
    private EditText search;
    private TextView searchTip1;
    private TextView searchTip2;
    private ImageView lupaGrande;
    private LinearLayout searchMenu;
    private int searchType = 1;
    private static final int SEARCH = Menu.FIRST;
    private static final int RECOVER = Menu.FIRST + 1;
    private static final int LOGIN = Menu.FIRST + 2;
    private EditText shit;
    private serverInterface server;
    private String user;
    private int currentPage = 1;
    private ArrayAdapter<String> adapter;
    private ArrayList<Song> arrayOfSongs = new ArrayList<Song>();
    private SongsAdapter adapterSong;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        server=new remoteServer();
        setContentView(R.layout.activity_instant_music_app_search);
        searchTip1 = findViewById(R.id.searchTip1);
        searchTip2 = findViewById(R.id.searchTip2);
        lupaGrande = findViewById(R.id.lupaGrande);
        searchMenu = findViewById(R.id.searchMenu);
        resList = findViewById(R.id.searchRes);
        adapterSong = new SongsAdapter(this, arrayOfSongs);
        resList.setAdapter(adapterSong);

        for(int i=0;i<resList.getCount();i++){
            SimpleCursorAdapter search=(SimpleCursorAdapter)resList.getAdapter();
            String categoria=search.getCursor().getString(3);
            System.out.println(categoria);

        }
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCursorAdapter search=(SimpleCursorAdapter)parent.getAdapter();
                String nombre=search.getCursor().getString(1);
                String artista=search.getCursor().getString(2);
                Song(nombre, artista);
            }
        });

        search = findViewById(R.id.searchbar2);
//        registerForContextMenu(resList);
        shit = findViewById(R.id.searchbar2);
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                search();
                resList.setSelection(0);
                registerForContextMenu(resList);
            }
        });
        Button Button1 = findViewById(R.id.menuButton1);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home();
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
        Button Button6 = findViewById(R.id.optionSearch);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                } else {
                    searchMenu.setVisibility(View.VISIBLE);
                }

            }
        });
        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                nameActivated();
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                categoryActivated();
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void nameActivated() {
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void categoryActivated() {
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);
    }

    private void Home() {
        Intent i = new Intent(this, Login.class);
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

    private void Song(String songName, String autorName) {
        Intent i = new Intent(this, SongActivity.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        this.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    private void search() {
        adapterSong.clear();
        shit = findViewById(R.id.searchbar2);
        switch (searchType) {
            case 1:
                server.searchShit(shit.getText().toString(),this);
                searchTip1.setVisibility(View.INVISIBLE);
                searchTip2.setVisibility(View.INVISIBLE);
                lupaGrande.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //shitcursor=server.searchCategory(shit.getText().toString());
                break;
            case 3:
                //shitcursor=server.searchArtist(shit.getText().toString());
                break;
            case 4:
                //shitcursor=server.searchAlbum(shit.getText().toString());
                break;
        }
    }
        /*if (shitCursor == null) {
            searchTip1.setVisibility(View.VISIBLE);
            searchTip2.setVisibility(View.VISIBLE);
            lupaGrande.setVisibility(View.VISIBLE);
            // Create an array to specify the fields we want to display in the list (only TITLE)
            String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

            // and an array of the fields we want to bind those fields to (in this case just text1)
            int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
            SimpleCursorAdapter search =
                    new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
            resList.setAdapter(search);
        } else {
            searchTip1.setVisibility(View.INVISIBLE);
            searchTip2.setVisibility(View.INVISIBLE);
            lupaGrande.setVisibility(View.INVISIBLE);

            startManagingCursor(shitCursor);

            // Create an array to specify the fields we want to display in the list (only TITLE)
            String[] from = new String[]{UsersDbAdapter.KEY_NAME, UsersDbAdapter.KEY_ARTIST, UsersDbAdapter.KEY_CATEGORY};

            // and an array of the fields we want to bind those fields to (in this case just text1)
            int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
            SimpleCursorAdapter search =
                    new SimpleCursorAdapter(this, R.layout.search_row, shitCursor, from, to);
            resList.setAdapter(search);
        }

    }
    */
    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            JSONArray results = data.getJSONArray("results");
            ArrayList<Song> newSongs = Song.fromJson(results);
            adapterSong.addAll(newSongs);
        }
        catch (JSONException e) {
            onErrorResponse(e);
        }
    }

    @Override
    public void onErrorResponse(Throwable throwable) {
        Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
    }
   /* public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SEARCH:
                shit = findViewById(R.id.searchbar2);
                server.searchShit(shit.getText().toString());
                return true;

        }
        return super.onOptionsItemSelected(item);
    }*/
}
