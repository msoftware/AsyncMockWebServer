package com.mosn.asyncmockwebserver;

import com.squareup.okhttp.mockwebserver.MockResponse;

public class Mock {

    public final String key;
    public final MockResponse response;
    public final MockDispatcher dispatcher;

    Mock(String key, MockResponse response, MockDispatcher dispatcher) {
        this.key = key;
        this.response = response;
        this.dispatcher = dispatcher;
    }

    public static Mock create(String key, MockResponse response, MockDispatcher mockDispatcher) {
        return new Mock(key, response, mockDispatcher);
    }
}
