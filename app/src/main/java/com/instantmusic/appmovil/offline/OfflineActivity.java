package com.instantmusic.appmovil.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OfflineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv_files;
    private OfflinePrefs prefs;
    private SongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        lv_files = findViewById(R.id.lv_files);

        prefs = new OfflinePrefs(this);

        lv_files.setOnItemClickListener(this);

        initializeList();
    }

    private void initializeList() {
        ArrayList<Song> songs = prefs.getSongs();
        if (songs == null) {
            return;
        }
        adapter = new SongsAdapter(this, songs, 0);
        lv_files.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Song song = adapter.getItem(position);

        Intent i = new Intent(this, SongActivity.class);
        i.putExtra(this.getPackageName() + ".dataString", song.songName);
        i.putExtra(this.getPackageName() + ".String", song.artist);
        i.putExtra(this.getPackageName() + ".duration", song.duration);
        i.putExtra(this.getPackageName() + ".positionId", 0);
        i.putExtra(this.getPackageName() + ".url", Downloader.getFilePath(song.id, this)); // special
        i.putExtra(this.getPackageName() + ".id", song.id);
        i.putExtra(this.getPackageName() + ".botonPlay", false);
        ArrayList<Integer> idSongs = new ArrayList<>();
        idSongs.add(song.id);
        i.putExtra(this.getPackageName() + ".songs", idSongs);
        this.startActivity(i);
    }
}
