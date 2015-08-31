package com.mosn.asyncmockwebserver;

import android.os.Handler;

import java.lang.reflect.Field;

public class ReflectionAsyncMockWebServer {

    ReflectionAsyncMockWebServer() {
    }

    AsyncMockWebServer getAsyncMockWebServer() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServer.class, "server");
        return (AsyncMockWebServer) field.get(null);
    }

    AsyncMockWebServerThread getAsyncMockWebServerThread() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServer.class, "thread");
        return (AsyncMockWebServerThread) field.get(getAsyncMockWebServer());
    }

    Handler getAsyncMockWebServerThreadHandler() throws Exception {
        Field field = ReflectionUtils.getPrivateField(AsyncMockWebServer.class, "threadHandler");
        return (Handler) field.get(getAsyncMockWebServer());
    }
}
