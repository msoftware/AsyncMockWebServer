package com.mosn.asyncmockwebserver.example.ui.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.mosn.asyncmockwebserver.example.ITunesSearchApplication;
import com.mosn.asyncmockwebserver.example.R;


public class SearchContentsActivity extends AppCompatActivity {

    SearchContentsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_contents_activity);

        presenter = new SearchContentsPresenter((ITunesSearchApplication) getApplicationContext());
        presenter.bindView(findViewById(R.id.search_contents_view));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.createOptionMenu(getMenuInflater(), menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
        presenter = null;
    }
}
