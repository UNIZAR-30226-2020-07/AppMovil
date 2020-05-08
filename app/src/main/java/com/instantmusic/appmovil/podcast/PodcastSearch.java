package com.instantmusic.appmovil.podcast;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.album.AlbumsAdapter;
import com.instantmusic.appmovil.friends.FriendsSearch;
import com.instantmusic.appmovil.main.Login;
import com.instantmusic.appmovil.main.Search;
import com.instantmusic.appmovil.main.Settings;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PodcastSearch extends AppCompatActivity implements JSONConnection.Listener {
    private ListView resList;
    private TextView searchTip1;
    private TextView searchTip2;
    private ImageView lupaGrande;
    private LinearLayout searchMenu;
    private String playList;
    private String creador;
    private int idPlaylist;
    private ArrayList<Integer> songs;
    private serverInterface server;
    private ArrayList<Album> arrayOfPodcasts = new ArrayList<>();
    private AlbumsAdapter adapterPodcast;
    private Button changeMenu2;
    private Button orderName;
    private Button orderCategory;
    private Button orderArtist;
    int searchType = 1;
    private EditText changeMenu;
    private int page = 1;
    private int cruz = 0;
    private EditText shit;
    private boolean ultima = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_search_podcast);
        resList = findViewById(R.id.searchRes);
        server = new remoteServer();
        resList = findViewById(R.id.searchRes);
        searchTip1 = findViewById(R.id.searchTip1);
        searchTip2 = findViewById(R.id.searchTip2);
        lupaGrande = findViewById(R.id.lupaGrande);
        searchMenu = findViewById(R.id.searchMenu);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playList = extras.getString("playlist");
            creador = extras.getString("creador");
            songs = extras.getIntegerArrayList("canciones");
            idPlaylist = extras.getInt("idPlaylist");
        }
        adapterPodcast = new AlbumsAdapter(this, arrayOfPodcasts, 2);
        searchMenu = findViewById(R.id.searchMenu);
        resList.setAdapter(adapterPodcast);
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Album> search = (ArrayAdapter<Album>) parent.getAdapter();
                Album podcast = search.getItem(position);
                if (podcast != null) {
                    Podcast(podcast.id);
                }
            }
        });
        EditText search = findViewById(R.id.searchbar2);
        shit = search;
        Button confirmButton = findViewById(R.id.search);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerForContextMenu(resList);
                searchTip1.setVisibility(View.INVISIBLE);
                searchTip2.setVisibility(View.INVISIBLE);
                lupaGrande.setVisibility(View.INVISIBLE);
                ultima = false;
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
        Button Button2 = findViewById(R.id.menuButton2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
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
        Button s3 = findViewById(R.id.switchArtist);
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                nameActivated();
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenu.setVisibility(View.INVISIBLE);
                artistActivated();

            }
        });


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMenu.setVisibility(View.INVISIBLE);
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void nameActivated() {
        searchType = 1;
        Button s1 = findViewById(R.id.switchName);
        Button s3 = findViewById(R.id.switchArtist);
        s1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s3.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint("Search by title");

    }

    private void artistActivated() {
        searchType = 3;
        Button s1 = findViewById(R.id.switchName);
        Button s3 = findViewById(R.id.switchArtist);
        s3.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick512));
        s1.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        EditText barra = findViewById(R.id.searchbar2);
        barra.setHint("Search by creator");

    }

    private void Home() {
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Search() {
        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Friends() {
        Intent i = new Intent(this, FriendsSearch.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Settings() {
        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

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

    private void Podcast(int idSong) {
        Intent i = new Intent(this, PodcastActivity.class);
        i.putExtra(this.getPackageName() + ".id", idSong);
        this.startActivity(i);
    }

    private void searchNextPage() {
        if (!ultima) {
            page = page + 1;
            switch (searchType) {
                case 1:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchPodcasts(page, shit.getText().toString(), this);
                    }
                    break;
                case 3:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchArtistsPodcasts(page, shit.getText().toString(), this);
                    }
                    break;
            }
        }
    }

    private void search() {
        searchMenu.setVisibility(View.INVISIBLE);
        adapterPodcast.clear();
        shit = findViewById(R.id.searchbar2);
        cruz = 1;
        if (page == 1) {
            adapterPodcast.clear();
        }
        Button cruzButton = findViewById(R.id.optionSearch);
        cruzButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.close));
        if (!ultima) {
            switch (searchType) {
                case 1:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchPodcasts(page, shit.getText().toString(), this);
                    }
                    break;
                case 3:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchArtists(page, shit.getText().toString(), new JSONConnection.Listener() {
                            @Override
                            public void onValidResponse(int responseCode, JSONObject data) {
                                if (responseCode == 200) {

                                }
                            }

                            @Override
                            public void onErrorResponse(Throwable throwable) {

                            }
                        });
                    }
                    break;

            }
            resList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (resList.getLastVisiblePosition() == resList.getAdapter().getCount() - 1 && totalItemCount != 0) {
                        searchNextPage();
                    }
                }
            });
        }
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            if (!ultima) {
                resList.setAdapter(adapterPodcast);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Album> newPodcasts = Album.fromJson(results, false, null, false);
                adapterPodcast.addAll(newPodcasts);
                if (data.isNull("next")) {
                    ultima = true;
                }
            }
        } catch (JSONException e) {
            onErrorResponse(e);
        }
    }

    @Override
    public void onErrorResponse(Throwable throwable) {

    }
}
