package com.instantmusic.appmovil.server.connect;

import android.os.AsyncTask;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * My own custom Http wrapper for JSON connections, because the existing ones are ugly, big size, with a lot of extra things that nobody uses, and hard to make them work.
 */
public class JSONConnection {

    /**
     * Makes a petition
     *
     * @param url      the url to connect to
     * @param body     if null, a GET will be made. If not, a POST will be made with that Json data
     * @param headers  extra headers
     * @param listener where to notify
     */
    public static void makePetition(String url, JSONObject body, Map<String, String> headers, Listener listener) {
        new CallAPI(listener).execute(new Petition(url, body, headers));
    }

    // ------------------- public classes -------------------

    /**
     * To notify responses of {@link #makePetition(String, JSONObject, Map, Listener)}
     */
    public interface Listener {
        /**
         * The connection was ok
         *
         * @param responseCode the returned response code
         * @param data         the returned data
         */
        void onValidResponse(int responseCode, JSONObject data);

        /**
         * An error ocurred (any error)
         *
         * @param throwable the raw error
         */
        void onErrorResponse(Throwable throwable);
    }

    // ------------------- private classes -------------------

    /**
     * Wrapper for all the petition data
     */
    static private class Petition {

        private String url;
        private JSONObject body;
        private Map<String, String> headers;

        private Petition(String url, JSONObject body, Map<String, String> headers) {
            this.url = url;
            this.body = body;
            this.headers = headers;
        }

    }

    /**
     * Wrapper for all the response data
     */
    static private class Response {
        private boolean error; // whether this is an ErrorResponse or not

        // only for Valid Response
        private int reponseCode;
        private JSONObject data;

        private Response(int reponseCode, JSONObject data) {
            this.reponseCode = reponseCode;
            this.data = data;
            error = false;
        }

        // only for Error Response
        private Throwable throwable;

        private Response(Throwable throwable) {
            this.throwable = throwable;
            error = true;
        }

    }

    // ------------------- internal -------------------

    /**
     * The Async task that makes the petition in the background
     */
    public static class CallAPI extends AsyncTask<Petition, Void, Response> {

        private Listener listener;

        private CallAPI(Listener listener) {
            this.listener = listener;
        }

        @Override
        protected Response doInBackground(Petition... petitions) {
            Petition petition = petitions[0];
            try {

                // initialize connection
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(petition.url).openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);


                // headers
                if (petition.headers != null) {
                    for (Map.Entry<String, String> item : petition.headers.entrySet()) {
                        urlConnection.setRequestProperty(item.getKey(), item.getValue());
                    }
                }

                if (petition.body == null) {
                    // get
                    urlConnection.setRequestMethod("GET");
                } else {
                    // post
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("charset", "utf-8");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);

                    //write POST data
                    byte[] postDataBytes = petition.body.toString().getBytes("UTF-8");
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    urlConnection.getOutputStream().write(postDataBytes);
                }

                // connect
                urlConnection.connect();

                // server response code
                int responseCode = urlConnection.getResponseCode();

                // server response body
                String responseBody;
                try {
                    responseBody = readInputStream(urlConnection.getInputStream());
                } catch (final IOException e) {
                    // This means that an error occurred, read the error from the ErrorStream
                    try {
                        responseBody = readInputStream(urlConnection.getErrorStream());
                    } catch (IOException e1) {
                        // this means also an error ocurred...just ignore as if no reponse was send
                        responseBody = "{}";
                    }
                }

                urlConnection.disconnect(); // disconnect connection

                // callback success
                return new Response(responseCode, new JSONObject(responseBody));

            } catch (Throwable e) {
                return new Response(e);
            }
        }

        // util
        private String readInputStream(final InputStream inputStream) throws IOException {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder responseString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseString.append(line);
            }
            bufferedReader.close();
            return responseString.toString();
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response.error) {
                listener.onErrorResponse(response.throwable);
            } else {
                listener.onValidResponse(response.reponseCode, response.data);
            }
        }

    }
}