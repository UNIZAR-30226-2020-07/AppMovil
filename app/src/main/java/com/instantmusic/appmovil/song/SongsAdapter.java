package com.instantmusic.appmovil.song;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instantmusic.appmovil.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter<Song> {
    private int tipoLayout;

    public SongsAdapter(Context context, ArrayList<Song> songs, int n_) {
        super(context, 0, songs);
        this.tipoLayout = n_;
    }

    public ArrayList<Song> getSongs() {
        final ArrayList<Song> songs = new ArrayList<>(getCount());
        for (int i = 0; i < getCount(); i++) {
            songs.add(getItem(i));
        }
        return songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song song = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if (this.tipoLayout == 0) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_row, parent, false);
            } else if (this.tipoLayout == 1) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_row, parent, false);
            } else if (this.tipoLayout == 2) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.myplaylists_row, parent, false);
            }
        }
        ImageView categoryImage = convertView.findViewById(R.id.iconoSong);
        TextView songName = (TextView) convertView.findViewById(R.id.text1);
        if (this.tipoLayout == 0 || this.tipoLayout == 1) {
            TextView artist = (TextView) convertView.findViewById(R.id.text2);
            artist.setText(song.artist);
            if (this.tipoLayout == 0) {
                TextView rateAverage = (TextView) convertView.findViewById(R.id.rate_average);
                DecimalFormat formato = new DecimalFormat("#.##");
                String rate = "Rate: ";
                rate = rate + formato.format(song.rate_average) + "☆";
                rateAverage.setText(rate);
            }
            songName.setText(song.songName);
        } else if (this.tipoLayout == 2) {
            songName.setText(song.songName);
        }

        if (categoriesMap.containsKey(song.category))
            categoryImage.setImageDrawable(getContext().getResources().getDrawable(categoriesMap.get(song.category)));

        // Return the completed view to render on screen
        return convertView;
    }

    private static ArrayMap<String, Integer> categoriesMap = initCategoriesMap();

    static private ArrayMap<String, Integer> initCategoriesMap() {
        ArrayMap<String, Integer> map = new ArrayMap<>();
        map.put("90s", R.drawable.noventasicon);
        map.put("Classic", R.drawable.boomerericon);
        map.put("Electronic", R.drawable.electronicicon);
        map.put("Reggae", R.drawable.notweedicon);
        map.put("R&B", R.drawable.rricon);
        map.put("Latin", R.drawable.maracasicon);
        map.put("Oldies", R.drawable.boomericon);
        map.put("Country", R.drawable.texasicom);
        map.put("Rap", R.drawable.rapicom);
        map.put("Rock", R.drawable.piedraicon);
        map.put("Metal", R.drawable.metalicon);
        map.put("Pop", R.drawable.popicon);
        map.put("Blues", R.drawable.azulicon);
        map.put("Jazz", R.drawable.jazzicon);
        map.put("Folk", R.drawable.folkicon);
        map.put("80s", R.drawable.ochentasicon);
        return map;
    }

}
