package com.example.android.favouritemovieprojectonemichael.Data;

/**
 * Created by LondonAegis on 05/02/2017.
 */

public class MovieData {
    private String movie_title;
    private String movie_poster_path;
    private String movie_description;

    public MovieData () {

    }

    public void setTitle(String title) {
        movie_title = title;
    }
    public void setPosterPath(String poster) {
        movie_poster_path = poster;
    }
    public void setDescription(String desc) {
        movie_description = desc;
    }

    public String getTitle() {
        return movie_title;
    }
    public String getPosterPath() {
        return movie_poster_path;
    }
    public String getMovieDescription() {
        return movie_description;
    }
}
