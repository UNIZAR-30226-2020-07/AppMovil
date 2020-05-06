package com.instantmusic.appmovil.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.adapter.HorizontalListView;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.album.AlbumsAdapter;
import com.instantmusic.appmovil.friends.FriendsSearch;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.playlist.PlaylistAdapter;
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

public class Login extends AppCompatActivity {
    private ArrayList<Playlist> arrayOfPlaylist = new ArrayList<>();
    private ArrayList<Album> arrayOfPodcast = new ArrayList<>();
    private ArrayList<Song> arrayOfSongs = new ArrayList<>();
    private PlaylistAdapter adapterPlaylist;
    private AlbumsAdapter adapterPodcast;
    private SongsAdapter adapterSongs;
    private serverInterface server;
    private HorizontalListView myPlaylist;
    private HorizontalListView mySongs;
    private HorizontalListView myPodcast;
    private String username;
    private Button pausedSong;
    private Button pausedSong2;
    private LinearLayout layoutPaused;
    private Song paused_song;
    private int pause_seconds;

    @SuppressLint("WrongViewCast")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_login);
        server = new remoteServer();
        myPlaylist = findViewById(R.id.myPlayLists);
        mySongs = findViewById(R.id.mySongs);
        myPodcast = findViewById(R.id.myPodcasts);
        pausedSong = findViewById(R.id.button);
        pausedSong2 = findViewById(R.id.button3);
        layoutPaused = findViewById(R.id.layout);
        layoutPaused.setVisibility(View.INVISIBLE);
        pausedSong.setVisibility(View.INVISIBLE);
        pausedSong.setEnabled(false);
        pausedSong2.setVisibility(View.INVISIBLE);
        pausedSong2.setEnabled(false);
        adapterPlaylist = new PlaylistAdapter(this, arrayOfPlaylist, 0);
        adapterSongs = new SongsAdapter(this, arrayOfSongs, 2);
        adapterPodcast = new AlbumsAdapter(this, arrayOfPodcast, 2);
        myPlaylist.setAdapter(adapterPlaylist);
        mySongs.setAdapter(adapterSongs);
        myPodcast.setAdapter(adapterPodcast);
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        username = data.getString("username");
                        JSONArray playlistsUser = data.getJSONArray("playlists");
                        ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser, true);
                        adapterPlaylist.addAll(newPlaylists);

                        ArrayList<Album> newPodcasts = Album.fromJson(data.getJSONArray("albums"), false, null, true);
                        for ( int i = 0; i < newPodcasts.size(); i++ ) {
                            if ( newPodcasts.get(i).esPodcast ) {
                                adapterPodcast.add(newPodcasts.get(i));
                            }
                        }

                        String text = "Continue playback: ";
                        if ( !data.isNull("pause_song") ) {
                            paused_song = new Song(data.getJSONObject("pause_song"));
                            text = text + paused_song.songName;
                            pause_seconds = data.optInt("pause_second", 0);
                            layoutPaused.setVisibility(View.VISIBLE);
                            pausedSong.setVisibility(View.VISIBLE);
                            pausedSong.setEnabled(true);
                            pausedSong2.setVisibility(View.VISIBLE);
                            pausedSong2.setEnabled(true);
                            pausedSong.setText(text);
                        }
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
        server.songsRecommended(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if (responseCode == 200) {
                    try {
                        JSONArray songsRecommended = data.getJSONArray("results");
                        ArrayList<Song> newSongs = Song.fromJson(songsRecommended, true, null);
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
        Button createP = findViewById(R.id.createPlaylist);
        createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreate();
            }
        });
        Button Button2 = findViewById(R.id.menuButton2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        pausedSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSong(paused_song, true);
            }
        });

        pausedSong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSong(paused_song, true);
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

        myPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Playlist> search = (ArrayAdapter<Playlist>) parent.getAdapter();
                Playlist playlist = (Playlist) search.getItem(position);
                String creador = username;
                openPlaylist(playlist.playlistName, creador, playlist.id);
            }
        });myPodcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Album> search = (ArrayAdapter<Album>) parent.getAdapter();
                Album podcast = search.getItem(position);
                openPlaylist(podcast.name, podcast.artistName, podcast.id);
            }
        });

        mySongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Song> search = (ArrayAdapter<Song>) parent.getAdapter();
                Song cancion = search.getItem(position);
                openSong(cancion, false);
            }
        });
        Button Button6 = findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlaylist();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        adapterPlaylist.clear();
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        username = data.getString("username");

                        JSONArray playlistsUser = data.getJSONArray("playlists");
                        ArrayList<Playlist> newPlaylists = Playlist.fromJson(playlistsUser, true);
                        adapterPlaylist.addAll(newPlaylists);

                        ArrayList<Album> newPodcasts = Album.fromJson(data.getJSONArray("albums"), false, null, true);
                        for ( int i = 0; i < newPodcasts.size(); i++ ) {
                            if ( newPodcasts.get(i).esPodcast ) {
                                adapterPodcast.add(newPodcasts.get(i));
                            }
                        }

                        String text = "Continue playback: ";
                        if (!data.isNull("pause_song")) {
                            paused_song = new Song(data.getJSONObject("pause_song"));
                            text = text + paused_song.songName;
                            pause_seconds = data.optInt("pause_second", 0);
                            layoutPaused.setVisibility(View.VISIBLE);
                            pausedSong.setVisibility(View.VISIBLE);
                            pausedSong.setEnabled(true);
                            pausedSong2.setVisibility(View.VISIBLE);
                            pausedSong2.setEnabled(true);
                            pausedSong.setText(text);
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
    }

    private void createPlaylist() {
        LinearLayout panel = findViewById(R.id.panelPlaylist);
        EditText insert = findViewById(R.id.insertTitle);
        String title = insert.getText().toString();
        panel.setVisibility(View.GONE);
        findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(false);
        server.addPlaylist(title, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if (responseCode == 201) {
                    Playlist newPlaylist = new Playlist(data, true);
                    adapterPlaylist.add(newPlaylist);
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });
    }

    private void openCreate() {
        LinearLayout panel = findViewById(R.id.panelPlaylist);
        if (panel.getVisibility() == View.VISIBLE) {
            panel.setVisibility(View.INVISIBLE);
        } else {
            panel.setVisibility(View.VISIBLE);
        }
        findViewById(com.instantmusic.appmovil.R.id.acceptPlaylist).setClickable(true);
    }

    private void openPlaylist(String playlist, String creador, int idPlaylist) {
        Intent i = new Intent(this, PlaylistActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("playlist", playlist);
        i.putExtra("creador", creador);
        i.putExtra("idPlaylist", idPlaylist);
        startActivityForResult(i, 1);
    }

    private void openSong(Song song, boolean seguirReproduccion) {
        IntentTransfer.setData("songs", Collections.singletonList(song));
        IntentTransfer.setData("positionId", 0);
        IntentTransfer.setData("botonPlay", false);
        if (seguirReproduccion)
            IntentTransfer.setData("pauseSecond", pause_seconds);

        this.startActivity(new Intent(this, SongActivity.class));
    }

    private void Search() {
        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);
    }

    private void Podcasts() {
        Intent i = new Intent(this, PodcastSearch.class);
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
    }


}
