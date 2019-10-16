package com.example.netflixroulette.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.MenuItem;

import com.example.netflixroulette.MainActivity;
import com.example.netflixroulette.R;

public class ShowInDbAsync extends AsyncTask<Integer, Void, Boolean> {
    private MenuItem saveBut;
    private Activity activity;

    public ShowInDbAsync(Activity activity, MenuItem saveBut) {
        this.saveBut = saveBut;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        if (((MainActivity) activity).getDb().getMovieDao().isMovieInDB(integers[0]) > 0 ||
                ((MainActivity) activity).getDb().getMovieDao().isTvShowInDB(integers[0]) > 0 ||
                ((MainActivity) activity).getDb().getMovieDao().isTvShowInDB(integers[0]) > 0)
            return true;
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result)
            saveBut.setIcon(activity.getDrawable(R.drawable.saved));
    }
}
