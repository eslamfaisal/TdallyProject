package com.fekrah.tdally.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.adapters.BanksSpinnerAdapter;
import com.fekrah.tdally.helper.SharedHelper;
import com.fekrah.tdally.models.BanksResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferCommitionActivity extends AppCompatActivity {

    @BindView(R.id.banks_spinner)
    Spinner product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_commition);
        ButterKnife.bind(this);
        Apis api = BaseClient.getBaseClient().create(Apis.class);
        api.getBanks().enqueue(new Callback<BanksResponse>() {
            @Override
            public void onResponse(Call<BanksResponse> call, final Response<BanksResponse> response) {

                if (response.body() != null) {

                    ArrayAdapter<String> myAdapter = new BanksSpinnerAdapter(
                            getApplicationContext(), response.body().getData());
                    product.setAdapter(myAdapter);

                    product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BanksResponse> call, Throwable t) {
                Log.d("eeeeeeeeeeeeeee", "onFailure: " + t.getMessage());
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    private void getProducts(Api api) {


    }

}
