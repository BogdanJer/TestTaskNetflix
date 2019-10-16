package com.example.netflixroulette;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.netflixroulette.db.room.AppDatabase;
import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.network.movies.Movie;
import com.example.netflixroulette.network.persons.Work;
import com.example.netflixroulette.network.tv.shows.TvShow;
import com.example.netflixroulette.ui.MovieDetailsFragment;
import com.example.netflixroulette.ui.SavedMovieDetailsFragment;
import com.example.netflixroulette.ui.director_search.SearchByPersonFragment;
import com.example.netflixroulette.ui.saved_movies.SavedMoviesFragment;
import com.example.netflixroulette.ui.title_search.SearchByTitleFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMovieClickListener, OnSavedMovieClickListener {
    public static final String SHOW = "show";
    public static final String LIST_SHOW = "list";
    private static final String SAVED_MOVIES_FRAGMENT_NAME = "saved_movies";
    private static final String SEARCH_BY_TITLE_FRAGMENT_NAME = "title_search";
    private static final String SEARCH_BY_PERSON_FRAGMENT_NAME = "person_search";
    private List<Fragment> fragmentList;
    private AppDatabase db;

    private ViewPager viewPager;

    private FragmentManager fm;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = Room.databaseBuilder(this, AppDatabase.class, "app_db").build();
        fragmentList = new ArrayList<>();

        Fragment fragment = new SavedMoviesFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(SAVED_MOVIES_FRAGMENT_NAME)
                .commit();
        fragmentList.add(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String fragmentName = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_saved_movies:
                fragmentName = SAVED_MOVIES_FRAGMENT_NAME;
                fragment = new SavedMoviesFragment();
                break;
            case R.id.nav_title_search:
                fragmentName = SEARCH_BY_TITLE_FRAGMENT_NAME;
                fragment = new SearchByTitleFragment();
                break;
            case R.id.nav_person_search:
                fragmentName = SEARCH_BY_PERSON_FRAGMENT_NAME;
                fragment = new SearchByPersonFragment();
                break;
        }

        boolean contains = false;

        for (Fragment f : fragmentList) {
            if (f.getClass().equals(fragment.getClass())) {
                fragment = f;
                contains = true;
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);

        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(fragmentName)
                .commit();

        if (!contains)
            fragmentList.add(fragment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
        return false;
    }

    @Override
    public void onMovieClick(Show show) {
        Fragment fragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();

        if (show instanceof Movie)
            bundle.putSerializable(SHOW, (Movie) show);
        else if (show instanceof Work)
            bundle.putSerializable(SHOW, (Work) show);
        else if (show instanceof TvShow)
            bundle.putSerializable(SHOW, (TvShow) show);

        fragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSavedMovieClick(Show show) {
        Fragment fragment = new SavedMovieDetailsFragment();
        Bundle bundle = new Bundle();

        if (show instanceof Movie)
            bundle.putSerializable(SHOW, (Movie) show);
        else if (show instanceof Work)
            bundle.putSerializable(SHOW, (Work) show);
        else if (show instanceof TvShow)
            bundle.putSerializable(SHOW, (TvShow) show);

        fragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public AppDatabase getDb() {
        return db;
    }
}
