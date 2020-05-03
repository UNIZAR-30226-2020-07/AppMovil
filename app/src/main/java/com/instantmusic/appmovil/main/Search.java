package com.instantmusic.appmovil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.album.AlbumActivity;
import com.instantmusic.appmovil.album.AlbumsAdapter;
import com.instantmusic.appmovil.artist.Artist;
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.artist.ArtistsAdapter;
import com.instantmusic.appmovil.podcast.PodcastSearch;
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
import java.util.Collections;

public class Search extends AppCompatActivity implements JSONConnection.Listener {
    private ListView resList;
    private TextView searchTip1;
    private TextView searchTip2;
    private ImageView lupaGrande;
    private LinearLayout searchMenu;
    private int searchType = 1;
    private EditText shit;
    private serverInterface server;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private ArrayList<Album> arrayOfAlbums = new ArrayList<>();
    private ArrayList<Artist> arrayOfArtists = new ArrayList<>();
    private SongsAdapter adapterSong;
    private AlbumsAdapter adapterAlbum;
    private ArtistsAdapter adapterArtist;
    private int cruz = 0;
    private int page = 1;
    private boolean ultima = false;

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
        adapterAlbum = new AlbumsAdapter(this, arrayOfAlbums, 0);
        adapterArtist = new ArtistsAdapter(this, arrayOfArtists);
        resList.setAdapter(adapterSong);
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchType == 1 || searchType == 2) {
                    ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                    Song cancion = search.getItem(position);
                    if (cancion != null) {
                        Song(cancion);
                    }
                } else if (searchType == 3) {
                    ArrayAdapter<Artist> search = (ArrayAdapter<Artist>) parent.getAdapter();
                    Artist artista = search.getItem(position);
                    if (artista != null) {
                        Artist(artista.id);
                    }
                } else if (searchType == 4) {
                    ArrayAdapter<Album> search = (ArrayAdapter<Album>) parent.getAdapter();
                    Album album = search.getItem(position);
                    if (album != null) {
                        Album(album.id);
                    }
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

    private void searchNextPage() {
        if (!ultima) {
            page = page + 1;
            switch (searchType) {
                case 1:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchSongs(page, shit.getText().toString(), this);
                    }
                    break;
                case 2:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchGenres(page, shit.getText().toString(), this);
                    }
                    break;
                case 3:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchArtists(page, shit.getText().toString(), this);
                    }
                    break;
                case 4:
                    if (!(shit.getText().toString().equals(""))) {
                        server.searchAlbums(page, shit.getText().toString(), this);
                    }
                    break;
            }
        }
    }

    private void nameActivated() {
        page = 1;
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
        page = 1;
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
        page = 1;
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
        page = 1;
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
        nameActivated();
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Podcasts() {
        nameActivated();
        Intent i = new Intent(this, PodcastSearch.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Friends() {
        nameActivated();
        Intent i = new Intent(this, Friends.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Settings() {
        nameActivated();
        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Song(Song song) {
        IntentTransfer.setData("songs", Collections.singletonList(song));
        IntentTransfer.setData("positionId", 0);
        IntentTransfer.setData("botonPlay", false);

        this.startActivity(new Intent(this, SongActivity.class));
    }

    private void Album(int idAlbum) {
        Intent i = new Intent(Search.this, AlbumActivity.class);
        i.putExtra("idAlbum", idAlbum);
        this.startActivity(i);
    }

    private void Artist(int idArtist) {
        Intent i = new Intent(Search.this, ArtistActivity.class);
        i.putExtra("idArtist", idArtist);
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
        shit = findViewById(R.id.searchbar2);
        String busqueda=shit.getText().toString();
        if (page == 1) {
            adapterSong.clear();
            adapterAlbum.clear();
            adapterArtist.clear();
        }
        cruz = 1;
        Button cruzButton = findViewById(R.id.optionSearch);
        cruzButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.close));
        if (!ultima) {
            switch (searchType) {
                case 1:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchSongs(page, busqueda, this);
                    }
                    break;
                case 2:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchGenres(page, busqueda, this);
                    }
                    break;
                case 3:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchArtists(page, busqueda, this);
                    }
                    break;
                case 4:
                    if (!(shit.getText().toString().equals(""))) {
                        resList.setVisibility(View.VISIBLE);
                        page = 1;
                        server.searchAlbums(page, busqueda, this);
                    }
                    break;

            }
        }
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            if (searchType == 4 && !ultima) {
                resList.setAdapter(adapterAlbum);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Album> newAlbums = Album.fromJson(results, false, null);
                adapterAlbum.addAll(newAlbums);
            } else if (searchType == 3 && !ultima) {
                resList.setAdapter(adapterArtist);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Artist> newArtists = Artist.fromJson(results);
                adapterArtist.addAll(newArtists);
            } else if (!ultima) {
                resList.setAdapter(adapterSong);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Song> newSongs = Song.fromJson(results, true, null);
                adapterSong.addAll(newSongs);
            }
            if (data.isNull("next")) {
                ultima = true;
            }
        } catch (JSONException e) {
            onErrorResponse(e);
        }
    }

    @Override
    public void onErrorResponse(Throwable throwable) {
        throwable.printStackTrace();
    }
}
