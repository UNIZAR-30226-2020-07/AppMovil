package com.instantmusic.appmovil.server.connect;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * My own custom Http wrapper for JSON connections, because the existing ones are ugly, big size, with a lot of extra things that nobody uses, and hard to make them work.
 * Edit: arg, why HttpConnection doesn't allow patch...now I need to use the ugly Volley
 */
public class JSONConnection {

    /**
     * Different methods for the http connection
     */
    public enum METHOD {
        GET(Request.Method.GET),
        POST(Request.Method.POST),
        PUT(Request.Method.PUT),
        PATCH(Request.Method.PATCH),
        DELETE(Request.Method.DELETE),
        ;

        public final int v; // internal Volley value

        METHOD(int v) {
            this.v = v;
        }
    }

    /**
     * Makes a petition
     *
     * @param url      the url to connect to
     * @param method   the method to use
     * @param body     if null, a GET will be made. If not, a POST will be made with that Json data
     * @param headers  extra headers
     * @param listener where to notify
     * @param queue    queue to launch the petition
     */
    public static void makePetition(String url, METHOD method, JSONObject body, final Map<String, String> headers, final Listener listener, RequestQueue queue) {
        JsonObjectRequest request = new JsonObjectRequest(
                method == null ? Request.Method.DEPRECATED_GET_OR_POST : method.v,
                url,
                body,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // shouldn't run, but just in case
                        listener.onValidResponse(200, response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            // a response, call listener
                            try {
                                listener.onValidResponse(
                                        error.networkResponse.statusCode,
                                        new JSONObject(new String(error.networkResponse.data))
                                );
                            } catch (JSONException e) {
                                // json error
                                listener.onErrorResponse(e);
                            }
                        } else {
                            // no respone, error listener
                            listener.onErrorResponse(error);
                        }
                    }
                }) {
            @Override
            protected com.android.volley.Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // convert valid responses into invalid ones, so that we can access the statusCode
                return com.android.volley.Response.error(new VolleyError(new NetworkResponse(
                        response.statusCode,
                        response.data,
                        response.notModified,
                        response.networkTimeMs,
                        response.allHeaders
                )));
            }

            @Override
            public Map<String, String> getHeaders() {
                // return the headers
                return headers == null ? Collections.<String, String>emptyMap() : headers;
            }
        };

        // start
        queue.add(request);
    }

    // ------------------- public classes -------------------

    /**
     * To notify responses of {@link #makePetition(String, METHOD, JSONObject, Map, Listener, RequestQueue)}
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
}