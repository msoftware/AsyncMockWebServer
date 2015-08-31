# AsyncMockWebServer

[MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) is useful library for unit test.

However it's can use for unit test only. And if run the MockWebServer from UI thread, an NetworkOnMainThreadException occur.

If used the AsyncMockWebServer you can run the [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) in running app.

## Usage

Usage is easy. Call `AsyncMockWebServer.start()` at `Application#onCreate`

```java
public class ITunesSearchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AsyncMockWebServer.start(new AsyncMockWebServerListener() {
            @Override
            public void onStarted(String endpoint) {
                ITunesSearchMock.setupMockSearchApi(ITunesSearchApplication.this);
            }

            @Override
            public void onStopped() {}
        });
    }
}
```

After the server start, `AsyncMockWebServerListener#onStarted(String endpoint)` will be called back. You can add mock.

Needs `com.squareup.okhttp.mockwebserver.MockResponse` , `Dispatcher` to mock data.

What is `MockResponse` and `Dispatcher`? Please see [this page](https://github.com/square/okhttp/tree/master/mockwebserver#api).

```java
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
```

If call the HTTP request, you should rewrite to endpoint of AsyncMockWebServer.


```java
String asyncMockWebServerEndPoint = AsyncMockWebServer.getEndPoint();

RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
        .setEndpoint(asyncMockWebServerEndPoint);
```

## Licence

```
Copyright 2015 Shogo Mizumoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```