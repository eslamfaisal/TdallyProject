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
import android.widget.Toast;

import com.fekrah.tdally.R;
import com.fekrah.tdally.activities.AdsBySubCategoryActivity;
import com.fekrah.tdally.models.CategoriesResponse;
import com.rafakob.drawme.DrawMeButton;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    List<CategoriesResponse.SubCategory> subCategories;
    Activity context;

    public SubCategoryAdapter(List<CategoriesResponse.SubCategory> subCategories, Activity context) {
        this.subCategories = subCategories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_category_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final CategoriesResponse.SubCategory subCategory = subCategories.get(i);
        Animation a = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
        a.reset();
        viewHolder.button.setText(subCategory.getNameSubCategory());

        viewHolder.button.clearAnimation();
        viewHolder.button.startAnimation(a);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AdsBySubCategoryActivity.class);
                intent.putExtra("sub_id",subCategory.getUbcCategoryId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        DrawMeButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.sub_cat_item_btn);
        }
    }
}
