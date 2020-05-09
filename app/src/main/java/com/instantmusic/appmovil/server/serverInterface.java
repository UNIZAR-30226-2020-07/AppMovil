package com.instantmusic.appmovil.server;

import com.instantmusic.appmovil.podcast.PodcastSearch;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;

import java.util.ArrayList;

public interface serverInterface  {
    void searchSongs(int page, String shit, JSONConnection.Listener listener );
    void searchSongsByArtist(int page, String shit, JSONConnection.Listener listener );
    void searchAlbums(int page, String title, JSONConnection.Listener listener);
    void searchArtists(int page, String title, JSONConnection.Listener listener);
    void searchGenres(int page, String title, JSONConnection.Listener listener);
    void searchAFriend(String name, JSONConnection.Listener listener);
    void searchPodcasts(int page,String title, JSONConnection.Listener listener );
    void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener);
    void getUserById(int userId, JSONConnection.Listener listener);
    void getUserData(JSONConnection.Listener listener);
    void getPlaylistData(int idPlaylist, JSONConnection.Listener listener);
    void getSongData(int idSong, JSONConnection.Listener listener);
    void getAlbumData(int idAlbum, JSONConnection.Listener listener);
    void getArtistData(int idArtist, JSONConnection.Listener listener);
    void login(String username_email, String password, JSONConnection.Listener listener);
    void addOrRemoveSong(int idPlaylist, ArrayList<Integer> songs, JSONConnection.Listener listener);
    void changeNamePlaylist(String namePlaylist, int idPlaylist, JSONConnection.Listener listener);
    void changeDataUser(String nameUser, String password, int idPlaylist, JSONConnection.Listener listener);
    void addFriend(JSONArray friends, int idUser, JSONConnection.Listener listener);
    void addPlaylist(String playlist, JSONConnection.Listener listener);
    void deletePlaylist(int idPlaylist, JSONConnection.Listener listener);
    void addOrRemovePodcast(ArrayList<Integer> albums, JSONConnection.Listener listener);
    void rateASong(int idSong, int rate, JSONConnection.Listener listener);
    void songsRecommended(JSONConnection.Listener listener);
    void recoverPassword(String email, JSONConnection.Listener listener);
    void saveMinutesSong(int seconds, int idSong, JSONConnection.Listener listener);
    void searchArtistsPodcasts(int page, String toString, JSONConnection.Listener listener);
}
