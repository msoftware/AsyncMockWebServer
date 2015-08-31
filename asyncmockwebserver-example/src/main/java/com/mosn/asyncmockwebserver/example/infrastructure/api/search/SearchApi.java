package com.mosn.asyncmockwebserver.example.infrastructure.api.search;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface SearchApi {
    @GET("/search")
    void search(@Query("term") String term, Callback<SearchApiDto> callback);
}
