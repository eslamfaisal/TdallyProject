package com.fekrah.tdally.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fekrah.tdally.R;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class AdDetailsActivity extends AppCompatActivity {

    List<String> imagesList;
    SliderLayout sliderLayout;
    private int imagesNum = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details_actvity);
        setSliderViews();
    }

    private void setSliderViews() {

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.WORM); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        imagesList = new ArrayList<>();
        imagesList.add("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        imagesList.add("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        imagesList.add("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");


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

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
