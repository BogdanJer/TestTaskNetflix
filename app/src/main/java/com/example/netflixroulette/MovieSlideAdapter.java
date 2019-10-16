package com.example.netflixroulette;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.netflixroulette.network.Show;
import com.example.netflixroulette.ui.SavedMovieDetailsFragment;

import java.util.ArrayList;

public class MovieSlideAdapter extends FragmentPagerAdapter {
    private ArrayList<Show> list;

    public MovieSlideAdapter(FragmentManager fm, ArrayList<Show> list) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new SavedMovieDetailsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}