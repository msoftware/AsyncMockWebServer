package com.mosn.asyncmockwebserver.example.domain.repository.callback;

import retrofit.RetrofitError;
import retrofit.client.Response;

public interface NetworkCallback<T> {
    void onFailure(RetrofitError error);
    void onSuccess(T obj, Response response);
}
