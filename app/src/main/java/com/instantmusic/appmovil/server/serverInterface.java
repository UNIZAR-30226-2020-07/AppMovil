package com.instantmusic.appmovil.server;

import com.instantmusic.appmovil.server.connect.JSONConnection;
import java.util.ArrayList;

public interface serverInterface  {
    void searchSongs(String shit, JSONConnection.Listener listener );
    void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener);
    void getUserData(JSONConnection.Listener listener);
    void getPlaylistData(int idPlaylist, JSONConnection.Listener listener);
    void getSongData(int idSong, JSONConnection.Listener listener);
    void login(String username_email, String password, JSONConnection.Listener listener);
    void addSongToPlaylist(int idPlaylist, ArrayList<Integer> songs, JSONConnection.Listener listener);
    void changeNamePlaylist(String namePlaylist, int idPlaylist, JSONConnection.Listener listener);
    void addPlaylist(String playlist, JSONConnection.Listener listener);
    void deletePlaylist(int idPlaylist, JSONConnection.Listener listener);
    void rateASong(int idSong, int rate, JSONConnection.Listener listener);
}
