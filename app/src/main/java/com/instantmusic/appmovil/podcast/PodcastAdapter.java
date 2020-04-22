package com.instantmusic.appmovil.podcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.playlist.Playlist;

import java.util.ArrayList;

public class PodcastAdapter extends ArrayAdapter<Podcast> {
    private int tipoLayout;
    public PodcastAdapter(Context context, ArrayList<Podcast> playlists, int n) {
        super(context, 0, playlists);
        this.tipoLayout = n;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Podcast playlist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if ( this.tipoLayout == 0 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists_row, parent, false);
            }
            else if ( this.tipoLayout == 1 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists2_row, parent, false);
            }
        }
        // Lookup view for data population
        TextView playlistName = (TextView) convertView.findViewById(R.id.text1);
        // Populate the data into the template view using the data object
        playlistName.setText(playlist.playlistName);
        // Return the completed view to render on screen
        return convertView;
    }
}
