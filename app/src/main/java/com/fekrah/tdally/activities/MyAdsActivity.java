package com.fekrah.tdally.activities;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fekrah.tdally.R;
import com.fekrah.tdally.fragments.AllAdsFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MyAdsActivity extends AppCompatActivity implements AllAdsFragment.ShowAllAdsListeneer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(this.getString(R.string.prev), AllAdsFragment.class)
                .add(this.getString(R.string.current), AllAdsFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
