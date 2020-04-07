package com.instantmusic.appmovil.server;

import com.instantmusic.appmovil.server.connect.JSONConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class remoteServer implements serverInterface {


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

    public void searchShit(String title, JSONConnection.Listener listener) {
        initialize()
                .setUrl("songs")
                .putParameter("page", "1")
                .setListener(listener)
                .execute();
    }

    public void getUserData(JSONConnection.Listener listener) {
        initialize()
                .setUrl("rest-auth/user")
                .setListener(listener)
                .execute();
    }

    public void getPlaylistData(int idPlaylist, JSONConnection.Listener listener) {
        String url = "playlists/";
        url = url + idPlaylist;
        initialize()
                .setUrl(url)
                .setListener(listener)
                .execute();
    }

    public void getSongData(int idSong, JSONConnection.Listener listener) {
        String url = "songs/";
        url = url + idSong;
        initialize()
                .setUrl(url)
                .setListener(listener)
                .execute();
    }

    @Override
    public void addPlaylist(String playlist, JSONConnection.Listener listener) {
        ArrayList<Integer> canciones = new ArrayList<>();
        initialize()
                .setUrl("playlists")
                .putData("name", playlist)
                .putData("songs", canciones)
                .setListener(listener)
                .execute();
    }

    @Override
    public void addSongToPlaylist(String namePlaylist, int idPlaylist, ArrayList<Integer> songs, JSONConnection.Listener listener) {
        String url = "playlists/";
        url = url + idPlaylist;
        initialize()
                .setUrl(url)
                .setMethod(JSONConnection.METHOD.PATCH)
                .putData("songs",songs)
                .setListener(listener)
                .execute();
    }

    @Override
    public void changeNamePlaylist(String namePlaylist, int idPlaylist, JSONConnection.Listener listener) {
        String url = "playlists/";
        url = url + idPlaylist;
        initialize()
                .setUrl(url)
                .setMethod(JSONConnection.METHOD.PATCH)
                .putData("name",namePlaylist)
                .setListener(listener)
                .execute();
    }

    // ------------------- To save data -------------------

    // saved data
    private static String key = null;

    /**
     * Start a petition. Initializes the auth header
     *
     * @return a petition object
     */
    private Petition initialize() {
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
         * Adds a string to send (in the body)
         *
         * @param key   key
         * @param value value
         * @return this to chain
         */
        public Petition putData(String key, String value) {
            return _putData(key, value);
        }

        /**
         * Adds an array of ints to send (in the body)
         *
         * @param key   key
         * @param value value (array of ints)
         * @return this to chain
         */
        public Petition putData(String key, List<Integer> value) {
            return _putData(key, new JSONArray(value));
        }

        /**
         * Adds an int to send (in the body)
         *
         * @param key   key
         * @param value value (array of ints)
         * @return this to chain
         */
        public Petition putData(String key, int value) {
            return _putData(key, value);
        }

        /**
         * Adds a boolean to send (in the body)
         *
         * @param key   key
         * @param value value (array of ints)
         * @return this to chain
         */
        public Petition putData(String key, boolean value) {
            return _putData(key, value);
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
         * Specify the method to use. If not called GET or POST will be used whether data was added or not
         *
         * @param method method to use
         */
        public Petition setMethod(JSONConnection.METHOD method) {
            this.method = method;
            return this;
        }

        /**
         * Runs the petition
         */
        public void execute() {
            JSONConnection.makePetition(url, method, body, headers, this);
        }

        // ------------------- private fields -------------------

        private JSONConnection.METHOD method = null;
        private String url = BASE_URL;
        private JSONObject body = null;
        private Map<String, String> headers = null;
        private JSONConnection.Listener listener = null;

        /**
         * Base url of the api
         */
        private static final String BASE_URL = "https://ps-20-server-django-app.herokuapp.com/api/v1/";

        private Petition _putData(String key, Object obj) {
            if (body == null)
                body = new JSONObject();

            try {
                body.put(key, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

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
            throwable.printStackTrace(); // print by default
            if (listener != null)
                listener.onErrorResponse(throwable);
        }
    }
}