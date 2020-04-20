package com.instantmusic.appmovil.main;

import android.content.Intent;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Search extends AppCompatActivity implements JSONConnection.Listener {
    private ListView resList;
    private EditText search;
    private TextView searchTip1;
    private TextView searchTip2;
    private ImageView lupaGrande;
    private LinearLayout searchMenu;
    private int searchType = 1;
    private boolean flag_loading = false;
    private EditText shit;
    private serverInterface server;
    private ArrayList<Song> arrayOfSongs = new ArrayList<Song>();
    private SongsAdapter adapterSong;
    private int cruz = 0;
    private int page=1;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        server = new remoteServer();
        setContentView(R.layout.activity_instant_music_app_search);
        searchTip1 = findViewById(R.id.searchTip1);
        searchTip2 = findViewById(R.id.searchTip2);
        lupaGrande = findViewById(R.id.lupaGrande);
        searchMenu = findViewById(R.id.searchMenu);
        resList = findViewById(R.id.searchRes);
        resList.setVisibility(View.INVISIBLE);
        adapterSong = new SongsAdapter(this, arrayOfSongs, 0);
        resList.setAdapter(adapterSong);
        resList.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        //additems();
                    }
                }
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = (Song) search.getItem(position);
                String name = cancion.songName;
                String artista = cancion.artist;
                int duracion = cancion.duration;
                String url = cancion.url;
                int idSong = cancion.id;
                Song(name, artista, duracion, url, idSong);
            }
        });

        search = findViewById(R.id.searchbar2);
        shit = findViewById(R.id.searchbar2);
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerForContextMenu(resList);
                searchTip1.setVisibility(View.INVISIBLE);
                searchTip2.setVisibility(View.INVISIBLE);
                lupaGrande.setVisibility(View.INVISIBLE);
                search();
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
                if (cruz == 0) {
                    if (searchMenu.getVisibility() == View.VISIBLE) {
                        searchMenu.setVisibility(View.INVISIBLE);
                    } else {
                        searchMenu.setVisibility(View.VISIBLE);
                    }

                } else {
                    ListView search = findViewById(R.id.searchRes);
                    search.setVisibility(View.INVISIBLE);
                    searchTip1.setVisibility(View.VISIBLE);
                    searchTip2.setVisibility(View.VISIBLE);
                    lupaGrande.setVisibility(View.VISIBLE);
                    Button cruzButton = findViewById(R.id.optionSearch);
                    cruzButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.downarrow));
                    cruz = 0;
                }
            }
        });

        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        Button s3 = findViewById(R.id.switchArtist);
        Button s4 = findViewById(R.id.switchAlbum);

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
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                artistActivated();

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                albumActivated();
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

    private void searchNextPage() {
        page=page+1;
        server.searchSongs(page,shit.getText().toString(),this);
    }

    private void nameActivated() {
        searchType = 1;
        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        Button s3 = findViewById(R.id.switchArtist);
        Button s4 = findViewById(R.id.switchAlbum);
        s1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s2.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s3.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s4.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint(getResources().getString(R.string.search1));

    }

    private void categoryActivated() {
        searchType = 2;
        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        Button s3 = findViewById(R.id.switchArtist);
        Button s4 = findViewById(R.id.switchAlbum);
        s2.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s1.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s3.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s4.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint(getResources().getString(R.string.search3));
    }

    private void artistActivated() {
        searchType = 3;
        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        Button s3 = findViewById(R.id.switchArtist);
        Button s4 = findViewById(R.id.switchAlbum);
        s3.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s1.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s2.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s4.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint(getResources().getString(R.string.search2));

    }

    private void albumActivated() {
        searchType = 4;
        Button s1 = findViewById(R.id.switchName);
        Button s2 = findViewById(R.id.switchCategory);
        Button s3 = findViewById(R.id.switchArtist);
        Button s4 = findViewById(R.id.switchAlbum);
        s4.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s2.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s1.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        s3.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint(getResources().getString(R.string.search4));
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

    private void Song(String songName, String autorName, int durationSong, String stream_url, int idSong) {
        Intent i = new Intent(this, SongActivity.class);
        i.putExtra(this.getPackageName() + ".dataString", songName);
        i.putExtra(this.getPackageName() + ".String", autorName);
        i.putExtra(this.getPackageName() + ".duration", durationSong);
        i.putExtra(this.getPackageName() + ".url", stream_url);
        i.putExtra(this.getPackageName() + ".id", idSong);
        this.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        ListView search = findViewById(R.id.searchRes);
        search.setVisibility(View.INVISIBLE);
        searchTip1.setVisibility(View.VISIBLE);
        searchTip2.setVisibility(View.VISIBLE);
        lupaGrande.setVisibility(View.VISIBLE);
        Button cruzButton = findViewById(R.id.optionSearch);
        cruzButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.downarrow));
        cruz = 0;
    }

    private void search() {
        adapterSong.clear();
        shit = findViewById(R.id.searchbar2);
        cruz = 1;
        Button cruzButton = findViewById(R.id.optionSearch);
        cruzButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.close));

        switch (searchType) {
            case 1:
                if ( !(shit.getText().toString().equals("")) ) {
                    resList.setVisibility(View.VISIBLE);
                    page=1;
                    server.searchSongs(page,shit.getText().toString(), this);
                }
                break;
            case 2:
                if ( !(shit.getText().toString().equals("")) ) {
                    resList.setVisibility(View.VISIBLE);
                    page=1;
                    server.searchGenres(page,shit.getText().toString(), this);
                }
                break;
            case 3:
                if ( !(shit.getText().toString().equals("")) ) {
                    resList.setVisibility(View.VISIBLE);
                    page=1;
                    server.searchArtists(page,shit.getText().toString(), this);
                }
                break;
            case 4:
                if ( !(shit.getText().toString().equals("")) ) {
                    resList.setVisibility(View.VISIBLE);
                    page=1;
                    server.searchAlbums(page,shit.getText().toString(), this);
                }
                break;
        }
        resList.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (resList.getLastVisiblePosition() ==resList.getAdapter().getCount() - 1 && totalItemCount != 0) {
                    searchNextPage();
                }
            }
        });
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            JSONArray results = data.getJSONArray("results");
            ArrayList<Song> newSongs = Song.fromJson(results);
            adapterSong.addAll(newSongs);
        } catch (JSONException e) {
            onErrorResponse(e);
        }
    }

    @Override
    public void onErrorResponse(Throwable throwable) {
        throwable.printStackTrace();
    }
}
