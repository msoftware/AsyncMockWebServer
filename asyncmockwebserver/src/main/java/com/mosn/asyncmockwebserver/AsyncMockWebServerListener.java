package com.mosn.asyncmockwebserver;

public interface AsyncMockWebServerListener {
    void onStarted(String endpoint);
    void onStopped();
}
