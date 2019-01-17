package com.fekrah.tdally.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.SharedHelper;

import java.util.Locale;

import butterknife.BindView;

public class SplashScreen extends AppCompatActivity {

    View langDialog;

    Button arabic;

    Button english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (SharedHelper.getKey(this,"lang").equals("ar")||SharedHelper.getKey(this,"lang").equals("ar")){
            startActivity(new Intent(getApplicationContext(),SplashActivity.class));
            finish();
        }else {
           langDialog =  findViewById(R.id.dialog);
            animation2();

            arabic = langDialog.findViewById(R.id.arabic);
            english = langDialog.findViewById(R.id.english);

            arabic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("ar");
                    startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                    finish();
                }
            });
            english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("en");
                    startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                    finish();
                }
            });
        }



    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }

    private void animation2() {
        langDialog.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);

        langDialog.startAnimation(anim);
    }
}
