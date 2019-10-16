package com.example.netflixroulette.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<Movie> getSavedMovies();

    @Query("SELECT * FROM tv_shows")
    List<TvShow> getSavedTvShows();

    @Query("SELECT * FROM works")
    List<Work> getSavedWorks();

    @Insert
    void saveMovie(Movie movie);

    @Insert
    void saveTvShow(TvShow tvShow);

    @Insert
    void saveWork(Work work);

    @Query("SELECT EXISTS (SELECT * FROM movies WHERE id = :id)")
    int isMovieInDB(int id);

    @Query("SELECT EXISTS (SELECT * FROM tv_shows WHERE id = :id)")
    int isTvShowInDB(int id);

    @Query("SELECT EXISTS (SELECT * FROM works WHERE id = :id)")
    int isWorkInDB(int id);

    @Delete
    void deleteMovie(Movie movie);

    @Delete
    void deleteTvShow(TvShow tvShow);

    @Delete
    void deleteWork(Work work);
}
