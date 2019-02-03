package com.fekrah.tdally.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.Constants;
import com.fekrah.tdally.models.CategoriesResponse;
import com.fekrah.tdally.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.ViewHolder>{

    private RecyclerView unitAdsRecyclerView;
    private List<CategoriesResponse.Data> data;
    Activity context;

    public CategoryAdapter(List<CategoriesResponse.Data> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        unitAdsRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_horizontal_scroll_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final CategoriesResponse.Data category = data.get(i);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitAdsRecyclerView.smoothScrollToPosition(i);
                Toast.makeText(context, category.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.name.setText(category.getName());
        holder.image.setImageURI(Constants.IMAGES_BASE_URL +category.getImage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView image;
        TextView name;
        View mainView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainView = itemView;
            image = mainView.findViewById(R.id.unit_image);
            name = mainView.findViewById(R.id.unit_name);

        }


    }
}
