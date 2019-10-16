package com.example.netflixroulette.async;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.netflixroulette.MovieAdapter;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.movies.DataMovies;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.tv.shows.DataTvShows;
import com.example.netflixroulette.network.tv.shows.TvShow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FindMoviesAsync extends AsyncTask<String, Void, List<Show>> {
    private TheMovieDbAPI api;
    private ProgressBar progressBar;
    private MovieAdapter adapter;

    public FindMoviesAsync(MovieAdapter adapter, TheMovieDbAPI api, ProgressBar progressBar) {
        this.adapter = adapter;
        this.api = api;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Show> doInBackground(String... strings) {
        List<Show> list = new ArrayList<>();

        try {
            Response<DataMovies> responseMovies = api.getMovies(strings[0], strings[1]).execute();
            Response<DataTvShows> responseTvShows = api.getTVShows(strings[0], strings[1]).execute();

            if (responseMovies.isSuccessful()) {
                if (responseMovies.body().getMovies() != null)
                    for (Movie movie : responseMovies.body().getMovies()) {
                        System.out.println(movie.getId() + "\t" + movie.getPosterPath());
                        list.add(movie);
                    }
            }
            if (responseTvShows.isSuccessful()) {
                if (responseTvShows.body().getTvShows() != null)
                    for (TvShow tvShow : responseTvShows.body().getTvShows()) {
                        System.out.println(tvShow.getId() + "\t" + tvShow.getPosterPath());
                        list.add(tvShow);
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Show> shows) {
        progressBar.setVisibility(View.GONE);
        adapter.addItems(shows);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
