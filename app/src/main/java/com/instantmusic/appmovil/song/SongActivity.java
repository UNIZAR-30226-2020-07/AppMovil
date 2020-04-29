package com.instantmusic.appmovil.song;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.playlist.addSongToPlaylist;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SongActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    // objects
    private serverInterface server = new remoteServer();
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    // views
    private TextView songName;
    private TextView autorName;
    private TextView ratingScale;
    private ImageView loop;
    private ImageView shuffle;
    private LinearLayout searchMenu;
    private FloatingActionButton play;
    private FloatingActionButton next;
    private FloatingActionButton previous;
    private RatingBar ratingBar;
    private SeekBar seekBar;
    private TextView seekBarHint;

    // vars
    private boolean autoPlay;
    private boolean isShuffled = false;
    private boolean isPrepared = false;
    private String song;
    private String artist;
    private String urlSong;
    private int idSong;
    private int positionRandomId = 0;
    private int durationSong;
    private int rateSong;
    private int positionId;
    private Button seeArtist;
    public Song thisSong;

    // arrays
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Integer> idSongs;
    private ArrayList<Integer> randomIdSongs;

    // ------------------- activity -------------------

    /**
     * Activity starts
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_song);

        // get views
        loop = findViewById(R.id.loop);
        play = findViewById(R.id.play);
        next = findViewById(R.id.nextSong);
        previous = findViewById(R.id.previousSong);
        shuffle = findViewById(R.id.shuffle);
        ratingBar = findViewById(R.id.ratingBar);
        ratingScale = findViewById(R.id.tvRatingScale);
        searchMenu = findViewById(R.id.searchMenu);
        seekBar = findViewById(R.id.seekBar);
        seekBarHint = findViewById(R.id.minuto);
        songName = findViewById(R.id.songname);
        autorName = findViewById(R.id.autorname);
        seeArtist=findViewById(R.id.seeArtist);

        // other initializations
        initRatingBar();
        initSeekBar();
        initMediaPlayer();

        // continue loading
        onNewIntent(getIntent());
    }

    /**
     * Load the song
     *
     * @param intent must have the song info
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // get data
        song = intent.getStringExtra(getPackageName() + ".dataString");
        String autor = intent.getStringExtra(getPackageName() + ".String");
        durationSong = 1000 * intent.getIntExtra(getPackageName() + ".duration", 0);
        urlSong = intent.getStringExtra(getPackageName() + ".url");
        idSong = intent.getIntExtra(getPackageName() + ".id", 0);
        idSongs = intent.getIntegerArrayListExtra(getPackageName() + ".songs");
        positionId = intent.getIntExtra(getPackageName() + ".positionId", 0);
        boolean botonPlay = intent.getBooleanExtra(getPackageName() + ".botonPlay", false);

        // set data
        songName.setText(song);
        autorName.setText(autor);
        if (idSongs.size() > 1) {
            next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, android.R.drawable.ic_media_next));
        }
        else {
            next.setEnabled(false);
        }

        desordenar();

        // set rate of the song
        server.getSongData(idSong, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if (responseCode == 200) {
                    Song newSong = new Song(data);
                    thisSong=newSong;
                    artist = newSong.artist;
                    rateSong = newSong.rate;
                    reloadRating();
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
            }
        });

        // load the song
        loadSong(botonPlay);
    }

    /**
     * Goes back to the previous activity
     */
    private void backScreen() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    /**
     * Starts the 'add current song to a playlist' activity
     */
    private void addPlaylist() {
        Intent i = new Intent(this, addSongToPlaylist.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("song", song);
        i.putExtra("id", idSong);
        startActivityForResult(i, 1);
    }

    /**
     * The activity ends
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() == 0 ) {
            server.getUserData(new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) {
                    if ( isShuffled ) {
                        try {
                            server.saveMinutesSong(data.getInt("id"), mediaPlayer.getCurrentPosition() / 1000, randomIdSongs.get(positionRandomId), new JSONConnection.Listener() {
                                @Override
                                public void onValidResponse(int responseCode, JSONObject data) { }
                                @Override
                                public void onErrorResponse(Throwable throwable) { }
                            });
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            server.saveMinutesSong(data.getInt("id"), mediaPlayer.getCurrentPosition() / 1000, idSongs.get(positionId), new JSONConnection.Listener() {
                                @Override
                                public void onValidResponse(int responseCode, JSONObject data) { }
                                @Override
                                public void onErrorResponse(Throwable throwable) { }
                            });
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onErrorResponse(Throwable throwable) { }
            });
        }
        stopSong();
    }

    // ------------------- listeners -------------------

    /**
     * A button is clicked. This is set in the layout xml file.
     *
     * @param view clicked button
     */
    public void onButtonClick(View view) throws JSONException {
        switch (view.getId()) {
            // bar
            case R.id.scrolldown: // top-left back arrow
                // go back
                backScreen();
                break;

            // Options menu
            case R.id.optionSong:
                // options menu
                toggleOptionsMenu();
                break;
            case R.id.seeArtist:
                // see artist option
                toggleOptionsMenu();
                seeArtist();
                break;
            case R.id.addPlaylist:
                // add to playlist option
                toggleOptionsMenu();
                addPlaylist();
                break;

            // song buttons
            case R.id.shuffle:
                // shuffle songs
                toggleShuffle();
                break;
            case R.id.previousSong:
                // previous song
                previousSong();
                break;
            case R.id.play:
                // play/pause button
                if (mediaPlayer.isPlaying()) {
                    pauseSong();
                } else {
                    playSong();
                }
                break;
            case R.id.nextSong:
                nextSong();
                break;
            case R.id.loop:
                // loop button
                toggleLoop();
                break;
        }
    }

    // ------------------- Menu -------------------

    /**
     * Toggles the option menu
     */
    private void toggleOptionsMenu() {
        searchMenu.setVisibility(
                searchMenu.getVisibility() == View.VISIBLE
                        ? View.INVISIBLE
                        : View.VISIBLE
        );
    }

    // ------------------- Song -------------------

    /**
     * Starts/Continues playing the song
     */
    private void playSong() {
        if (!isPrepared) return;

        mediaPlayer.start();
        play.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause));
        enableSeekBarUpdate();
    }

    /**
     * Pauses the song
     */
    private void pauseSong() {
        if (!isPrepared) return;

        mediaPlayer.pause();
        play.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play));
        disableSeekBarUpdate(false);
    }

    /**
     * Stops the song
     */
    private void stopSong() {
        if (!isPrepared) return;

        // note: calling .stop() requires to prepare the song again, so simply pause if playing and seek to 0
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        disableSeekBarUpdate(true);
        play.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play));
    }

    /**
     * Puts the previous song
     */
    private void previousSong() {
        // stop
        boolean auto = mediaPlayer.isPlaying();
        stopSong();

        if ( isShuffled ) {
            if (mediaPlayer.getCurrentPosition() < 1000 && positionRandomId != 0) {
                // go prev
                positionRandomId--;

                // check if now is not last song of list
                if (positionRandomId != (randomIdSongs.size() - 1)) {
                    next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, android.R.drawable.ic_media_next));
                    next.setEnabled(true);
                }

                loadSongData(auto);

            }
            else {
                // continue (or not)
                if (auto) {
                    playSong();
                }
            }
        }
        else {
            if (mediaPlayer.getCurrentPosition() < 1000 && positionId != 0) {
                // go prev
                positionId--;

                // check if now is not last song of list
                if (positionId != (idSongs.size() - 1)) {
                    next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, android.R.drawable.ic_media_next));
                    next.setEnabled(true);
                }

                loadSongData(auto);

            }
            else {
                // continue (or not)
                if (auto) {
                    playSong();
                }
            }
        }
    }

    /**
     * Puts the next song
     */
    private void nextSong() {
        // stop
        boolean auto = mediaPlayer.isPlaying();
        stopSong();


        if ( isShuffled ) {
            // go next
            positionRandomId++;

            // check if now is last song of list
            if (positionRandomId == (randomIdSongs.size() - 1)) {
                next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, R.drawable.ic_skip_next_black_24dp));
                next.setEnabled(false);
            }

            // load next
            loadSongData(auto);
        }
        else {
            // go next
            positionId++;

            // check if now is last song of list
            if (positionId == (idSongs.size() - 1)) {
                next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, R.drawable.ic_skip_next_black_24dp));
                next.setEnabled(false);
            }

            // load next
            loadSongData(auto);
        }
    }

    /**
     * Toggles looping
     */
    private void toggleLoop() {
        mediaPlayer.setLooping(!mediaPlayer.isLooping());
        loop.setImageDrawable(ContextCompat.getDrawable(this,
                mediaPlayer.isLooping() ? R.drawable.repeat2 : R.drawable.repeat
        ));
    }

    /**
     * Toggles shuffling
     */
    private void toggleShuffle() {
        isShuffled = !isShuffled;
        shuffle.setImageDrawable(ContextCompat.getDrawable(this,
                isShuffled ? R.drawable.shufflewhite : R.drawable.shuffle2
        ));
    }
    private void seeArtist() throws JSONException {
        Intent i = new Intent(this, ArtistActivity.class);
        int idArtist=thisSong.album.getJSONObject("artist").getInt("id");
        i.putExtra("idArtist",idArtist);
        startActivityForResult(i, 1);
    }
    // ------------------- MediaPlayer -------------------

    /**
     * Initializes the media player
     */
    private void initMediaPlayer() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    /**
     * Makes petition to server to get the song data
     */
    private void loadSongData(final boolean auto) {
        if ( isShuffled ) {
            final int loadedPosition = positionRandomId;
            server.getSongData(idSongs.get(positionRandomId), new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) {
                    if (responseCode == 200) {

                        // another petition
                        if (loadedPosition != positionRandomId) return;

                        // get data
                        Song nextSong = new Song(data);
                        song = nextSong.songName;
                        artist = nextSong.artist;
                        durationSong = nextSong.duration;
                        urlSong = nextSong.url;

                        // set data
                        songName.setText(song);
                        autorName.setText(artist);

                        // continue loading
                        loadSong(auto);
                    }
                }

                @Override
                public void onErrorResponse(Throwable throwable) {

                }
            });
        }
        else {
            final int loadedPosition = positionId;
            server.getSongData(idSongs.get(positionId), new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) {
                    if (responseCode == 200) {

                        // another petition
                        if (loadedPosition != positionId) return;

                        // get data
                        Song nextSong = new Song(data);
                        song = nextSong.songName;
                        artist = nextSong.artist;
                        durationSong = nextSong.duration;
                        urlSong = nextSong.url;

                        // set data
                        songName.setText(song);
                        autorName.setText(artist);

                        // continue loading
                        loadSong(auto);
                    }
                }

                @Override
                public void onErrorResponse(Throwable throwable) {
                }
            });
        }

    }

    /**
     * Starts the loading of the song
     */
    public void loadSong(boolean auto) {
        isPrepared = false;
        autoPlay = auto;
        mediaPlayer.reset();
        try {
            songName.setText(R.string.loading);
            autorName.setText(R.string.loading);
            play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            play.setEnabled(false);
            mediaPlayer.setDataSource(urlSong);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't load song", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Finishes the loading of a song
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        // song ready to play
        songName.setText(song);
        autorName.setText(artist);
        play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        play.setEnabled(true);
        reloadSeekBar();
        isPrepared = true;
        if (autoPlay) {
            playSong();
        }
    }


    /**
     * The song ends
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if ((positionId + 1) < idSongs.size()) {
            nextSong();
        } else {
            stopSong();
        }
    }

    /**
     *
     */
    private void desordenar() {
        randomIdSongs = new ArrayList<>(idSongs);
        Collections.shuffle(randomIdSongs);
    }

    // ------------------- Rating bar -------------------

    /**
     * To convert from numerical to categorical
     */
    private static final String[] ratings = new String[]{"Very bad", "Bad", "Good", "Great", "Awesome. I love it"};

    /**
     * Initializes the rating bar
     */
    private void initRatingBar() {
        ratingBar.setOnRatingBarChangeListener(this);
    }

    /**
     * Reloads the rating
     */
    private void reloadRating() {
        ratingBar.setRating(rateSong);
    }

    /**
     * When the rating changes
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        final int rating_int = Math.round(rating);
        ratingScale.setText(ratings[rating_int - 1]);
        ratingScale.setVisibility(View.VISIBLE);
        if (fromUser) {
            server.rateASong(idSong, rating_int, null);
        }
    }

    // ------------------- SeekBar -------------------

    /**
     * Seekbar update delay
     */
    private static final int DELAY_MILLIS = 100;

    /**
     * Initializes the seekbar
     */
    private void initSeekBar() {
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(this);
    }

    /**
     * The update seekbar runnable.
     * Thread to move seekbar based on the current position of the song
     */
    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            assert mediaPlayer != null;
            if (!mediaPlayer.isPlaying()) return;

            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this, DELAY_MILLIS); //Looping the thread after 0.1 second
        }
    };

    /**
     * Enables the seekbar update
     */
    public void enableSeekBarUpdate() {
        handler.removeCallbacks(moveSeekBarThread); // to avoid dangling callback
        moveSeekBarThread.run();
    }

    /**
     * Disables the seekbar update
     *
     * @param reset if true, the seekbar will be reseated
     */
    public void disableSeekBarUpdate(boolean reset) {
        handler.removeCallbacks(moveSeekBarThread);
        if (reset) {
            seekBar.setProgress(0);
            seekBarHint.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Reloads the seekbar from current song
     */
    private void reloadSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration() == -1 ? durationSong : mediaPlayer.getDuration());
        seekBarHint.setVisibility(View.INVISIBLE);
    }

    /**
     * The user starts moving the seekbar
     */
    public void onStartTrackingTouch(SeekBar seekBar) {
        disableSeekBarUpdate(false);
    }

    /**
     * The seekbar changes, update the current time hint
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        seekBarHint.setText(createTimerLabel(progress));
        double percent = progress / (double) seekBar.getMax();
        int offset = seekBar.getThumbOffset();
        int seekWidth = seekBar.getWidth();
        int val = (int) Math.round(percent * (seekWidth - 2 * offset));
        int labelWidth = seekBarHint.getWidth();
        seekBarHint.setX(offset + seekBar.getX() + val
                - Math.round(percent * offset)
                - Math.round(percent * labelWidth / 2));
        seekBarHint.setVisibility(View.VISIBLE);
    }

    /**
     * The user stopped moving the seekbar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
        enableSeekBarUpdate();
    }

    /**
     * Converts milliseconds to string (m:ss)
     *
     * @param duration as milliseconds
     * @return duration as string
     */
    private String createTimerLabel(int duration) {
        duration /= 1000; // to seconds
        return String.format(Locale.getDefault(), "%d:%02d", duration / 60, duration % 60);
    }
}
