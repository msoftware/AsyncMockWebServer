package com.mosn.asyncmockwebserver.example.infrastructure.api.search;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.mosn.asyncmockwebserver.example.ITunesSearchApplication;
import com.mosn.asyncmockwebserver.example.domain.model.Content;
import com.mosn.asyncmockwebserver.example.domain.model.Contents;
import com.mosn.asyncmockwebserver.example.domain.repository.ContentsRepository;
import com.mosn.asyncmockwebserver.example.domain.repository.callback.NetworkCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ContentsRepositoryImpl implements ContentsRepository {

    @Inject
    RestAdapter.Builder restAdapterBuilder;

    public ContentsRepositoryImpl(ITunesSearchApplication application) {
        application.getComponent().inject(this);
    }

    @Override
    public void search(final String keyword, @NonNull final NetworkCallback<Contents> callback) {

        restAdapterBuilder.build().create(SearchApi.class).search(keyword, new Callback<SearchApiDto>() {
            final Handler mainHandler = new Handler(Looper.getMainLooper());
            @Override
            public void success(final SearchApiDto dto, final Response response) {

                final Contents contents = Contents.create(keyword);
                contents.contents.addAll(convertContentList(new ArrayList<Content>(), dto.results));

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(contents, response);
                    }
                });
            }

            @Override
            public void failure(final RetrofitError error) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(error);
                    }
                });
            }
        });
    }

    private List<Content> convertContentList(@NonNull List<Content> contents, @NonNull List<SearchApiDto.Result> results) {
        if (contents.size() >= results.size()) {
            return contents;
        } else {
            SearchApiDto.Result result = results.get(contents.size());
            contents.add(generateContent(result));
            return convertContentList(contents, results);
        }
    }

    private Content generateContent(SearchApiDto.Result result) {
        Content content = Content.create(result.artistId, result.collectionId, result.trackId);
        content.wrapperType = result.wrapperType;
        content.kind = result.kind;
        content.artistId = result.artistId;
        content.trackId = result.trackId;
        content.collectionId = result.collectionId;
        content.artistName = result.artistName;
        content.collectionName = result.collectionCensoredName;
        content.trackName = result.trackName;
        content.artworkUrl100 = result.artworkUrl100;
        content.primaryGenreName = result.primaryGenreName;
        return content;
    }
}
