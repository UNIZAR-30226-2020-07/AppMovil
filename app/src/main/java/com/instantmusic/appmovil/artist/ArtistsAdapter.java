package com.instantmusic.appmovil.artist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import java.util.ArrayList;

public class ArtistsAdapter extends ArrayAdapter<Artist> {

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Artist artist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_artist_row, parent, false);
        }
        // Lookup view for data population
        TextView artistName = (TextView) convertView.findViewById(R.id.artistName);
        ImageView iconImage = convertView.findViewById(R.id.iconoSong);
        // Populate the data into the template view using the data object
        artistName.setText(artist.name);
        iconImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.account));
        // Return the completed view to render on screen
        return convertView;
    }
}
