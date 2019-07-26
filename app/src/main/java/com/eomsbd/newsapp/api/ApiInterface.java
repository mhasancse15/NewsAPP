package com.eomsbd.newsapp.api;

import com.eomsbd.newsapp.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News>getNews(
            @Query("country") String country,
            @Query("apikey") String apiKey
    );

    @GET("everything")
    Call<News>getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apikey") String apiKey
    );


}
