package com.instantmusic.appmovil.server.connect;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
    }

    /**
     * Makes a petition
     *
     * @param url      the url to connect to
     * @param method   the method to use
     * @param body     if null, a GET will be made. If not, a POST will be made with that Json data
     * @param headers  extra headers
     * @param listener where to notify
     */
    public static void makePetition(String url, METHOD method, JSONObject body, final Map<String, String> headers, final Listener listener) {

        // build headers
        Headers.Builder headers_okhttp = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                headers_okhttp.add(header.getKey(), header.getValue());
            }
        }

        // prepare body
        RequestBody body_okhttp = body == null
                ? null
                : RequestBody.create(JSON, body.toString());

        // prepare method
        if (method == null) {
            method = body_okhttp != null ? METHOD.POST : METHOD.GET;
        }

        // prepare request
        Request request = new Request.Builder()
                .url(url)
                .method(method.name(), body_okhttp)
                .headers(headers_okhttp.build())
                .build();

        // prepare callback
        Callback callback = new Callback() {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Request request, final IOException throwable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // failure
                        listener.onErrorResponse(throwable);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final int code = response.code();
                    final JSONObject json = new JSONObject(new String(response.body().bytes()));
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // valid
                            listener.onValidResponse(code, json);
                        }
                    });
                } catch (final Throwable e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // error
                            listener.onErrorResponse(e);
                        }
                    });
                }
            }
        };

        // run
        client.newCall(request).enqueue(callback);
    }

    // ------------------- public classes -------------------

    /**
     * To notify responses of {@link #makePetition(String, METHOD, JSONObject, Map, Listener)}
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

    // ------------------- Private data -------------------

    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}