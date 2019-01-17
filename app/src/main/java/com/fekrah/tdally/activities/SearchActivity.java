package com.fekrah.tdally.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fekrah.tdally.R;

import org.cryse.widget.persistentsearch.PersistentSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.searchview)
    PersistentSearchView mSearchView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        this.setSupportActionBar(mToolbar);

    }

    MenuItem mSearchMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                if (mSearchMenuItem != null) {
                    openSearch();
                    return true;
                } else {
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }
    public void openSearch() {
        View menuItemView = findViewById(R.id.action_search);
        mSearchView.setStartPositionFromMenuItem(menuItemView);
        mSearchView.openSearch();
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearching()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


}
