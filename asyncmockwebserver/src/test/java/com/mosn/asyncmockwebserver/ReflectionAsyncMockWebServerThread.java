package com.mosn.asyncmockwebserver;

import android.os.Handler;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionAsyncMockWebServerThread {

    private AsyncMockWebServerThread serverThread;

    ReflectionAsyncMockWebServerThread(AsyncMockWebServerThread serverThread) {
        this.serverThread = serverThread;
    }

    MockWebServer getMockWebServer() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServerThread.class, "mockWebServer");
        return (MockWebServer) field.get(serverThread);
    }

    Handler getHandler() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServerThread.class, "serverHandler");
        return (Handler) field.get(serverThread);
    }

    @SuppressWarnings("unchecked")
    Map<String, Mock> getMockMap() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServerThread.class, "mockMap");
        return (Map<String, Mock>) field.get(serverThread);
    }

    Dispatcher getDispatcher() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServerThread.class, "dispatcher");
        return (Dispatcher) field.get(serverThread);
    }
}
