package com.instantmusic.appmovil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter<Song> {

    public SongsAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song song = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_row, parent, false);
        }
        // Lookup view for data population
        TextView songName = (TextView) convertView.findViewById(R.id.text1);
        TextView artist = (TextView) convertView.findViewById(R.id.text2);
        TextView category = (TextView) convertView.findViewById(R.id.text3);

        // Populate the data into the template view using the data object
        songName.setText(song.songName);
        artist.setText(song.artist);
        category.setText(song.category);

        // Return the completed view to render on screen
        return convertView;
    }
}
