package com.mosn.asyncmockwebserver.example.ui.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;

import com.mosn.asyncmockwebserver.example.ITunesSearchApplication;
import com.mosn.asyncmockwebserver.example.domain.model.Contents;
import com.mosn.asyncmockwebserver.example.domain.repository.ContentsRepository;
import com.mosn.asyncmockwebserver.example.domain.repository.callback.NetworkCallback;
import com.mosn.asyncmockwebserver.example.infrastructure.api.search.ContentsRepositoryImpl;
import com.mosn.asyncmockwebserver.example.ui.Presenter;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchContentsPresenter
        implements Presenter, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = SearchContentsPresenter.class.getSimpleName();

    @Nullable private SearchContentsView searchContentsView;

    private ContentsRepository repository;

    SearchContentsPresenter(@NonNull ITunesSearchApplication application) {
        repository = new ContentsRepositoryImpl(application);
    }

    @Override
    public void bindView(@NonNull View view) {
        searchContentsView = (SearchContentsView) view;
        searchContentsView.setOnItemClickListener(this);
    }

    @Override
    public void unbindView() {
        if (searchContentsView == null) return;
        searchContentsView.setOnItemClickListener(null);
        searchContentsView.setOnQueryTextListener(null);
        searchContentsView = null;
    }

    @Override
    public boolean isBinding() {
        return searchContentsView != null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // NOP.
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    void createOptionMenu(@NonNull MenuInflater inflater, @NonNull Menu menu) {
        if (searchContentsView == null) return;
        searchContentsView.createOptionsMenu(inflater, menu);
        searchContentsView.setOnQueryTextListener(this);
    }

    void search(final String keyword) {
        Log.d(TAG, "start search. keyword:" + keyword + " isBinding:" + isBinding() + " (" + searchContentsView + ")");

        if (searchContentsView == null) return;

        searchContentsView.startSearch();

        repository.search(keyword, new NetworkCallback<Contents>() {
            @Override
            public void onSuccess(Contents contents, Response response) {
                Log.d(TAG, "success search. keyword:" + keyword + " response:" + response);
                searchContentsView.stopSearch();
                searchContentsView.setSearchContents(contents);
            }

            @Override
            public void onFailure(RetrofitError error) {
                Log.d(TAG, "failure search. keyword:" + keyword + " error:" + error);
                searchContentsView.stopSearch();
                searchContentsView.occurredSearchError(keyword);
            }
        });
    }
}
