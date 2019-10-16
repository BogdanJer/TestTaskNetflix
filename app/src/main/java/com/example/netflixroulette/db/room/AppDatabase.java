package com.example.netflixroulette.db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.netflixroulette.db.room.dao.MovieDao;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

@Database(entities = {Movie.class, TvShow.class, Work.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();

    /** static final Migration MIGRATION_1_2 = new Migration(1,2) {
    @Override public void migrate(@NonNull SupportSQLiteDatabase database) {
    database.execSQL("CREATE TABLE tv_shows ()");
    }
    };*/
}
