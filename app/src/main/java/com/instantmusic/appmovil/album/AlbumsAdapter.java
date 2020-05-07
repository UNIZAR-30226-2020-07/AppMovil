package com.instantmusic.appmovil.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instantmusic.appmovil.R;

import java.util.ArrayList;

public class AlbumsAdapter extends ArrayAdapter<Album> {

    public AlbumsAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }
        private int tipoLayout;
        public AlbumsAdapter(Context context, ArrayList<Album> albums, int n_) {
            super(context, 0, albums);
            this.tipoLayout=n_;
        }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Album album = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if ( this.tipoLayout == 0 || this.tipoLayout == 2 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_album_row, parent, false);
            }
            else if ( this.tipoLayout == 1 || this.tipoLayout == 3) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists_row, parent, false);
            }
        }
        ImageView iconImage = convertView.findViewById(R.id.iconoSong);
        TextView albumName = (TextView) convertView.findViewById(R.id.text1);
        if ( this.tipoLayout == 0 || this.tipoLayout == 2 ) {
            // Lookup view for data population
            TextView artistAlbum = (TextView) convertView.findViewById(R.id.text2);
            // Populate the data into the template view using the data object
            artistAlbum.setText(album.artistName);
            albumName.setText(album.name);
        }
        if (this.tipoLayout == 1 || this.tipoLayout == 3) {
            albumName.setText(album.name);
        }

        if ( this.tipoLayout == 3 || this.tipoLayout == 2) {
            iconImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.podcasticon));
        }
        else {
            iconImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.album));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
