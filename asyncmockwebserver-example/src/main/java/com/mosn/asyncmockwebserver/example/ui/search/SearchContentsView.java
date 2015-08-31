package com.mosn.asyncmockwebserver.example.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mosn.asyncmockwebserver.example.R;
import com.mosn.asyncmockwebserver.example.domain.model.Contents;
import com.mosn.asyncmockwebserver.example.utils.ToastUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchContentsView extends FrameLayout {

    @Bind(R.id.search_contents_toolbar) protected Toolbar toolbar;
    @Bind(R.id.search_contents_listview) protected ListView contentsListView;
    @Bind(R.id.search_contents_progress) protected ProgressWheel progressWheel;
    @Bind(R.id.search_contents_search_status_textview) protected TextView searchStatusTextView;
    protected SearchView searchView;

    public SearchContentsView(Context context) {
        super(context);
    }

    public SearchContentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchContentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        contentsListView.setEmptyView(searchStatusTextView);

        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);

        stopSearch();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
        searchView.setOnQueryTextListener(null);
        searchView = null;
    }

    void createOptionsMenu(@NonNull MenuInflater inflater, @NonNull Menu menu) {
        inflater.inflate(R.menu.search_contents_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSubmitButtonEnabled(false);
    }

    void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener onItemClickListener) {
        if (contentsListView == null) return;
        contentsListView.setOnItemClickListener(onItemClickListener);
    }

    void setOnQueryTextListener(@Nullable SearchView.OnQueryTextListener onQueryTextListener) {
        if (searchView == null) return;
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    void startSearch() {
        if (contentsListView == null || progressWheel == null || searchView == null) return;

        contentsListView.setAdapter(null);
        searchStatusTextView.setText(R.string.search_contents_loading_text);
        progressWheel.spin();
        searchView.clearFocus();
    }

    void stopSearch() {
        if (searchStatusTextView == null || progressWheel == null) return;

        searchStatusTextView.setText(R.string.search_contents_empty_text);
        progressWheel.stopSpinning();
    }

    void occurredSearchError(String keyword) {
        ToastUtils.show(getContext(), "occurred search error. keyword:" + keyword);
    }

    void setSearchContents(@NonNull Contents contents) {
        if (contentsListView == null) return;

        if (contents.contents.size() == 0) {
            ToastUtils.show(getContext(), "Not find content. keyword:" + contents.searchKeyword);
            return;
        }

        SearchContentsAdapter adapter = new SearchContentsAdapter(getContext(), contents);
        contentsListView.setAdapter(adapter);
    }
}
