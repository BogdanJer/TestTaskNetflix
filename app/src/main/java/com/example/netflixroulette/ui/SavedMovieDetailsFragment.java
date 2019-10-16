package com.example.netflixroulette.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.netflixroulette.R;
import com.example.netflixroulette.async.AddDeleteMovieAsync;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.netflixroulette.MainActivity.LIST_SHOW;
import static com.example.netflixroulette.MainActivity.SHOW;

public class SavedMovieDetailsFragment extends Fragment implements Serializable {
    private TextView movieRating;
    private TextView movieReleaseYear;
    private ImageView moviePoster;
    private TextView movieCategory;
    private TextView movieDirector;
    private TextView movieTitle;
    private ViewPager viewPager;

    private MenuItem saveBut;

    private DrawerLayout drawer;

    private Show show;
    private ArrayList<Show> list;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_movie_details, container, false);

        drawer = getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        // saveBut = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.saved_but_toolbar);

        show = (Show) getArguments().getSerializable(SHOW);
        list = (ArrayList<Show>) getArguments().getSerializable(LIST_SHOW);

        viewPager = view.findViewById(R.id.details_saved_movie_view_pager);
        movieRating = view.findViewById(R.id.details_saved_movie_rating);
        movieReleaseYear = view.findViewById(R.id.details_saved_movie_release_year);
        moviePoster = view.findViewById(R.id.details_saved_movie_poster);
        movieCategory = view.findViewById(R.id.details_saved_movie_category);
        movieDirector = view.findViewById(R.id.details_saved_movie_director);
        movieTitle = view.findViewById(R.id.details_saved_movie_title);

        String title = "";
        String rating = "";
        String releaseYear = "";
        String posterPath = "";
        if (show instanceof Movie) {
            title = ((Movie) show).getTitle();
            rating = movieRating.getHint().toString() + ": " + ((Movie) show).getVoteAverage();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((Movie) show).getReleaseDate();
            posterPath = ((Movie) show).getPosterPath();
        } else if (show instanceof TvShow) {
            title = ((TvShow) show).getName();
            rating = movieRating.getHint().toString() + ": " + ((TvShow) show).getVoteAverage();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((TvShow) show).getFirstAirDate();
            posterPath = ((TvShow) show).getPosterPath();
        } else if (show instanceof Work) {
            title = ((Work) show).getTitle();
            rating = movieRating.getHint().toString() + ": " + ((Work) show).getVoteAverage();
            releaseYear = movieReleaseYear.getHint().toString() + ": " + ((Work) show).getReleaseDate();
            posterPath = ((Work) show).getPosterPath();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        movieRating.setText(rating);
        movieReleaseYear.setText(releaseYear);

        Glide.with(getContext())
                .load(TheMovieDbAPI.IMAGES_BASE_URL + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(moviePoster);

        movieTitle.setText(title);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        //saveBut = menu.findItem(R.id.saved_but_toolbar);
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
        }
        return true;
    }


}
