package com.instantmusic.appmovil.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.PlaylistAdapter;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private ListView resList;
    private String username;
    private int id;
    private serverInterface server;
    private ArrayList<Playlist> arrayOfPlaylists = new ArrayList<>();
    private PlaylistAdapter adapterPlaylist;
    private LinearLayout searchMenu;
    private List<Integer> friends = new ArrayList<>();
    private int idUser;

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_friends);
        resList = findViewById(R.id.searchRes);
        server = new remoteServer();
        TextView name=findViewById(R.id.username);
        searchMenu=findViewById(R.id.searchMenu);
        TextView friendPlaylist=findViewById(R.id.friendPlaylist);
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            username= extras.getString("friend");
            id = extras.getInt("id");
            name.setText(username);
            friendPlaylist.setText(username+"'s");
        }
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylists,2);
        resList.setAdapter(adapterPlaylist);
        server.getUserById(id, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data)  {
                if ( responseCode == 200 ) {

                    Friend playlistSelected = new Friend(data,true);
                    if ( playlistSelected.playlists != null ) {
                        adapterPlaylist.addAll(playlistSelected.playlists);
                    }
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        idUser=data.getInt("id");
                        JSONArray userFriends=data.getJSONArray("friends");
                        for(int i=0;i<userFriends.length();i++){
                            JSONObject f=userFriends.getJSONObject(i);
                            if(f.getInt("id")!=id)
                            friends.add(f.getInt("id"));
                        }
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
        name.setText(username);
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Playlist> search = (ArrayAdapter<Playlist>) parent.getAdapter();
                Playlist cancion = search.getItem(position);
                if ( cancion != null ) {
                    Playlist(cancion);
                }
            }
        });
        Button back=findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
        Button Button6 = findViewById(R.id.optionSong3);
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
        Button delete=findViewById(R.id.deleteFriend);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                }
                else {
                    searchMenu.setVisibility(View.VISIBLE);
                }
                deleteFriend();

            }
        });
    }
    private void deleteFriend(){
        server.addFriend(friends,idUser, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                backScreen();
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapterPlaylist.clear();
        server.getUserById(id, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if ( responseCode == 200 ) {
                    Friend playlistSelected = new Friend(data,true);
                    if ( playlistSelected.playlists != null ) {
                        adapterPlaylist.addAll(playlistSelected.playlists);
                    }
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });
    }
    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    private void Playlist(Playlist playlist) {
        Intent i=new Intent(this, PlaylistActivity.class);
        i.putExtra("playlist", playlist.playlistName);
        i.putExtra("creador", username);
        i.putExtra("idPlaylist", playlist.id);
        i.putExtra("friends","f");
        this.startActivity(i);
    }
}
