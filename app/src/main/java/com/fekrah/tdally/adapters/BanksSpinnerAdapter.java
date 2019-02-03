package com.fekrah.tdally.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.Constants;
import com.fekrah.tdally.models.BanksResponse;

import java.util.List;

public class BanksSpinnerAdapter extends ArrayAdapter<String > {

    List<BanksResponse.Data>products;

    Context mContext;

    public BanksSpinnerAdapter(@NonNull Context context, List<BanksResponse.Data>products) {
        super(context, R.layout.layout_product_spinner_item);
        this.products = products;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_product_spinner_item, parent, false);
            mViewHolder.mFlag =  convertView.findViewById(R.id.image);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.title = (TextView) convertView.findViewById(R.id.title);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageURI(Constants.IMAGES_BASE_URL+products.get(position).getImage());
        mViewHolder.mName.setText(products.get(position).getName());
        mViewHolder.title.setText(products.get(position).getTitle());


        return convertView;
    }

    private static class ViewHolder {
        SimpleDraweeView mFlag;
        TextView mName;
        TextView title;

    }
}
