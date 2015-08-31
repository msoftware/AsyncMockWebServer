package com.mosn.asyncmockwebserver;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.mosn.asyncmockwebserver.utils.MessageUtils;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

final class AsyncMockWebServerThread extends HandlerThread implements Handler.Callback {

    static final int COMMAND_START       = 101;
    static final int COMMAND_STOP        = 102;
    static final int COMMAND_ADD_MOCK    = 201;
    static final int COMMAND_DELETE_MOCK = 202;
    static final int COMMAND_CLEAR_MOCK  = 203;

    static final int EVENT_STARTED = 1001;
    static final int EVENT_STOPPED = 1002;

    @Nullable private MockWebServer mockWebServer;
    private final Handler serverHandler;
    private final Map<String, Mock> mockMap = new HashMap<>();
    private final Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            for (Map.Entry<String, Mock> entry : mockMap.entrySet()) {
                Mock mock = entry.getValue();
                if (mock.dispatcher.isDispatch(request)) {
                    return mock.response;
                }
            }
            return null;
        }
    };

    AsyncMockWebServerThread(
            String name, int priority, Looper looper, Handler.Callback callback) {
        super(name, priority);
        serverHandler = new Handler(looper, callback);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case COMMAND_START:
                commandStart(msg);
                return true;

            case COMMAND_STOP:
                commandStop();
                return true;

            case COMMAND_ADD_MOCK:
                commandAddMock(msg);
                return true;

            case COMMAND_DELETE_MOCK:
                commandDeleteMock(msg);
                return true;

            case COMMAND_CLEAR_MOCK:
                commandClearMock();
                return true;
        }
        return false;
    }

    private void commandStart(Message msg) {
        if (mockWebServer != null) return;

        mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(dispatcher);

        if (msg.obj != null && msg.obj instanceof MockWebServerConfig) {
            MockWebServerConfig config = (MockWebServerConfig) msg.obj;
            mockWebServer.setBodyLimit(config.maxBodyLength);
            mockWebServer.setProtocols(config.protocols);
            mockWebServer.setProtocolNegotiationEnabled(config.protocolNegotiationEnabled);
            if (config.serverSocketFactory != null) {
                mockWebServer.setServerSocketFactory(config.serverSocketFactory);
            }
        }

        try {
            mockWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            mockWebServer = null;
            return;
        }

        URL url = mockWebServer.getUrl("");
        Message notifyMessage = MessageUtils.obtain(serverHandler, url.toString(), EVENT_STARTED);
        serverHandler.sendMessage(notifyMessage);
    }

    private void commandStop() {
        if (mockWebServer == null) return;

        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mockWebServer = null;

        serverHandler.sendEmptyMessage(EVENT_STOPPED);
    }

    private void commandAddMock(Message msg) {
        if (msg.obj == null || !(msg.obj instanceof Mock)) return;
        Mock mock = (Mock) msg.obj;
        String key = mock.key;
        mockMap.put(key, mock);
    }

    private void commandDeleteMock(Message msg) {
        if (msg.obj == null || !(msg.obj instanceof String)) return;
        String key = (String) msg.obj;
        mockMap.remove(key);
    }

    private void commandClearMock() {
        mockMap.clear();
    }
}
