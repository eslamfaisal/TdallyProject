package com.fekrah.tdally.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
