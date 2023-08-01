package com.aic.khidmanow;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;



public class detailServiceAdapter extends RecyclerView.Adapter<detailServiceAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<detailitemVO> items;
    private int cuslayout;

    public detailServiceAdapter(FragmentActivity activity, ArrayList<detailitemVO> items,int cuslayout) {
        this.context = activity;
        this.items = items;
        this.cuslayout=cuslayout;
    }
    @Override
    public detailServiceAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final detailitemVO album = items.get(position);
        holder.detailQ.setText(album.getDetailq());
        holder.detailR.setText(album.getDetailr());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {


        private TextView detailQ;
        private TextView detailR;

        public CustomViewHolder(View view) {
            super(view);
            detailQ = view.findViewById(R.id.detailQ);
            detailR = view.findViewById(R.id.detailR);
        }
    }
}
