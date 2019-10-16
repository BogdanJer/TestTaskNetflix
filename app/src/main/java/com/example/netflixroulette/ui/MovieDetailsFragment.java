package com.example.netflixroulette.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.netflixroulette.R;
import com.example.netflixroulette.async.AddDeleteMovieAsync;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

import static com.example.netflixroulette.MainActivity.SHOW;

public class MovieDetailsFragment extends Fragment {
    private ImageView moviePoster;
    private TextView movieReleaseYear;
    private TextView movieRating;
    private TextView movieDirector;
    private TextView movieSummary;
    private ImageButton saveBut;

    private DrawerLayout drawer;

    private Show show;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        drawer = getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        saveBut = getActivity().findViewById(R.id.saved_but_toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        setHasOptionsMenu(true);

        moviePoster = view.findViewById(R.id.details_movie_poster);
        movieReleaseYear = view.findViewById(R.id.details_movie_release_year);
        movieRating = view.findViewById(R.id.details_movie_rating);
        movieDirector = view.findViewById(R.id.details_movie_director);
        movieSummary = view.findViewById(R.id.details_movie_summary);

        show = (Show) getArguments().getSerializable(SHOW);

        String title = "";
        String releaseYear = "";
        String rating = "";
        String posterPath = "";
        String summary = "";
        if (show instanceof Movie) {
            title = ((Movie) show).getTitle();
            posterPath = ((Movie) show).getPosterPath();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((Movie) show).getReleaseDate();
            rating = movieRating.getHint().toString() + ": " + ((Movie) show).getVoteAverage();
            summary = ((Movie) show).getOverview();
        } else if (getArguments().getSerializable(SHOW) instanceof TvShow) {
            title = ((TvShow) show).getName();
            posterPath = ((TvShow) show).getPosterPath();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((TvShow) show).getFirstAirDate();
            rating = movieRating.getHint().toString() + ": " + ((TvShow) show).getVoteAverage();
            summary = ((TvShow) show).getOverview();
        } else if (getArguments().getSerializable(SHOW) instanceof Work) {
            title = ((Work) show).getTitle();
            posterPath = ((Work) show).getPosterPath();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((Work) show).getReleaseDate();
            rating = movieRating.getHint().toString() + ": " + ((Work) show).getVoteAverage();
            summary = ((Work) show).getOverview();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);

        Glide.with(getContext())
                .load(TheMovieDbAPI.IMAGES_BASE_URL + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(moviePoster);

        movieReleaseYear.setText(releaseYear);
        movieRating.setText(rating);
        //movieDirector.setText(movie.get);
        movieSummary.setText(summary);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                getActivity().onBackPressed();
                break;
            case R.id.saved_but_toolbar:
                new AddDeleteMovieAsync(getActivity(), show, item).execute();
                break;
        }
        return true;
    }
}
