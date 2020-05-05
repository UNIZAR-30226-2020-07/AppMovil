package com.instantmusic.appmovil.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.instantmusic.appmovil.main.Login;
import com.instantmusic.appmovil.main.Search;
import com.instantmusic.appmovil.main.Settings;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
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

public class FriendsSearch extends AppCompatActivity implements JSONConnection.Listener {
    private ListView resList;
    private int searchType = 1;
    private EditText shit;
    private serverInterface server;
    private ArrayList<Friend> arrayOfFriends = new ArrayList<>();
    private FriendsAdapter adapterFriends;


    private boolean ultima = false;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        server = new remoteServer();
        setContentView(R.layout.activity_instant_music_app_friends_search);
        resList = findViewById(R.id.searchRes);
        resList.setVisibility(View.INVISIBLE);
        adapterFriends =new FriendsAdapter(this, arrayOfFriends,0);
        resList.setAdapter(adapterFriends);
        server.searchAFriend(" ", new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if ( responseCode == 200 ) {
                        JSONArray results = data.getJSONArray("results");
                        ArrayList<Friend> newSongs = Friend.fromJson(results);
                        adapterFriends.addAll(newSongs);
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Playlist> search = (ArrayAdapter<Playlist>) parent.getAdapter();
                Playlist cancion = search.getItem(position);
                if (cancion != null) {
                    Playlist(cancion);
                }
            }
        });

        EditText search = findViewById(R.id.searchbar2);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                registerForContextMenu(resList);
                ultima = false;
                search();
                return true;
            }
        });
        shit = search;
        Button confirmButton = findViewById(R.id.addFriend);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addFriend();
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
        Button Button4 = findViewById(R.id.menuButton2);
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        Button Button5 = findViewById(R.id.menuButton5);
        Button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings();
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
    private void addFriend(){
        Intent i = new Intent(this, FriendsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);
    }
    private void searchNextPage() {

            server.searchAFriend(shit.getText().toString(), this);

    }
    private void Home() {

        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Podcasts() {

        Intent i = new Intent(this, PodcastSearch.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Search() {

        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Settings() {

        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }

    private void Playlist(Playlist song) {
        Intent i=new Intent(this,PlaylistActivity.class);
        Bundle extra=new Bundle();
        extra.putString("playlist",song.playlistName);
        extra.putString("creador",song.user);
        extra.putInt("idPlaylist",song.id);
        this.startActivity(new Intent(this, PlaylistActivity.class));
    }

    private void Album(int idAlbum) {
        Intent i = new Intent(FriendsSearch.this, AlbumActivity.class);
        i.putExtra("idAlbum", idAlbum);
        this.startActivity(i);
    }

    private void Artist(int idArtist) {
        Intent i = new Intent(FriendsSearch.this, ArtistActivity.class);
        i.putExtra("idArtist", idArtist);
        this.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        ListView search = findViewById(R.id.searchRes);
        search.setVisibility(View.INVISIBLE);
    }

    private void search() {
        shit = findViewById(R.id.searchbar2);
        String busqueda=shit.getText().toString();
        adapterFriends.clear();
        if (!ultima) {
            if (!(shit.getText().toString().equals(""))) {
                resList.setVisibility(View.VISIBLE);
                server.searchAFriend(busqueda,this);
            }
        }
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            if (!ultima) {
                resList.setAdapter(adapterFriends);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Friend> newSongs = Friend.fromJson(results);
                adapterFriends.addAll(newSongs);
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
