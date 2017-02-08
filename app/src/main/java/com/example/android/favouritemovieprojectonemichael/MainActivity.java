package com.example.android.favouritemovieprojectonemichael;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.favouritemovieprojectonemichael.Data.MovieData;
import com.example.android.favouritemovieprojectonemichael.Utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MyMovieAdapter mMyMovieAdapter;

    /*
        Reference initiation for later use
     */
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MovieData[] simpleJsonWeatherData;

    private URL movieSearchUrl;
    private String searchCriteria = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking with the views from XML
        mErrorMessageDisplay = (TextView) findViewById (R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMyMovieAdapter = new MyMovieAdapter();
        mRecyclerView.setAdapter(mMyMovieAdapter);

        loadMovieData();
    }

    /**
     * This method will make the View for the JSON data visible and hide the error message.
     */
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    /**
     * This method will make the error message visible and hide the JSON View
     */
    private void showErrorMessage() {
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public class ConnectAndGetMovieList extends AsyncTask<URL, Void, MovieData[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieData[] doInBackground(URL... params) {
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(params[0]);

                simpleJsonWeatherData = NetworkUtils
                        .getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieData[] s) {
            super.onPostExecute(s);
            // As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (s != null) {
                showJsonDataView();
                // COMPLETED (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
                mMyMovieAdapter.setMovieData(s);
            } else {
                showErrorMessage();
            }
            /*
            if (s != null && s.length > 0) {
                // Call showJsonDataView if we have valid, non-null results
                showJsonDataView();

                // ImageView iv = (ImageView) findViewById(R.id.poster_image);
                for (int i=0; i<s.length; i++) {
                    // String uriPoster
                    Picasso.with(MainActivity.this)
                            .load("http://image.tmdb.org/t/p/w500/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg")
                            .into(iv);
                }
            } else {
                // Call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
            */
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            // COMPLETED (46) Instead of setting the text to "", set the adapter to null before refreshing
            mMyMovieAdapter.setMovieData(null);
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadMovieData() {
        showJsonDataView();

        // Connect to TheMovieDB and get popular list automatically when app is launched
        movieSearchUrl = NetworkUtils.buildMovieUrl(searchCriteria);
        new ConnectAndGetMovieList().execute(movieSearchUrl);
    }
}
