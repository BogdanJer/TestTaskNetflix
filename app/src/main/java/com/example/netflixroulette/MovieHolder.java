package com.example.netflixroulette;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

public class MovieHolder extends RecyclerView.ViewHolder {
    private ImageView movieImage;
    private TextView movieTitle;
    private LinearLayout movieLayout;

    private View view;

    private Show show;

    public MovieHolder(View view) {
        super(view);

        this.view = view;

        movieImage = view.findViewById(R.id.movie_image);
        movieTitle = view.findViewById(R.id.movie_title);
        movieLayout = view.findViewById(R.id.movie_layout);
    }

    public void bind(Movie movie) {
        this.show = movie;
    }

    public void bind(TvShow tvShow) {
        this.show = tvShow;
    }

    public void bind(Work work) {
        this.show = work;
    }

    public ImageView getMovieImage() {
        return movieImage;
    }

    public View getView() {
        return view;
    }

    public TextView getMovieTitle() {
        return movieTitle;
    }

    public LinearLayout getMovieLayout() {
        return movieLayout;
    }
}
