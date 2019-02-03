package com.fekrah.tdally.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.SharedHelper;
import com.fekrah.tdally.models.CategoriesResponse;
import com.fekrah.tdally.models.LoginResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_ADRESS = "ADRESS";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static String USER_LOCATION = "USER_LOCATION";

    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.password)
    EditText pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.login_btn)
    void loginBtn(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.logging));
        progressDialog.show();
        if (userName.getText().toString().equals("")|| pass.getText().toString().equals("")){
            Toast.makeText(this, "رجاء ادخال اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.logIn("123456",userName.getText().toString(),pass.getText().toString()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body()!=null){
                    if (response.body().getStatus()){
                        SharedHelper.putKey(getApplicationContext(), USER_NAME, response.body().getData().getName());
                        SharedHelper.putKey(getApplicationContext(), USER_ADRESS, response.body().getData().getAddress());
                        SharedHelper.putKey(getApplicationContext(), USER_IMAGE, response.body().getData().getImage());
                        SharedHelper.putKey(getApplicationContext(), USER_TOKEN, response.body().getData().getUserToken());
                        SharedHelper.putKey(getApplicationContext(), USER_PHONE, response.body().getData().getPhone());
                        SharedHelper.putKey(getApplicationContext(), USER_LOCATION, response.body().getData().getAddress());

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "اسم المستخدم او كلمة المرور خاطئ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goForget(View view) {
        startActivity(new Intent(getApplicationContext(),ResetPassActivity.class));
    }

    public void goRegister(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

    }

    public void goMain(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }
}
