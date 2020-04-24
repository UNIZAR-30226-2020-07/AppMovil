package com.instantmusic.appmovil.artist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.adapter.HorizontalListView;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.album.AlbumActivity;
import com.instantmusic.appmovil.album.AlbumsAdapter;
import com.instantmusic.appmovil.main.Search;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ArtistActivity extends AppCompatActivity {
    private ArrayList<Album> arrayOfAlbums = new ArrayList<>();
    private AlbumsAdapter adapterAlbums;
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private SongsAdapter adapterSongs;
    private HorizontalListView albums;
    private HorizontalListView songs;
    private int idArtist;
    private serverInterface server;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_artist);
        server = new remoteServer();
        albums = findViewById(R.id.myPlayLists);
        songs = findViewById(R.id.mySongs);
        Button Button1 = findViewById(R.id.backButton);
        adapterAlbums = new AlbumsAdapter(this, arrayOfAlbums, 1);
        adapterSongs = new SongsAdapter(this, arrayOfSongs, 2);
        albums.setAdapter(adapterAlbums);
        songs.setAdapter(adapterSongs);

        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            idArtist = extras.getInt("idArtist");
        }

        server.getArtistData(idArtist, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if ( responseCode == 200 ) {
                        Artist artista = new Artist(data);
                        TextView name=findViewById(R.id.artisName2);
                        name.setText(data.getString("name"));
                        JSONArray albumsArtist = data.getJSONArray("albums");
                        adapterAlbums.addAll(Album.fromJson(albumsArtist, true, artista));

                        server.searchSongsByArtist(1, artista.name, new JSONConnection.Listener() {
                            @Override
                            public void onValidResponse(int responseCode, JSONObject data) {
                                if ( responseCode == 200 ) {
                                    try {
                                        JSONArray results = data.getJSONArray("results");
                                        ArrayList<Song> newSongs = Song.fromJson(results, true, null);
                                        adapterSongs.addAll(newSongs);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onErrorResponse(Throwable throwable) {

                            }
                        });
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });

        albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Album> search = (ArrayAdapter<Album>) parent.getAdapter();
                Album album =  search.getItem(position);
                assert album != null;
                openAlbum(album.id);
            }
        });

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backScreen();
            }
        });
    }

    private void openAlbum(int idAlbum) {
        Intent i = new Intent(ArtistActivity.this, AlbumActivity.class);
        i.putExtra("idAlbum", idAlbum);
        this.startActivity(i);
    }

    private void backScreen(){
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
}
