package com.example.netflixroulette.ui.saved_movies;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixroulette.MainActivity;
import com.example.netflixroulette.MovieAdapter;
import com.example.netflixroulette.OnSavedMovieClickListener;
import com.example.netflixroulette.R;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

import java.util.List;

public class SavedMoviesFragment extends Fragment {
    public static OnSavedMovieClickListener movieClickListener;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private DrawerLayout drawer;
    private List<Movie> movieList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            movieClickListener = (OnSavedMovieClickListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString() + " must implement OnSavedMovieClickListener");
        }

        drawer = getActivity().findViewById(R.id.drawer_layout);

        adapter = new MovieAdapter(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_movies, container, false);
        setHasOptionsMenu(true);

        int orientation = getResources().getConfiguration().orientation;
        recyclerView = view.findViewById(R.id.saved_movies_recycler_view);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.notifyDataSetChanged();
        new Thread(() -> {
            movieList = ((MainActivity) getActivity()).getDb().getMovieDao().getSavedMovies();
            adapter.removeItems();
            List<TvShow> tvShowsList = ((MainActivity) getActivity()).getDb().getMovieDao().getSavedTvShows();
            List<Work> worksList = ((MainActivity) getActivity()).getDb().getMovieDao().getSavedWorks();

            adapter.addMovies(movieList);
            adapter.addTvShows(tvShowsList);
            adapter.addWorks(worksList);
        }).start();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyChanges();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
        return false;
    }
}