package com.fekrah.tdally.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.activities.AdDetailsActivity;
import com.fekrah.tdally.helper.Constants;
import com.fekrah.tdally.models.Ad;
import com.fekrah.tdally.models.AllAdsResponse;

import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter <AdsAdapter.ViewHoldr>{

    Activity context;
    List<AllAdsResponse.Data> ads;

    public AdsAdapter(Activity context, List<AllAdsResponse.Data> ads) {
        this.context = context;
        this.ads = ads;
    }

    @NonNull
    @Override
    public ViewHoldr onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHoldr(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_item,viewGroup,false));
    }
   private int lastPosition=-1;
    @Override
    public void onBindViewHolder(@NonNull ViewHoldr holder, int position) {

        AllAdsResponse.Data ad = ads.get(position);

        if (MainActivity.allAds){
            holder.updateAd.setVisibility(View.GONE);
        }else {
            holder.updateAd.setVisibility(View.VISIBLE);
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,AdDetailsActivity.class));
            }
        });
        holder.name.setText(ad.getName());
        holder.date.setText(ad.getDate());
        holder.price.setText(ad.getPrice());
        holder.distance.setText(ad.getLocation());

        holder.image.setImageURI(Constants.IMAGES_BASE_URL+ad.getImage());

        if (ad.getType_ad().equals("جديد")){
            holder.statusNew.setVisibility(View.VISIBLE);
            holder.statusUsed.setVisibility(View.GONE);
        }else {
            holder.statusNew.setVisibility(View.GONE);
            holder.statusUsed.setVisibility(View.VISIBLE);
        }
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class ViewHoldr extends RecyclerView.ViewHolder {
        View mainView,updateAd;
        SimpleDraweeView image;
        TextView price,name,distance,date,views,statusNew,statusUsed;
        public ViewHoldr(@NonNull View itemView) {
            super(itemView);
            mainView = itemView;
            updateAd= mainView.findViewById(R.id.updateAd);
            image = mainView.findViewById(R.id.ad_image);
            name= mainView.findViewById(R.id.add_name);
            price = mainView.findViewById(R.id.price);
            views = mainView.findViewById(R.id.viewers);
            distance = mainView.findViewById(R.id.distance);
            date= mainView.findViewById(R.id.date);
            statusNew = mainView.findViewById(R.id.add_status);
            statusUsed = mainView.findViewById(R.id.state_used);
        }
    }
}
