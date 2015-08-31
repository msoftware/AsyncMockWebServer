package com.mosn.asyncmockwebserver.example;

import android.app.Application;

import com.mosn.asyncmockwebserver.example.infrastructure.InfrastructureModule;

public class ITunesSearchApplication extends Application {

    private ITunesSearchComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
        component.inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    protected ITunesSearchComponent buildComponent() {
        return DaggerITunesSearchComponent.builder()
                .iTunesSearchModule(new ITunesSearchModule(this))
                .infrastructureModule(new InfrastructureModule())
                .build();
    }

    public ITunesSearchComponent getComponent() {
        return component;
    }
}
