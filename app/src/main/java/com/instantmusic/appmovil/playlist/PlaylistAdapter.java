package com.instantmusic.appmovil.song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.song.Song;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        super(context, 0, playlists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Playlist song = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists_row, parent, false);
        }
        // Lookup view for data population
        TextView songName = (TextView) convertView.findViewById(R.id.text1);

        // Populate the data into the template view using the data object
        songName.setText(song.playlistName);
        // Return the completed view to render on screen
        return convertView;
    }
}
