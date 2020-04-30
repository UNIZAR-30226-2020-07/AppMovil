package com.instantmusic.appmovil.offline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {

    public static void downloadSong(Song song, Context cntx) {
        new DownloadFile(song, cntx).execute();
    }

    static String getFilePath(int id, Context cntx) {
        return cntx.getFileStreamPath(getFileName(id)).getAbsolutePath();
    }

    // ------------------- Internal -------------------

    private static String getFileName(int id) {
        return "file_" + id + ".mp3";
    }

    private static class DownloadFile extends AsyncTask<Void, Integer, Void> {

        private final Song song;
        private final WeakReference<Context> cntx;
        private ProgressDialog progress;

        DownloadFile(Song song, Context cntx) {
            this.song = song;
            this.cntx = new WeakReference<>(cntx);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(cntx.get());
            progress.setMessage(String.format(cntx.get().getString(R.string.download_progress), song.songName));
            progress.setMax(100);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            progress.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(song.url);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = cntx.get().openFileOutput(getFileName(song.id), Context.MODE_PRIVATE);

                byte[] data = new byte[1024];

                long total = 0;

                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                // save
                new OfflinePrefs(cntx.get()).saveSong(song);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
