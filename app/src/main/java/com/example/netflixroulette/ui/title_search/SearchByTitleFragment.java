package com.example.netflixroulette.ui.title_search;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixroulette.MovieAdapter;
import com.example.netflixroulette.OnMovieClickListener;
import com.example.netflixroulette.R;
import com.example.netflixroulette.async.FindMoviesAsync;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.utils.InternetConnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchByTitleFragment extends Fragment implements View.OnClickListener {
    public static OnMovieClickListener movieClickListener;
    private EditText searchField;
    private TheMovieDbAPI api;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            movieClickListener = (OnMovieClickListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString() + " must implement OnMovieClickListener");
        }

        progressBar = getActivity().findViewById(R.id.progress_bar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        drawer = getActivity().findViewById(R.id.drawer_layout);

        api = retrofit.create(TheMovieDbAPI.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_movies_by_title, container, false);

        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);

        ImageButton searchBut = view.findViewById(R.id.search_by_title_but);
        searchBut.setOnClickListener(this);

        searchField = view.findViewById(R.id.search_by_title_edit_text);

        int orientation = getResources().getConfiguration().orientation;

        recyclerView = view.findViewById(R.id.search_by_title_recycler);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MovieAdapter(false);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onClick(View v) {
        if (!InternetConnection.checkConnection(getContext())) {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        }
        adapter.removeItems();
        if (searchField.getText() != null && searchField.getText().toString() != "") {
            new FindMoviesAsync(adapter, api, progressBar).execute(searchField.getText().toString(), getString(R.string.the_movie_db_key));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}