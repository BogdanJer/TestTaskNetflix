package com.example.netflixroulette.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.netflixroulette.network.TheMovieDbAPI;
import com.example.netflixroulette.network.persons.DataPerson;
import com.example.netflixroulette.network.persons.Person;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.ui.director_search.SearchByPersonFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FindPersonsMovies extends AsyncTask<String, Void, List<Work>> {
    private final String FIND_PERSONS_MOVIES = "Find person's movies";

    private TheMovieDbAPI api;
    private SearchByPersonFragment.MovieTvShowAdapter adapter;
    private ProgressBar progressBar;

    public FindPersonsMovies(TheMovieDbAPI api, SearchByPersonFragment.MovieTvShowAdapter adapter, ProgressBar progressBar) {
        this.api = api;
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Work> doInBackground(String... strings) {
        List<Work> list = new ArrayList<>();
        try {
            Response<DataPerson> response = api.getPerson(strings[0], strings[1]).execute();

            if (response.isSuccessful()) {
                if (response.body().getResults() != null) {
                    for (Person person : response.body().getResults()) {
                        if (person.getKnownForDepartment().compareTo("Directing") == 0) {
                            for (Work work : response.body().getResults().get(0).getKnownFor())
                                list.add(work);
                            break;
                        }
                    }
                }
            } else {
                Log.e(FIND_PERSONS_MOVIES, "Unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Work> work) {
        progressBar.setVisibility(View.GONE);
        adapter.addItems(work);
    }

    @Override
    protected void onCancelled() {

    }
}
