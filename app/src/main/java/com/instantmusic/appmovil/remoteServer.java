package com.instantmusic.appmovil;

import android.content.Context;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class remoteServer implements serverInterface {
    public Cursor searchShit(String shit) {
        return null;
    }

    public void registUser(String username, String email, String password1, String password2, JSONConnection.Listener listener) {
        initialize()
                .setUrl("rest-auth/registration")
                .putData("username", username)
                .putData("email", email)
                .putData("password1", password1)
                .putData("password2", password2)
                .setListener(listener)
                .execute();
    }

    /**
     * Makes a petition to login the user
     *
     * @param username_email username or email (base on the existence of the '@' char
     * @param password       super secret thing
     * @param listener       where to notify
     */
    public void login(String username_email, String password, JSONConnection.Listener listener) {
        initialize()
                .setUrl("rest-auth/login")
                .putData(username_email.contains("@") ? "email" : "username", username_email)
                .putData("password", password)
                .setListener(listener)
                .execute();
    }

    public static void getUserData(JSONConnection.Listener listener){
        initialize()
                .setUrl("rest-auth/user")
                .setListener(listener)
                .execute();
    }

    public int checkUser(String mail) {

        return 0;
    }

    @Override
    public Cursor infoUser(String email) {
        return null;
    }

    public int deleteUser(String email){
        return 1;
    }
    public int songInfo(String name,String artist,String categoria) {

        return 0;

    }

    public Cursor buscarCancion(String song) {
        return null;
    }

    public Cursor buscarArtista(String artist) {
        return null;
    }

    @Override
    public Cursor allPlaylists(String user) {
        return null;

    }

    public long addSong(String name,String artist,String category) {
        return 0;
    }

    @Override
    public long addPlaylist(String playlist,String author) {

        return 0;
    }

    public int recover(String mail) {
        return 0;
    }


    @Override
    public int addSongToPlaylist(String playlist, String song,String author) {

        return 0;
    }
    public void close(){
    }

    // ------------------- To save data -------------------

    // saved data
    private static String key = null;

    /**
     * Start a petition. Initializes the auth header
     *
     * @return a petition object
     */
    private static Petition initialize() {
        Petition petition = new Petition();
        if (key != null)
            petition.putHeader("Authorization", "Token " + key);
        return petition;
    }

    // ------------------- petition object -------------------

    /**
     * A petition object
     */
    private static class Petition implements JSONConnection.Listener {

        /**
         * Sets the url for the petition
         *
         * @param url can be relative or absolute
         * @return this to chain
         */
        public Petition setUrl(String url) {
            if (!url.endsWith("/"))
                url += "/";

            if (url.startsWith("http")) {
                this.url = url;
            } else {
                this.url = BASE_URL + url;
            }

            return this;
        }

        /**
         * Adds a parameter (those in the url, "url?key=value")
         * Calling {@link #setUrl(String)} after this will clear the parameters
         *
         * @param key   key
         * @param value value
         * @return this to chain
         */
        public Petition putParameter(String key, String value) {
            try {
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
                url += (url.contains("?") ? "&" : "?") + key + "=" + value;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return this;
        }

        /**
         * Adds a header (for the internal connection)
         *
         * @param key   key
         * @param value value
         * @return this to chain
         */
        public Petition putHeader(String key, String value) {
            if (headers == null)
                headers = new HashMap<>();

            headers.put(key, value);

            return this;
        }

        /**
         * Adds an element to send (in the body)
         *
         * @param key   key
         * @param value value
         * @return this to chain
         */
        public Petition putData(String key, String value) {
            if (body == null)
                body = new JSONObject();

            try {
                body.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        /**
         * Sets the listener where to notify
         *
         * @param listener where to notify
         * @return this to chain
         */
        public Petition setListener(JSONConnection.Listener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Runs the petition
         */
        public void execute() {
            JSONConnection.makePetition(url, body, headers, this);
        }

        // ------------------- private fields -------------------

        private String url = BASE_URL;
        private JSONObject body = null;
        private Map<String, String> headers = null;
        private JSONConnection.Listener listener = null;

        /**
         * Base url of the api
         */
        private static final String BASE_URL = "https://ps-20-server-django-app.herokuapp.com/api/v1/";

        // ------------------- listener -------------------

        @Override
        public void onValidResponse(int responseCode, JSONObject data) {

            // get the key parameter for autologin
            // this may be problematic, consider change
            if (data.has("key") && data.length() == 1) {
                try {
                    key = data.getString("key");
                } catch (JSONException ignore) {
                }
            }

            if (listener != null)
                listener.onValidResponse(responseCode, data);
        }

        @Override
        public void onErrorResponse(Throwable throwable) {
            if (listener != null)
                listener.onErrorResponse(throwable);
        }
    }
}