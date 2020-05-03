package com.instantmusic.appmovil.offline;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;
import com.instantmusic.appmovil.song.SongActivity;
import com.instantmusic.appmovil.song.SongsAdapter;

import java.util.ArrayList;

/**
 * An activity that shows the downloaded songs to play them offline
 */
public class OfflineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // static
    private static final int MENU_REMOVE = 0;

    // variables
    private ListView lv_files;
    private OfflinePrefs prefs;
    private SongsAdapter adapter;
    private ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        // views
        lv_files = findViewById(R.id.off_lv_files);

        // data
        prefs = new OfflinePrefs(this);

        // initialize views
        lv_files.setOnItemClickListener(this);
        registerForContextMenu(lv_files);

        // initalize data
        songs = prefs.getSongs();
        adapter = new SongsAdapter(this, songs, 0);
        lv_files.setAdapter(adapter);

        updateUI();
    }

    /**
     * Refresh the status of UI elements
     */
    private void updateUI() {
        if (adapter.isEmpty()) {
            lv_files.setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // when clicked song, play song (treat as playlist)
        ArrayList<Song> offlineSongs = new ArrayList<>(songs);
        for (Song song : offlineSongs) {
            // convert the url to the offline one
            song.url = Downloader.getFilePath(song.id, this);
        }
        IntentTransfer.setData("songs", offlineSongs);
        IntentTransfer.setData("positionId", position);
        IntentTransfer.setData("botonPlay", false);

        this.startActivity(new Intent(this, SongActivity.class));
    }

    // ------------------- Menu -------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == lv_files) {
            // only one option
            menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_REMOVE:
                // remove from the adapter and the storage
                Song song = adapter.getItem(info.position);
                if (song != null) {
                    adapter.remove(song);
                    prefs.removeSong(song.id);
                    Downloader.removeSong(song.id, this);
                    updateUI();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
