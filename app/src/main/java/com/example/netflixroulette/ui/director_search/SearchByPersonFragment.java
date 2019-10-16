package com.example.netflixroulette.ui.director_search;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.netflixroulette.OnMovieClickListener;
import com.example.netflixroulette.R;
import com.example.netflixroulette.async.FindPersonsMovies;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchByPersonFragment extends Fragment implements View.OnClickListener {
    private TheMovieDbAPI api;
    private RecyclerView recyclerView;
    private MovieTvShowAdapter adapter;
    private ProgressBar progressBar;

    private EditText searchField;
    private ImageButton searchBut;

    private DrawerLayout drawer;

    private OnMovieClickListener movieClickListener;

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

        api = retrofit.create(TheMovieDbAPI.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_person, container, false);

        drawer = getActivity().findViewById(R.id.drawer_layout);

        searchField = view.findViewById(R.id.search_by_person_edit_text);
        searchBut = view.findViewById(R.id.search_by_person_but);
        searchBut.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.search_by_person_recycler);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new MovieTvShowAdapter();
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

        String name = searchField.getText().toString();

        if (name.compareTo("") != 0) {
            new FindPersonsMovies(api, adapter, progressBar).execute(name, getString(R.string.the_movie_db_key));
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

    private class MovieTvShowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView moviePoster;
        private TextView movieName;
        private LinearLayout movieLayout;

        private Work work;

        public MovieTvShowHolder(View view) {
            super(view);

            moviePoster = view.findViewById(R.id.movie_image);
            movieName = view.findViewById(R.id.movie_title);
            movieLayout = view.findViewById(R.id.movie_layout);

            movieLayout.setOnClickListener(this);
        }

        public void bind(Work work) {
            this.work = work;

            movieName.setText(work.getTitle());

            Glide.with(getContext())
                    .load(TheMovieDbAPI.IMAGES_BASE_URL + work.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            movieClickListener.onMovieClick(work);
        }
    }

    public class MovieTvShowAdapter extends RecyclerView.Adapter<MovieTvShowHolder> {
        private List<Work> list;

        public MovieTvShowAdapter() {
            list = new ArrayList<>();
        }

        @NonNull
        @Override
        public MovieTvShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MovieTvShowHolder(inflater.inflate(R.layout.movie_item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieTvShowHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItems(List<Work> work) {
            list.addAll(work);
            notifyItemRangeInserted(0, list.size());
        }

        public void removeItems() {
            notifyItemRangeRemoved(0, list.size());
            list.clear();
        }
    }
}