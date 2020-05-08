package com.instantmusic.appmovil.friends;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class FriendsSearch extends AppCompatActivity implements JSONConnection.Listener {
    private ListView resList;
    private int searchType = 1;
    private EditText shit;
    private serverInterface server;
    private ArrayList<Friend> arrayOfFriends = new ArrayList<>();
    private FriendsAdapter adapterFriends;
    private TextView user;

    private boolean ultima = false;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        server = new remoteServer();
        setContentView(R.layout.activity_instant_music_app_friends_search);
        resList = findViewById(R.id.searchRes);
        resList.setVisibility(View.INVISIBLE);
        user=findViewById(R.id.username);
        adapterFriends =new FriendsAdapter(this, arrayOfFriends,0);
        resList.setAdapter(adapterFriends);
        server.getUserData( new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if ( responseCode == 200 ) {
                        JSONArray friends=data.getJSONArray("friends");
                        ArrayList<Friend> newSongs = Friend.fromJson(friends,false);
                        adapterFriends.addAll(newSongs);
                        resList.setVisibility(View.VISIBLE);
                        user.setText(data.getString("username"));
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Friend> search = (ArrayAdapter<Friend>) parent.getAdapter();
                Friend cancion = search.getItem(position);
                if (cancion != null) {
                    Friend(cancion);
                }
            }
        });

        EditText search = findViewById(R.id.searchbar2);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(count==1){
                    search();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
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
    }
    private void addFriend(){
        Intent i = new Intent(this, FriendsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);
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

    private void Friend(Friend friend) {
        Intent i=new Intent(this,FriendsActivity.class);
        i.putExtra("friend",friend.username);
        i.putExtra("id",friend.id);
        this.startActivity(i);
    }

    @Override
    public void onBackPressed() {

    }

    private void search() {
        shit = findViewById(R.id.searchbar2);
        adapterFriends.clear();
        if (!ultima) {
            if (!(shit.getText().toString().equals(""))) {
                server.getUserData( new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                        if ( responseCode == 200 ) {
                            JSONArray friends=data.getJSONArray("friends");
                            String busqueda=shit.getText().toString();
                            JSONArray filterFriends=new JSONArray();
                            for (int i = 0; i < friends.length(); ++i) {
                                JSONObject obj = friends.getJSONObject(i);
                                String id = obj.getString("username");
                                if (id.equals(busqueda)) {
                                    filterFriends.put(obj);
                                }
                            }
                            if(busqueda.length()==0) {
                                ArrayList<Friend> newSongs = Friend.fromJson(friends, false);
                                adapterFriends.addAll(newSongs);
                                resList.setVisibility(View.VISIBLE);
                                user.setText(data.getString("username"));
                            }else{
                                ArrayList<Friend> newSongs = Friend.fromJson(filterFriends, false);
                                adapterFriends.addAll(newSongs);
                                resList.setVisibility(View.VISIBLE);
                                user.setText(data.getString("username"));
                            }
                        }
                    }
                    @Override
                    public void onErrorResponse(Throwable throwable) {
                    }
                });
            }
        }
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) {
        try {
            if (!ultima) {
                resList.setAdapter(adapterFriends);
                JSONArray results = data.getJSONArray("results");
                ArrayList<Friend> newSongs = Friend.fromJson(results,false);
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
