package com.instantmusic.appmovil.song;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instantmusic.appmovil.IntentTransfer;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.artist.ArtistActivity;
import com.instantmusic.appmovil.offline.Downloader;
import com.instantmusic.appmovil.playlist.addSongToPlaylist;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private int currentPos;
    private int continueAt;

    // arrays
    private List<Song> original_songs_list;
    private List<Song> songs_list;

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

        // other initializations
        initRatingBar();
        initSeekBar();
        initMediaPlayer();

        // continue loading
        loadData();
    }

    /**
     * Load the song
     */
    protected void loadData() {

        // get data
        original_songs_list = songs_list = IntentTransfer.getData("songs");
        currentPos = IntentTransfer.getData("positionId");
        boolean botonPlay = IntentTransfer.getData("botonPlay");
        Integer pauseSecond = IntentTransfer.getData("pauseSecond");

        // set data
        if (songs_list.size() > 1) {
            next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, android.R.drawable.ic_media_next));
        } else {
            next.setEnabled(false);
            shuffle.setEnabled(false);
        }

        // load the song
        loadSong(botonPlay, pauseSecond == null ? -1 : pauseSecond);
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
        i.putExtra("song", getCurrentSong().songName);
        i.putExtra("id", getCurrentSong().id);
        startActivityForResult(i, 1);
    }


    /**
     * The activity ends
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSong();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isOnline()) return; // offline mode, can't save

        if (mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() > 0) {
            // playing or paused not at start, save
            int seconds = mediaPlayer.getCurrentPosition() / 1000;
            server.saveMinutesSong(seconds, getCurrentSong().id, null);
            IntentTransfer.setData("continueSong", getCurrentSong());
            IntentTransfer.setData("continueSeconds", seconds);
        } else {
            // paused at start, remove
            server.saveMinutesSong(-1, -1, null);
            IntentTransfer.setData("continueSong", null);
        }
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
            case R.id.offline:
                toggleOptionsMenu();
                Downloader.downloadSong(getCurrentSong(), this);
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
                nextSong(false);
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
     * @return the current playing song
     */
    private Song getCurrentSong() {
        return songs_list.get(currentPos);
    }

    /**
     * @return true iff the current playing song is an online one
     */
    private boolean isOnline() {
        return getCurrentSong().url.startsWith("http");
    }

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

        if (mediaPlayer.getCurrentPosition() < 1000 && currentPos != 0) {
            // go prev
            currentPos--;

            updateNextUI();

            loadSong(auto, -1);

        } else {
            // continue (or not)
            if (auto) {
                playSong();
            }
        }
    }

    /**
     * Puts the next song
     * @param forceAuto if true, next song will autoplay (if false only if currently playing)
     */
    private void nextSong(boolean forceAuto) {

        // save auto setting
        boolean auto = forceAuto || mediaPlayer.isPlaying();

        // stop
        stopSong();

        // go next
        currentPos++;

        updateNextUI();

        // load next
        loadSong(auto, -1);
    }

    private void updateNextUI() {
        if (currentPos == (songs_list.size() - 1)) {
            // now is the last song
            next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, R.drawable.ic_skip_next_black_24dp));
            next.setEnabled(false);
        } else {
            // not the last song anymore
            next.setImageDrawable(ContextCompat.getDrawable(SongActivity.this, android.R.drawable.ic_media_next));
            next.setEnabled(true);
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
        if (isShuffled) {
            // shuffle now
            Song currentSong = getCurrentSong();
            songs_list = new ArrayList<>(original_songs_list);
            Collections.shuffle(songs_list);
            songs_list.remove(currentSong);
            songs_list.add(0, currentSong);
            currentPos = 0;
        } else {
            // unshuffle
            currentPos = original_songs_list.indexOf(getCurrentSong());
            songs_list = original_songs_list;
        }
        updateNextUI();
    }

    private void seeArtist() throws JSONException {
        Intent i = new Intent(this, ArtistActivity.class);
        int idArtist = getCurrentSong().album.getJSONObject("artist").getInt("id");
        i.putExtra("idArtist", idArtist);
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
     * Starts the loading of the song
     */
    public void loadSong(boolean auto, int cont) {
        isPrepared = false;
        autoPlay = auto;
        continueAt = cont;
        mediaPlayer.reset();
        try {
            songName.setText(R.string.loading);
            autorName.setText(R.string.loading);
            play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            play.setEnabled(false);

            if (isOnline()) {
                mediaPlayer.setDataSource(getCurrentSong().url);
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(getCurrentSong().url);
                    mediaPlayer.setDataSource(fileInputStream.getFD());
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
        songName.setText(getCurrentSong().songName);
        autorName.setText(getCurrentSong().artist);
        play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        play.setEnabled(true);
        if (continueAt >= 0) {
            mediaPlayer.seekTo(continueAt * 1000);
        }
        reloadSeekBar();
        reloadRating();
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
        if ((currentPos + 1) < songs_list.size()) {
            nextSong(true);
        } else {
            stopSong();
        }
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
        ratingBar.setRating(getCurrentSong().rate);
        ratingBar.setEnabled(isOnline());
    }

    /**
     * When the rating changes
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        final int rating_int = Math.round(rating);
        if (rating_int == 0) {
            // no rating
            ratingScale.setVisibility(View.INVISIBLE);
        } else {
            // rating
            ratingScale.setText(ratings[rating_int - 1]);
            ratingScale.setVisibility(View.VISIBLE);
            if (fromUser && isOnline()) {
                server.rateASong(getCurrentSong().id, rating_int, null);
                getCurrentSong().rate = rating_int;
            }
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
            seekBar.setProgress(mediaPlayer.getCurrentPosition());

            if (mediaPlayer.isPlaying())
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
        seekBar.setMax(mediaPlayer.getDuration() == -1 ? getCurrentSong().duration : mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
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
