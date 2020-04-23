package com.instantmusic.appmovil.album;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import java.util.ArrayList;

public class AlbumsAdapter extends ArrayAdapter<Album> {

    public AlbumsAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Album album = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_album_row, parent, false);
        }
        // Lookup view for data population
        TextView albumName = (TextView) convertView.findViewById(R.id.text1);
        TextView artistAlbum = (TextView) convertView.findViewById(R.id.text2);
        ImageView iconImage = convertView.findViewById(R.id.iconoSong);
        // Populate the data into the template view using the data object
        artistAlbum.setText(album.artistName);
        albumName.setText(album.name);
        iconImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.album));
        // Return the completed view to render on screen
        return convertView;
    }
}
