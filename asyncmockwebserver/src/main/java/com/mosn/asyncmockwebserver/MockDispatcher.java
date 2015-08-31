package com.mosn.asyncmockwebserver;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

public interface MockDispatcher {
    boolean isDispatch(RecordedRequest request);
}
