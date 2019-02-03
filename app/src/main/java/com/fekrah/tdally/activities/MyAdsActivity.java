package com.fekrah.tdally.activities;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fekrah.tdally.R;
import com.fekrah.tdally.adapters.AdsAdapter;
import com.fekrah.tdally.fragments.AllAdsFragment;
import com.fekrah.tdally.helper.SharedHelper;
import com.fekrah.tdally.models.Ad;
import com.fekrah.tdally.models.AllAdsResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdsActivity extends AppCompatActivity implements AllAdsFragment.ShowAllAdsListeneer {

    @BindView(R.id.all_ads)
    RecyclerView allAdsRecyclerView;

    LinearLayoutManager linearLayoutManager;
    List<Ad> ads;
    AdsAdapter adsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        ButterKnife.bind(this);

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getMyAds(SharedHelper.getKey(this,LoginActivity.USER_TOKEN),"all").enqueue(new Callback<AllAdsResponse>() {
            @Override
            public void onResponse(Call<AllAdsResponse> call, Response<AllAdsResponse> response) {
                if (response.body()!=null){
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    adsAdapter = new AdsAdapter(MyAdsActivity.this,response.body().getData());
                    allAdsRecyclerView.setLayoutManager(linearLayoutManager);
                    allAdsRecyclerView.setAdapter(adsAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsResponse> call, Throwable t) {

            }
        });


    }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add(this.getString(R.string.prev), AllAdsFragment.class)
//                .add(this.getString(R.string.current), AllAdsFragment.class)
//                .create());
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(adapter);
//
//        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
//        viewPagerTab.setViewPager(viewPager);



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
