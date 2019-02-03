package com.fekrah.tdally.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.models.TermsResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditions extends AppCompatActivity {

    @BindView(R.id.progBar)
    ProgressBar loading;

    @BindView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.contactUs("123","ar","terms and conditions").enqueue(new Callback<TermsResponse>() {
            @Override
            public void onResponse(Call<TermsResponse> call, Response<TermsResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.body()!=null){
                    content.setText(response.body().getData().getDescription());
                }
            }

            @Override
            public void onFailure(Call<TermsResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                content.setText(t.getMessage());
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
