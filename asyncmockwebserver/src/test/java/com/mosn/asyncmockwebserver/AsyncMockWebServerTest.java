package com.mosn.asyncmockwebserver;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;

import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

@Config(sdk = 21, constants = BuildConfig.class)
@RunWith(RobolectricGradleTestRunner.class)
public class AsyncMockWebServerTest {

    private static final String TAG = AsyncMockWebServerTest.class.getSimpleName();

    ReflectionAsyncMockWebServer reflectionServer;
    ReflectionAsyncMockWebServerThread reflectionServerThread;

    public AsyncMockWebServerTest() throws Exception {
        reflectionServer = new ReflectionAsyncMockWebServer();
        reflectionServerThread = new ReflectionAsyncMockWebServerThread(reflectionServer.getAsyncMockWebServerThread());
    }

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    @Test
    public void testExistAsyncMockWebServerThread() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        assertThat(serverThread, notNullValue());
    }

    @Test
    public void testExistAsyncMockWebServerThreadHandler() throws Exception {
        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        assertThat(serverThreadHandler, notNullValue());
    }

    @Test
    public void testExistMockWebServerEndPoint() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);
        AsyncMockWebServer.start();
        ShadowLooper.unPauseLooper(serverThreadLooper);

        String serverEndPoint = AsyncMockWebServer.getEndPoint();
        Log.d(TAG, "ServerEndPoint:" + serverEndPoint);
        assertThat(serverEndPoint, not(isEmptyOrNullString()));
    }

    @Test
    public void testStartAsyncMockWebServer() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);

        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_START), is(false));
        AsyncMockWebServer.start();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_START), is(true));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        MockWebServer mockWebServer = reflectionServerThread.getMockWebServer();
        assertThat(mockWebServer, notNullValue());
    }

    @Test
    public void testStopAsyncMockWebServer() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);

        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        AsyncMockWebServer.start();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_STOP), is(false));
        AsyncMockWebServer.stop();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_STOP), is(true));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        MockWebServer mockWebServer = reflectionServerThread.getMockWebServer();
        assertThat(mockWebServer, nullValue());
    }

    @Test
    public void testAddMock() throws Exception {
        Map<String, Mock> mockMap = reflectionServerThread.getMockMap();
        assertThat(mockMap.size(), is(0));

        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);

        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_ADD_MOCK), is(false));
        AsyncMockWebServer.addMock(Mock.create("dummyKey", new MockResponse(), new MockDispatcher() {
            @Override
            public boolean isDispatch(RecordedRequest request) {
                return false;
            }
        }));
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_ADD_MOCK), is(true));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        assertThat(mockMap.size(), is(1));
    }

    @Test
    public void testDeleteMock() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);

        AsyncMockWebServer.clearMock();
        AsyncMockWebServer.addMock(Mock.create("dummyKey", new MockResponse(), new MockDispatcher() {
            @Override
            public boolean isDispatch(RecordedRequest request) {
                return false;
            }
        }));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        Map<String, Mock> mockMap = reflectionServerThread.getMockMap();
        assertThat(mockMap.size(), is(1));

        ShadowLooper.pauseLooper(serverThreadLooper);

        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_DELETE_MOCK), is(false));
        AsyncMockWebServer.deleteMock("dummyKey");
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_DELETE_MOCK), is(true));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        assertThat(mockMap.size(), is(0));
    }

    @Test
    public void testClearMock() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);

        AsyncMockWebServer.clearMock();
        for (int i = 0; i < 3; i++) {
            AsyncMockWebServer.addMock(Mock.create("dummyKey" + i, new MockResponse(), new MockDispatcher() {
                @Override
                public boolean isDispatch(RecordedRequest request) {
                    return false;
                }
            }));
        }

        ShadowLooper.unPauseLooper(serverThreadLooper);

        Map<String, Mock> mockMap = reflectionServerThread.getMockMap();
        assertThat(mockMap.size(), is(3));

        ShadowLooper.pauseLooper(serverThreadLooper);

        Handler serverThreadHandler = reflectionServer.getAsyncMockWebServerThreadHandler();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_CLEAR_MOCK), is(false));
        AsyncMockWebServer.clearMock();
        assertThat(serverThreadHandler.hasMessages(AsyncMockWebServerThread.COMMAND_CLEAR_MOCK), is(true));

        ShadowLooper.unPauseLooper(serverThreadLooper);

        assertThat(mockMap.size(), is(0));
    }

    @Test
    public void testRestartAsyncMockWebServer() throws Exception {
        AsyncMockWebServerThread serverThread = reflectionServer.getAsyncMockWebServerThread();
        Looper serverThreadLooper = serverThread.getLooper();

        ShadowLooper.pauseLooper(serverThreadLooper);
        AsyncMockWebServer.start();
        ShadowLooper.unPauseLooper(serverThreadLooper);

        String beforeServerEndPoint = AsyncMockWebServer.getEndPoint();
        assertThat(beforeServerEndPoint, not(isEmptyOrNullString()));

        ShadowLooper.pauseLooper(serverThreadLooper);
        AsyncMockWebServer.stop();
        AsyncMockWebServer.start();
        ShadowLooper.unPauseLooper(serverThreadLooper);

        String afterServerEndPoint = AsyncMockWebServer.getEndPoint();
        assertThat(afterServerEndPoint,  not(isEmptyOrNullString()));
        assertThat(beforeServerEndPoint, not(equalToIgnoringCase(afterServerEndPoint)));
    }
}
