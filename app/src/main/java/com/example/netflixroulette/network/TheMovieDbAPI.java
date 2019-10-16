package com.example.netflixroulette.network;

import com.example.netflixroulette.network.movies.DataMovies;
import com.example.netflixroulette.network.persons.DataPerson;
import com.example.netflixroulette.network.tv.shows.DataTvShows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbAPI {
    String API_BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGES_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @GET("search/movie/")
    Call<DataMovies> getMovies(@Query("query") String title, @Query("api_key") String apiKey);

    @GET("search/person/")
    Call<DataPerson> getPerson(@Query("query") String name, @Query("api_key") String apiKey);

    @GET("search/tv/")
    Call<DataTvShows> getTVShows(@Query("query") String title, @Query("api_key") String apiKey);

    enum SEARCH_TYPE {
        MOVIE,
        TV,
        PERSON
    }

}
