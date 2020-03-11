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
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Song extends AppCompatActivity implements Runnable {
    //private static final int SEARCH = Menu.FIRST;
    serverInterface server;
    TextView songName;
    TextView autorName;
    Handler handler = new Handler();
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private int length;
    FloatingActionButton play;
    FloatingActionButton next;
    FloatingActionButton previous;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_song);
        server=new localServer(this);
        play = findViewById(R.id.play);
        next = findViewById(R.id.nextSong);
        previous = findViewById(R.id.previousSong);
        Bundle extras = getIntent().getExtras();

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
                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
                    Song.this.seekBar.setProgress(0);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
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
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isPlaying = false;
                        isPaused = false;
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mediaPlayer.getDuration());
                        handler.removeCallbacks(moveSeekBarThread);
                        handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
                        mediaPlayer.start();
                        new Thread(Song.this).start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPlaying = true;
        }
        else if ( isPlaying ) {
            play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
            mediaPlayer.pause();
            isPlaying = false;
            isPaused = true;
            //length = mediaPlayer.getCurrentPosition();
        }
        else if (isPaused) {
            play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_pause));
            handler.removeCallbacks(moveSeekBarThread);
            handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
            mediaPlayer.start();
            isPlaying = true;
            isPaused = false;
        }
    }

    public void run() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int total = mediaPlayer.getDuration();
                while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
                    try {
                      Thread.sleep(1000);
                       currentPosition = mediaPlayer.getCurrentPosition();
                    }
                    catch (InterruptedException e) {
                        return;
                    }
                    catch(Exception e) {
                        return;
                    }
                    seekBar.setProgress(currentPosition);
                }
            }
        });
    }

    /**
     * The Move seek bar. Thread to move seekbar based on the current position
     * of the song
     */
    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if(mediaPlayer.isPlaying()){

                int mediaPos_new = mediaPlayer.getCurrentPosition();
                int mediaMax_new = mediaPlayer.getDuration();
                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); //Looping the thread after 0.1 second
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
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
