package com.instantmusic.appmovil;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Song extends AppCompatActivity {
    private static final int SEARCH = Menu.FIRST;
    private serverInterface server;
    private TextView songName;
    private TextView autorName;
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    boolean wasPlaying = false;
    boolean sonando = false;
    ImageButton play;
    ImageButton next;
    ImageButton previous;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_song);
        server=new localServer(this);
        play = findViewById(R.id.play);
        next = findViewById(R.id.nextSong);
        previous = findViewById(R.id.nextSong);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String cancion = extras.getString(this.getPackageName() + ".dataString");
            songName = findViewById(R.id.SongName);
            songName.setText(cancion);
            String autor = extras.getString(this.getPackageName() + ".dataString");
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                seekBarHint.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);
                if (x < 10)
                    seekBarHint.setText("0:0" + x);
                else
                    seekBarHint.setText("0:" + x);

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

        try {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                wasPlaying = true;
                play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_play));
            }

            if (!wasPlaying) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }

                play.setImageDrawable(ContextCompat.getDrawable(Song.this, android.R.drawable.ic_media_pause));

                AssetFileDescriptor descriptor = getAssets().openFd("pegamos_tela.mp3");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setLooping(false);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                new Thread((Runnable) this).start();
            }
            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();

        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);
        }
    }

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
}
