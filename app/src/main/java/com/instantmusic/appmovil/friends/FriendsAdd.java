package com.instantmusic.appmovil.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.PlaylistAdapter;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FriendsAdd extends AppCompatActivity {
    private ListView resList;
    private String username;
    private int id;
    private serverInterface server;
    private ArrayList<Friend> arrayOfFriends = new ArrayList<>();
    private FriendsAdapter adapterFriend;
    private Button confirmSearch;
    private EditText searchBar;
    private int idUser;
    private JSONArray friends;
    private TextView notFriend;
    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_addfriend);
        resList = findViewById(R.id.searchRes);
        confirmSearch=findViewById(R.id.search);
        searchBar=findViewById(R.id.searchbar2);
        notFriend=findViewById(R.id.notFriend);
        server = new remoteServer();
        adapterFriend = new FriendsAdapter(this, arrayOfFriends,2);
        resList.setAdapter(adapterFriend);
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Friend f=(Friend)resList.getItemAtPosition(position);
                    addFriend(f.id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        idUser=data.getInt("id");
                        friends=data.getJSONArray("friends");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });
        Button back=findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        confirmSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapterFriend.clear();
    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    private void search(){
        adapterFriend.clear();
        String busqueda=searchBar.getText().toString();
        server.searchAFriend(busqueda, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if ( responseCode == 200 ) {
                    JSONArray results=data.getJSONArray("results");
                    ArrayList<Friend> newSongs = Friend.fromJson(results, false);
                    adapterFriend.addAll(newSongs);
                    resList.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
    }
    private void addFriend(int id) throws JSONException {
        List<Integer> nfriends=new ArrayList<>();
        for(int i=0;i<friends.length();i++){
            JSONObject f=friends.getJSONObject(i);
            nfriends.add(f.getInt("id"));
        }
        nfriends.add(id);
        server.addFriend(nfriends,idUser, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode != 200 ) {
                    notFriend.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
    }
}
