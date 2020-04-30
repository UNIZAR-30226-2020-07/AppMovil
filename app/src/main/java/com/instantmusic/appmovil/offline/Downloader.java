package com.instantmusic.appmovil.offline;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.song.Song;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

/**
 * Functions to download songs
 */
public class Downloader {

    /**
     * Main function, downloads the song (a dialog is shown)
     *
     * @param song song to download
     * @param cntx base context
     */
    public static void downloadSong(Song song, Context cntx) {
        new DownloadFile(song, cntx).execute();
    }

    // ------------------- Internal -------------------

    /**
     * Returns the path of a downloaded song
     * @param id id of the song
     * @param cntx base context
     * @return path of the file with that downloaded song (may not exist)
     */
    static String getFilePath(int id, Context cntx) {
        return cntx.getFileStreamPath(getFileName(id)).getAbsolutePath();
    }

    /**
     * Returns the filename for a given song id
     * @param id id of the song
     * @return filename of the file for that song
     */
    private static String getFileName(int id) {
        return "file_" + id + ".mp3";
    }

    /**
     * The downloading process.
     * When run, a dialog is shown while the dong is being downloaded.
     * After finishing the dialog is removed, a toast is shown, and the file is created and added to the list of downloaded songs (unless an error ocurred)
     */
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
            progress.setMessage(String.format(cntx.get().getString(R.string.off_downloading), song.songName));
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
            Toast.makeText(cntx.get(), R.string.off_downloaded, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            progress.dismiss();
            Toast.makeText(cntx.get(), R.string.off_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                // connect to the url
                URL url = new URL(song.url);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghtOfFile = connection.getContentLength();

                // download
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
