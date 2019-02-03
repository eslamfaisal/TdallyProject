package com.fekrah.tdally.activities;

import android.animation.Animator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fekrah.tdally.R;
import com.fekrah.tdally.adapters.AdsAdapter;
import com.fekrah.tdally.helper.InternalStorage;
import com.fekrah.tdally.helper.SampleSuggestionsBuilder;
import com.fekrah.tdally.helper.SimpleAnimationListener;
import com.fekrah.tdally.models.Ad;
import com.fekrah.tdally.models.AllAdsResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;

import org.cryse.widget.persistentsearch.DefaultVoiceRecognizerDelegate;
import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.VoiceRecognitionDelegate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.searchview)
    PersistentSearchView mSearchView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_search_tint)
    View mSearchTintView;

    @BindView(R.id.all_ads)
    RecyclerView allAdsRecyclerView;

    LinearLayoutManager linearLayoutManager;
    List<Ad> ads;
    AdsAdapter adsAdapter;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        this.setSupportActionBar(mToolbar);

        // setUpSearchView();
        VoiceRecognitionDelegate delegate = new DefaultVoiceRecognizerDelegate(this, VOICE_RECOGNITION_REQUEST_CODE);
        if (delegate.isVoiceRecognitionAvailable()) {
            mSearchView.setVoiceRecognitionDelegate(delegate);
        }
        // mSearchView.openSearch("Text Query");
        mSearchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {

            @Override
            public void onHomeButtonClick() {
                //Hamburger has been clicked
                onBackPressed();
            }

        });
        mSearchTintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.cancelEditing();
            }
        });
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this));
        mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {

//            @Override
//            public boolean onSuggestion(SearchItem searchItem) {
//                Log.d("onSuggestion", searchItem.getTitle());
//                return false;
//            }

            @Override
            public void onSearchEditOpened() {
                //Use this to tint the screen
                mSearchTintView.setVisibility(View.VISIBLE);
                mSearchTintView
                        .animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener())
                        .start();
            }

            @Override
            public void onSearchEditClosed() {
                //Use this to un-tint the screen
                mSearchTintView
                        .animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSearchTintView.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {

            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String string) {
                //   Toast.makeText(PlacesActivity.this, string + " Searched", Toast.LENGTH_LONG).show();
//                mRecyclerView.setVisibility(View.VISIBLE);
//                fillResultToRecyclerView(string);

                getSearch(string);

                try {
                    List<String> suggestions = (List<String>) InternalStorage.readObject(getApplicationContext(), "sug");
                    if (suggestions != null) {

                        if (!suggestions.contains(string)) {
                            Log.d("aaaa", "onSearch: yes added");
                            suggestions.add(0,string);
                            if (suggestions.size()>=6){
                                suggestions.remove(5);
                            }
                            InternalStorage.writeObject(getApplicationContext(), "sug", suggestions);
                        }else {
                            suggestions.remove(string);
                            suggestions.add(0,string);
                            if (suggestions.size()>=6){
                                suggestions.remove(5);
                            }
                            InternalStorage.writeObject(getApplicationContext(), "sug", suggestions);
                        }

                    }
                    Log.d("aaaa", "onSearch: tray");
                    mSearchView.setSuggestionBuilder(null);
                    mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(SearchActivity.this));


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("aaaa", "IOException: errore " + e.getMessage());
                    if (e.getMessage().contains("No such file or directory")){

                        List<String> suggestion = new ArrayList<>();
                        suggestion.add(0, string);
                        try {
                            InternalStorage.writeObject(getApplicationContext(), "sug", suggestion);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Log.d("aaaa", "onSearch: added");
                    }

                    Log.d("aaaa", "onSearch: tray");
                    mSearchView.setSuggestionBuilder(null);
                    mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(SearchActivity.this));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Log.d("aaaa", "ClassNotFoundException: " +e.getMessage());
                }



            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });


    }

    private void getSearch(String string) {

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getSearch(string).enqueue(new Callback<AllAdsResponse>() {
            @Override
            public void onResponse(Call<AllAdsResponse> call, Response<AllAdsResponse> response) {
                if (response.body()!=null){
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    adsAdapter = new AdsAdapter(SearchActivity.this,response.body().getData());
                    allAdsRecyclerView.setLayoutManager(linearLayoutManager);
                    allAdsRecyclerView.setAdapter(adsAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsResponse> call, Throwable t) {

            }
        });

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
