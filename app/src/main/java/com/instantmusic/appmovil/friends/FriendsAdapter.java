package com.instantmusic.appmovil.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.instantmusic.appmovil.R;

import java.util.ArrayList;

public class FriendsAdapter extends ArrayAdapter<Friend> {
    private int tipoLayout;
    public FriendsAdapter(Context context, ArrayList<Friend> playlists, int n) {
        super(context, 0, playlists);
        this.tipoLayout = 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Friend playlist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if ( this.tipoLayout == 0 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_row3, parent, false);
            }
            else if ( this.tipoLayout == 1 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists2_row, parent, false);
            }
        }
        // Lookup view for data population
        TextView podcastName = (TextView) convertView.findViewById(R.id.text1);
        String test=playlist.username;
        // Populate the data into the template view using the data object
        podcastName.setText(playlist.username);
        // Return the completed view to render on screen
        return convertView;
    }
}
