package com.instantmusic.appmovil.song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;

import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter<Song> {
    private int tipoLayout;
    public SongsAdapter(Context context, ArrayList<Song> songs,int n_) {
        super(context, 0, songs);
        this.tipoLayout=n_;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song song = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if ( this.tipoLayout == 0 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_row, parent, false);
            }
            else if ( this.tipoLayout == 1 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_row, parent, false);
            }
            else if ( this.tipoLayout == 2 ) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists_row, parent, false);
            }
        }
        ImageView categoryImage = convertView.findViewById(R.id.iconoSong);
        TextView songName = (TextView) convertView.findViewById(R.id.text1);
        if ( this.tipoLayout == 0 || this.tipoLayout == 1 ) {
            TextView artist = (TextView) convertView.findViewById(R.id.text2);
            artist.setText(song.artist);
        }
        else {
        }
        // Populate the data into the template view using the data object
        songName.setText(song.songName);
        switch(song.category) {
            case "90s":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.noventasicon));
            break;
            case "Classic":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.boomerericon));
            break;
            case "Electronic":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.electronicicon));
            break;
            case "Reggae":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.notweedicon));
            break;
            case "R&B":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rricon));
            break;
            case "Latin":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.maracasicon));
            break;
            case "Oldies":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.boomericon));
            break;
            case "Country":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.texasicom));
            break;
            case "Rap":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rapicom));
            break;
            case "Rock":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.piedraicon));
            break;
            case "Metal":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.metalicon));
            break;
            case "Pop":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.popicon));
            break;
            case "Blues":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.azulicon));
            break;
            case "Jazz":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.jazzicon));
            break;
            case "Folk":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.folkicon));
            break;
            case "80s":
                categoryImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ochentasicon));
            break;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
