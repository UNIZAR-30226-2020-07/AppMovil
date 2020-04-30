package com.instantmusic.appmovil.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import java.util.ArrayList;

public class OfflineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int MENU_REMOVE = 0;

    private ListView lv_files;
    private OfflinePrefs prefs;
    private SongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        lv_files = findViewById(R.id.off_lv_files);

        prefs = new OfflinePrefs(this);

        lv_files.setOnItemClickListener(this);
        registerForContextMenu(lv_files);

        ArrayList<Song> songs = prefs.getSongs();
        adapter = new SongsAdapter(this, songs, 0);
        lv_files.setAdapter(adapter);

        updateUI();
    }

    private void updateUI() {
        if (adapter.isEmpty()) {
            lv_files.setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = adapter.getItem(position);
        if (song == null) return; // just in case, but should never happen

        Intent i = new Intent(this, SongActivity.class);
        i.putExtra(getPackageName() + ".dataString", song.songName);
        i.putExtra(getPackageName() + ".String", song.artist);
        i.putExtra(getPackageName() + ".duration", song.duration);
        i.putExtra(getPackageName() + ".positionId", 0);
        i.putExtra(getPackageName() + ".url", Downloader.getFilePath(song.id, this)); // special
        i.putExtra(getPackageName() + ".id", song.id);
        i.putExtra(getPackageName() + ".botonPlay", false);
        ArrayList<Integer> idSongs = new ArrayList<>();
        idSongs.add(song.id);
        i.putExtra(getPackageName() + ".songs", idSongs);
        this.startActivity(i);
    }

    // ------------------- Menu -------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == lv_files) {
            menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_REMOVE:
                Song song = adapter.getItem(info.position);
                if (song != null) {
                    adapter.remove(song);
                    prefs.deleteSong(song.id);
                    updateUI();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
