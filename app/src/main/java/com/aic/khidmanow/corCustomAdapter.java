package com.aic.khidmanow;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import layout.fr_product_list;

public class corCustomAdapter extends RecyclerView.Adapter<corCustomAdapter.CustomViewHolder> {
    private int cuslayout;
    private Context context;
    private ArrayList<Album> items;

    public corCustomAdapter(FragmentActivity activity, ArrayList<Album> items, int reslayout) {
        this.context = activity;
        this.items = items;
        this.cuslayout = reslayout;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // holder.itemTitle.setText(items.get(position).getTitle());
        // holder.itemImage.setImageResource(items.get(position).getImage());
        final Album album = items.get(position);
        holder.itemTitle.setText(album.getTitle());
        //  myDB.showToast(ctx,album.getTitle());
        if (album.getUrl() != null) {
            Picasso.get()
                    .load(album.getUrl())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .into(holder.itemImage);
            Log.i("AlbumActivity", String.valueOf(album.getTitle()));
        }
        holder.itemImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle bundle = new Bundle();
                        bundle.putString("subcategory", album.getSubcategory());
                        Fragment myFragment = new fr_product_list();
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        Log.i("OnclickActivity", String.valueOf(album.getTitle()));
                    }
                }

        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {


        private ImageView itemImage;
        private TextView itemTitle;

        public CustomViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.item1_image);
            itemTitle = view.findViewById(R.id.item1_text);
        }
    }
}

