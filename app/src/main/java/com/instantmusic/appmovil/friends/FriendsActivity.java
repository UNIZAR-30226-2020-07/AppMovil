package com.instantmusic.appmovil.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.PlaylistAdapter;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class FriendsActivity extends AppCompatActivity {
    private ListView resList;
    private String username;
    private int id;
    private serverInterface server;
    private ArrayList<Playlist> arrayOfPlaylists = new ArrayList<>();
    private PlaylistAdapter adapterPlaylist;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_friends);
        resList = findViewById(R.id.searchRes);
        server = new remoteServer();
        TextView name=findViewById(R.id.username);
        TextView friendPlaylist=findViewById(R.id.friendPlaylist);
        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            username= extras.getString("friend");
            id = extras.getInt("id");
            name.setText(username);
            friendPlaylist.setText(username+"'s");
        }
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylists,0);
        resList.setAdapter(adapterPlaylist);
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
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapterPlaylist.clear();
        server.searchAFriend(username, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Playlist playlistSelected = new Playlist(data,true);
                    adapterPlaylist.addAll(playlistSelected);
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

        IntentTransfer.setData("playlist", playlist.playlistName);
        IntentTransfer.setData("creador", playlist.user);
        IntentTransfer.setData("idPlaylist", playlist.id);

        this.startActivity(new Intent(this, PlaylistActivity.class));
    }
}
