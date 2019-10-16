package com.example.netflixroulette.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.netflixroulette.MainActivity;
import com.example.netflixroulette.R;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;

public class AddDeleteMovieAsync extends AsyncTask<Void, Void, Boolean> {
    private MenuItem saveBut;
    private Show show;
    private Activity activity;

    public AddDeleteMovieAsync(Activity activity, Show show, MenuItem saveBut) {
        this.saveBut = saveBut;
        this.show = show;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean result = null;
        if (show instanceof Movie)
            result = ((MainActivity) activity).getDb().getMovieDao().isMovieInDB(((Movie) show).getId()) > 0;
        else if (show instanceof TvShow)
            result = ((MainActivity) activity).getDb().getMovieDao().isTvShowInDB(((TvShow) show).getId()) > 0;
        else if (show instanceof Work)
            result = ((MainActivity) activity).getDb().getMovieDao().isTvShowInDB(((Work) show).getId()) > 0;

        if (result) {
            if (show instanceof Movie)
                ((MainActivity) activity).getDb().getMovieDao().deleteMovie((Movie) show);
            else if (show instanceof TvShow)
                ((MainActivity) activity).getDb().getMovieDao().deleteTvShow((TvShow) show);
            else if (show instanceof Work)
                ((MainActivity) activity).getDb().getMovieDao().deleteWork((Work) show);
        } else {
            if (show instanceof Movie)
                ((MainActivity) activity).getDb().getMovieDao().saveMovie((Movie) show);
            else if (show instanceof TvShow)
                ((MainActivity) activity).getDb().getMovieDao().saveTvShow((TvShow) show);
            else if (show instanceof Work)
                ((MainActivity) activity).getDb().getMovieDao().saveWork((Work) show);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            saveBut.setIcon(activity.getDrawable(R.drawable.not_saved));
            Toast.makeText(activity, R.string.movie_deleted, Toast.LENGTH_LONG).show();
        } else {
            saveBut.setIcon(activity.getDrawable(R.drawable.saved));
            Toast.makeText(activity, R.string.movie_saved, Toast.LENGTH_LONG).show();
        }
    }
}