package com.fekrah.tdally.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fekrah.tdally.R;
import com.fekrah.tdally.adapters.AdsAdapter;
import com.fekrah.tdally.models.Ad;
import com.fekrah.tdally.models.AllAdsResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.google.android.gms.common.api.Api;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsBySubCategoryActivity extends AppCompatActivity {


    @BindView(R.id.all_ads)
    RecyclerView allAdsRecyclerView;

    LinearLayoutManager linearLayoutManager;
    List<Ad> ads;
    AdsAdapter adsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_by_sub_category);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String sub_key = intent.getStringExtra("sub_id");


        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getAdsBySub(sub_key,"sub_category").enqueue(new Callback<AllAdsResponse>() {
            @Override
            public void onResponse(Call<AllAdsResponse> call, Response<AllAdsResponse> response) {
                if (response.body()!=null){
                    linearLayoutManager = new LinearLayoutManager(AdsBySubCategoryActivity.this);
                    adsAdapter = new AdsAdapter(AdsBySubCategoryActivity.this,response.body().getData());
                    allAdsRecyclerView.setLayoutManager(linearLayoutManager);
                    allAdsRecyclerView.setAdapter(adsAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsResponse> call, Throwable t) {

            }
        });

    }
}
