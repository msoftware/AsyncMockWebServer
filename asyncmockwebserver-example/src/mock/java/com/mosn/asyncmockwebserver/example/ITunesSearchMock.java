package com.mosn.asyncmockwebserver.example;

import android.app.Application;
import android.content.res.AssetManager;

import com.mosn.asyncmockwebserver.AsyncMockWebServer;
import com.mosn.asyncmockwebserver.Mock;
import com.mosn.asyncmockwebserver.MockDispatcher;
import com.mosn.asyncmockwebserver.example.utils.FileUtils;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.io.IOException;

public class ITunesSearchMock {

    public static void setupMockSearchApi(Application application) {

        AssetManager assetManager = application.getAssets();

        try {
            String mockString = FileUtils.inputStreamToString(assetManager.open("mock.json"));
            AsyncMockWebServer.addMock(Mock.create(
                    "/search",
                    new MockResponse().setResponseCode(200).setBody(mockString),
                    new MockDispatcher() {
                        @Override
                        public boolean isDispatch(RecordedRequest request) {
                            return request.getPath().contains("/search");
                        }
                    }
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
