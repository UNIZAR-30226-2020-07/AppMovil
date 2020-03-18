package com.instantmusic.appmovil;

// Esta implementacion ha sido sacada de esta pagina: https://www.journaldev.com/22203/android-media-player-song-with-seekbar
// Sobre dicha implementacion se han realizado cambios para adecuarlo al modelo que queremos seguir.

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Song extends AppCompatActivity {
    serverInterface server;
    TextView songName;
    TextView autorName;
    TextView ratingScale;
    Handler handler = new Handler();
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isLooped = false;
    private boolean isShuffled = false;
    private LinearLayout searchMenu;
    FloatingActionButton play;
    FloatingActionButton next;
    FloatingActionButton previous;
    ImageView loop;
    ImageView shuffle;
    RatingBar ratingBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_song);
        server=new localServer(this);
        loop = findViewById(R.id.loop);
        previous = findViewById(R.id.previousSong);
        play = findViewById(R.id.play);
        next = findViewById(R.id.nextSong);
        shuffle = findViewById(R.id.shuffle);
        ratingBar = findViewById(R.id.ratingBar);
        ratingScale = findViewById(R.id.tvRatingScale);
        searchMenu = findViewById(R.id.searchMenu);
        Bundle extras = getIntent().getExtras();
        Button Button6 = findViewById(R.id.optionSong);
        Button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMenu.getVisibility() == View.VISIBLE) {
                    searchMenu.setVisibility(View.INVISIBLE);
                } else {
                    searchMenu.setVisibility(View.VISIBLE);
                }

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingScale.setText(String.valueOf(v));
                ratingScale.setVisibility(View.VISIBLE);
                String texto = "";
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        texto = "Very bad";
                        ratingScale.setText(texto);
                        break;
                    case 2:
                        texto = "Bad";
                        ratingScale.setText(texto);
                        break;
                    case 3:
                        texto = "Good";
                        ratingScale.setText(texto);
                        break;
                    case 4:
                        texto = "Great";
                        ratingScale.setText(texto);
                        break;
                    case 5:
                        texto = "Awesome. I love it";
                        ratingScale.setText(texto);
                        break;
                    default:
                        ratingScale.setText(texto);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMediaPlayer();
                play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
                isPaused = false;
                isPlaying = false;
                Song.this.seekBar.setProgress(0);
            }
        });

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( mediaPlayer != null ) {
                    if ( isLooped ) {
                        loop.setImageDrawable(ContextCompat.getDrawable(Song.this, R.drawable.repeat));
                        mediaPlayer.setLooping(false);
                        isLooped = false;
                    }
                    else {
                        loop.setImageDrawable(ContextCompat.getDrawable(Song.this, R.drawable.repeat2));
                        mediaPlayer.setLooping(true);
                        isLooped = true;
                    }
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( isShuffled ) {
                    shuffle.setImageDrawable(ContextCompat.getDrawable(Song.this, R.drawable.shuffle2));
                    isShuffled = false;
                }
                else {
                    shuffle.setImageDrawable(ContextCompat.getDrawable(Song.this, R.drawable.shufflewhite));
                    isShuffled = true;
                }
            }
        });

        if (extras != null) {
            String cancion = extras.getString(this.getPackageName() + ".dataString");
            songName = findViewById(R.id.SongName);
            songName.setText(cancion);
            String autor = extras.getString(this.getPackageName() + ".String");
            autorName = findViewById(R.id.AutorName);
            autorName.setText(autor);
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong();
            }
        });
        final TextView seekBarHint = findViewById(R.id.minuto);
        seekBar = findViewById(R.id.seekBar);
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarHint.setVisibility(View.VISIBLE);
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                seekBarHint.setVisibility(View.VISIBLE);
                seekBarHint.setText(createTimerLabel(progress));
                seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = seekBarHint.getWidth();
                seekBarHint.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && isPlaying) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                else if (mediaPlayer != null && isPaused) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });


    }

    public void playSong() {

        if (!isPlaying && !isPaused) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }

                play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_pause));
                AssetFileDescriptor descriptor = getAssets().openFd("pikete-italiano.mp3");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isPlaying = false;
                        isPaused = false;
                        play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
                        clearMediaPlayer();
                        Song.this.seekBar.setProgress(0);

                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mediaPlayer.getDuration());
                        handler.removeCallbacks(moveSeekBarThread);
                        handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if ( isPlaying ) {
            play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
            if ( mediaPlayer != null ) {
                mediaPlayer.pause();
                handler.removeCallbacks(moveSeekBarThread);
                handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
                isPlaying = false;
                isPaused = true;
            }
        }
        else if (isPaused) {
            play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_pause));
            if (mediaPlayer != null) {
                mediaPlayer.start();
                handler.removeCallbacks(moveSeekBarThread);
                handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
                isPlaying = true;
                isPaused = false;
            }
            else {
                isPlaying = false;
                isPaused = false;
            }
        }
    }


    /**
     * The Move seek bar. Thread to move seekbar based on the current position
     * of the song
     */
    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if (mediaPlayer != null ) {
                if(mediaPlayer.isPlaying()){
                    int mediaPos_new = mediaPlayer.getCurrentPosition();
                    int mediaMax_new = mediaPlayer.getDuration();
                    seekBar.setMax(mediaMax_new);
                    seekBar.setProgress(mediaPos_new);

                    handler.postDelayed(this, 100); //Looping the thread after 0.1 second
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        if ( mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public String createTimerLabel(int duration) {
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        timerLabel += min + ":";
        if ( sec < 10 ) {
            timerLabel += "0";
        }
        timerLabel += sec;
        return timerLabel;
    }
}
