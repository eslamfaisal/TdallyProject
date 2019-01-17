package com.fekrah.tdally.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
