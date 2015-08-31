package com.mosn.asyncmockwebserver.example;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ITunesSearchModule {

    private Application application;

    public ITunesSearchModule(Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }
}
