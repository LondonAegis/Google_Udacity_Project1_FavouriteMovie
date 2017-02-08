package com.example.android.favouritemovieprojectonemichael.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.favouritemovieprojectonemichael.Data.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by LondonAegis on 04/02/2017.
 */

public class NetworkUtils {

    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();
    /**
     * Strings for URL creation
     * API source : https://www.themoviedb.org/documentation/api/discover
     * popular movies : /discover/movie?sort_by=popularity.desc
     * http://api.themoviedb.org/3/movie/popular?language=en&api_key=d62ef072a85675edc021cd89cf9cf21f
     */
    final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    final static String BASE_URL = "http://api.themoviedb.org/3/movie";
    final static String PARAM_QUERY = "language";
    final static String PARAM_LANGUAGE = "en";
    final static String PARAM_API_KEY = "api_key";
    final static String PARAM_MY_API = "d62ef072a85675edc021cd89cf9cf21f";

    final static String PARAM_SORT_POP = "popular";
    // final static String PARAM_SORT_TOP_RATED = "top-rated";
    final static String sortBy = "desc";

    final static String POSTER_SIZE_W92 = "w92";
    final static String POSTER_SIZE_W154 = "w154";
    final static String POSTER_SIZE_W185 = "w185";
    final static String POSTER_SIZE_W342 = "w342";
    final static String POSTER_SIZE_W500 = "w500";
    final static String POSTER_SIZE_W780 = "w780";
    final static String POSTER_SIZE_ORIGINAL = "original";

    /* The format we want our API to return */
    private static final String format = "json";

    /**
     * Builds the URL used to query TheMovieDB.
     *
     * @param searchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildMovieUrl(String searchQuery) {
        Uri uri = null;

        if (searchQuery.equals(PARAM_SORT_POP)) {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(PARAM_SORT_POP)
                    .appendQueryParameter(PARAM_QUERY, PARAM_LANGUAGE)
                    .appendQueryParameter(PARAM_API_KEY, PARAM_MY_API)
                    .build();
        }
        else {
            uri  = Uri.parse(BASE_IMAGE_URL).buildUpon()
                    .appendPath(POSTER_SIZE_W500)
                    .appendPath(searchQuery)
                    .build();
        }
        try {
            URL url = new URL(uri.toString());
            Log.v(LOG_TAG, LOG_TAG + "/NetworkUtils/buildUrl : " + url.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static MovieData[] getMovieStringsFromJson(Context context, String movieListJsonStr)
            throws JSONException {

        /* results */
        final String RESULTS = "results";

        /* original title */
        final String ORIGINAL_TITLE = "original_title";
        final String DESCRIPTION = "description";

        /* poster path */
        final String POSTER_PATH = "poster_path";

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject moviesJson = new JSONObject(movieListJsonStr);

        /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = moviesJson.getJSONArray(RESULTS);

        /* String array to hold each day's weather String */
        MovieData[] parsedMovieData = new MovieData[movieArray.length()]; // 20

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieData = movieArray.getJSONObject(i);

            MovieData movieInfoHolder = new MovieData();
            movieInfoHolder.setTitle(movieData.getString(ORIGINAL_TITLE));

            Log.v(LOG_TAG, LOG_TAG + "/NetworkUtils/poster_path : " + movieData.getString(POSTER_PATH));
            Uri uri  = Uri.parse(BASE_IMAGE_URL).buildUpon()
                    .appendPath(POSTER_SIZE_W500)
                    .appendEncodedPath(movieData.getString(POSTER_PATH).replace("/", ""))
                    .build();
            URL url = null;
            try {
                url = new URL(uri.toString());
                Log.v(LOG_TAG, LOG_TAG + "/NetworkUtils/posterUrl : " + url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            movieInfoHolder.setPosterPath(url.toString());
            // movieInfoHolder.setDescription(movieData.getString(DESCRIPTION));

            parsedMovieData[i] = movieInfoHolder;
        }
        return parsedMovieData;
    }
}
