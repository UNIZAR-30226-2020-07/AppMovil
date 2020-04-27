package com.instantmusic.appmovil.offline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;

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
        new OfflinePrefs(cntx).saveSong(song);
        new DownloadFile(song.url, song.id, cntx).execute();
    }

    // ------------------- Internal -------------------

    static String getFileName(int id) {
        return "file_" + id + ".mp3";
    }

    static String getFilePath(int id, Context cntx) {
        return cntx.getFileStreamPath(getFileName(id)).getAbsolutePath();
    }

    private static class DownloadFile extends AsyncTask<Void, Integer, Void> {

        private final String url;
        private final int id;
        private final WeakReference<Context> cntx;
        private ProgressDialog progress;

        DownloadFile(String url, int id, Context cntx) {
            this.url = url;
            this.id = id;
            this.cntx = new WeakReference<>(cntx);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(cntx.get());
            progress.setMessage("Downloading file");
            progress.setMax(100);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.cancel();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(this.url);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = cntx.get().openFileOutput(getFileName(id), Context.MODE_PRIVATE);

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
            } catch (Exception e) {
            }
            return null;
        }

    }
}
