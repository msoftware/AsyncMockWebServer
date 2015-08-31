package com.mosn.asyncmockwebserver;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mosn.asyncmockwebserver.utils.MessageUtils;

public final class AsyncMockWebServer implements Handler.Callback {

    private static final AsyncMockWebServer server = new AsyncMockWebServer();

    private static final String TAG = AsyncMockWebServer.class.getSimpleName();

    private AsyncMockWebServerThread thread;
    @NonNull private AsyncMockWebServerListener listener = new AsyncMockWebServerListener() {
        @Override public void onStarted(String endpoint) {}
        @Override public void onStopped() {}
    };
    private Handler threadHandler;
    @Nullable private String endPoint;


    private AsyncMockWebServer() {
        createThread();
    }



    // =============================================
    // static method.
    // =============================================

    public static void init() {
        server.createThread();
    }

    public static void destroy() {
        server.destroyThread();
    }

    public static void start() {
        start(null, null);
    }

    public static void start(@Nullable MockWebServerConfig config) {
        start(config, null);
    }

    public static void start(@Nullable AsyncMockWebServerListener listener) {
        start(null, listener);
    }

    public static void start(@Nullable MockWebServerConfig config, @Nullable AsyncMockWebServerListener listener) {
        setListener(listener);
        server.sendStartCommand(config);
    }

    public static void stop() {
        server.sendStopCommand();
    }

    @Nullable
    public static String getEndPoint() {
        return server.endPoint;
    }

    public static AsyncMockWebServer addMock(@NonNull Mock mock) {
        server.sendAddMockCommand(mock);
        return server;
    }

    public static void deleteMock(@NonNull String key) {
        server.sendDeleteMockCommand(key);
    }

    public static void clearMock() {
        server.sendClearMockCommand();
    }

    public static void setListener(@Nullable AsyncMockWebServerListener listener) {
        if (listener == null) return;
        server.listener = listener;
    }



    // ==================================================
    // instance method.
    // ==================================================

    private void createThread() {
        if (existThread()) return;

        thread = new AsyncMockWebServerThread(
                AsyncMockWebServer.class.getSimpleName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT,
                Looper.myLooper(), this);
        thread.start();
        threadHandler = new Handler(thread.getLooper(), thread);
    }

    private void destroyThread() {
        if (!existThread()) return;

        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    thread.quitSafely();
                } else {
                    thread.quit();
                }
                threadHandler = null;
                thread = null;
            }
        });
    }

    private boolean existThread() {
        return thread != null && threadHandler != null;
    }

    private void sendStartCommand(@Nullable MockWebServerConfig config) {
        if (!existThread()) return;
        Message message = MessageUtils.obtain(
                threadHandler, config, AsyncMockWebServerThread.COMMAND_START);
        threadHandler.sendMessage(message);
    }

    private void sendStopCommand() {
        if (!existThread()) return;
        threadHandler.sendEmptyMessage(AsyncMockWebServerThread.COMMAND_STOP);
    }

    private void sendClearMockCommand() {
        if (!existThread()) return;
        threadHandler.sendEmptyMessage(AsyncMockWebServerThread.COMMAND_CLEAR_MOCK);
    }

    private void sendAddMockCommand(@NonNull Mock mock) {
        if (!existThread()) return;
        Message msg = MessageUtils.obtain(
                threadHandler, mock, AsyncMockWebServerThread.COMMAND_ADD_MOCK);
        threadHandler.sendMessage(msg);
    }

    private void sendDeleteMockCommand(@NonNull String key) {
        if (!existThread()) return;
        Message msg = MessageUtils.obtain(
                threadHandler, key, AsyncMockWebServerThread.COMMAND_DELETE_MOCK);
        threadHandler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case AsyncMockWebServerThread.EVENT_STARTED:
                endPoint = (String) msg.obj;
                if (BuildConfig.DEBUG) Log.d(TAG, "AsyncMockWebServer Started. endPoint:" + endPoint);
                listener.onStarted(endPoint);
                return true;

            case AsyncMockWebServerThread.EVENT_STOPPED:
                if (BuildConfig.DEBUG) Log.d(TAG, "AsyncMockWebServer Stopped.");
                listener.onStopped();
                return true;
        }
        return false;
    }
}
