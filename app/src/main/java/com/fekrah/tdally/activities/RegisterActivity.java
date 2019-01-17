package com.fekrah.tdally.activities;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.fekrah.tdally.R;

import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    boolean expanded;
    BottomSheetBehavior termsSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        RelativeLayout llBottomSheet = findViewById(R.id.terms_sheet);
        termsSheet = BottomSheetBehavior.from(llBottomSheet);
    }

    public void showTerms(View view) {
        expanded = true;
        termsSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        if (expanded) {
            termsSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            expanded = false;
        } else
            super.onBackPressed();
    }

    public void back(View view) {
        onBackPressed();
    }
}
