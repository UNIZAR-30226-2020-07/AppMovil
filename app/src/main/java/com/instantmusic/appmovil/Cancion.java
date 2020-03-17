package com.instantmusic.appmovil;

import android.widget.ImageView;

public class Cancion extends Elemento {
    private String category;
    private ImageView imageCategory;

    public Cancion(String name, String artist, String category ) {
        this.name = name;
        this.artist = artist;
        this.category = category;
        switch (category) {
            case 1:

                break;
            case 2:
                //shitcursor=server.searchCategory(shit.getText().toString());
                break;
            case 3:
                //shitcursor=server.searchArtist(shit.getText().toString());
                break;
            case 4:
                //shitcursor=server.searchAlbum(shit.getText().toString());
                break;
        }
    }

    public String getCategory() {
        return this.category;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getArtist() {
        return this.artist;
    }
}
