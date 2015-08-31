package com.mosn.asyncmockwebserver.example.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosn.asyncmockwebserver.example.infrastructure.api.ApiCommon;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class InfrastructureModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    public OkClient provideOkClient(OkHttpClient okHttpClient) {
        return new OkClient(okHttpClient);
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    public GsonConverter provideGsonConverter(Gson gson) {
        return new GsonConverter(gson, "UTF-8");
    }

    @Provides
    public RestAdapter.Builder provideRestAdapterBuilder(OkClient okClient, GsonConverter gsonConverter) {
        return new RestAdapter.Builder()
                .setEndpoint(ApiCommon.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(okClient)
                .setConverter(gsonConverter);
    }
}
