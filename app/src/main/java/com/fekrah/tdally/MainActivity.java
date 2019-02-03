package com.fekrah.tdally;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.activities.AboutUsActivity;
import com.fekrah.tdally.activities.AddAdsActivity;
import com.fekrah.tdally.activities.ContactUsActivity;
import com.fekrah.tdally.activities.LoginActivity;
import com.fekrah.tdally.activities.MyAdsActivity;
import com.fekrah.tdally.activities.RegisterActivity;
import com.fekrah.tdally.activities.SearchActivity;
import com.fekrah.tdally.activities.SplashActivity;
import com.fekrah.tdally.activities.TermsAndConditions;
import com.fekrah.tdally.activities.TransferCommitionActivity;
import com.fekrah.tdally.adapters.CategoryAdapter;
import com.fekrah.tdally.adapters.SubCategoryAdapter;
import com.fekrah.tdally.fragments.AllAdsFragment;
import com.fekrah.tdally.helper.Constants;
import com.fekrah.tdally.helper.SharedHelper;
import com.fekrah.tdally.models.BunnersResponse;
import com.fekrah.tdally.models.CategoriesResponse;
import com.fekrah.tdally.models.Category;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.google.android.gms.common.api.Api;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DiscreteScrollView.ScrollStateChangeListener<CategoryAdapter.ViewHolder>,
        DiscreteScrollView.OnItemChangedListener<CategoryAdapter.ViewHolder>,
        View.OnClickListener,AllAdsFragment.ShowAllAdsListeneer {

    FragmentManager fragmentManager = getSupportFragmentManager();
    AllAdsFragment allAdsFragment = new AllAdsFragment();
    SliderLayout sliderLayout;
    View mainView;
    private int imagesNum = 3;
    List<String> imagesList;

    private List<Category> adList;

    @BindView(R.id.unit_ad_picker)
    DiscreteScrollView unitAdPicker;

    @BindView(R.id.imgNext)
    ImageView next;
    @BindView(R.id.imgPrev)
    ImageView previus;

    @BindView(R.id.sub_recycler_view)
    RecyclerView subCategoryRecyclerView;

    LinearLayoutManager subCategoryLayoutManager;
    SubCategoryAdapter subCategoryAdapter;

    boolean mainViewShowed = true;

    View langDialog;

    Button arabic;

    Button english;

    public static boolean allAds=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setLangDialog();
        fragmentManager.beginTransaction().add(R.id.all_container, allAdsFragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav = navigationView.getHeaderView(0);
        SimpleDraweeView profileImage  = nav.findViewById(R.id.profile_image);
        TextView profileName= nav.findViewById(R.id.user_name);
        profileImage.setImageURI(SharedHelper.getKey(this,Constants.IMAGES_BASE_URL+LoginActivity.USER_IMAGE));
        profileName.setText(SharedHelper.getKey(this,LoginActivity.USER_NAME));
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.DROP); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getBunners().enqueue(new Callback<BunnersResponse>() {
            @Override
            public void onResponse(Call<BunnersResponse> call, Response<BunnersResponse> response) {
                if (response.body().getStatus()){
                    imagesList = new ArrayList<>();
                    for (BunnersResponse.Data dat :response.body().getData()){
                        imagesList.add(Constants.IMAGES_BASE_URL+dat.getMessage());
                    }
                    setSliderViews();
                }
            }

            @Override
            public void onFailure(Call<BunnersResponse> call, Throwable t) {

            }
        });
        categoryView();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.action_all){
                    allAds=true;
                    if (menuItem.getItemId()==R.id.action_all){

                        if (mainViewShowed){
                            mainViewShowed = false;
                            findViewById(R.id.all_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.main_view).setVisibility(View.GONE);

                        }else {
                            mainViewShowed = true;
                            findViewById(R.id.all_container).setVisibility(View.GONE);
                            findViewById(R.id.main_view).setVisibility(View.VISIBLE);

                        }
                        invalidateOptionsMenu();
                    }
                }else if (menuItem.getItemId()==R.id.action_share){
                    shareApp();
                }else if (menuItem.getItemId()==R.id.action_search){
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.action_all){
                    if (menuItem.getItemId()==R.id.action_all){
                        allAds=true;
                        if (mainViewShowed){
                            mainViewShowed = false;
                           findViewById(R.id.all_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.main_view).setVisibility(View.GONE);

                        }else {
                            mainViewShowed = true;
                            findViewById(R.id.all_container).setVisibility(View.GONE);
                            findViewById(R.id.main_view).setVisibility(View.VISIBLE);

                        }
                    }
                }else if (menuItem.getItemId()==R.id.action_share){
                    shareApp();
                }else if (menuItem.getItemId()==R.id.action_search){
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                }
                invalidateOptionsMenu();
                return true;
            }
        });

        subCategoryLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        subCategoryRecyclerView.setLayoutManager(subCategoryLayoutManager);

    }

   List< CategoriesResponse.Data> categories;

    void categoryView() {
        unitAdPicker.addOnItemChangedListener(this);
        unitAdPicker.addScrollStateChangeListener(this);
        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getCategory("123456","ar").enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.body()!=null){
                    unitAdPicker.setSlideOnFling(true);
                    categories = response.body().getData();
                    unitAdPicker.setAdapter(new CategoryAdapter(categories, MainActivity.this));

                    unitAdPicker.scrollToPosition(0);
                    unitAdPicker.setItemTransitionTimeMillis(150);
                    unitAdPicker.setItemTransformer(new ScaleTransformer.Builder()
                            .setMinScale(0.8f)
                            .build());

                    previus.setVisibility(View.GONE);

                    subCategoryRecyclerView.setAdapter(new SubCategoryAdapter(categories.get(0).getSubCategory(),MainActivity.this));
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {

            }
        });

    }

    private void setSliderViews() {

        for (int i = 0; i < imagesNum; i++) {

            SliderView sliderView = new SliderView(getApplicationContext());
            sliderView.setImageUrl(imagesList.get(i));
//            switch (i) {
//                case 0:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//                case 1:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//                case 2:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
//                    break;
//                case 3:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//            }


            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            // sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(getApplicationContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (langView.getVisibility()==View.VISIBLE){
            langView.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }

    MenuItem back;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        back = menu.findItem(R.id.action_settings);
        if (mainViewShowed){
            back.setVisible(false);
        }else {
            back.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mainViewShowed = true;
            findViewById(R.id.all_container).setVisibility(View.GONE);
            findViewById(R.id.main_view).setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addAds) {

            startActivity(new Intent(this,AddAdsActivity.class));
            // Handle the camera action
        } else if (id == R.id.register) {
            startActivity(new Intent(this,RegisterActivity.class));
        } else if (id == R.id.myAds) {
            allAds=false;
            startActivity(new Intent(this,MyAdsActivity.class));
        } else if (id == R.id.transMoney) {
            startActivity(new Intent(this,TransferCommitionActivity.class));
        } else if (id == R.id.contactUs) {
            startActivity(new Intent(this,ContactUsActivity.class));
        } else if (id == R.id.change_language) {
            langView.setVisibility(View.VISIBLE);
            animation2();
        }else if (id == R.id.aboutApp) {
            startActivity(new Intent(this,AboutUsActivity.class));
        }else if (id == R.id.rules) {
            startActivity(new Intent(this, TermsAndConditions.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCurrentItemChanged(@Nullable CategoryAdapter.ViewHolder viewHolder, int i) {



    }

    @Override
    public void onScrollStart(@NonNull CategoryAdapter.ViewHolder currentItemHolder, int i) {


    }

    @Override
    public void onScrollEnd(@NonNull CategoryAdapter.ViewHolder currentItemHolder, int i) {
        currentPosition = i;
        subCategoryRecyclerView.setAdapter(new SubCategoryAdapter(categories.get(i).getSubCategory(),MainActivity.this));
        if (currentPosition == 0) {

            previus.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        } else if (categories.size() - 1 == currentPosition) {
            next.setVisibility(View.GONE);
            previus.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
            previus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onScroll(float position, int i,
                         int newIndex, @Nullable CategoryAdapter.ViewHolder currentHolder,
                         @Nullable CategoryAdapter.ViewHolder newCurrent) {

    }

    int currentPosition = 0;

    @OnClick(R.id.imgNext)
    void setNext() {
        if (currentPosition < categories.size()) {

            unitAdPicker.scrollToPosition(currentPosition + 1);
            subCategoryRecyclerView.setAdapter(new SubCategoryAdapter(categories.get(currentPosition+1).getSubCategory(),MainActivity.this));
            unitAdPicker.startViewTransition(unitAdPicker);
            currentPosition = currentPosition + 1;
            if (currentPosition == 0) {
                previus.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            } else if (categories.size() - 1 == currentPosition) {
                next.setVisibility(View.GONE);
                previus.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
                previus.setVisibility(View.VISIBLE);
            }

        }
    }

    @OnClick(R.id.imgPrev)
    void setPrevius() {
        if (currentPosition > 0) {

            unitAdPicker.scrollToPosition(currentPosition - 1);
            subCategoryRecyclerView.setAdapter(new SubCategoryAdapter(categories.get(currentPosition-1).getSubCategory(),MainActivity.this));
            currentPosition = currentPosition - 1;
            if (currentPosition == 0) {

                previus.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            } else if (categories.size() - 1 == currentPosition) {
                next.setVisibility(View.GONE);
                previus.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
                previus.setVisibility(View.VISIBLE);
            }
        }
    }

    void checkShopKind(int i) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_fad_in);
        a.reset();
        if (i==0){
            setAllUnVisible();
          showShopKind(R.id.markat,a);
        }else if (i==1){
            setAllUnVisible();
            showShopKind(R.id.families,a);
        }else if (i==2){
            setAllUnVisible();
            showShopKind(R.id.sweet_savory,a);
        }else if (i==3){
            setAllUnVisible();
            showShopKind(R.id.technology,a);
        }else {
            setAllUnVisible();
        }


    }

    void showShopKind(int id,Animation a){
       LinearLayout linearLayout = findViewById(id);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(a);
    }
    void setAllUnVisible(){
        findViewById(R.id.markat).setVisibility(View.GONE);
        findViewById(R.id.families).setVisibility(View.GONE);
        findViewById(R.id.technology).setVisibility(View.GONE);
        findViewById(R.id.sweet_savory).setVisibility(View.GONE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void setLangDialog(){
        langDialog =  findViewById(R.id.dialog);
        langView = findViewById(R.id.chang_lang_bg);
        langView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
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
        langDialog.setVisibility(View.VISIBLE);
        langDialog.clearAnimation();
        langDialog.startAnimation(anim);
    }

    RelativeLayout langView;

    void shareApp(){
        String shareBody = "مشاركة تطبيق تدللى";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "مشاركة تطبيق تدللى");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
    }
}
