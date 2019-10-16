package com.example.netflixroulette;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;
import com.example.netflixroulette.ui.saved_movies.SavedMoviesFragment;
import com.example.netflixroulette.ui.title_search.SearchByTitleFragment;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
    private ArrayList<Show> list;
    private boolean isSavedMovie;

    public MovieAdapter(boolean isSavedMovie) {
        list = new ArrayList<>();
        this.isSavedMovie = isSavedMovie;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieHolder(inflater.inflate(R.layout.movie_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        if (list.get(position) instanceof Movie) {
            Movie movie = (Movie) list.get(position);

            if (movie.getPosterPath() != null)
                Glide.with(holder.getView())
                        .load(String.format("%s%s", TheMovieDbAPI.IMAGES_BASE_URL, movie.getPosterPath()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getMovieImage());

            holder.getMovieTitle().setText(movie.getOriginalTitle());
            holder.getMovieLayout().setOnClickListener(v -> {
                if (!isSavedMovie)
                    SearchByTitleFragment.movieClickListener.onMovieClick(movie);
                else
                    SavedMoviesFragment.movieClickListener.onSavedMovieClick(movie);
            });

            holder.bind(movie);
        } else if (list.get(position) instanceof TvShow) {
            TvShow tvShow = (TvShow) list.get(position);

            if (tvShow.getPosterPath() != null)
                Glide.with(holder.getView())
                        .load(String.format("%s%s", TheMovieDbAPI.IMAGES_BASE_URL, tvShow.getPosterPath()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getMovieImage());

            holder.getMovieTitle().setText(tvShow.getOriginalName());
            holder.getMovieLayout().setOnClickListener(v -> {
                if (!isSavedMovie)
                    SearchByTitleFragment.movieClickListener.onMovieClick(tvShow);
                else
                    SavedMoviesFragment.movieClickListener.onSavedMovieClick(tvShow);
            });
            holder.bind(tvShow);
        } else if (list.get(position) instanceof Work) {
            Work work = (Work) list.get(position);

            if (work.getPosterPath() != null)
                Glide.with(holder.getView())
                        .load(String.format("%s%s", TheMovieDbAPI.IMAGES_BASE_URL, work.getPosterPath()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getMovieImage());

            holder.getMovieTitle().setText(work.getTitle());
            holder.getMovieLayout().setOnClickListener(v -> {
                if (!isSavedMovie)
                    SearchByTitleFragment.movieClickListener.onMovieClick(work);
                else
                    SavedMoviesFragment.movieClickListener.onSavedMovieClick(work);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItems(List<Show> shows) {
        list.addAll(shows);
        notifyItemRangeInserted(0, list.size());
    }

    public void removeItems() {
        if (list.size() != 0) {
            notifyItemRangeRemoved(0, list.size());
            list.clear();
        }
    }

    public void addMovies(List<Movie> movies) {
        list.addAll(movies);
    }

    public void addTvShows(List<TvShow> tvShows) {
        list.addAll(tvShows);
    }

    public void addWorks(List<Work> works) {
        list.addAll(works);
    }

    public void notifyChanges() {
        notifyItemRangeInserted(0, list.size());
    }
}