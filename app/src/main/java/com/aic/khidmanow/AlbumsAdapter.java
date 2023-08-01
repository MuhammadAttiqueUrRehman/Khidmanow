package com.aic.khidmanow;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.squareup.picasso.Picasso;

import java.util.List;

import layout.fr_product_list;


/**
 * Created by Administrator on 19-06-2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private Context ctx;

    private List<Album> albumList;
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cardtext);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.ctx = mContext;
        this.albumList = albumList;
    }


    @Override
    public AlbumsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.card_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.MyViewHolder holder, int position) {
        final Album album = albumList.get(position);
        holder.title.setText(album.getTitle());
        //  myDB.showToast(ctx,album.getTitle());
        if (album.getUrl() != null) {
            Picasso.get()
                    .load(album.getUrl())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .into(holder.thumbnail);
            Log.i("AlbumActivity", String.valueOf(album.getTitle()));
        }
        holder.thumbnail.setOnClickListener(
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
        return albumList.size();
    }
}
